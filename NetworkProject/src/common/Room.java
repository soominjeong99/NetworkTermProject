package common;

import java.io.Serializable;
import java.util.List;
import java.util.Vector;

public class Room implements Serializable {
    private int selectMusic = 0;      
    private String title;            // 방 제목
    private String pass;                // 방 비밀번호
    private List<Account> joiner;    // 방에 입장한 유저목록
    private boolean gameStart;        // 게임중인지 아닌지
    private boolean resultSetting;    // 방에 존재하는 사람이 모두 게임이 끝났는지 확인
    private boolean twoUserRoom;    // 2인용인지 체크
    
    //======== 생성자 ================================
    public Room(Account owner, String title, boolean twouser, String pass) {            // 비밀번호 방 생성자
        joiner = new Vector<>(); // 방에 들어간 사람들 관리
        joiner.add(owner);
        this.title = title;
        this.pass = pass;
        gameStart = false;
        resultSetting = false;
        twoUserRoom = twouser;
    }
    
    public Room(Account owner, String title, boolean twouser) {        // 비밀번호가 아닌 방 생성자
        this(owner, title, twouser, "");
        gameStart = false;
    }
    //===============================================
    
    // 방의 상태와 정보를 출력하기 위한 메소드 정의
 
    // Getter and Setter =====================================
    public int getSelectMusic() {
        return selectMusic;
    }
 
    public void setSelectMusic(int selectMusic) {
        this.selectMusic = selectMusic;
    }
 
    public String getTitle() {
        return title;
    }
 
    public void setTitle(String title) {
        this.title = title;
    }
 
    public String getPass() {
        return pass;
    }
 
    public void setPass(String pass) {
        this.pass = pass;
    }
 
    public List<Account> getJoiner() {
        return joiner;
    }
 
    public void setJoiner(List<Account> joiner) {
        this.joiner = joiner;
    }
 
    public boolean isGameStart() {
        return gameStart;
    }
 
    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }
    
    public boolean isResultSetting() {
        return resultSetting;
    }
 
    public void setResultSetting(boolean resultSetting) {
        this.resultSetting = resultSetting;
    }
    
    public boolean isTwoUserRoom() {
        return twoUserRoom;
    }
 
    public void setTwoUserRoom(boolean twoUserRoom) {
        this.twoUserRoom = twoUserRoom;
    }
    // ===================================================

	public void enterAccount(Account user) {  // 방 접속했을때 유저정보추가
		this.joiner.add(user);
	}
    
}
