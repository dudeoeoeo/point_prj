package com.kei.reviewservice;

import com.kei.reviewservice.business.review.constant.ActionType;
import com.kei.reviewservice.business.review.constant.Type;
import com.kei.reviewservice.business.review.dto.request.ReviewReq;
import com.kei.reviewservice.business.user.dto.request.SignUpReq;
import com.kei.reviewservice.common.jwt.JwtProperties;
import com.kei.reviewservice.common.jwt.TokenProvider;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.kei.reviewservice.utils.TestControllerUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.config.location=classpath:application-test.yml"})
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.MethodName.class)
@DisplayName("API 통합 테스트")
public class ApiTestController {

    private MockMvc mockMvc;
    private TokenProvider tokenProvider;

    private static final String USER_ID = "UT3ee7379d-955b-4e15-821c-a096ef141ed1";
    private static final String USER_ID2 = "UT2qweqw-asdqw12-qwd1q-qweasd";
    private static final String REVIEW_ID = "sfd980ew-d619-44a1-9afa-197fec789343";
    private static final String REVIEW_ID_WITH_IMAGE = "asd167as-d619-44a1-9afa-197fec789343";

    private static final String PLACE_ID = "0cc30f8b-d619-44a1-9afa-197fec075792";
    private static final String PLACE_ID_WITH_IMAGE = "1cc30f8b-d619-44a1-9afa-197fec890531";

    private static final String REVIEW_IMAGE_ID = "5qwe892d-955b-4e15-821c-a096ef141ed1";
    private static final String FILE_PATH = "src/test/resources/image/watch.jpg";

    @Autowired
    public ApiTestController(MockMvc mockMvc, TokenProvider tokenProvider)
    {
        this.mockMvc = mockMvc;
        this.tokenProvider = tokenProvider;
    }

    private MockMultipartHttpServletRequestBuilder getPutBuilder(String requestPath) {
        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(requestPath);
        builder.with(new RequestPostProcessor() {
            @Override
            public MockHttpServletRequest postProcessRequest(MockHttpServletRequest request) {
                request.setMethod("PUT");
                return request;
            }
        });
        return builder;
    }

    private String getToken() {
        return JwtProperties.HEADER_PREFIX + tokenProvider.generateToken(USER_ID);
    }

    private String getToken2() {
        return JwtProperties.HEADER_PREFIX + tokenProvider.generateToken(USER_ID2);
    }

    @Test
    @DisplayName("유저 회원가입 테스트")
    void _01_test() throws Exception {
        SignUpReq req = new SignUpReq();
        req.setName("test");
        req.setEmail("test@test.com");
        req.setPassword("12341234");

        final ResultActions result = mockMvc.perform(
                post("/users")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJson(req))
        );

