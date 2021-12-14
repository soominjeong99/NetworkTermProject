package handler;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;

import javax.swing.JOptionPane;

import client.ClientUI;

public class BtCountHandler implements ActionListener{
    ClientUI ui;
    public BtCountHandler(ClientUI c) {
        ui = c;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	String result = null;
        // ���� �о�� �� =====================
    	 try {
    		 
  	       // ����Ʈ ������ �����б�
  	        String filePath = "./rank.txt"; // ���и� ������ ��� ����
  	        FileInputStream fileStream = null; // ���� ��Ʈ��
  	        
  	        fileStream = new FileInputStream( filePath );// ���� ��Ʈ�� ����
  	        //���� ����
  	        byte[ ] readBuffer = new byte[fileStream.available()];
  	        while (fileStream.read( readBuffer ) != -1){}
  	        result = new String(readBuffer); //���

  	        fileStream.close(); //��Ʈ�� �ݱ�
  	    } catch (Exception a) {
  		   a.getStackTrace();
  	    }
       //
        JOptionPane.showMessageDialog(null, "����� ������ " + result + " �Դϴ�"); // �ڽ��� �������� ���

    }

}