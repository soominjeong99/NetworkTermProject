package handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import client.ClientUI;

public class BtExitHandler implements ActionListener {
	ClientUI ui;
	public BtExitHandler(ClientUI c) {
		ui = c;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// 종료버튼 
		int i = JOptionPane.showConfirmDialog(ui, "종료하시겠습니까?");
		if(i == 0) {
			ui.net.sendLogoutRequest();
			System.exit(0);
		}
	}
}
