/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
GroupOrder DB Table을 가져 오기 위한 클래스
 */
package com.example.ecommerce;

import java.util.HashMap;
import java.util.Map;

public class GroupOrderDB {

    private String group_order_id;
    private int price;
    private String site;
    private String univ;
    private int status;
    //0이면 주문안함 상태
    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public GroupOrderDB(){}

    public void setGroup_order_id(String group_order_id) {
        this.group_order_id = group_order_id;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public void setUniv(String univ) {
        this.univ = univ;
    }

    public String getGroup_order_id() {
        return group_order_id;
    }

    public int getPrice() {
        return price;
    }

    public String getSite() {
        return site;
    }

    public String getUniv() {
        return univ;
    }

    public GroupOrderDB(String goi, int price, String site , String univ, int status){
        this.group_order_id = goi;
        this.price = price;
        this.site = site;
        this.univ = univ;
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("group_order_id", group_order_id);
        result.put("price", price);
        result.put("site", site);
        result.put("univ", univ);
        result.put("status", status);
        return result;
    }


}