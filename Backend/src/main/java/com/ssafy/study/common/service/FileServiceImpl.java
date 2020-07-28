package com.ssafy.study.common.service;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl implements FileService {

	@Override
	public String uploadFile(MultipartFile file, String basePath) {
		String saveName;
		try {
			makeDir(basePath);

			String oriName = file.getOriginalFilename();
			String ext = oriName.substring(oriName.lastIndexOf("."));
			saveName = calDatePath(basePath);
			saveName += File.separator + fileRandomFileName(file.getBytes()) + ext;
			String filePath = basePath + File.separator + saveName;

			file.transferTo(new File(filePath));

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return saveName;

	}

	private String calDatePath(String basePath) {
		Calendar cal = Calendar.getInstance();

		String yearPath = File.separator + cal.get(Calendar.YEAR);
		String monthPath = yearPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.MONTH) + 1);
		String datePath = monthPath + File.separator + new DecimalFormat("00").format(cal.get(Calendar.DATE));
		makeDir(basePath + File.separator + datePath);

		return datePath;
	}

	private void makeDir(String path) {
		File file = new File(path);
		if (!file.exists())
			file.mkdirs();
	}

	private String fileRandomFileName(byte[] fileData) {
		UUID uuid = UUID.randomUUID();
		SimpleDateFormat smf = new SimpleDateFormat("HHmmss");
		String dateFormat = smf.format(new Date(System.currentTimeMillis()));

		String saveName = uuid.toString().replaceAll("-", "") + "_" + dateFormat;

		return saveName;
	}
}
