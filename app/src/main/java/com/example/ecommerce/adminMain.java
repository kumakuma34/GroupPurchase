/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */

/*
adminMain.java -> 관리자 계정으로 접속 했을 때 보여지는 화면 입니다.
 */

package com.example.ecommerce;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adminMain extends AppCompatActivity {

    Context context = null;

    FloatingActionButton FABtn = null;
    String userID = null;
    TextView tvLogOut = null;
    TextView tvHello = null;

    LinearLayout univLinear = null;
    LinearLayout orderLinear = null;
    ScrollView univScroll = null;
    ScrollView orderScroll = null;

    ProgressBar pbSeoul = null;
    ProgressBar pbYonsei = null;
    ProgressBar pbKorea = null;
    ProgressBar pbChungang = null;
    ProgressBar pbInha = null;
    ProgressBar pbAjou = null;

    Button cbSeoul     = null;
    Button cbYonsei   = null;
    Button cbKorea    = null;
    Button cbChungang     = null;
    Button cbInha     = null;
    Button cbAjou     = null;

    String admin = null;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);

        Intent intent = getIntent();
        userID = intent.getStringExtra("user_id");
        context = this;

        univScroll = (ScrollView) findViewById(R.id.univScroll);
        orderScroll =  (ScrollView) findViewById(R.id.orderScroll);
        univLinear = (LinearLayout) findViewById(R.id.univLinear);
        orderLinear = (LinearLayout) findViewById(R.id.orderLinear);;

        pbSeoul = (ProgressBar) findViewById(R.id.pbSeoul);
        pbYonsei =(ProgressBar) findViewById(R.id.pbYonsei);
        pbKorea = (ProgressBar) findViewById(R.id.pbKorea);
        pbChungang = (ProgressBar) findViewById(R.id.pbChungang);
        pbInha = (ProgressBar) findViewById(R.id.pbInha);
        pbAjou = (ProgressBar) findViewById(R.id.pbAjou);

        cbSeoul     =(Button) findViewById(R.id.btnSeoul);
        cbYonsei   = (Button) findViewById(R.id.btnYonsei);
        cbKorea    = (Button) findViewById(R.id.btnKorea);
        cbChungang =(Button) findViewById(R.id.btnChungang);
        cbInha     = (Button) findViewById(R.id.btnInha);
        cbAjou     = (Button) findViewById(R.id.btnAjou);


        pbSeoul.setMax(100000);
        pbYonsei.setMax(100000);
        pbKorea.setMax(100000);
        pbChungang.setMax(100000);
        pbInha.setMax(100000);
        pbAjou.setMax(100000);

        tvLogOut = (TextView) findViewById(R.id.tvLogOut);
        tvLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                finish();
            }
        });

        ////Event Handler 구현 부분///////////
        cbSeoul.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pbSeoul.setProgress(0);
                final String key = admin + "_서울대";
                dbr.child("group_order").orderByChild("group_order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final GroupOrderDB grouporder = singleSnapshot.getValue(GroupOrderDB.class);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                grouporder.setStatus(1);
                                postValues = grouporder.toMap();
                                childUpdates.put("/group_order/" + key, postValues);
                                dbr.updateChildren(childUpdates);

                                dbr.child("order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                final OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                Map<String, Object> postValues = null;
                                                final String uID = order.getUser_id();

                                                dbr.child("user").orderByChild("user_id").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                                UserDB user = singleSnapshot.getValue(UserDB.class);
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                Map<String, Object> postValues = null;
                                                                String univ =user.getUser_univ();
                                                                if(univ.equals("서울대")){
                                                                    order.setStatus(1);
                                                                    postValues = order.toMap();

                                                                    String key2 = uID + "-" + admin + "-" + order.getModel();
                                                                    childUpdates.put("/order/" + key2, postValues);
                                                                    dbr.updateChildren(childUpdates);

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
                Drawable drawable = getResources().getDrawable(R.drawable.roundbtn);

                cbSeoul.setEnabled(false);
                cbSeoul.setText("주문 대기");
                cbSeoul.setBackground(drawable);

                setGroupOrderScroll();

            }
        });
        //서울대 주문 버튼의 onclickEventHandler
        cbYonsei.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pbYonsei.setProgress(0);
                final String key = admin + "_연세대";
                dbr.child("group_order").orderByChild("group_order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final GroupOrderDB grouporder = singleSnapshot.getValue(GroupOrderDB.class);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                grouporder.setStatus(1);
                                postValues = grouporder.toMap();
                                childUpdates.put("/group_order/" + key, postValues);
                                dbr.updateChildren(childUpdates);

                                dbr.child("order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                final OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                Map<String, Object> postValues = null;
                                                final String uID = order.getUser_id();

                                                dbr.child("user").orderByChild("user_id").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                                UserDB user = singleSnapshot.getValue(UserDB.class);
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                Map<String, Object> postValues = null;
                                                                String univ =user.getUser_univ();
                                                                if(univ.equals("연세대")){
                                                                    order.setStatus(1);
                                                                    postValues = order.toMap();

                                                                    String key2 = uID + "-" + admin + "-" + order.getModel();
                                                                    childUpdates.put("/order/" + key2, postValues);
                                                                    dbr.updateChildren(childUpdates);

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
                Drawable drawable = getResources().getDrawable(R.drawable.roundbtn);

                cbYonsei.setEnabled(false);
                cbYonsei.setText("주문 대기");
                cbYonsei.setBackground(drawable);

                setGroupOrderScroll();


            }
        });
        //연세대 주문 버튼의 onclickListener

        cbKorea.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pbKorea.setProgress(0);
                final String key = admin + "_고려대";
                dbr.child("group_order").orderByChild("group_order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final GroupOrderDB grouporder = singleSnapshot.getValue(GroupOrderDB.class);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                grouporder.setStatus(1);
                                postValues = grouporder.toMap();
                                childUpdates.put("/group_order/" + key, postValues);
                                dbr.updateChildren(childUpdates);

                                dbr.child("order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                final OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                Map<String, Object> postValues = null;
                                                final String uID = order.getUser_id();

                                                dbr.child("user").orderByChild("user_id").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                                UserDB user = singleSnapshot.getValue(UserDB.class);
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                Map<String, Object> postValues = null;
                                                                String univ =user.getUser_univ();
                                                                if(univ.equals("고려대")){
                                                                    order.setStatus(1);
                                                                    postValues = order.toMap();

                                                                    String key2 = uID + "-" + admin + "-" + order.getModel();
                                                                    childUpdates.put("/order/" + key2, postValues);
                                                                    dbr.updateChildren(childUpdates);

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
                Drawable drawable = getResources().getDrawable(R.drawable.roundbtn);

                cbKorea.setEnabled(false);
                cbKorea.setText("주문 대기");
                cbKorea.setBackground(drawable);

                setGroupOrderScroll();

            }
        });
        //고려대 주문 버튼의 onClickListener

        cbChungang.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pbChungang.setProgress(0);
                final String key = admin + "_중앙대";
                dbr.child("group_order").orderByChild("group_order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final GroupOrderDB grouporder = singleSnapshot.getValue(GroupOrderDB.class);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                grouporder.setStatus(1);
                                postValues = grouporder.toMap();
                                childUpdates.put("/group_order/" + key, postValues);
                                dbr.updateChildren(childUpdates);

                                dbr.child("order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                final OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                Map<String, Object> postValues = null;
                                                final String uID = order.getUser_id();

                                                dbr.child("user").orderByChild("user_id").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                                UserDB user = singleSnapshot.getValue(UserDB.class);
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                Map<String, Object> postValues = null;
                                                                String univ =user.getUser_univ();
                                                                if(univ.equals("중앙대")){
                                                                    order.setStatus(1);
                                                                    postValues = order.toMap();

                                                                    String key2 = uID + "-" + admin + "-" + order.getModel();
                                                                    childUpdates.put("/order/" + key2, postValues);
                                                                    dbr.updateChildren(childUpdates);

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
                Drawable drawable = getResources().getDrawable(R.drawable.roundbtn);

                cbChungang.setEnabled(false);
                cbChungang.setText("주문 대기");
                cbChungang.setBackground(drawable);

                setGroupOrderScroll();

            }
        });
        //중앙대 주문 버튼의 onClickListener

        cbAjou.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pbAjou.setProgress(0);
                final String key = admin + "_아주대";
                dbr.child("group_order").orderByChild("group_order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final GroupOrderDB grouporder = singleSnapshot.getValue(GroupOrderDB.class);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                grouporder.setStatus(1);
                                postValues = grouporder.toMap();
                                childUpdates.put("/group_order/" + key, postValues);
                                dbr.updateChildren(childUpdates);

                                dbr.child("order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                final OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                Map<String, Object> postValues = null;
                                                final String uID = order.getUser_id();

                                                dbr.child("user").orderByChild("user_id").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                                UserDB user = singleSnapshot.getValue(UserDB.class);
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                Map<String, Object> postValues = null;
                                                                String univ =user.getUser_univ();
                                                                if(univ.equals("아주대")){
                                                                    order.setStatus(1);
                                                                    postValues = order.toMap();

                                                                    String key2 = uID + "-" + admin + "-" + order.getModel();
                                                                    childUpdates.put("/order/" + key2, postValues);
                                                                    dbr.updateChildren(childUpdates);

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
                Drawable drawable = getResources().getDrawable(R.drawable.roundbtn);

                cbAjou.setEnabled(false);
                cbAjou.setText("주문 대기");
                cbAjou.setBackground(drawable);

                setGroupOrderScroll();

            }
        });
        //아주대 주문 버튼의 onClickListener

        cbInha.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                pbInha.setProgress(0);
                final String key = admin + "_인하대";
                dbr.child("group_order").orderByChild("group_order_id").equalTo(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                final GroupOrderDB grouporder = singleSnapshot.getValue(GroupOrderDB.class);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                grouporder.setStatus(1);
                                postValues = grouporder.toMap();
                                childUpdates.put("/group_order/" + key, postValues);
                                dbr.updateChildren(childUpdates);

                                dbr.child("order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                final OrderDB order = singleSnapshot.getValue(OrderDB.class);
                                                Map<String, Object> childUpdates = new HashMap<>();
                                                Map<String, Object> postValues = null;
                                                final String uID = order.getUser_id();

                                                dbr.child("user").orderByChild("user_id").equalTo(uID).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                                                UserDB user = singleSnapshot.getValue(UserDB.class);
                                                                Map<String, Object> childUpdates = new HashMap<>();
                                                                Map<String, Object> postValues = null;
                                                                String univ =user.getUser_univ();
                                                                if(univ.equals("인하대")){
                                                                    order.setStatus(1);
                                                                    postValues = order.toMap();

                                                                    String key2 = uID + "-" + admin + "-" + order.getModel();
                                                                    childUpdates.put("/order/" + key2, postValues);
                                                                    dbr.updateChildren(childUpdates);

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
                Drawable drawable = getResources().getDrawable(R.drawable.roundbtn);

                cbInha.setEnabled(false);
                cbInha.setText("주문 대기");
                cbInha.setBackground(drawable);

                setGroupOrderScroll();

            }
        });
        //인하대 주문 버튼의 onClickListener

        tvHello = (TextView) findViewById(R.id.tvHello);
        dbr.child("user").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        admin = singleSnapshot.getValue(UserDB.class).getAdmin();
                        tvHello.setText(admin+" 관리자 계정입니다");
                        setUnivScroll(admin);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });


       setGroupOrderScroll();
       //주문 내역이 동적으로 밑에 뜨게 해주는 함수
    }

    void setUnivScroll(String admin){
        dbr.child("group_order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                        GroupOrderDB gob = singleSnapshot.getValue(GroupOrderDB.class);
                        if(gob.getStatus()==0){
                            int value = singleSnapshot.getValue(GroupOrderDB.class).getPrice();
                            if(gob.getUniv().equals("서울대")){ pbSeoul.setProgress(value); }
                            if(gob.getUniv().equals("연세대")){ pbYonsei.setProgress(value);}
                            if(gob.getUniv().equals("고려대")){ pbKorea.setProgress(value);}
                            if(gob.getUniv().equals("중앙대")){ pbChungang.setProgress(value);}
                            if(gob.getUniv().equals("인하대")){ pbInha.setProgress(value);}
                            if(gob.getUniv().equals("아주대")){ pbAjou.setProgress(value);}
                        }
                    }
                    int red = ContextCompat.getColor(context, R.color.MYRED);
                    Drawable tmp = ContextCompat.getDrawable(context, R.drawable.roundbtn);
                    if(pbSeoul.getProgress()==pbSeoul.getMax()){
                        cbSeoul.setBackgroundColor(red);
                        cbSeoul.setText("주문");
                    }
                    else{
                        cbSeoul.setText("주문 대기");
                        cbSeoul.setEnabled(false);
                    }
                    if(pbYonsei.getProgress()==pbYonsei.getMax()){
                        cbYonsei.setBackgroundColor(red);
                        cbYonsei.setText("주문");
                    }
                    else{
                        cbYonsei.setText("주문 대기");
                        cbYonsei.setEnabled(false);
                    }
                    if(pbKorea.getProgress()==pbKorea.getMax()){
                        cbKorea.setBackgroundColor(red);
                        cbKorea.setText("주문");
                    }
                    else{
                        cbKorea.setText("주문 대기");
                        cbKorea.setEnabled(false);
                    }
                    if(pbChungang.getProgress()==pbChungang.getMax()){
                        cbChungang.setBackgroundColor(red);
                        cbChungang.setText("주문");
                    }
                    else{
                        cbChungang.setText("주문 대기");
                        cbChungang.setEnabled(false);
                    }
                    if(pbInha.getProgress()==pbInha.getMax()){
                        cbInha.setBackgroundColor(red);
                        cbInha.setText("주문");
                    }
                    else{
                        cbInha.setText("주문 대기");
                        cbInha.setEnabled(false);

                    }
                    if(pbAjou.getProgress()==pbAjou.getMax()){
                        cbAjou.setBackgroundColor(red);
                        cbAjou.setText("주문");
                    }
                    else{
                        cbAjou.setText("주문 대기");
                        cbAjou.setEnabled(false);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }
    //DB에서 주문을 확인 하고 , 주문 양에 따른 이벤트 처리
    //일절 금액 이상 주문이 모였다면 관리자가 주문을 넣을 수 있도록 주문 버튼을 활성화 시키고, 그렇지 않다면 버튼은 비활성화 된다.


    void setGroupOrderScroll() {
        dbr.child("user").orderByChild("user_id").equalTo(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                        String admin = singleSnapshot.getValue(UserDB.class).getAdmin();
                        dbr.child("group_order").orderByChild("site").equalTo(admin).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    orderLinear.removeAllViews();
                                    int i=0;
                                    LinearLayout[] llUniv = new LinearLayout[6];
                                    ImageView[] ivUniv = new ImageView[6];
                                    TextView[] tvUniv = new TextView[6];
                                    TextView[] tvStatus = new TextView[6];

                                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                        GroupOrderDB groupOrder = singleSnapshot.getValue(GroupOrderDB.class);
                                        int status = groupOrder.getStatus();
                                        String sText = "";
                                        if(status!=0){
                                            if(status == 1){ sText = " 주문이 완료되었습니다.";}
                                            else if(status==2){ sText = " 입금이 완료되었습니다."; }
                                            else if(status==3){ sText = " 배송이 완료되었습니다.";  }

                                            llUniv[i] = new LinearLayout(adminMain.this);
                                            llUniv[i].setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT ));
                                            llUniv[i].setOrientation(LinearLayout.HORIZONTAL);
                                            ivUniv[i] = new ImageView(adminMain.this);
                                            tvUniv[i] = new TextView(adminMain.this);
                                            tvStatus[i] = new TextView(adminMain.this);

                                            if(groupOrder.getUniv().equals("서울대")){
                                                ivUniv[i].setImageResource(R.drawable.seoul_univ);
                                                tvUniv[i].setText("서울대 ");
                                            }
                                            if(groupOrder.getUniv().equals("연세대")){
                                                ivUniv[i].setImageResource(R.drawable.yonsei_univ);
                                                tvUniv[i].setText("연세대 ");
                                            }
                                            if(groupOrder.getUniv().equals("고려대")){
                                                ivUniv[i].setImageResource(R.drawable.korea_univ);
                                                tvUniv[i].setText("고려대 ");}
                                            if(groupOrder.getUniv().equals("중앙대")){
                                                ivUniv[i].setImageResource(R.drawable.chungang_univ);
                                                tvUniv[i].setText("중앙대 ");
                                            }
                                            if(groupOrder.getUniv().equals("인하대")){
                                                ivUniv[i].setImageResource(R.drawable.inha_univ);
                                                tvUniv[i].setText("인하대 ");
                                            }
                                            if(groupOrder.getUniv().equals("아주대")){
                                                ivUniv[i].setImageResource(R.drawable.ajou_univ);
                                                tvUniv[i].setText("아주대 ");
                                            }
                                            tvUniv[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                                            tvStatus[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                                            tvStatus[i].setText(sText);
                                            llUniv[i].addView(ivUniv[i]);
                                            llUniv[i].addView(tvUniv[i]);
                                            llUniv[i].addView(tvStatus[i]);
                                            orderLinear.addView(llUniv[i]);

                                            llUniv[i].setGravity(Gravity.LEFT);
                                            ivUniv[i].getLayoutParams().height=250;
                                            ivUniv[i].getLayoutParams().width=250;
                                            ivUniv[i].requestLayout();
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {}
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }
        });
    }
}