package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import common.Account;
import common.Room;
import handler.BtLogOutHandler;

public class PersonalServer extends Thread {

   // static ================================================================
   static UserAccountPool accountPool;     // 전체 유저와 현재 접속중인 유저 관리
   static DatagramSocket ds;                // UDP를 위한 소켓
   static List<Room> rooms;                // 생성된 방 관리

   static {
      accountPool = new UserAccountPool();
      rooms = new ArrayList<>();
      try {
         ds = new DatagramSocket(9999);
      }catch(IOException e) {
         System.out.println("alramSocket create failed.. " + e.toString());
      }
   }

   static void sendAlramToAll(String alram) {            // 모든 유저에게 UDP전송
      DatagramPacket dp = new DatagramPacket(alram.getBytes(), alram.getBytes().length);
      for(Account a : accountPool.getCurrentUser()) {
         dp.setSocketAddress(a.getSocketAddress());
         try {
            System.out.println("server UDP send");

            ds.send(dp);
         }catch(IOException e) {
            System.out.println("[server-Thread] send alarm failed .. " + e.toString());
         }
      }
   }    

   static void sendAlramToUser(SocketAddress sa, String alram) {        // 특정 유저에게 UDP 전송
      DatagramPacket dp = new DatagramPacket(alram.getBytes(), alram.getBytes().length);
      dp.setSocketAddress(sa);
      try {
         System.out.println("server UDP send");
         ds.send(dp);
      }catch(IOException e) {
         System.out.println("[server-Thread] send alarm failed .. " + e.toString());
      }

   }

   static void sendAlramToUsers(Room r, String alram) {            // 방에 있는 사람에게 UDP 전송
      DatagramPacket dp = new DatagramPacket(alram.getBytes(), alram.getBytes().length);

      for(Account a : r.getJoiner()) {
         SocketAddress sa = a.getSocketAddress();
         dp.setSocketAddress(sa);
         try {
            System.out.println("server UDP send");
            System.out.println("txt"+alram);
            ds.send(dp);
         }catch(IOException e) {
            System.out.println("[server-Thread] send alarm failed .. " + e.toString());
         }
      }
   }

   //=========================================================================================

   private Socket socket;
   private ObjectOutputStream oos;
   private ObjectInputStream ois;
   private Account user;        // 현재 계정 객체 저장
   private Account whuser; //귓속말 객체 저장

