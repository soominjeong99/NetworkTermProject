package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

 
public class BtSignUpCancelHandler implements ActionListener {
    ClientUI ui;
    public BtSignUpCancelHandler(ClientUI c) {
        ui = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 로그인 패널로 이동 =====================
        ui.setTitle("4BINGO");
        ui.setSize(400, 355);
        ui.setContentPane(ui.pnLogin);
        // =======================================
    }
}
