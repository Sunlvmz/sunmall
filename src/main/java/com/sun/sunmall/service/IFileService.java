package com.sun.sunmall.service;

import com.sun.sunmall.common.ServerResponse;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by sun on 2017/5/18.
 */
public interface IFileService {
    String uploadImageFile(MultipartFile multipartFile, String path);
}
