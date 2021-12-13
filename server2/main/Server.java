package server2.main;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress("127.0.0.1", 5500)); // 쓰레드로 서버관리
            List<Socket> socketList = new ArrayList<>();
            List<SessionThread> sessionThreadList = new ArrayList<>();
            CheckThread checkThread = new CheckThread(socketList, sessionThreadList);
            checkThread.start();
        

            while (true) {
            
                Socket socket = serverSocket.accept();
                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("[연결 수락 : " + isa.getHostName() + "]");
                SessionThread sessionThread = new SessionThread(socket, socketList, sessionThreadList, checkThread);
                sessionThread.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
