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
        // 파일 읽어올 곳 =====================
    	 try {
    		 
  	       // 바이트 단위로 파일읽기
  	        String filePath = "./rank.txt"; // 승패를 저장할 대상 파일
  	        FileInputStream fileStream = null; // 파일 스트림
  	        
  	        fileStream = new FileInputStream( filePath );// 파일 스트림 생성
  	        //버퍼 선언
  	        byte[ ] readBuffer = new byte[fileStream.available()];
  	        while (fileStream.read( readBuffer ) != -1){}
  	        result = new String(readBuffer); //출력

  	        fileStream.close(); //스트림 닫기
  	    } catch (Exception a) {
  		   a.getStackTrace();
  	    }
       //
        JOptionPane.showMessageDialog(null, "당신의 전적은 " + result + " 입니다"); // 자신의 전적정보 출력

    }

}