package com.ssafy.study.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUtils {

	public String uploadFile(MultipartFile file, String basePath) throws IOException {
		String saveName;

		makeDir(basePath);

		String oriName = file.getOriginalFilename();
		String ext = oriName.substring(oriName.lastIndexOf("."));
		saveName = calDatePath(basePath);
		saveName += File.separator + fileRandomFileName(file.getBytes()) + ext;
		String filePath = basePath + File.separator + saveName;

		file.transferTo(new File(filePath));

		return saveName;
	}

	public byte[] downloadFile(String baseUrl, String path) throws IOException {

		FileInputStream fis = null;
		byte[] images = null;
		try {
			fis = new FileInputStream(baseUrl + path);
			images = IOUtils.toByteArray(fis);

		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return images;
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

		String saveName = uuid.toString().replaceAll("-", "") + dateFormat;

		return saveName;
	}

	private String getExtension(String file) {
		return file.substring(file.lastIndexOf(".") + 1);
	}

}
