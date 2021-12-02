package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

 
public class BtSignupFinishHandler implements ActionListener {
    ClientUI ui;
    public BtSignupFinishHandler(ClientUI c) {
        ui = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // ������� �Է� ������ ������ ������ ���� =========================
        String nick = ui.pnSignup.tfId.getText().trim();
        String name = ui.pnSignup.tfname.getText().trim();
        String pass = String.valueOf(ui.pnSignup.tfpw.getPassword());
        String repass = String.valueOf(ui.pnSignup.tfrpw.getPassword());
        String email = ui.pnSignup.tfemail.getText().trim();
        String sns = ui.pnSignup.tfsns.getText().trim();

        ui.net.sendCreateRequest(nick, pass, name, repass, email, sns);
        // ==============================================================
    }
    
}
