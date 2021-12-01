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
       
    // 2인용 방 만들기
            if(target.tfPw.isEditable() ) {
                if(String.valueOf(target.tfPw.getPassword()).equals("")) {
                    JOptionPane.showMessageDialog(target, "비밀번호를 입력해주세요.");
                    return;
                } else {
                    pw = String.valueOf(target.tfPw.getPassword()).trim();
                }
            }
            target.ui.net.sendCreateRoomRequest(title, pw, true);
            target.setVisible(false);
            
        }
    }
    



