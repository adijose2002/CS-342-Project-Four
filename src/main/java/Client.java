import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.application.Platform;

public class Client extends Thread {

	Socket socketClient;

	ObjectOutputStream out;
	ObjectInputStream in ;

	private Consumer < Serializable > callback;
	private Consumer < Serializable > call;
	private Consumer < Serializable > delete;

	Client(Consumer < Serializable > call, Consumer < Serializable > clientList, Consumer < Serializable > clearing) {

		callback = call;
		call = clientList;
		delete = clearing;
	}

	public void run() {
		synchronized(this) {
			try {
				socketClient = new Socket("127.0.0.1", 5555);
				out = new ObjectOutputStream(socketClient.getOutputStream());
				in = new ObjectInputStream(socketClient.getInputStream());
				socketClient.setTcpNoDelay(true);
			} catch (Exception e) {}

			while (true) {

				try {
					sentMessage MESSAGE = (sentMessage) in.readObject();
					if (MESSAGE.cat == 2) {
						String message = MESSAGE.text;
						callback.accept(message);
					} else if (MESSAGE.cat == 1) {
						delete.accept(5);
						insertClient(MESSAGE.clientList);

					}
				} catch (Exception e) {
					Platform.exit();
				}
			}
		}

	}

	public void insertClient(ArrayList < String > l) {

		int i = 0;
		while (i < l.size()) {
			call.accept(l.get(i));
			i = i + 1;

		}

	}

	public void send(String data) {

		try {

			out.writeObject(data);
			out.reset();

		} catch (IOException e) {

			System.out.println("Not Sent");

		}

	}

}