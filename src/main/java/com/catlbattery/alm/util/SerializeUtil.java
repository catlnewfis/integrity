package com.catlbattery.alm.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SerializeUtil {

	private static Log log = LogFactory.getLog(SerializeUtil.class);

	public static byte[] serialize(Object object) {
		ObjectOutputStream oos = null;
		ByteArrayOutputStream baos = null;
		byte[] bytes = null;
		try {
			baos = new ByteArrayOutputStream();
			oos = new ObjectOutputStream(baos);
			oos.writeObject(object);
			bytes = baos.toByteArray();
		} catch (IOException e) {
			log.warn("Serialize Exception: " + e.getMessage());
		}
		return bytes;
	}

	public static Object unserialize(byte[] bytes) {
		ByteArrayInputStream bais = null;
		Object object = null;
		try {
			bais = new ByteArrayInputStream(bytes);
			ObjectInputStream ois = new ObjectInputStream(bais);
			object = ois.readObject();
		} catch (IOException e) {
			log.warn("Unserialize Exception: " + e.getMessage());
		} catch (ClassNotFoundException e) {
			log.warn("Unserialize Exception: " + e.getMessage());
		}
		return object;
	}

}
