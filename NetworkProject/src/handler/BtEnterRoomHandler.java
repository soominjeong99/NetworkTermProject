package handler;
 
import java.awt.event.ActionEvent;
import client2.main.Window;
import processing.core.PApplet;
import client2.view.PlayersInfo;

import java.awt.event.ActionListener;

import client.ClientUI;


 
public class BtEnterRoomHandler implements ActionListener{
    ClientUI ui;
    public BtEnterRoomHandler(ClientUI c) {
        ui = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        // 해당 방의 위치를 찾아 서버로 전송하기 위한 동작 ====
        int idx = ui.pnRoom.btList.indexOf(e.getSource());
        ui.net.sendEnterRoomRequest(idx);
        
    }
    
}
