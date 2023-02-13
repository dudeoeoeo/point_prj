package com.kei.reviewservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class TestControllerUtils {

    private static final ObjectMapper om = new ObjectMapper();

    public static String toJson(Object obj) {
        try {
            return om.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
