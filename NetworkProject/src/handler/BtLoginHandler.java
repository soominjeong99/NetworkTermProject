package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;
 
 
public class BtLoginHandler implements ActionListener{
 
    ClientUI ui;
    public BtLoginHandler(ClientUI c) {
        ui = c;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        // 입력한 아이디와 비밀번호를 서버로 전송 =============================
        String nick = ui.pnLogin.tfid.getText().trim();
        String pass = String.valueOf(ui.pnLogin.tfpw.getPassword()).trim();
        ui.net.sendLoginRequest(nick, pass);
        // =================================================================
    }
}
