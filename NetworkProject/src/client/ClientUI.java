package client;

import java.awt.event.ActionListener;

import javax.swing.JFrame;

import client2.main.Window;
import clientPanel.LoginPanel;
import clientPanel.PnInfoPanel;
import clientPanel.RoomPanel;
import clientPanel.SignupPanel;
import handler.BtChatHandler;
import handler.BtCreateRoomHandler;
import handler.BtEnterRoomHandler;
import handler.BtExitHandler;
import handler.BtHowHandler;
import handler.BtLogOutHandler;
import handler.BtLoginHandler;
import handler.BtSignUpCancelHandler;
import handler.BtSignupFinishHandler;
import handler.BtSingnUpHandler;
import handler.BtCountHandler;

 
public class ClientUI extends JFrame{ // Ŭ���̾�Ʈ GUI ����
    static public ClientNetwork net;
    public String ip;
    public LoginPanel pnLogin;
    public SignupPanel pnSignup;
    public RoomPanel pnRoom;
    
   
    
	//public MessageListFrame m;
	public PnInfoPanel pnInfo;
    public ClientUI(String ip) {
        this.ip = ip;
        setUIcomponent();
        addListeners();
        net = new ClientNetwork(this);
    }
 
 
    private void addListeners() {
        
        // ������ �߰��� ���� ��� ����
        // ȸ������ �гη� �̵��ϱ� ���� ������ ȣ�� =============
        ActionListener bsh = new BtSingnUpHandler(this);
        pnLogin.btsign.addActionListener(bsh);
        // ====================================================
 
        // ȸ������ �Ϸ�Ǿ��� �� ������ ȣ�� ====================
        ActionListener bsfh = new BtSignupFinishHandler(this);
        pnSignup.btCreate.addActionListener(bsfh);
        // ====================================================
 
         // ȸ������ �гο��� ��� ��ư Ŭ���� ������ ȣ�� =========
         ActionListener bch = new BtSignUpCancelHandler(this);
        pnSignup.btCancel.addActionListener(bch);
        // ====================================================
        ActionListener blgh = new BtLoginHandler(this);
        pnLogin.btlogin.addActionListener(blgh);
        
        ActionListener bcrh = new BtCreateRoomHandler(this);
        pnRoom.btCreateRoom.addActionListener(bcrh);
        
        // �� 8���� �� ��ư 
        for(int i=0;i<=7;i++) {
            ActionListener berh = new BtEnterRoomHandler(this);
            pnRoom.btList.get(i).addActionListener(berh);
        }
        
        //�α׾ƿ� ��ư ���� 
        ActionListener logout = new BtLogOutHandler(this);
        pnRoom.btLogOut.addActionListener(logout);
        
        //�α׾ƿ� ��ư ���� 
        ActionListener count = new BtCountHandler(this);
        pnRoom.btcount.addActionListener(count);
        //���� ��ư ����
        ActionListener beh = new BtExitHandler(this);
		pnRoom.btExit.addActionListener(beh);
		 //ä�� ���� 
		   ActionListener chat = new BtChatHandler(this);   
		      pnRoom.textField.addActionListener(chat);
		      //���� ����
		      ActionListener how = new BtHowHandler(this);
		      pnRoom.bthow.addActionListener(how);
		
	/*	MouseListener blmh = new BtListSendMessageHandler(this);
		pnRoom.userList.addMouseListener(blmh);
		
		ActionListener bsmlh = new BtShowMessageListHandler(this);
		pnInfo.btMessage.addActionListener(bsmlh);
*/
    }

    
   
 
    private void setUIcomponent() {
        // Client UI�� ����� �г� ����
    	
        setTitle("4 Bingo - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 355);
        pnSignup = new SignupPanel();        // ȸ������ �г� ��ü ����
        pnLogin = new LoginPanel();
        pnInfo = new PnInfoPanel();
        pnRoom = new RoomPanel(this);
        setContentPane(pnLogin);
    }


}