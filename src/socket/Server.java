package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
    private final int MAX_USER = 2;
    private Socket[] clientSockets = new Socket[2];
    private DataInputStream[] in = new DataInputStream[2];
    private DataOutputStream[] out = new DataOutputStream[2];

    static GameManager gameManager = new GameManager();

    @Override
    public void run() {
        initServer();

        // 각 클라이언트에게 메시지 받아 응답 보내는 쓰레드 실행
        Thread handleMessageFrom0Thread = new Thread(new GetMessage(0));
        handleMessageFrom0Thread.start();
        Thread handleMessageFrom1Thread = new Thread(new GetMessage(1));
        handleMessageFrom1Thread.start();
    }
    

    public void initServer() {
        try {
            // server open
            ServerSocket serverSocket = new ServerSocket(5000);

            int userNumber = 0;
            while (userNumber < MAX_USER) {
                clientSockets[userNumber] = serverSocket.accept(); // 클라이언트 연결 수락

                in[userNumber] = new DataInputStream(clientSockets[userNumber].getInputStream());
                out[userNumber] = new DataOutputStream(clientSockets[userNumber].getOutputStream());

                // 유저 번호 알려줌
                out[userNumber].writeInt(userNumber);

                userNumber++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 클라이언트로 메시지를 전송하는 메소드
    private void sendMessage(String message) {
        System.out.println(">>> " + message);
        try {
            out[0].writeUTF(message);
            out[0].flush();
            out[1].writeUTF(message);
            out[1].flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // 클라이언트에게 메시지를 받아 적절한 응답을 보내는 쓰레드
    private class GetMessage implements Runnable {
        int userNumber;

        public GetMessage(int userNumber) {
            this.userNumber = userNumber;
        } 

        @Override
        public void run() {
            try {
                while (true) {
                    String message = in[userNumber].readUTF(); // 클라이언트로부터 메시지 수신
                    
                    if (message != null) {
                        System.out.println("<<< " + message);
                        String response = gameManager.handleReceivedMessage(message);

                        if (response != null) {
                            sendMessage(response);
                        }
                    }
                }
            } catch (IOException e) {
                try {
                    clientSockets[0].close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
