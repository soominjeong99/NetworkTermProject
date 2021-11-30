package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerStart {

	public static void main(String[] args) {


			System.out.println("서버시작");
			try(ServerSocket server = new ServerSocket(9999)){ 
			while(!server.isClosed()) {
				
				Socket socket = server.accept();
				Thread p = new PersonalServer(socket); // 쓰레드를 이용해 서버관리
				p.start();

				System.out.println("서버 접속");
			}
			server.close();
		}catch(IOException e) {
			System.out.println("[server] network error "  + e.toString()); 
		}
	}

}
