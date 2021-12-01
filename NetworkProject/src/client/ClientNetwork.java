package client;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.List;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client2.main.Window;
import client2.view.PlayersInfo;
import common.Account;
import common.Room;
import processing.core.PApplet;

public class ClientNetwork extends Thread {
   private static Socket soc; // 핵심 연결 소켓
   private ObjectOutputStream oos = null;
   private ObjectInputStream ois = null;
   private int flag = 0; //여러명 동시에 1:1 매칭 방지
   private DatagramSocket ds; // 서브 소켓(receive용)
   private ClientUI ui; // 클라이언트 GUI
   private Account user; // 사용자정보
   private String nick;    // user을 저장하기 위한 nick


   public ClientNetwork(ClientUI c) {
      this.ui = c; 
      System.out.println("연결중");
      try {
         soc = new Socket((c.ip), 9999); // 서버1랑 연결
         System.out.println("??");
         oos = new ObjectOutputStream(soc.getOutputStream());
         ois = new ObjectInputStream(soc.getInputStream());
         System.out.println("[client] connected to server");
         ds = new DatagramSocket(soc.getLocalPort()); // // TCP와 UDP는 같은 포트로 사용할 수 있음


      } catch (IOException e) {
         System.out.println("[client] network error " + e.toString()); // 예외처리
      }
      start();
   }

   @Override
   public void run() { 
      while (!ds.isClosed()) {
         DatagramPacket dp = new DatagramPacket(new byte[2048], 2048);
         try {
            ds.receive(dp);
            System.out.println("client UDP received");
            String data = new String(dp.getData(), 0, dp.getLength());
            System.out.println(data);
            String msg = null;
            String[] str = null;
            
            if(data.startsWith("chat")) {
               str = "message".split("#");
               msg = data.substring(4);
            }
            else {
            str = data.split("#");}
            
            switch (str[0]) {
            //  서버의 UDP 데이터 전송에 대한 값에 따라 처리
            case "userListChange":    // 로그인 유저 목록
               sendUserListRequest();
               break;
            case "changerooms":        // 방 목록 최신화
               sendStateRoomRequest();
               break;
            case "message": //프로토콜관리
               receive(msg);
               break;
            case "fight": //1:1 게임 초대 
                String who = str[1];
                if (flag == 1)
                   sendload(who);
                else if(flag == 0) {
                    flag = 1;
                   sendFightRequest(who);
                } 
             break;
          case "refuse": // 1:1 게임 거절
             String sorry = str[1];
             refuse(sorry);
             break;
            case "load": // 1:1 게임 매칭중인데 다른 사람이 신청할경우
                String loadmem = str[1];
                loading(loadmem);
                break;

            }
         } catch (IOException e) { // 예외처리
            System.out.println("dp failed .. " + e.toString());
            ds.close();
            break;
         }

      }
   }

   // ==============================================================================

   public void sendCreateRequest(String nick, String pass, String name, String repass) { // 회원신청
      String resp = null;
      System.out.println(" [client] request : ");
      if (nick.trim().equals("") || pass.trim().equals("")) {
         JOptionPane.showMessageDialog(ui, "아이디와 비밀번호를 입력하세요.");
         return;
      }
      if (!pass.equals(repass)) {
         JOptionPane.showMessageDialog(ui, "비밀번호를 확인하세요");
         return;
      }
      if (!ui.pnSignup.rbAgree.isSelected()) {
         JOptionPane.showMessageDialog(ui, "약관을 읽고 동의해주세요.");
         return;
      }
      synchronized (oos) {
         try {

            System.out.println(soc.getLocalPort());
            oos.writeObject("create#" + nick + "#" + pass + "#" + name);

            resp = (String)ois.readObject();

            System.out.println("[client] response : " + resp);
            String[] data = resp.split("#");
            // 여기서 ui 제어.
            if (data[0].equals("true")) {
               ui.pnSignup.tfId.setText(nick);
               ui.pnSignup.tfpw.setText("");
               ui.pnSignup.tfname.setText("");
               ui.pnSignup.tfrpw.setText("");
               JOptionPane.showMessageDialog(ui, "아이디가 생성되었습니다.");
               // 로그인 페이지로 이동.
               ui.setSize(400, 355);
               ui.setTitle("다섯 줄을 향한 무한한 도전 - 로그인");
               ui.setContentPane(ui.pnLogin);
               ui.pnLogin.tfid.setText(nick);

            } else {
               JOptionPane.showMessageDialog(ui, "이미 중복된 아이디가 있습니다."); 
            }

         } catch (ClassNotFoundException | IOException e) { // 예외처리 
            System.out.println("[client] network error " + e.toString());
         }
      }
   }

