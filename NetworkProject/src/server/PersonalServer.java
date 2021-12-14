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
   static UserAccountPool accountPool;     // ��ü ������ ���� �������� ���� ����
   static DatagramSocket ds;                // UDP�� ���� ����
   static List<Room> rooms;                // ������ �� ����

   static {
      accountPool = new UserAccountPool();
      rooms = new ArrayList<>();
      try {
         ds = new DatagramSocket(9999);
      }catch(IOException e) {
         System.out.println("alramSocket create failed.. " + e.toString());
      }
   }

   static void sendAlramToAll(String alram) {            // ��� �������� UDP����
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

   static void sendAlramToUser(SocketAddress sa, String alram) {        // Ư�� �������� UDP ����
      DatagramPacket dp = new DatagramPacket(alram.getBytes(), alram.getBytes().length);
      dp.setSocketAddress(sa);
      try {
         System.out.println("server UDP send");
         ds.send(dp);
      }catch(IOException e) {
         System.out.println("[server-Thread] send alarm failed .. " + e.toString());
      }

   }

   static void sendAlramToUsers(Room r, String alram) {            // �濡 �ִ� ������� UDP ����
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
   private Account user;        // ���� ���� ��ü ����
   private Account whuser; //�ӼӸ� ��ü ����

   // ������ ================================
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

         }catch(IOException | ClassNotFoundException e) {    // �����
            System.out.println("[server] ���� ����");
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

         // Ŭ���̾�Ʈ�� ��û�� ���� ó��
         case "create":            // ȸ������
            resp = accountPool.create(command[1], command[2], command[3]);
            sendToClient(resp);
            break;    
         case "join":            // �α���
            String result = accountPool.login(command[1], command[2], socket.getRemoteSocketAddress());
            user = accountPool.getAccountMap().get(command[1]);
            sendToClient(result);
            nick = command[1];
            if(result.equals("true")) {            // ��� �������� ������� ���� ��û ����
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
         case "get":                // RoomPanel ���� ���                    
            resp = accountPool.getCurrentUser();
            sendToClient(resp);
            break;   

         case "createroom":    // �� ����
            if(rooms.size() >=8) {
               sendToClient(null);
               sendToClient(0);
            } else {
               user.setJoinRoomIndex(rooms.size());
               // command�� ���̰� 3�̶�� ������� �ƴϰ� 1�ο��̳� 2�ο�
               // command�� ���̰� 4��� ������̱� ������ ������ 2�ο�
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
         case "roomlist":    // �� ��� �ҷ�����
            sendToClient(rooms);
            break;    

         case "enterroom":        // �� ����
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
            case "secretroom":    // ����� �����
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
            sendAlramToUser(whuser.getSocketAddress(),"chat" + "[�ӼӸ�] " + nick + " : "+ msgs);
            sendAlramToUser(user.getSocketAddress(),"chat" + "[�ӼӸ�] " + nick + " : "+ msgs);
            }
            else
               sendAlramToUser(user.getSocketAddress(),"chat" + who+ "(��)�� �������� ���� ������Դϴ�.");
            //sendAlramToUser(user.getSocketAddress(),"chat" + command[1]);
            break;
            case "fight":
               whuser = accountPool.getAccountMap().get(msg);
               if(whuser != null) {
                  sendAlramToUser(whuser.getSocketAddress(),"fight#" + nick);
                  //sendAlramToUser(user.getSocketAddress(),"chat" + whuser.nick + "�Բ� ������ ��û�Ͽ����ϴ�.");
                  JOptionPane.showMessageDialog(null, whuser.nick + "�Բ� ������ ��û�Ͽ����ϴ�");
               }
            else
               sendAlramToUser(user.getSocketAddress(),"chat" + msg+ "(��)�� �������� ���� ������Դϴ�.");
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

   // TCP�� �̿��� Ŭ���̾�Ʈ���� ����������
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