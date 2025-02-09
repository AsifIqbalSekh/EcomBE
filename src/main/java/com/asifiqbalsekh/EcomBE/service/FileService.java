package com.asifiqbalsekh.EcomBE.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadProductImage(String folderPath, MultipartFile image);
}
