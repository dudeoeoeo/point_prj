package com.kei.reviewservice.common.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ImageUtil {

    public List<String> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty())
            return new ArrayList<>();

        return files.stream().map(f -> f.getOriginalFilename()).collect(Collectors.toList());
    }
}
