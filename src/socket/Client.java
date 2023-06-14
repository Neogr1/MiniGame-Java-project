package socket;

import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable {
    public static int userNumber;
    private static DataInputStream in;
    private static DataOutputStream out;

    private static String receivedMessage;

    private static String ip;

    public Client(String ip) {
        Client.ip = ip;
    }

    public void run() {
        Socket socket;
        try {
            socket = new Socket(ip, 5000);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            // 유저 번호 저장
            userNumber = in.readInt();

            Thread receiveThread = new Thread(new ReceiveThread());
            receiveThread.start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
    // 서버로부터 메시지 수신하는 쓰레드
    private class ReceiveThread extends Thread {
        @Override
        public void run() {
            String message;
            try {
                while (true) {
                    message = in.readUTF(); // 서버로부터 메시지 수신

                    if (message != null) {
                        receivedMessage = message;
                    }
                }

            } catch (IOException e) {
                // mainGui에서 종료하도록
                receivedMessage = "EXIT";
            }
        }
    }
    
    
    // 서버로 메시지를 전송하는 메소드
    public static void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 서버로부터 받은 메시지를 각 게임에게 전달하고 받은 메시지는 null로 set
    // 동기화 필요
    public static synchronized String getReceivedMessage() {
        String temp = receivedMessage;
        if (temp != null) {
            receivedMessage = null;
            return temp;
        }

        return null;
    }
}
