package com.Fpoly.MusicAppCNPM.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.Fpoly.MusicAppCNPM.Database.DAO.UserDAO;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.SucessCallBack;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegistrationActivity extends AppCompatActivity {
    EditText et_name, et_email, et_pass;
    Button bt_register;
    TextView tv_login;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        et_name = findViewById(R.id.name);
        et_email = findViewById(R.id.email1);
        et_pass = findViewById(R.id.pass1);
        bt_register = findViewById(R.id.btnSignUp);
        tv_login = findViewById(R.id.tvSignIn);
        firebaseAuth = FirebaseAuth.getInstance();

        bt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = et_name.getText().toString().trim();
                final String email = et_email.getText().toString().trim();
                final String password = et_pass.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(), "Enter yout name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter your email address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter your password!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }
                // Thêm user vào firebase Authentication
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(RegistrationActivity.this, "Đăng Ký Thất Bại", Toast.LENGTH_SHORT).show();
                                    Log.e("login_error",task.getException().toString());
                                } else {
                                    final String ID = firebaseAuth.getCurrentUser().getUid();
                                    final UserDAO userDAO = new UserDAO(RegistrationActivity.this);
                                    // Gọi lại hàm Thêm user vào Firestore
                                    userDAO.signUp(ID, name, password, email, new SucessCallBack() {
                                        @Override
                                        public void getCallBack(Boolean isSucees) {
                                            if(isSucees){
                                                UserInfor userInfor = UserInfor.getInstance();
                                                userInfor.setID(ID);
                                                Toast.makeText(RegistrationActivity.this, "Đăng Ký Thành Công", Toast.LENGTH_SHORT).show();
                                                Log.e("login_error","Đăng Ký Thành Công");
                                                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                                                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                                                finish();
                                            }else{
                                                Toast.makeText(RegistrationActivity.this, "Đăng Ký Thất Bại", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        });
            }
        });
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
}
