package com.catlbattery.alm.commons;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesHolder {

	private static Log log = LogFactory.getLog(PropertiesHolder.class);

	private static Properties properties;

	static {
		try {
			properties = PropertiesLoaderUtils.loadAllProperties("properties/prop.properties");
		} catch (IOException e) {
			log.warn(e.getMessage());
		}
	}

	public static String getValue(String key) {
		String value = properties.getProperty(key);
		return value;
	}

	public static Properties getProperties() {
		return properties;
	}

}
