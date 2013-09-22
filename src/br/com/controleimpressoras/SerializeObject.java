package br.com.controleimpressoras;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializeObject implements Serializable {
	private static final long serialVersionUID = 1L;
	ByteArrayOutputStream bos;

	public SerializeObject() {
		bos = new ByteArrayOutputStream();
	}

	public SerializeObject(Object objectToSerialize) throws IOException {
		this();
		serialize(objectToSerialize);
	}

	public void serialize(Object objectToSerialize) throws IOException {
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(objectToSerialize);
		os.flush();
		os.close();
	}

	public Object deserialize() throws IOException, ClassNotFoundException {
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		ObjectInputStream is = new ObjectInputStream(bis);
		Object value = is.readObject();
		is.close();
		return value;
	}
}