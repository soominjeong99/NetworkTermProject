package common;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class Account implements Serializable{  // �������� 
    public String nick;         // ���̵�
    private String pass;        // ��й�ȣ
    private String name;        // �̸�
    private SocketAddress socketAddress;    // ������ ip�� port
    private int win;            
    private int lose;
    private int draw;
    private int joinRoomIndex;            // �������� ��
    private List<Message> messagelist;    // �������� ����
    private int maxScore;                 // ������� �ִ� ����
    
    //======== ������ ================================
    public Account(String nick, String pass, String name) {
        this.nick = nick;
        this.pass = pass;
        this.name = name;
        win = lose = draw = 0;
        joinRoomIndex = -1;        // �濡 ���� ���� ����
        this.socketAddress = null;
        messagelist = new ArrayList<>();
        maxScore = 0;
    }
    
    //==============================================
    
    
    // ����� ������ ��� or ���ϱ� ���� �޼ҵ� ����
 
 
    // Getter and Setter ===============================
    public String getNick() {
        return nick;
    }
 
    public void setNick(String nick) {
        this.nick = nick;
    }
 
    public String getPass() {
        return pass;
    }
 
    public void setPass(String pass) {
        this.pass = pass;
    }
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public SocketAddress getSocketAddress() {
        return socketAddress;
    }
 
    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }
 
    public int getWin() {
        return win;
    }
 
    public void setWin(int win) {
        this.win = win;
    }
 
    public int getLose() {
        return lose;
    }
 
    public void setLose(int lose) {
        this.lose = lose;
    }
 
    public int getDraw() {
        return draw;
    }
 
    public void setDraw(int draw) {
        this.draw = draw;
    }
 
    public int getJoinRoomIndex() {
        return joinRoomIndex;
    }
 
    public void setJoinRoomIndex(int idx) {
        this.joinRoomIndex = idx;
    }
 
    public List<Message> getMessagelist() {
        return messagelist;
    }
 
    public void setMessagelist(List<Message> messagelist) {
        this.messagelist = messagelist;
    }
    
    public int getMaxScore() {
        return maxScore;
    }
 
    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }
    //=================================================
}
