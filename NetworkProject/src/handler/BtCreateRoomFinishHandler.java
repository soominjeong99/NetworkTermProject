package handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client2.view.PlayersInfo;
import clientPanel.CreateRoomFrame;
 

 
public class BtCreateRoomFinishHandler implements ActionListener {
    CreateRoomFrame target;
    public BtCreateRoomFinishHandler(CreateRoomFrame c) {
        target = c;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        
        String title = target.tfTitle.getText().trim();
        String pw = "";
       
    // 2�ο� �� �����
            if(target.tfPw.isEditable() ) {
                if(String.valueOf(target.tfPw.getPassword()).equals("")) {
                    JOptionPane.showMessageDialog(target, "��й�ȣ�� �Է����ּ���.");
                    return;
                } else {
                    pw = String.valueOf(target.tfPw.getPassword()).trim();
                }
            }
            target.ui.net.sendCreateRoomRequest(title, pw, true);
            target.setVisible(false);
            
        }
    }
    