   // 생성자 ================================
   public PersonalServer(Socket socket) {
      this.socket = socket;
      try {
         oos = new ObjectOutputStream(socket.getOutputStream());
         ois = new ObjectInputStream(socket.getInputStream());
      }catch(IOException e) {
         System.out.println("failed\n");
      }
   }
   //========================================
   @Override
   public void run() {
      String nick = null;
      System.out.println("run\n");
      String[] command = null;
      String msg = null;
      // System.out.println("[server] received : ");
      while(socket.isConnected()) {
         String received = null;
         try {
            received = (String)ois.readObject();

         }catch(IOException | ClassNotFoundException e) {    // 종료시
            System.out.println("[server] 유저 종료");
         }
         
         System.out.println("[server] received : " + received);
         if(received == null) {
            try {
               socket.close();
               break;
            } catch (IOException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
            }
         }
         
         if(received.startsWith("chat")) {
            command[0] ="chat";
            msg = received.substring(4);
         }
         else if(received.startsWith("whis")) {
            command[0] ="whis";
            msg = received.substring(4);
         }
         else if(received.startsWith("fight")) {
            command[0] = "fight";
            msg = received.substring(5);
         }
         else {
         command = received.split("#");}
         Object resp = null;
         System.out.println(command[0]);
         switch(command[0]) {

         // 클라이언트의 요청에 따른 처리
         case "create":            // 회원가입
            resp = accountPool.create(command[1], command[2], command[3]);
            sendToClient(resp);
            break;    
         case "join":            // 로그인
            String result = accountPool.login(command[1], command[2], socket.getRemoteSocketAddress());
            user = accountPool.getAccountMap().get(command[1]);
            sendToClient(result);
            nick = command[1];
            if(result.equals("true")) {            // 모든 유저에게 유저목록 갱신 요청 전송
               sendAlramToAll("userListChange");
               sendAlramToAll("chat" + nick + " has joined" );
            }
            break;
         case "logout":         
            boolean logOutResult = accountPool.logOut(user);
            if(logOutResult) {
               sendAlramToAll("userListChange");
               sendAlramToAll("chat" + nick + " is leaving" );
            }
            sendToClient(logOutResult);
            break;
         case "get":                // RoomPanel 유저 목록                    
            resp = accountPool.getCurrentUser();
            sendToClient(resp);
            break;   

         case "createroom":    // 방 생성
            if(rooms.size() >=8) {
               sendToClient(null);
               sendToClient(0);
            } else {
               user.setJoinRoomIndex(rooms.size());
               // command의 길이가 3이라면 비번방이 아니고 1인용이나 2인용
               // command의 길이가 4라면 비번방이기 때문에 무조건 2인용
               if(command.length == 3) {
                  if(command[2].equals("true")) {
                     rooms.add(new Room(user,command[1],true,""));
                  } else {
                     rooms.add(new Room(user,command[1],false,""));
                  }
               } else {
                  rooms.add(new Room(user,command[1],true,command[3]));
               }
               resp = rooms.get(rooms.size()-1);
               sendToClient(resp);
               sendAlramToAll("changerooms");
            }
            break;
         case "roomlist":    // 방 목록 불러오기
            sendToClient(rooms);
            break;    
            
         case "enterroom":        // 방 입장
                user.setJoinRoomIndex(Integer.valueOf(command[1]));
                int roomIndex = user.getJoinRoomIndex();
                if(rooms.get(roomIndex).getJoiner().size() != 2 && rooms.get(roomIndex).getPass().equals("")) {
                    rooms.get(roomIndex).enterAccount(user);
                    resp = rooms.get(roomIndex);
                    sendToClient(resp);
                    sendAlramToAll("changerooms");
                } else {
                    resp = null;
                    sendToClient(resp);
                }
                break;
            case "secretroom":    // 비번방 입장시
                if(command[1].equals((rooms.get(user.getJoinRoomIndex()).getPass()))) {
                    rooms.get(user.getJoinRoomIndex()).enterAccount(user);
                    resp = rooms.get(user.getJoinRoomIndex());
                    sendAlramToAll("changerooms");
                } else {
                    user.setJoinRoomIndex(-1);
                    resp = null;
                }
                sendToClient(resp);
                break;
            case "chat": 
            sendAlramToAll("chat" + nick + " : " + msg);
            break;
            case "how": 
            sendAlramToUser(user.getSocketAddress(),"chat" + command[1]);
            break;
            case "whis":
               String who = null;
               String msgs = null;
               who = msg.substring(0,msg.indexOf(" "));
               msgs = msg.substring((msg.indexOf(" "))+1);
            whuser = accountPool.getAccountMap().get(who);
            if(whuser != null) {
            sendAlramToUser(whuser.getSocketAddress(),"chat" + "[귓속말] " + nick + " : "+ msgs);
            sendAlramToUser(user.getSocketAddress(),"chat" + "[귓속말] " + nick + " : "+ msgs);
            }
            else
               sendAlramToUser(user.getSocketAddress(),"chat" + who+ "(은)는 접속하지 않은 사용자입니다.");
            //sendAlramToUser(user.getSocketAddress(),"chat" + command[1]);
            break;
            case "fight":
               whuser = accountPool.getAccountMap().get(msg);
               if(whuser != null) {
                  sendAlramToUser(whuser.getSocketAddress(),"fight#" + nick);
                  //sendAlramToUser(user.getSocketAddress(),"chat" + whuser.nick + "님께 결투를 신청하였습니다.");
                  JOptionPane.showMessageDialog(null, whuser.nick + "님께 결투를 신청하였습니다");
               }
            else
               sendAlramToUser(user.getSocketAddress(),"chat" + msg+ "(은)는 접속하지 않은 사용자입니다.");
               break;
            case "refuse":
               whuser = accountPool.getAccountMap().get(command[1]);
                  sendAlramToUser(whuser.getSocketAddress(),"refuse#" + command[2]);   
            case "load":
                whuser = accountPool.getAccountMap().get(command[1]);
                   sendAlramToUser(whuser.getSocketAddress(),"load#" + command[2]);
                   break;
                  
               
        }
    }
}

   // TCP를 이용한 클라이언트에게 데이터전송
   private void sendToClient(Object resp) {
      try {
         oos.reset();    
         oos.writeObject(resp);
         System.out.println(resp);
      }catch(IOException e) {
         System.out.println("server write fail.. " + e.toString());
      }
   }


}