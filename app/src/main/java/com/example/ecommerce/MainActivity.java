/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
MainActivity -> 일반 소비자 계정으로 접속하였을 때 만나게 되는 페이지.
소비자는 사이트 별로 주문을 넣거나, 현재 우리 대학 사람들이 특정 사이트에 얼마 만큼의 주문을 넣었는지를 확인 할 수 있다.
또한 주문이 들어간 물품에 대해서는 결제를 진행할 수 있다.
 */
package com.example.ecommerce;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.acl.Group;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton FABtn = null;
    String userID = null;
    String userUniv = null;
    TextView tvLogOut = null;
    TextView tvHello = null;
    TextView univ = null;
    TextView tvMoney = null;
    LinearLayout layout = null;
    Context context;
    ProgressBar progressBar1, progressBar2, progressBar3;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");
        context = this;

        findViewById(R.id.editText3).setEnabled(false);

        tvLogOut = (TextView) findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });
        tvMoney = findViewById(R.id.tvMoney);
        dbr.child("user_account").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        tvMoney.setText(singleSnapshot.getValue(UserAccountDB.class).getAccount()+"원");
                    }}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FABtn = (FloatingActionButton) findViewById(R.id.FABtn);
        FABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), orderPage.class);
                intent.putExtra("user_id", userID);
                startActivity(intent);
                finish();
            }
        });


        tvHello = (TextView) findViewById(R.id.tvHello);
        tvHello.setText(userID + "님 안녕하세요");

        univ = (TextView)findViewById(R.id.user_Univ);
        progressBar1 = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar2 = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar3 = (ProgressBar)findViewById(R.id.progressBar3);

        progressBar1.setMax(100000);
        progressBar1.setProgress(0);
        progressBar2.setMax(100000);
        progressBar2.setProgress(0);
        progressBar3.setMax(100000);
        progressBar3.setProgress(0);
        dbr.child("user").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        UserDB user = singleSnapshot.getValue(UserDB.class);
                        userUniv = user.getUser_univ();
                        univ.setText(userUniv);
                        String g_key = "착한구두_" + userUniv;
                        Log.d("ID", g_key);
                        dbr.child("group_order").orderByChild("group_order_id").equalTo(g_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        GroupOrderDB gorder = singleSnapshot.getValue(GroupOrderDB.class);
                                        int price = gorder.getPrice();
                                        int status = gorder.getStatus();

                                        Log.d("DB", "firebase님 살려주세요");
                                        if(status == 0)
                                            progressBar2.setProgress(price);


                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        g_key = "텐바이텐_" + userUniv;
                        Log.d("ID", g_key);
                        dbr.child("group_order").orderByChild("group_order_id").equalTo(g_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        GroupOrderDB gorder = singleSnapshot.getValue(GroupOrderDB.class);
                                        int price = gorder.getPrice();
                                        int status = gorder.getStatus();

                                        Log.d("DB", "firebase님 살려주세요");
                                        if(status == 0)
                                            progressBar1.setProgress(price);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        g_key = "마켓컬리_" + userUniv;
                        Log.d("ID", g_key);
                        dbr.child("group_order").orderByChild("group_order_id").equalTo(g_key).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                        GroupOrderDB gorder = singleSnapshot.getValue(GroupOrderDB.class);
                                        int price = gorder.getPrice();
                                        int status = gorder.getStatus();
                                        Log.d("DB", "firebase님 살려주세요");
                                        if(status == 0)
                                            progressBar3.setProgress(price);

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
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //userUniv 알아내는 과정

        layout = (LinearLayout)findViewById(R.id.orderLinear);




        dbr.child("order").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        //order 내역이 있으면 여기서부터 각각 linear Activity 한개씩 생성
                        LinearLayout parentLL = new LinearLayout(MainActivity.this);
                        parentLL.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        parentLL.setOrientation(LinearLayout.HORIZONTAL);


                        OrderDB order = singleSnapshot.getValue(OrderDB.class);
                        String site = order.getSite();
                        int status = order.getStatus();
                        String model = order.getModel();

                        TextView topTV1 = new TextView(MainActivity.this);
                        topTV1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        topTV1.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        topTV1.setPadding(20, 10, 10, 10);
                        topTV1.setTextColor(Color.parseColor("#FF7200"));
                        topTV1.setTextSize(13);
                        topTV1.setText(site + "-" + model);
                        parentLL.addView(topTV1);

                        TextView topTV2 = new TextView(MainActivity.this);
                        topTV2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        topTV2.setBackgroundColor(Color.parseColor("#00FFFFFF"));
                        topTV2.setPadding(20, 10, 10, 10);
                        topTV2.setTextColor(Color.parseColor("#FF7200"));
                        topTV2.setTextSize(13);
                        if(status == 0)
                            topTV2.setText("아직 목표 주문량을 달성하지 못하였습니다");
                        else if(status == 1)
                            topTV2.setText("입금 대기 중 입니다.");
                        else if(status == 2)
                            topTV2.setText("배송 준비 중 입니다.");

                        parentLL.addView(topTV2);

                        if(status == 1){
                            final Button btn = new Button(context);
                            btn.setText("결제");
                            btn.setWidth(50);
                            btn.setHeight(50);
                            parentLL.addView(btn);
                            final String sModel = site+"-"+model;
                            btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), PurchasePage.class);
                                    intent.putExtra("user_id", userID);
                                    intent.putExtra("model", sModel);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }

                        layout.addView(parentLL);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}