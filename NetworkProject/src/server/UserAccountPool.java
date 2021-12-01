package server;

import java.io.File;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import common.Account;

public class UserAccountPool {
    private Map<String,Account> accountMap;        // 유저의 계정 보관
    private Set<Account> currentUser;            // 현재 접속중인 유저
    private File address;                        
    
    // 생성자 ==========================================================
    public UserAccountPool() {
        
        // 서버에 저장된 계정 파일 호출 =====================================
       
 
        // ============================================================
        
        accountMap = new HashMap<>();
        currentUser = new HashSet<>();

    }
    // ================================================================
    
 
    // User의 정보를 제어할 메서드 정의
    
    public String create(String id, String pass, String name) {    
    	//System.out.println("여기까지 오나? 제발 알려줘\n");
        if(accountMap.containsKey(id)) {
            return "false#이미 아이디가 존재합니다.";
        } else {
            accountMap.put(id, new Account(id, pass, name));
            System.out.println(accountMap);
            return "true";
        }
    }
    
    
    public String login(String id, String pass, SocketAddress sa) {        // 로그인 
        if(accountMap.containsKey(id)) {
            if(!currentUser.contains(new Account(id, "", ""))) {
                if(accountMap.get(id).getPass().equals(pass)) {
                    currentUser.add(accountMap.get(id));
                    accountMap.get(id).setSocketAddress(sa);
                    return "true";
                }else {
                    return "false#비밀번호가 일치하지 않습니다.";
                }
            } else {
                return "false#이미 접속중인 아이디입니다.";
            }
        } else {
            return "false#존재하지 않는 아이디입니다.";
        }
    }

	public boolean logOut(Account user) {
		if(user == null) {
			return false;
		}
		user.setSocketAddress(null);
		if(currentUser.remove(user)) {
			return true;
		}
		return false;
	}

    // Getter and Setter ========================================
    public Map<String, Account> getAccountMap() {
        return accountMap;
    }
 
    public void setAccountMap(Map<String, Account> accountMap) {
        this.accountMap = accountMap;
    }
 
    public Set<Account> getCurrentUser() {
        return currentUser;
    }
 
    public void setCurrentUser(Set<Account> currentUser) {
        this.currentUser = currentUser;
    }
 
    public File getAddress() {
        return address;
    }
 
    public void setAddress(File address) {
        this.address = address;
    }
    //============================================================
}
