package com.rabbit.manager.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.rabbit.manager.service.FileUploadService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.cloudinary.utils.StringUtils;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService{

    private final Cloudinary cloudinary;
    @Override
    public String uploadFile(MultipartFile multipartFile) throws IOException {
        return cloudinary.uploader()
                .upload(multipartFile.getBytes(),
                        Map.of("public_id", UUID.randomUUID().toString()))
                .get("url")
                .toString();
    }
	@Override
	public void DeleteFile(String imagePath) throws IOException {
		String publicId = extractPublicId(imagePath);
		cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
	}
	
	private String extractPublicId(String imageUrl) {
        int startIndex = imageUrl.lastIndexOf("/") + 1;
        int endIndex = imageUrl.lastIndexOf(".");
        if (startIndex != -1 && endIndex != -1) {
            return imageUrl.substring(startIndex, endIndex);
        } else {
            throw new IllegalArgumentException("Invalid image URL");
        }
    }
}
