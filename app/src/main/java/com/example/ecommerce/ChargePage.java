/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */

/*
ChargePage -> 유저가 잔액을 충전 할 수 있게 해주는 페이지.
 */
package com.example.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChargePage extends AppCompatActivity {

    String userID = null;
    String sModel = null;
    TextView tvUserID = null;
    TextView tvMoney = null;
    Button btnCharge = null;
    Button btnCancel = null;
    EditText etMoney = null;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge_page);

        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");
        sModel = intent.getStringExtra("model");

        tvUserID =   findViewById(R.id.tvUserID);
        tvMoney =    findViewById(R.id.tvMoney);
        btnCharge =findViewById(R.id.btnCharge);
        btnCancel =  findViewById(R.id.btnCancel);
        etMoney=findViewById(R.id.etMoney);

        tvUserID.setText(userID + "님");
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

        btnCharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sMoney = etMoney.getText().toString();
                if(sMoney.length()==0){
                    Toast.makeText(getApplicationContext(), "충전할 가격을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    final int money = Integer.parseInt(sMoney);
                    dbr.child("user_account").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    UserAccountDB ua = singleSnapshot.getValue(UserAccountDB.class);
                                    ua.setAccount(ua.getAccount()+money);

                                    Map<String, Object> childUpdates = new HashMap<>();
                                    Map<String, Object> postValues = null;
                                    postValues = ua.toMap();
                                    childUpdates.put("/user_account/" + userID, postValues);
                                    dbr.updateChildren(childUpdates);

                                    Toast.makeText(getApplicationContext(), "충전이 완료되었습니다..", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), PurchasePage.class);
                                    intent.putExtra("user_id", userID);
                                    intent.putExtra("model", sModel);
                                    startActivity(intent);
                                    finish();

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "충전이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), PurchasePage.class);
                intent.putExtra("user_id", userID);
                intent.putExtra("model", sModel);
                startActivity(intent);
                finish();
            }
        });
    }
}