   public void sendLoginRequest(String nick, String pass) { // 로그인 신청
      this.nick = nick;
      String resp = null;
      System.out.println("[client] request : ");

      // 아이디와 비밀번호의 공백 체크 ===========================================
      if (nick.trim().equals("") || pass.trim().equals("")) {
         JOptionPane.showMessageDialog(ui, "아이디와 비밀번호를 입력하세요.");
         return;
      }
      // =======================================================================

      synchronized (oos) {
         try {
            // 서버로 요청 =========================================
            oos.writeObject("join#" + nick + "#" + pass);

            // 서버로부터 결과 값 읽어옴
            resp = (String) ois.readObject();
            System.out.println("[client] response : " + resp);

            // GUI 제어.
            String[] data = resp.split("#");
            if (data[0].equals("true")) {
               System.out.println("come");
               ui.pnLogin.tfid.setText("");
               ui.pnLogin.tfpw.setText("");
               ui.setSize(800, 800);

               ui.setTitle("다섯 줄을 향한 무한한 도전 ");
               ui.setLocationRelativeTo(null);
               System.out.println("hi my name is " + nick);
               ui.setContentPane(ui.pnRoom);
               ui.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
               sendStateRoomRequest();
          
            } else {
               ui.pnLogin.tfid.setText("");
               ui.pnLogin.tfpw.setText("");
               JOptionPane.showMessageDialog(ui, data[1]);
            }

         } catch (ClassNotFoundException | IOException e) { // 예외처리
            System.out.println("[client] network error " + e.toString());
         }
      }
   }

   public void sendLogoutRequest() {
      boolean resp = false;
      synchronized (oos) {
         try {
            oos.writeObject("logout");
            resp = (Boolean) ois.readObject();
            if (resp == true) {
               JOptionPane.showMessageDialog(ui, "로그아웃 되었습니다.");
               ui.setSize(400, 355);
               ui.setTitle("다섯 줄을 향한 무한한 도전 - 로그인");
               ui.setLocationRelativeTo(null);
               ui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               ui.setContentPane(ui.pnLogin);
            } else {

            }
         } catch (ClassNotFoundException | IOException e) {
         }
      }
   }
   
   @SuppressWarnings("unchecked")
   public void sendUserListRequest() {
      Set<Account> resp = null;
      synchronized (oos) {
         try {
            oos.writeObject("get");
            resp = (Set<Account>) ois.readObject();

            // 방목록을 List에 뿌려주기 위한 작업
            String[] ar = new String[resp.size()];
            int i = 0;

            for (Account a : resp) {
               ar[i++] = a.nick.toString();
            }
            ui.pnRoom.userList.setListData(ar);
         } catch (Exception e) {
            System.out.println(e.toString());
         }

      }
   }
   
   public void receive(String s) {
      ui.pnRoom.messageArea.append(s + "\n");
   }
   //방만들기 
   public void sendCreateRoomRequest(String title, String pw, boolean twouser) {
      Room resp = null;
      synchronized (oos) {
         try {
            oos.writeObject("createroom#" + title + "#" + twouser + "#" + pw);
            resp = (Room) ois.readObject();;
            System.out.println(resp);

            if (resp == null) {
               JOptionPane.showMessageDialog(ui, "이미 모든 방이 생성되어있습니다.");
            }  else {
               // 2인용 방 만들기 성공했을 때 게임 대기방에서 띄울 정보 셋팅
            	 try {
            		 PlayersInfo.MINE_LABEL = this.nick;
                     PApplet.main(Window.class);
                 } catch (NullPointerException e) {
                     e.printStackTrace();
                 }
            }
         } catch (ClassNotFoundException | IOException e) {
            System.out.println(e.toString());
         }
      }
   }

