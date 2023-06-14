import java.io.IOException;

import socket.Client;
import socket.Server;

public class GameRunner {

	private static LogIn logIn;
	private static Server server;
	private static Client client;

	public static void main(String[] args) throws IOException {
		
		logIn = new LogIn();

		// start button을 누를 때까지 spin
		while (logIn.getContentPane().isShowing()) {;}
		
		// 1P면 서버도 열어야 함
		if (logIn.ser) {
			openServer();
		}
		openClient();

		// login창 숨기고 게임 창 띄우기
		new MainGui();
	}

	private static void openServer() {
		server = new Server();
		Thread serverThread = new Thread (server);
		serverThread.start();
	}

	private static void openClient() {
		client = new Client(logIn.ip);
		Thread clientThread = new Thread (client);
		clientThread.start();
	}
}
