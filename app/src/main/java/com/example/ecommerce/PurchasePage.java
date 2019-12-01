/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
PurchasePage -> 주문을 넣은 물품에 대해 결제를 진행할 수 있는 페이지

 */
package com.example.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class PurchasePage extends AppCompatActivity {

    String userID = null;
    TextView tvUserID = null;
    TextView tvMoney = null;
    TextView tvModel = null;
    TextView tvPrice = null;
    Button   btnPurchase = null;
    Button   btnCancel = null;
    String sModel = null;
    Button btnCharge = null;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_page);

        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");
        sModel = intent.getStringExtra("model");
        //버튼 눌러서 값 넘길때

        tvUserID =   findViewById(R.id.tvUserID);
        tvMoney =    findViewById(R.id.tvMoney);
        tvModel =    findViewById(R.id.tvModel);
        btnPurchase =findViewById(R.id.btnPurchase);
        btnCancel =  findViewById(R.id.btnCancel);
        tvPrice=findViewById(R.id.tvPrice);
        btnCharge = findViewById(R.id.btnCharge);

        tvUserID.setText(userID + "님");
        tvModel.setText(sModel);
        dbr.child("user_account").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        tvMoney.setText(singleSnapshot.getValue(UserAccountDB.class).getAccount() + "won");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        dbr.child("order").orderByChild("order_id").equalTo(userID+"-" + sModel).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        OrderDB order = singleSnapshot.getValue(OrderDB.class);
                        tvPrice.setText(order.getPrice()+"");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dbr.child("user_account").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final int value = singleSnapshot.getValue(UserAccountDB.class).getAccount();
                                final String key = userID +"-"+ sModel;
                                dbr.child("order").orderByChild("order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                int newValue = value-order.getPrice();
                                                if (newValue>=0){

                                                    UserAccountDB ua = new UserAccountDB(userID, newValue);
                                                    Map<String, Object> childUpdates = new HashMap<>();
                                                    Map<String, Object> postValues = null;
                                                    postValues = ua.toMap();
                                                    childUpdates.put("/user_account/" + userID, postValues);
                                                    dbr.updateChildren(childUpdates);

                                                    order.setStatus(2);
                                                    childUpdates = new HashMap<>();
                                                    postValues = order.toMap();
                                                    childUpdates.put("/order/" + key, postValues);
                                                    dbr.updateChildren(childUpdates);

                                                    Toast.makeText(getApplicationContext(), "결제가 완료되었습니다..", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    intent.putExtra("user_id", userID);
                                                    startActivity(intent);
                                                    finish();
                                                }
                                                else{
                                                    Toast.makeText(getApplicationContext(), "돈이 부족합니다.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "결제가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_id", userID);
                startActivity(intent);
                finish();
            }
        });

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChargePage.class);
                intent.putExtra("user_id", userID);
                intent.putExtra("model", sModel);
                startActivity(intent);
                finish();
            }
        });

    }
}