package com.imooc.files.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UploaderService {

    String uploadFdfs(MultipartFile file, String suffix) throws IOException;
}
