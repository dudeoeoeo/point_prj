# Point API

개발환경

- SpringBoot 2.7.3
- Gradle 7.5
- Java 11

# DB Table DDL
```
CREATE TABLE `place` (
   `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_yn` bit(1) DEFAULT NULL,
   `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `create_dt` datetime(6) DEFAULT NULL,
   `delete_dt` datetime(6) DEFAULT NULL,
   `update_dt` datetime(6) DEFAULT NULL,
   `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `place_id` varchar(255) UNIQUE COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `subject` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL
 ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
CREATE TABLE `users` (
   `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_yn` bit(1) DEFAULT NULL,
   `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `create_dt` datetime(6) DEFAULT NULL,
   `delete_dt` datetime(6) DEFAULT NULL,
   `update_dt` datetime(6) DEFAULT NULL,
   `email` varchar(255) UNIQUE COLLATE utf8mb4_unicode_ci NOT NULL,
   `password` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
   `name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
   `user_id` varchar(255) UNIQUE COLLATE utf8mb4_unicode_ci NOT NULL
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
CREATE TABLE `point` (
   `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_yn` bit(1) DEFAULT NULL,
   `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `create_dt` datetime(6) DEFAULT NULL,
   `delete_dt` datetime(6) DEFAULT NULL,
   `update_dt` datetime(6) DEFAULT NULL,
   `point` bigint DEFAULT NULL,
   `point_id` varchar(255) UNIQUE COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `user_id` bigint DEFAULT NULL,
   CONSTRAINT `FK5x0rfwo6q3kubvion9ecld8ya` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
