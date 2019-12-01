/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
OrderPage -> 소비자가 원하는 사이트와 모델에 대해 주문을 넣을 수 있는 페이지.
 */
package com.example.ecommerce;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.Map;

public class orderPage extends AppCompatActivity {
    String[] siteList = {
            "사이트 선택", "텐바이텐", "착한구두", "마켓컬리"
    };
    Spinner siteSpinner = null;
    String userUniv = null;
    ArrayAdapter<String> siteAdapter = null;
    EditText modelName = null;
    EditText quantity = null;
    EditText totalPrice = null;
    Button orderBtn = null;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();

    private static String getRandomString(int length)
    {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();
        String a = "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z";

        String chars[] =a.split(",");

        for (int i=0 ; i<length ; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        Intent intent = getIntent();
        final  String userID = intent.getStringExtra("user_id");

        siteSpinner = (Spinner) findViewById(R.id.siteName);
        siteAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                siteList
        );
        siteSpinner.setAdapter(siteAdapter);
        siteSpinner.setSelection(0);

        modelName = (EditText) findViewById(R.id.modelName);
        quantity = (EditText) findViewById(R.id.quantity);
        totalPrice = (EditText) findViewById(R.id.totalPrice);

        orderBtn = (Button)findViewById(R.id.orderBtn);
        dbr.child("user").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        UserDB user = singleSnapshot.getValue(UserDB.class);
                        userUniv = user.getUser_univ();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String site = (String)siteSpinner.getSelectedItem();
                String model = modelName.getText().toString();
                int count = Integer.valueOf(quantity.getText().toString());
                final int price = Integer.valueOf(totalPrice.getText().toString());

                String key = userID+"-"+ site + "-" + model;

                OrderDB order = new OrderDB(key,site, model,count, price, userID, 0);
                Map<String, Object> childUpdates = new HashMap<>();
                Map<String, Object> postValues = null;
                postValues = order.toMap();
                childUpdates.put("/order/" + key, postValues);
                dbr.updateChildren(childUpdates);


                final String g_key = site + '_' + userUniv;
                dbr.child("group_order").orderByChild("group_order_id").equalTo(g_key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                GroupOrderDB gorder = singleSnapshot.getValue(GroupOrderDB.class);
                                int price_now = gorder.getPrice();
                                singleSnapshot.getRef().child("price").setValue(price_now + price);
                                Log.d("Group Order : ", "group exist, changed data");
                            }

                        }
                        else{
                            GroupOrderDB grouporder = new GroupOrderDB(g_key, price, site, userUniv, 0);
                            Map<String, Object> childUpdates = new HashMap<>();
                            Map<String, Object> postValues = null;
                            postValues = grouporder.toMap();
                            childUpdates.put("/group_order/" + g_key, postValues);
                            dbr.updateChildren(childUpdates);
                            Log.d("Group Order : ", "made new tuple on firebase");

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user_id", userID);
                startActivity(intent);
                finish();

            }
        });





    }

}