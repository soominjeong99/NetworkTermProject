package client;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
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
   private static Socket soc; // �ٽ� ���� ����
   private ObjectOutputStream oos = null;
   private ObjectInputStream ois = null;
   private int flag = 0; //������ ���ÿ� 1:1 ��Ī ����
   private DatagramSocket ds; // ���� ����(receive��)
   private ClientUI ui; // Ŭ���̾�Ʈ GUI
   private Account user; // ���������
   private String nick;    // user�� �����ϱ� ���� nick
   private String salt; //��ȣȭ�� ����

   public ClientNetwork(ClientUI c) {
      this.ui = c; 
      System.out.println("������");
      try {
         soc = new Socket((c.ip), 9999); // ����1�� ����
         System.out.println("??");
         oos = new ObjectOutputStream(soc.getOutputStream());
         ois = new ObjectInputStream(soc.getInputStream());
         System.out.println("[client] connected to server");
         ds = new DatagramSocket(soc.getLocalPort()); // // TCP�� UDP�� ���� ��Ʈ�� ����� �� ����


      } catch (IOException e) {
         System.out.println("[client] network error " + e.toString()); // ����ó��
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
            //  ������ UDP ������ ���ۿ� ���� ���� ���� ó��
            case "userListChange":    // �α��� ���� ���
               sendUserListRequest();
               break;
            case "changerooms":        // �� ��� �ֽ�ȭ
               sendStateRoomRequest();
               break;
            case "message": //�������ݰ���
               receive(msg);
               break;
            case "fight": //1:1 ���� �ʴ� 
                String who = str[1];
                if (flag == 1)
                   sendload(who);
                else if(flag == 0) {
                    flag = 1;
                   sendFightRequest(who);
                } 
             break;
          case "refuse": // 1:1 ���� ����
             String sorry = str[1];
             refuse(sorry);
             break;
            case "load": // 1:1 ���� ��Ī���ε� �ٸ� ����� ��û�Ұ��
                String loadmem = str[1];
                loading(loadmem);
                break;

            }
         } catch (IOException e) { // ����ó��
            System.out.println("dp failed .. " + e.toString());
            ds.close();
            break;
         }

      }
   }

   // ==============================================================================

   public void sendCreateRequest(String nick, String pass, String name, String repass, String email, String sns) { // ȸ����û
      String resp = null;
      System.out.println(" [client] request : ");
      if (nick.trim().equals("") || pass.trim().equals("")) {
         JOptionPane.showMessageDialog(ui, "���̵�� ��й�ȣ�� �Է��ϼ���.");
         return;
      }
      if (name.trim().equals("")) {
          JOptionPane.showMessageDialog(ui, "�̸��� �Է��ϼ���.");
          return;
       }
      if (email.trim().equals("") || sns.trim().equals("")) {
          JOptionPane.showMessageDialog(ui, "�̸��ϰ� SNS�ּҸ� �Է��ϼ���.");
          return;
       }
      if (!pass.equals(repass)) {
         JOptionPane.showMessageDialog(ui, "��й�ȣ�� Ȯ���ϼ���");
         return;
      }
      if (!ui.pnSignup.rbAgree.isSelected()) {
         JOptionPane.showMessageDialog(ui, "����� �а� �������ּ���.");
         return;
      }
      synchronized (oos) {
         try {

            System.out.println(soc.getLocalPort());
            
            //��ȣȭ
          //salt�� ���� 
            salt = Salt();
            pass = SHA512(pass, salt);
            
            oos.writeObject("create#" + nick + "#" + pass + "#" + name + "#" + email+ "#" + sns);
            
            resp = (String)ois.readObject();
            System.out.println("[client] response : " + resp);
            
            //����� ������ ����
            OutputStream output = null;
            output = new FileOutputStream("./userData.txt", true);
            String userdata = null;
            userdata = nick + " / " + pass + " / " + name + " / " + email+ " / " + sns + "\r\n";
 			output.write(userdata.getBytes());
 			output.close();
 			
            String[] data = resp.split("#");
            // ���⼭ ui ����.
            if (data[0].equals("true")) {
               ui.pnSignup.tfId.setText(nick);
               ui.pnSignup.tfpw.setText("");
               ui.pnSignup.tfname.setText("");
               ui.pnSignup.tfrpw.setText("");
               ui.pnSignup.tfemail.setText("");
               ui.pnSignup.tfsns.setText("");
               JOptionPane.showMessageDialog(ui, "���̵� �����Ǿ����ϴ�.");
               // �α��� �������� �̵�.
               ui.setSize(400, 355);
               ui.setTitle("4BINGO - �α���");
               ui.setContentPane(ui.pnLogin);
               ui.pnLogin.tfid.setText(nick);

            } else {
               JOptionPane.showMessageDialog(ui, "�̹� �ߺ��� ���̵� �ֽ��ϴ�."); 
            }

         } catch (ClassNotFoundException | IOException e) { // ����ó�� 
            System.out.println("[client] network error " + e.toString());
         }
      }
   }

   public void sendLoginRequest(String nick, String pass) { // �α��� ��û
      this.nick = nick;
      String resp = null;
      System.out.println("[client] request : ");

      // ���̵�� ��й�ȣ�� ���� üũ ===========================================
      if (nick.trim().equals("") || pass.trim().equals("")) {
         JOptionPane.showMessageDialog(ui, "���̵�� ��й�ȣ�� �Է��ϼ���.");
         return;
      }
      // =======================================================================

      synchronized (oos) {
         try {
            // ������ ��û =========================================
            pass = SHA512(pass, salt);
             
            oos.writeObject("join#" + nick + "#" + pass);

            // �����κ��� ��� �� �о��
            resp = (String) ois.readObject();
            System.out.println("[client] response : " + resp);

            // GUI ����.
            String[] data = resp.split("#");
            if (data[0].equals("true")) {
               System.out.println("come");
               ui.pnLogin.tfid.setText("");
               ui.pnLogin.tfpw.setText("");
               ui.setSize(800, 800);

               ui.setTitle("4BINGO");
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

         } catch (ClassNotFoundException | IOException e) { // ����ó��
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
               JOptionPane.showMessageDialog(ui, "�α׾ƿ� �Ǿ����ϴ�.");
               ui.setSize(400, 355);
               ui.setTitle("4BINGO");
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

            // ������ List�� �ѷ��ֱ� ���� �۾�
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
   //�游��� 
   public void sendCreateRoomRequest(String title, String pw, boolean twouser) {
      Room resp = null;
      synchronized (oos) {
         try {
            oos.writeObject("createroom#" + title + "#" + twouser + "#" + pw);
            resp = (Room) ois.readObject();;
            System.out.println(resp);

            if (resp == null) {
               JOptionPane.showMessageDialog(ui, "�̹� ��� ���� �����Ǿ��ֽ��ϴ�.");
            }  else {
               // 2�ο� �� ����� �������� �� ���� ���濡�� ��� ���� ����
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

   //���ϰ���
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
                  System.out.println("�� !null");
                  ui.pnRoom.btList.get(i).setText("");
                  ui.pnRoom.btList.get(i).setEnabled(true);
                  ui.pnRoom.btList.get(i).setText("<html>���� : " +resp.get(i).getTitle() + "<br/>" + "���� : " + resp.get(i).getJoiner().get(0).getNick() + "<br/>" + "�ο� : " + resp.get(i).getJoiner().size() + " / 2" + "<br/>" + "��ȣ�� : " + (resp.get(i).getPass().equals("") ? "NO" : "YES") + "<br/>" + "�� ���� : " + (resp.get(i).isGameStart() ? "���� ��.." : "��� ��..")+  "</html>");               
                  if(resp.get(i).getJoiner().size() == 2) {
                     ui.pnRoom.btList.get(i).setEnabled(false);
                  }
                  if(!resp.get(i).isTwoUserRoom()) {
                     ui.pnRoom.btList.get(i).setText("<html>���� : " +resp.get(i).getTitle() + "<br/>" + "���� : " + resp.get(i).getJoiner().get(0).getNick() + "<br/>" + "�ο� : " + resp.get(i).getJoiner().size() + " / 1" + "<br/>" + "��ȣ�� : " + (resp.get(i).getPass().equals("") ? "NO" : "YES") + "<br/>" + "�� ���� : " + (resp.get(i).isGameStart() ? "���� ��.." : "��� ��..")+  "</html>");               
                     ui.pnRoom.btList.get(i).setEnabled(false);
                  }
                  i++;
               } while (i < resp.size());
            }
            while (i < 8) {
               System.out.println("�� null");
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
      
      int select = JOptionPane.showConfirmDialog(null, s+"������ ���� ������û�� �Խ��ϴ�");
      if(select == 0) //�� �����
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
      JOptionPane.showMessageDialog(null, s+"���� ������ �����ϼ̽��ϴ�.");
      flag = 0;
   }
   
   public void loading(String s) {
       JOptionPane.showMessageDialog(null, s+"���� ���� �ٸ� ������ ��û�� ���� ���Դϴ�.");
    }
   
   
    // �� ����
    public void sendEnterRoomRequest(int idx) {        
        synchronized (oos) {
            try {
                oos.writeObject("enterroom#" + idx);
                Room resp = (Room) ois.readObject();
                if (!(resp == null)) {        
                    // ��й�ȣ ���� �ƴϰ�, ���� ������ ��� ȭ��
                	 try {
                		 PlayersInfo.MINE_LABEL = this.nick;
                        String oppo = resp.getJoiner().get(0).getNick();
                         PlayersInfo.OPPONENT_LABEL =  oppo;
                         PApplet.main(Window.class);
                     } catch (NullPointerException e) {
                         e.printStackTrace();
                     }
 
                } else {        // ������� ��
                    String pw = JOptionPane.showInputDialog("��й�ȣ�� �Է��ϼ���.");
                    if (!(pw.equals(""))) {
                        oos.writeObject("secretroom#" + pw);
                        resp = (Room) ois.readObject();
                        if(resp != null) {    
                            // ��й�ȣ ���ε� ��й�ȣ ��ġ�Ͽ� ���� ������ ��� ȭ��        
                        	 PlayersInfo.MINE_LABEL = this.nick;
                             String oppo = resp.getJoiner().get(0).getNick();
                              PlayersInfo.OPPONENT_LABEL =  oppo;
                              PApplet.main(Window.class);
                                 
                        }else {
                            JOptionPane.showMessageDialog(ui, "��й�ȣ�� Ʋ�Ƚ��ϴ�.");
                        }
                    }
                }
            }catch (ClassNotFoundException | IOException e) {
                    System.out.println(e.toString());
            }
        }
    }
    
    public static String Salt() { 
    	String salt=""; 
    	try { 
    		SecureRandom random = SecureRandom.getInstance("SHA1PRNG"); 
    		byte[] bytes = new byte[16]; 
    		random.nextBytes(bytes); 
    		salt = new String(Base64.getEncoder().encode(bytes)); 
    		} 
    	catch (NoSuchAlgorithmException e) { e.printStackTrace(); } 
    	return salt; 
    	
    } 
    //sha512 
    public static String SHA512(String password, String hash) { 
    	String salt = hash+password; 
    	String hex = null; 
    	try { 
    		MessageDigest msg = MessageDigest.getInstance("SHA-512"); 
    		msg.update(salt.getBytes());
    		hex = String.format("%128x", new BigInteger(1, msg.digest())); 
    		} catch (NoSuchAlgorithmException e) { 
    			e.printStackTrace(); } 
    	return hex; 
    }

}
