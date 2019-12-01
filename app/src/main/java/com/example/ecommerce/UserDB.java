/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
User Table의 값을 DB에서 읽어 오기 위한 Table
 */

package com.example.ecommerce;

import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserDB {

    private String user_id;
    private String user_pw;
    private String user_univ;
    private String user_phone;
    private String user_email;
    private String admin;

    public UserDB(){}
    public UserDB(String id, String pw, String univ, String phone, String email){
        this.user_id = id;
        this.user_pw = pw;
        this.user_univ = univ;
        this.user_phone = phone;
        this.user_email = email;
        this.admin = "";
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setUser_pw(String user_pw) {
        this.user_pw = user_pw;
    }

    public void setUser_univ(String user_univ) {
        this.user_univ = user_univ;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_pw() {
        return user_pw;
    }

    public String getUser_univ() {
        return user_univ;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getAdmin() {
        return admin;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("user_id", user_id);
        result.put("user_pw", user_pw);
        result.put("user_univ", user_univ);
        result.put("user_phone", user_phone);
        result.put("user_email", user_email);
        result.put("admin", admin);
        return result;
    }
}