package com.ssafy.groute.service;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Path;

@Service
public interface StorageService {
    void init(String location) throws Exception;
    String store(MultipartFile file, String location) throws Exception;
//    Resource loadAsResource(String filename);
//    void delete();
}
