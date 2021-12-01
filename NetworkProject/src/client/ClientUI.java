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

 
public class ClientUI extends JFrame{ // 클라이언트 GUI 제어
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
        
        // 리스너 추가를 위한 기능 정의
        // 회원가입 패널로 이동하기 위한 리스너 호출 =============
        ActionListener bsh = new BtSingnUpHandler(this);
        pnLogin.btsign.addActionListener(bsh);
        // ====================================================
 
        // 회원가입 완료되었을 때 리스너 호출 ====================
        ActionListener bsfh = new BtSignupFinishHandler(this);
        pnSignup.btCreate.addActionListener(bsfh);
        // ====================================================
 
         // 회원가입 패널에서 취소 버튼 클릭시 리스너 호출 =========
         ActionListener bch = new BtSignUpCancelHandler(this);
        pnSignup.btCancel.addActionListener(bch);
        // ====================================================
        ActionListener blgh = new BtLoginHandler(this);
        pnLogin.btlogin.addActionListener(blgh);
        
        ActionListener bcrh = new BtCreateRoomHandler(this);
        pnRoom.btCreateRoom.addActionListener(bcrh);
        
        // 총 8개의 방 버튼 
        for(int i=0;i<=7;i++) {
            ActionListener berh = new BtEnterRoomHandler(this);
            pnRoom.btList.get(i).addActionListener(berh);
        }
        
        //로그아웃 버튼 제어 
        ActionListener logout = new BtLogOutHandler(this);
        pnRoom.btLogOut.addActionListener(logout);
        
        //로그아웃 버튼 제어 
        ActionListener count = new BtCountHandler(this);
        pnRoom.btcount.addActionListener(count);
        //종료 버튼 제어
        ActionListener beh = new BtExitHandler(this);
		pnRoom.btExit.addActionListener(beh);
		 //채팅 제어 
		   ActionListener chat = new BtChatHandler(this);   
		      pnRoom.textField.addActionListener(chat);
		      //도움말 제어
		      ActionListener how = new BtHowHandler(this);
		      pnRoom.bthow.addActionListener(how);
		
	/*	MouseListener blmh = new BtListSendMessageHandler(this);
		pnRoom.userList.addMouseListener(blmh);
		
		ActionListener bsmlh = new BtShowMessageListHandler(this);
		pnInfo.btMessage.addActionListener(bsmlh);
*/
    }

    
   
 
    private void setUIcomponent() {
        // Client UI에 출력할 패널 정의
    	
        setTitle("다섯 줄을 향한 무한한 도전  - 로그인");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 355);
        pnSignup = new SignupPanel();        // 회원가입 패널 객체 생성
        pnLogin = new LoginPanel();
        pnInfo = new PnInfoPanel();
        pnRoom = new RoomPanel(this);
        setContentPane(pnLogin);
    }


}