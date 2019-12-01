/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
OrderDB -> Order Table의 DB를 읽어 오기 위한 클래스

 */
package com.example.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class OrderDB {
    private String order_id;
    private String site;
    private String model;
    private int count;
    private int price;
    private String user_id;
    private int status;

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrder_id() {
        return order_id;
    }

    public String getSite() {
        return site;
    }

    public String getModel() {
        return model;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getStatus() {
        return status;
    }

    public OrderDB(){}
    public OrderDB(String oid, String site, String model, int cnt, int prc, String uid, int status){
        this.order_id = oid;
        this.site = site;
        this.model = model;
        this.count = cnt;
        this.price = prc;
        this.user_id = uid;
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("order_id", order_id);
        result.put("site", site);
        result.put("model", model);
        result.put("count", count);
        result.put("price", price);
        result.put("user_id", user_id);
        result.put("status", status);
        return result;
    }

}