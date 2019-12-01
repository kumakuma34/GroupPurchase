/*
2019-2학기 전자상거래 최종 프로젝트
@12163786 장현수
@12161652 정명현
@12162892 김지은
 */
/*
SignIn -> 회원가입을 진행 하는 페이지.
 */
package com.example.ecommerce;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    String[] univList = {
            "소속기관", "서울대", "연세대", "고려대","중앙대", "인하대", "아주대"
    };
    Spinner univSpinner = null;
    ArrayAdapter<String> univAdapter = null;
    EditText idText = null;
    EditText pwText = null;
    EditText pwChkText = null;
    EditText emailText = null;
    EditText phoneText = null;

    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbr = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        idText = (EditText) findViewById(R.id.userID);
        pwText = (EditText) findViewById(R.id.userPW);
        pwChkText = (EditText) findViewById(R.id.userChkPW);
        emailText = (EditText) findViewById(R.id.userEmail);
        phoneText = (EditText) findViewById(R.id.userPhone);
        univSpinner = (Spinner) findViewById(R.id.userUniv);
        univAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,
                univList
        );
        univSpinner.setAdapter(univAdapter);
        univSpinner.setSelection(0);


        Button signInBtn = (Button) findViewById(R.id.signInBtn);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String sUniv = univSpinner.getSelectedItem().toString();
                final String sID = idText.getText().toString();
                final String sPW = pwText.getText().toString();
                final String sChkPW = pwChkText.getText().toString();
                final String sEmail = emailText.getText().toString();
                final String sPhone = phoneText.getText().toString();

                if (sID.length() == 0) {
                    Toast.makeText(getApplicationContext(), "ID를 입력해주세요", Toast.LENGTH_LONG).show();
                } else if (sPW.length() == 0) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
                } else if (!sChkPW.equals(sPW)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 다릅니다.", Toast.LENGTH_LONG).show();
                } else if (!isValidEmail(sEmail)) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
                }  else if (sPhone.length() == 0) {
                    Toast.makeText(getApplicationContext(), "전화번호를 입력해주세요", Toast.LENGTH_LONG).show();
                }else if (sUniv.equals(univList[0])) {
                    Toast.makeText(getApplicationContext(), "소속기관을 선택해주세요", Toast.LENGTH_LONG).show();
                } else {
                    dbr.child("user").orderByChild("user_id").equalTo(sID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                                    idText.setText("");
                                    Toast.makeText(getApplicationContext(), "이미 존재하는 id 입니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                //ID 중복체크하기
                                UserDB user = new UserDB(sID, sPW, sUniv, sPhone,sEmail);
                                Map<String, Object> childUpdates = new HashMap<>();
                                Map<String, Object> postValues = null;
                                postValues = user.toMap();
                                childUpdates.put("/user/" + sID, postValues);
                                dbr.updateChildren(childUpdates);

                                UserAccountDB ua = new UserAccountDB(sID, 0);
                                childUpdates = new HashMap<>();
                                postValues = ua.toMap();
                                childUpdates.put("/user_account/" + sID, postValues);
                                dbr.updateChildren(childUpdates);

                                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                                startActivity(intent);
                                finish();
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

    public boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }
}