        result.andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("사진을 포함한 리뷰 작성 테스트")
    void _02_test() throws Exception {
        final String token = getToken();

        ReviewReq req = new ReviewReq();
        req.setPlaceId(4L);
        req.setSubject("제목1");
        req.setContent("내용");
        req.setStar(4);

        final MockMultipartFile files = new MockMultipartFile("files", "watch.jpg",
                "image/jpg", new FileInputStream(FILE_PATH));
        final MockMultipartFile partReq = new MockMultipartFile("req", "req",
                "application/json", toJson(req).getBytes(StandardCharsets.UTF_8));

        final ResultActions result = mockMvc.perform(
                multipart("/review")
                        .file(files)
                        .file(partReq)
                        .header(JwtProperties.HEADER_STRING, token)
        );

        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.action").exists())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.attachedPhotoIds").isArray())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.placeId").exists())
        ;
    }

    @Test
    @DisplayName("사진을 포함하지 않은 리뷰 작성 테스트")
    void _03_test() throws Exception {
        final String token = getToken();

        ReviewReq req = new ReviewReq();
        req.setPlaceId(3L);
        req.setSubject("제목1");
        req.setContent("내용");
        req.setStar(4);

        final MockMultipartFile partReq = new MockMultipartFile("req", "",
                "application/json", toJson(req).getBytes(StandardCharsets.UTF_8));

        final ResultActions result = mockMvc.perform(
                multipart("/review")
                        .file(partReq)
                        .header(JwtProperties.HEADER_STRING, token)
        );

        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.action").exists())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.attachedPhotoIds").isArray())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.placeId").exists())
        ;
    }

    @Test
    @DisplayName("사진이 없는 리뷰 수정 테스트")
    void _04_test() throws Exception {
        final String token = getToken();

        Map<String, Object> map = new HashMap<>();
        map.put("placeId", 101);
        map.put("subject", "리뷰 제목 수정");
        map.put("content", "리뷰 내용 수정");
        map.put("star", 1.7);

        final MockMultipartHttpServletRequestBuilder builder = getPutBuilder("/review/" + REVIEW_ID);

        final MockMultipartFile partReq = new MockMultipartFile("req", "",
                "application/json", toJson(map).getBytes(StandardCharsets.UTF_8));

        final ResultActions result = mockMvc.perform(
                builder
                        .file(partReq)
                        .header(JwtProperties.HEADER_STRING, token)
        );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.action").exists())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.attachedPhotoIds").isArray())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.placeId").exists())
        ;
    }

    @Test
    @DisplayName("사진이 있는 리뷰 수정 테스트")
    void _05_test() throws Exception {
        final String token = getToken2();

        Map<String, Object> map = new HashMap<>();
        map.put("placeId", 102);
        map.put("subject", "리뷰 제목 수정");
        map.put("content", "리뷰 내용 수정");
        map.put("star", 1.7);

        final MockMultipartHttpServletRequestBuilder builder = getPutBuilder("/review/" + REVIEW_ID_WITH_IMAGE);

        final MockMultipartFile partReq = new MockMultipartFile("req", "",
                "application/json", toJson(map).getBytes(StandardCharsets.UTF_8));

        final ResultActions result = mockMvc.perform(
                builder
                        .file(partReq)
                        .header(JwtProperties.HEADER_STRING, token)
        );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.action").exists())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.attachedPhotoIds").isArray())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.placeId").exists())
        ;
    }

    @Test
    @DisplayName("리뷰 삭제 테스트")
    void _06_test() throws Exception {
        final String token = getToken();

        final ResultActions result =
                mockMvc.perform(
                        delete("/review/" + REVIEW_ID)
                        .header(JwtProperties.HEADER_STRING, token)
        );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").exists())
                .andExpect(jsonPath("$.action").exists())
                .andExpect(jsonPath("$.reviewId").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.attachedPhotoIds").isArray())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.placeId").exists())
        ;
    }

    @Test
    @DisplayName("사진이 없는 리뷰의 포인트 적립 테스트")
    void _07_test() throws Exception {
        final String token = getToken();

        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.REVIEW.name());
        map.put("action", ActionType.ADD.name());
        map.put("reviewId", REVIEW_ID);
        map.put("content", "내용");
        map.put("attachedPhotoIds", new ArrayList<>());
        map.put("userId", USER_ID);
        map.put("placeId", PLACE_ID);

        final ResultActions result =
                mockMvc.perform(
                        post("/event")
                                .content(toJson(map))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.point").value(2))
        ;
    }

    @Test
    @DisplayName("사진이 있는 리뷰의 포인트 적립 테스트")
    void _08_test() throws Exception {
        final String token = getToken2();

        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.REVIEW.name());
        map.put("action", ActionType.ADD.name());
        map.put("reviewId", REVIEW_ID_WITH_IMAGE);
        map.put("content", "내용");
        map.put("attachedPhotoIds", Arrays.asList(REVIEW_IMAGE_ID));
        map.put("userId", USER_ID2);
        map.put("placeId", PLACE_ID_WITH_IMAGE);

        final ResultActions result =
                mockMvc.perform(
                        post("/event")
                                .content(toJson(map))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.point").value(3))
        ;
    }

    @Test
    @DisplayName("사진이 없는 리뷰의 사진 추가 후 포인트 적립 테스트")
    void _09_test() throws Exception {
        final String token = getToken();

        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.REVIEW.name());
        map.put("action", ActionType.MOD.name());
        map.put("reviewId", REVIEW_ID);
        map.put("content", "내용");
        map.put("attachedPhotoIds", Arrays.asList(UUID.randomUUID().toString()));
        map.put("userId", USER_ID);
        map.put("placeId", PLACE_ID);

        final ResultActions result =
                mockMvc.perform(
                        post("/event")
                                .content(toJson(map))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.point").value(3))
        ;
    }

    @Test
    @DisplayName("사진이 있는 리뷰의 사진 삭제 후 포인트 적립 테스트")
    void _10_test() throws Exception {
        final String token = getToken2();

        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.REVIEW.name());
        map.put("action", ActionType.MOD.name());
        map.put("reviewId", REVIEW_ID_WITH_IMAGE);
        map.put("content", "내용");
        map.put("attachedPhotoIds", new ArrayList<>());
        map.put("userId", USER_ID2);
        map.put("placeId", PLACE_ID_WITH_IMAGE);

        final ResultActions result =
                mockMvc.perform(
                        post("/event")
                                .content(toJson(map))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.point").value(2))
        ;
    }

    @Test
    @DisplayName("1번 유저의 리뷰 삭제 테스트")
    void _11_test() throws Exception {
        final String token = getToken();

        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.REVIEW.name());
        map.put("action", ActionType.DELETE.name());
        map.put("reviewId", REVIEW_ID);
        map.put("content", "내용");
        map.put("attachedPhotoIds", Arrays.asList(UUID.randomUUID().toString()));
        map.put("userId", USER_ID);
        map.put("placeId", PLACE_ID);

        final ResultActions result =
                mockMvc.perform(
                        post("/event")
                                .content(toJson(map))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.point").value(0))
        ;
    }

    @Test
    @DisplayName("2번 유저의 리뷰 삭제 테스트")
    void _12_test() throws Exception {
        final String token = getToken2();

        Map<String, Object> map = new HashMap<>();
        map.put("type", Type.REVIEW.name());
        map.put("action", ActionType.DELETE.name());
        map.put("reviewId", REVIEW_ID_WITH_IMAGE);
        map.put("content", "내용");
        map.put("attachedPhotoIds", new ArrayList<>());
        map.put("userId", USER_ID2);
        map.put("placeId", PLACE_ID_WITH_IMAGE);

        final ResultActions result =
                mockMvc.perform(
                        post("/event")
                                .content(toJson(map))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").exists())
                .andExpect(jsonPath("$.point").value(0))
        ;
    }

    @Test
    @DisplayName("1번 유저의 포인트 로그 조회 테스트")
    void _13_test() throws Exception {
        final String token = getToken();

        final ResultActions result =
                mockMvc.perform(
                        get("/point")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPoint").exists())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].stockPoint").exists())
                .andExpect(jsonPath("$.list[0].reviewPoint").exists())
                .andExpect(jsonPath("$.list[0].action").exists())
        ;
    }

    @Test
    @DisplayName("2번 유저의 포인트 로그 조회 테스트")
    void _14_test() throws Exception {
        final String token = getToken2();

        final ResultActions result =
                mockMvc.perform(
                        get("/point")
                                .accept(MediaType.APPLICATION_JSON)
                                .header(JwtProperties.HEADER_STRING, token)
                );

        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userPoint").exists())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.list[0].stockPoint").exists())
                .andExpect(jsonPath("$.list[0].reviewPoint").exists())
                .andExpect(jsonPath("$.list[0].action").exists())
        ;
    }
}

