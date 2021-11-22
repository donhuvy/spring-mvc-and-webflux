package com.apress.prospringmvc.bookstore.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by Iuliana Cosmina on 23/05/2020
 */
public interface FileStorageService {
	void store(MultipartFile file);

	Stream<Path> loadAll();

	Resource loadAsResource(String filename);

	void deleteAll();

}
