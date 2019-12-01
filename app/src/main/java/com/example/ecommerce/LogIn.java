/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
Login Page -> 입력받은 ID와 PW를 기반으로 로그인을 진행하거나, 회원가입 화면으로 넘어갈 수 있게 하는 페이지
 */
package com.example.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity {

    Button signInBtn = null;
    Button logInBtn = null;
    EditText idText = null;
    EditText pwText = null;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signInBtn = (Button) findViewById(R.id.signInBtn);
        logInBtn = (Button) findViewById(R.id.logInBtn);
        idText = (EditText) findViewById(R.id.userID);
        pwText = (EditText) findViewById(R.id.userPW);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignIn.class);
                startActivity(intent);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sID = idText.getText().toString();
                final String sPW = pwText.getText().toString();
                if (sID.length() == 0 || sPW.length() == 0) { //값 없는경우 안넘어갈 것
                    Toast.makeText(getApplicationContext(), "값을 입력해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Query userQuery = dbr.child("user").orderByChild("user_id").equalTo(sID);
                    idText.setText("");
                    pwText.setText("");
                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    UserDB user = singleSnapshot.getValue(UserDB.class);
                                    System.out.println(user);
                                    if (user.getUser_pw().equals(sPW)) {
                                        if(user.getAdmin().length()>0){
                                            Intent intent = new Intent(getApplicationContext(), adminMain.class);
                                            intent.putExtra("user_id", sID);
                                            startActivity(intent);
                                            finish();
                                        }
                                        else{
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.putExtra("user_id", sID);
                                            startActivity(intent);
                                            finish();
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "비밀번호가 틀렸습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), "ID가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(), "취소됨", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }


}