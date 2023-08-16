package com.rabbit.manager.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    String uploadFile(MultipartFile multipartFile) throws IOException;
    void DeleteFile(String imagepath) throws IOException;
}