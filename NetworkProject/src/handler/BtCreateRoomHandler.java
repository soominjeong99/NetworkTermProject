package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import client.ClientUI;
import clientPanel.CreateRoomFrame;
 

 
public class BtCreateRoomHandler implements ActionListener { 
    ClientUI ui;
    public BtCreateRoomHandler(ClientUI c) {
        ui = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) { // ������ �������� ����
 
        CreateRoomFrame r = new CreateRoomFrame(ui);
        String [] titles = new String[] { "����÷����սô�.", "�ųʰ���","�ϴ� ��������.","���ǵ� �Ѱ���"};
        r.tfTitle.setText(titles[(int)(Math.random()*titles.length)]);
        r.setVisible(true);
        r.setLocationRelativeTo(null);
    }
}
