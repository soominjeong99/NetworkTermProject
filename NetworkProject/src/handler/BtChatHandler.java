package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import client.ClientUI;
 
 
public class BtChatHandler implements ActionListener{
 
    ClientUI ui;
    public BtChatHandler(ClientUI c) {
        ui = c;
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        String message = ui.pnRoom.textField.getText();
        try {
           if(message.startsWith("/w ")) // 귓속말 하기
              ui.net.sendWhisper(message.substring(3));
           else if(message.startsWith("/f ")) // 1:1 신청
              ui.net.sendFight(message.substring(3));
           else
              ui.net.sendMessage(message);
      } catch (IOException e1) {
         // TODO Auto-generated catch block
         e1.printStackTrace();
      }
        ui.pnRoom.textField.setText("");
        // =================================================================
    }
}