CREATE TABLE `point_log` (
   `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_yn` bit(1) DEFAULT NULL,
   `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `create_dt` datetime(6) DEFAULT NULL,
   `delete_dt` datetime(6) DEFAULT NULL,
   `update_dt` datetime(6) DEFAULT NULL,
   `action` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `photoYn` bit(1) DEFAULT NULL,
   `point` int DEFAULT NULL,
   `review_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `saved_point` int DEFAULT NULL,
   `user_id` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   KEY `idx_review_id` (`review_id`),
   KEY `idx_user_id` (`user_id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
CREATE TABLE `review` (
   `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_yn` bit(1) DEFAULT NULL,
   `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `create_dt` datetime(6) DEFAULT NULL,
   `delete_dt` datetime(6) DEFAULT NULL,
   `update_dt` datetime(6) DEFAULT NULL,
   `content` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `review_id` varchar(255) UNIQUE COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `star` double NOT NULL,
   `subject` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `place_id` bigint DEFAULT NULL,
   `user_id` bigint DEFAULT NULL,
   CONSTRAINT `FK6cpw2nlklblpvc7hyt7ko6v3e` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
   CONSTRAINT `FKn429agmmvh298piqrnnd4gbfg` FOREIGN KEY (`place_id`) REFERENCES `place` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 
CREATE TABLE `review_image` (
   `id` bigint PRIMARY KEY NOT NULL AUTO_INCREMENT,
   `create_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `delete_yn` bit(1) DEFAULT NULL,
   `update_by` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `create_dt` datetime(6) DEFAULT NULL,
   `delete_dt` datetime(6) DEFAULT NULL,
   `update_dt` datetime(6) DEFAULT NULL,
   `name` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `review_image_id` varchar(255) UNIQUE COLLATE utf8mb4_unicode_ci DEFAULT NULL,
   `review_id` bigint DEFAULT NULL,
   CONSTRAINT `FK16wp089tx9nm0obc217gvdd6l` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
 ```
 
 # 서버 구동
 
 $ git clone https://github.com/dudeoeoeo/point_prj -b main
 // docker compose 설치 必
 
 $ docker-compose up


# API Spec

## Sign Up
// request url
POST http://localhost:8080/users 

// request body
{
    "name": "test",
    "email": "test@test.com",
    "password": "12341234"
}

## Login
// request url
POST http://localhost:8080/login

// request body
{
    "email": "test@test.com",
    "password": "12341234"
}

------------------------------------------------------------------
// request header (위 회원가입과 로그인 API 제외한 모든 API 필수 헤더)
Authorization / Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIzZWU3Mzc5ZC05NTViLTRlMTUtODIxYy1hMDk2ZWYxNDFlZDEiLCJleHAiOjE2NzYxNjE5NDh9._hz3TxX28n7DDZHHXjLiWXtnB3FLThURtniXk6983xtAH1oao8R-cg6dqUjggTG_izuookoLXJPb2nbwGP8HBQ

## Add review
// request url
POST http://localhost:8080/review

// request part
key: files value: image
key: req value: {"placeId": 1,"subject": "리뷰 제목","content": "내용","star": 3.5} // application/json

// response body
{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "2c23e8f2-3fb2-4ab3-9d98-ad7a05efc328",
    "content": "내용1",
    "attachedPhotoIds": [
        "ffcdb7e9-817a-4403-b142-9753cb0782eb"
    ],
    "userId": "3ee7379d-955b-4e15-821c-a096ef141ed1",
    "placeId": "0cc30f8b-d619-44a1-9afa-197fec075792"
}

## Modify review
// request url
PUT http://localhost:8080/review/{reviewId}
reviewId: String, "2c23e8f2-3fb2-4ab3-9d98-ad7a05efc328"

// request part
key: files value: image
key: req value: {"placeId": 1,"subject": "리뷰 수정 제목","content": "리뷰 수정 내용","star": 4.5} // application/json

// response body
{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "2c23e8f2-3fb2-4ab3-9d98-ad7a05efc328",
    "content": "내용1",
    "attachedPhotoIds": [
        "ffcdb7e9-817a-4403-b142-9753cb0782eb"
    ],
    "userId": "3ee7379d-955b-4e15-821c-a096ef141ed1",
    "placeId": "0cc30f8b-d619-44a1-9afa-197fec075792"
}

## Delete review
// request url
DELETE http://localhost:8080/review/{reviewId}
reviewId: String, "2c23e8f2-3fb2-4ab3-9d98-ad7a05efc328"

// response body
{
    "type": "REVIEW",
    "action": "DELETE",
    "reviewId": "2c23e8f2-3fb2-4ab3-9d98-ad7a05efc328",
    "content": "내용1",
    "attachedPhotoIds": [
        "ffcdb7e9-817a-4403-b142-9753cb0782eb"
    ],
    "userId": "3ee7379d-955b-4e15-821c-a096ef141ed1",
    "placeId": "0cc30f8b-d619-44a1-9afa-197fec075792"
}

## Add point
// request url
POST http://localhost:8080/event

// request body
{
    "type": "REVIEW",
    "action": "ADD",
    "reviewId": "2c23e8f2-3fb2-4ab3-9d98-ad7a05efc328",
    "content": "내용1",
    "attachedPhotoIds": [
        "ffcdb7e9-817a-4403-b142-9753cb0782eb"
    ],
    "userId": "3ee7379d-955b-4e15-821c-a096ef141ed1",
    "placeId": "0cc30f8b-d619-44a1-9afa-197fec075792"
}

// response body
{
    "userId": "3ee7379d-955b-4e15-821c-a096ef141ed1",
    "point": 3
}

## check pointLog list
// request url
GET http://localhost:8080/point

// response body
{
    "userPoint": 0,
    "list": [
        {
            "stockPoint": 3,
            "reviewPoint": 3,
            "action": "ADD"
        },
        {
            "stockPoint": -1,
            "reviewPoint": 2,
            "action": "MOD"
        },
        {
            "stockPoint": -2,
            "reviewPoint": -2,
            "action": "DELETE"
        }
    ]
}

 
 =================================================================================================
 TODO: 리뷰 작성, 수정, 삭제 이벤트가 발생할 경우 Point 와 Log 를 쌓는 것은 MQ 에 담고 MQ server 에서 처리
 =================================================================================================
 
