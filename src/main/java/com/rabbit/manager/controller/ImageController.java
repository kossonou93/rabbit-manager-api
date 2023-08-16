package com.rabbit.manager.controller;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.rabbit.manager.model.Rabbit;
import com.rabbit.manager.repository.RabbitRepository;
import com.rabbit.manager.service.FileUploadService;
import com.rabbit.manager.service.RabbitService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/image")
@CrossOrigin
@RequiredArgsConstructor
public class ImageController {
	
	@Autowired
	RabbitService rabbitService;
	
	@Autowired
	RabbitRepository rabbitRepository;
	
	private final FileUploadService fileUploadService;

	private static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@PostMapping("/uploadFS/{id}")
	public void uploadImageFS(@RequestParam("image") MultipartFile file, @PathVariable("id") String id)
			throws IOException {
		Rabbit r = rabbitRepository.findById(id).get();
        String imageURL = fileUploadService.uploadFile(file);
        r.setImagePath(imageURL);
        rabbitRepository.save(r);
	}

	@PutMapping("/update/{id}")
	public void UpdateImage(@RequestParam("image") MultipartFile file, @PathVariable("id") String id) throws IOException {
		Rabbit r = rabbitRepository.findById(id).get();
		fileUploadService.DeleteFile(r.getImagePath());
		String imageURL = fileUploadService.uploadFile(file);
        r.setImagePath(imageURL);
        rabbitRepository.save(r);
	}
}
