package com.catlbattery.alm.util;

import java.io.File;
import java.io.FileFilter;

public class FileUtil {

	public static final FileFilter filesOnly = new FileFilter() {

		@Override
		public boolean accept(File pathname) {
			return pathname.isFile();
		}
	};
}