   //방목록갱신
   @SuppressWarnings("unchecked")
   public void sendStateRoomRequest() {
      List<Room> resp = null;
      synchronized (oos) {
         try {
            oos.writeObject("roomlist");
            resp = (List<Room>) ois.readObject();
            int i = 0;
            System.out.println(resp);
            if (!resp.isEmpty()) {
               do {
                  System.out.println("룸 !null");
                  ui.pnRoom.btList.get(i).setText("");
                  ui.pnRoom.btList.get(i).setEnabled(true);
                  ui.pnRoom.btList.get(i).setText("<html>제목 : " +resp.get(i).getTitle() + "<br/>" + "방장 : " + resp.get(i).getJoiner().get(0).getNick() + "<br/>" + "인원 : " + resp.get(i).getJoiner().size() + " / 2" + "<br/>" + "암호방 : " + (resp.get(i).getPass().equals("") ? "NO" : "YES") + "<br/>" + "방 상태 : " + (resp.get(i).isGameStart() ? "게임 중.." : "대기 중..")+  "</html>");               
                  if(resp.get(i).getJoiner().size() == 2) {
                     ui.pnRoom.btList.get(i).setEnabled(false);
                  }
                  if(!resp.get(i).isTwoUserRoom()) {
                     ui.pnRoom.btList.get(i).setText("<html>제목 : " +resp.get(i).getTitle() + "<br/>" + "방장 : " + resp.get(i).getJoiner().get(0).getNick() + "<br/>" + "인원 : " + resp.get(i).getJoiner().size() + " / 1" + "<br/>" + "암호방 : " + (resp.get(i).getPass().equals("") ? "NO" : "YES") + "<br/>" + "방 상태 : " + (resp.get(i).isGameStart() ? "게임 중.." : "대기 중..")+  "</html>");               
                     ui.pnRoom.btList.get(i).setEnabled(false);
                  }
                  i++;
               } while (i < resp.size());
            }
            while (i < 8) {
               System.out.println("룸 null");
               ui.pnRoom.btList.get(i).setEnabled(false);
               ui.pnRoom.btList.get(i).setText("");
               i++;
            }

         } catch (ClassNotFoundException | IOException e) {
         }
      }
   }

   public void sendMessage(String s) throws IOException {
      synchronized (oos) {
            oos.writeObject("chat" + s);
      }
   }
   
   public void sendWhisper(String s) throws IOException {
      synchronized (oos) {
            oos.writeObject("whis" + s);
      }
   }
   
   public void sendhow(String s) throws IOException {
      synchronized (oos) {
            oos.writeObject("how#" + s);
      }
   }
   
   public void sendFight(String s) throws IOException {
      synchronized (oos) {
            oos.writeObject("fight" + s);
      }
      gameStart();
   }
   
   public void sendRefuse(String s) throws IOException {
      synchronized (oos) {
            oos.writeObject("refuse#" + s + "#" + nick);
      }
   }
   
   public void gameStart() throws IOException
   {
	   try {
           PApplet.main(Window.class);
       } catch (NullPointerException e) {
           e.printStackTrace();
       }
  }

   
   public void sendFightRequest(String s) throws IOException {
      
      int select = JOptionPane.showConfirmDialog(null, s+"님으로 부터 결투신청이 왔습니다");
      if(select == 0) //방 만들기
      {
    	  try{
    		  gameStart();
          } catch (NullPointerException e) {
              e.printStackTrace();
          }
    	  flag = 0;
      }
      else if(select == 1 || select == 2)
      {
         sendRefuse(nick);
      }
   }
   
   public void sendload(String s) throws IOException {
       synchronized (oos) {
             oos.writeObject("load#" + s + "#" + nick);
       }
 }
   
   public void refuse(String s) {
      JOptionPane.showMessageDialog(null, s+"님이 결투를 거절하셨습니다.");
      flag = 0;
   }
   
   public void loading(String s) {
       JOptionPane.showMessageDialog(null, s+"님은 현재 다른 유저의 요청에 응답 중입니다.");
    }
   
   
    // 방 입장
    public void sendEnterRoomRequest(int idx) {        
        synchronized (oos) {
            try {
                oos.writeObject("enterroom#" + idx);
                Room resp = (Room) ois.readObject();
                if (!(resp == null)) {        
                    // 비밀번호 방이 아니고, 입장 성공시 띄울 화면
                	 try {
                		 PlayersInfo.MINE_LABEL = this.nick;
                        String oppo = resp.getJoiner().get(0).getNick();
                         PlayersInfo.OPPONENT_LABEL =  oppo;
                         PApplet.main(Window.class);
                     } catch (NullPointerException e) {
                         e.printStackTrace();
                     }
 
                } else {        // 비번방일 때
                    String pw = JOptionPane.showInputDialog("비밀번호를 입력하세요.");
                    if (!(pw.equals(""))) {
                        oos.writeObject("secretroom#" + pw);
                        resp = (Room) ois.readObject();
                        if(resp != null) {    
                            // 비밀번호 방인데 비밀번호 일치하여 입장 성공시 띄울 화면        
                        	 PlayersInfo.MINE_LABEL = this.nick;
                             String oppo = resp.getJoiner().get(0).getNick();
                              PlayersInfo.OPPONENT_LABEL =  oppo;
                              PApplet.main(Window.class);
                                 
                        }else {
                            JOptionPane.showMessageDialog(ui, "비밀번호가 틀렸습니다.");
                        }
                    }
                }
            }catch (ClassNotFoundException | IOException e) {
                    System.out.println(e.toString());
            }
        }
    }    
}
