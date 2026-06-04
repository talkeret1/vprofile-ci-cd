package com.visualpathit.account.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.visualpathit.account.model.User;
import com.visualpathit.account.service.UserService;

@Controller
public class FileUploadController {

	@Autowired
	private UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

	@RequestMapping(value = { "/upload" }, method = RequestMethod.GET)
	public final String upload(final Model model) {
		return "upload";
	}

	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public @ResponseBody String uploadFileHandler(
			@RequestParam("name") String name,
			@RequestParam("userName") String userName,
			@RequestParam("file") MultipartFile file) {

		logger.info("Upload request received for user: {}", userName);

		if (file == null || file.isEmpty()) {
			return "You failed to upload " + name + ".png because the file was empty.";
		}

		try {
			byte[] bytes = file.getBytes();

			String rootPath = System.getProperty("catalina.home");
			if (rootPath == null || rootPath.isBlank()) {
				return "Server error: invalid root path.";
			}

			File dir = new File(rootPath + File.separator + "tmpFiles");

			if (!dir.exists() && !dir.mkdirs()) {
				logger.error("Failed to create directory: {}", dir.getAbsolutePath());
				return "Server error: cannot create upload directory.";
			}

			File serverFile = new File(dir, name + ".png");

			User user = userService.findByUsername(userName);
			if (user == null) {
				return "User not found: " + userName;
			}

			user.setProfileImg(name + ".png");
			user.setProfileImgPath(serverFile.getAbsolutePath());
			userService.save(user);

			try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile))) {
				stream.write(bytes);
			}

			logger.info("File uploaded successfully to: {}", serverFile.getAbsolutePath());

			return "You successfully uploaded file=" + name + ".png";

		} catch (IOException e) {
			logger.error("IO error during upload for user: {}", userName, e);
			return "You failed to upload " + name + ".png => IO error";
		} catch (Exception e) {
			logger.error("General error during upload for user: {}", userName, e);
			return "You failed to upload " + name + ".png => " + e.getMessage();
		}
	}
}