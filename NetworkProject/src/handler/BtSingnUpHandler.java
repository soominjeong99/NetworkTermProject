package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;

 
public class BtSingnUpHandler implements ActionListener{
    ClientUI ui;
    
    public BtSingnUpHandler(ClientUI c) {
        ui = c;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        // 회원가입 패널로이동 ======================
        ui.setTitle("다섯 줄을 향한 무한한 도전 - 회원가입");
        ui.setLocationRelativeTo(null);
        ui.setSize(450, 700);
        ui.setContentPane(ui.pnSignup);
        // ======================================
    }
}
