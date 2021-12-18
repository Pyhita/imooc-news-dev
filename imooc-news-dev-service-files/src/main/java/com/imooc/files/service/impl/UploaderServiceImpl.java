package com.imooc.files.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.imooc.files.service.UploaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class UploaderServiceImpl implements UploaderService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @Override
    public String uploadFdfs(MultipartFile file, String suffix) throws IOException {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = fastFileStorageClient.
                uploadFile(inputStream, file.getSize(), suffix, null);
        inputStream.close();
        return storePath.getFullPath();
    }
}