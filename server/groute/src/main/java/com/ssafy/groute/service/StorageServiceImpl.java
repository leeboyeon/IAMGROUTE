package com.ssafy.groute.service;

import com.ssafy.groute.controller.UserController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;


@Service
public class StorageServiceImpl implements StorageService {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

//    @Value("${spring.servlet.multipart.location}")
    @Value("${spring.http.multipart.location}")
    private String uploadPath;


    @Override
    public void init(String location) throws Exception{

        Files.createDirectories(Paths.get(location));

    }

    @Override
    public String store(MultipartFile file, String location) throws Exception{
//        Path root = Paths.get(location);
        Path root = Paths.get(URI.create(location));  // window
//        Path root = Paths.get(file.getName());
        logger.debug("file:{} location: {} root : {}", file, location, root);
        LocalDate date = LocalDate.now();
        String fileName = date + "_" + file.getOriginalFilename();
        if (!Files.exists(root)) {
            init(location);
        }

        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, root.resolve(fileName),
                StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }

//
//    @Override
//    public Resource loadAsResource(String filename) {
//        // TODO Auto-generated method stub
//        return null;
//    }
        // 전체를 지우는 코드임. 하나만 지우고 싶을 경우 수정 요함
//    @Override
//    public void delete() {
//        FileSystemUtils.deleteRecursively(Paths.get(uploadPath).toFile());
//
//    }
}
