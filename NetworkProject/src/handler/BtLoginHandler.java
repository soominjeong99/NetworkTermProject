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
        // �Է��� ���̵�� ��й�ȣ�� ������ ���� =============================
        String nick = ui.pnLogin.tfid.getText().trim();
        String pass = String.valueOf(ui.pnLogin.tfpw.getPassword()).trim();
        ui.net.sendLoginRequest(nick, pass);
        // =================================================================
    }
}
