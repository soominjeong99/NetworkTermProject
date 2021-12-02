package server;

import java.io.File;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import common.Account;

public class UserAccountPool {
    private Map<String,Account> accountMap;        // ������ ���� ����
    private Set<Account> currentUser;            // ���� �������� ����
    private File address;                        
    
    // ������ ==========================================================
    public UserAccountPool() {
        
        // ������ ����� ���� ���� ȣ�� =====================================
       
 
        // ============================================================
        
        accountMap = new HashMap<>();
        currentUser = new HashSet<>();

    }
    // ================================================================
    
 
    // User�� ������ ������ �޼��� ����
    
    public String create(String id, String pass, String name, String email, String sns) {    
    	//System.out.println("������� ����? ���� �˷���\n");
        if(accountMap.containsKey(id)) {
            return "false#�̹� ���̵� �����մϴ�.";
        } else {
            accountMap.put(id, new Account(id, pass, name, email, sns));
            System.out.println(accountMap);
            return "true";
        }
    }
    
    
    public String login(String id, String pass, SocketAddress sa) {        // �α��� 
        if(accountMap.containsKey(id)) {
            if(!currentUser.contains(new Account(id, "", "", "", ""))) {
                if(accountMap.get(id).getPass().equals(pass)) {
                    currentUser.add(accountMap.get(id));
                    accountMap.get(id).setSocketAddress(sa);
                    return "true";
                }else {
                    return "false#��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
                }
            } else {
                return "false#�̹� �������� ���̵��Դϴ�.";
            }
        } else {
            return "false#�������� �ʴ� ���̵��Դϴ�.";
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
