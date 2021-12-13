package server;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


import common.Account;

public class UserAccountPool {
    private Map<String,Account> accountMap;        // ������ ���� ����
    private Set<Account> currentUser;            // ���� �������� ����
    private File file;                        

    // ������ ==========================================================
    public UserAccountPool() {
    	accountMap = new HashMap<>();
        // ������ ����� ���� ���� ȣ�� =====================================
    	file = new File("userData.text");
    	try {
    		FileReader fr = new FileReader(file);
    		BufferedReader br = new BufferedReader (fr);
    		String line;
    		
    		while ((line = br.readLine()) != null) {
    			String id, pw, name, sns, email;
    			
    		}
    		
    		
    	} catch (Exception e) {
    	
    	}

        // ============================================================

        
        currentUser = new HashSet<>();

    }
    // ================================================================


    // User�� ������ ������ �޼��� ����

    public String create(String id, String pass, String name) {    
        if(accountMap.containsKey(id)) {
            return "false#�̹� ���̵� �����մϴ�.";
        } else {
            accountMap.put(id, new Account(id, pass, name));
            System.out.println(accountMap);
            return "true";
        }
    }


    public String login(String id, String pass, SocketAddress sa) {        // �α��� 
        if(accountMap.containsKey(id)) {
            if(!currentUser.contains(new Account(id, "", ""))) {
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

    
    //============================================================
}