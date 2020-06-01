package com.example.eventnotify.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

public class ObjectConverter {

	public static String serializeObject(Object sourceObject) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(sourceObject);
			return Base64.getEncoder().encodeToString(baos.toByteArray());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static Object deserializeObject(String sourceString) {
		try {
			byte[] sourceBytes = Base64.getDecoder().decode(sourceString);
			ByteArrayInputStream bais = new ByteArrayInputStream(sourceBytes);
			ObjectInputStream os = new ObjectInputStream(bais);
			return os.readObject();
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}

	}
}
