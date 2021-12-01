package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import client.ClientUI;
 
 
public class BtHowHandler implements ActionListener{
 
    ClientUI ui;
    public BtHowHandler(ClientUI c) {
        ui = c;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) { 
        String message = "귓속말 : /w 유저이름 메세지\n 초대 : /f 유저이름";
        try {
         ui.net.sendhow(message);
      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }

        // =================================================================
    }
}