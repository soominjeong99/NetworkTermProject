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
        // ȸ������ �гη��̵� ======================
        ui.setTitle("4BINGO");
        ui.setLocationRelativeTo(null);
        ui.setSize(450, 700);
        ui.setContentPane(ui.pnSignup);
        // ======================================
    }
}
