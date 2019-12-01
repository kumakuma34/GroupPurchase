/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
UserAccount Table의 값을 DB에서 읽어오기 위한 class
 */
package com.example.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class UserAccountDB {
    private String user_id;
    private int account;
    UserAccountDB(){}

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getAccount() {
        return account;
    }

    UserAccountDB(String user_id, int account){
        this.user_id = user_id;
        this.account = account;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("account", account);
        return result;
    }

}