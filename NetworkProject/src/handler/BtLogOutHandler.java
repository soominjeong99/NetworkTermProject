package handler;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.Socket;

import client.ClientNetwork;
import client.ClientUI;

 
public class BtLogOutHandler implements ActionListener{
	ClientUI ui;
	public BtLogOutHandler(ClientUI c) {
		ui = c;
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// �α׾ƿ� ���� ������ ������
		ui.net.sendLogoutRequest();
	}

}
