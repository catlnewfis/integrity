package com.catlbattery.alm.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class IoUtil {

	/**
	 * 从输入流中获取字节数组
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	public static byte[] readBytes(InputStream inputStream) throws IOException {
		byte[] buffer = new byte[1024];
		int len = 0;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		while ((len = inputStream.read(buffer)) != -1) {
			bos.write(buffer, 0, len);
		}
		bos.close();
		return bos.toByteArray();
	}

	public static String readInputStream(InputStream inputStream) throws IOException {
		return readInputStream(inputStream, null);
	}

	public static String readInputStream(InputStream inputStream, String encoding) throws IOException {
		BufferedReader bReader = null;
		InputStreamReader sReader = null;
		BufferedInputStream bis = new BufferedInputStream(inputStream);

		if (encoding != null && !encoding.isEmpty()) {
			sReader = new InputStreamReader(bis, encoding);// 设置编码方式
		} else {
			sReader = new InputStreamReader(bis);
		}
		bReader = new BufferedReader(sReader);

		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = bReader.readLine()) != null) {
			sb.append(line);
			sb.append("\n");
		}

		bReader.close();
		return sb.toString();
	}

}
