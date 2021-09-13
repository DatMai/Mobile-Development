package com.Fpoly.MusicAppCNPM.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.google.android.gms.tasks.OnFailureListener;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText txtUserName,txtPassword,txtEmail;
    Button btnSignIn;
    private static final String TAG = "FACELOG";
    private FirebaseAuth mAuth;
    private Button btnfacebook, btngmail;
    public static final String IDACCOUNT = "IDACCOUNT";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
//Ánh Xạ===========================================================================================================
        txtUserName = findViewById(R.id.edUserName);
        txtPassword = findViewById(R.id.edPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnfacebook = findViewById(R.id.btnfacebook);
        btngmail = findViewById(R.id.btngmail);
        TextView signUp_text = findViewById(R.id.tvSignUp);
        TextView Forgetpass = findViewById(R.id.tvForgetpass);
//Thao tác với các nút==============================================================================================
//Khi Nhấn vào nút đăng ký
            signUp_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signUp();
                }
            });
//Khi nhấn vào nút Quên Mật Khẩu
        Forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPasswordDialog();
            }
        });
//Khi Nhấn vào nút đăng nhập thông thường
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
//Khi Nhấn vào nút đăng nhập bằng FaceBook
        btnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faceboook();
            }
        });
//Khi Nhấn vào nút Đăng nhập bằng Gmail
        btngmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gmail();
            }
        });
    }


//Hàm Đăng Ký=======================================================================================================
    private void signUp() {
        startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
        finish();
    }
//Hàm Đăng Nhập Thông Thường
    private void login() {
        final String UserName = txtUserName.getText().toString();
        String Password = txtPassword.getText().toString();
        // Kiểm tra dữ liệu người dùng nhập vào
        if(UserName.isEmpty()){
            txtUserName.setError("Vui Lòng Nhập Tên Đăng Nhập");
            txtUserName.requestFocus();
        }
        else if(Password.isEmpty()){
            txtPassword.setError("Vui Lòng Nhập Mật Khẩu");
            txtPassword.requestFocus();
        }
        else if (UserName.isEmpty() && Password.isEmpty()){
            Toast.makeText(LoginActivity.this, "Các Ô Không Được Để Trống", Toast.LENGTH_SHORT).show();
        }
        // khi nhập đủ thông tin
        else if(!(UserName.isEmpty() && Password.isEmpty())){
            mAuth.signInWithEmailAndPassword(UserName,Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Đăng Nhập Lỗi, Vui Lòng Thử Lại", Toast.LENGTH_SHORT).show();
                    }
                    // thành công
                    else {
                        updateUI();
                    }
                }
            });
        }
        else{
            Toast.makeText(LoginActivity.this, "Có Lỗi Xảy Ra!", Toast.LENGTH_SHORT).show();
        }
    }
//    Save data
    private void saveData(String userID) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IDACCOUNT, userID);
        editor.apply();
    }
    //Khôi Phục Mật Khẩu================================================================================================
//    Dialog
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder= new AlertDialog.Builder(this);
        //set Layout linear layout
        LinearLayout linearLayout = new LinearLayout(this);
        //views to get in dialog
        final EditText emailEt = new EditText(this);
        emailEt.setHint("Please Enter Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,50,10,10);

        builder.setView(linearLayout);

        //button Recover
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                String email = emailEt.getText().toString().trim();
                beginRecover(email);
            }
        });
        //button Cancel
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Show Dialog
        builder.create().show();
    }
    // bắt đâu xử lí khôi phục
    private void beginRecover(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Email sent ", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this, "Fail .... Please Enter Again", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //get and show proper error message
                Toast.makeText(LoginActivity.this, "Sending email"+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            updateUI();
        } else {
            saveData("default");
        }
    }

//Chuyển Activity khi đăng nhập thành công
    private void updateUI() {
        saveData(mAuth.getCurrentUser().getUid()) ;
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setID(mAuth.getCurrentUser().getUid());
        Log.d("demo", (mAuth.getCurrentUser().getUid())) ;
        Intent HomePage = new Intent(LoginActivity.this, MainActivity.class);
        overridePendingTransition(R.anim.slide_out_left, R.anim.slide_in_right);
        startActivity(HomePage);
    }

//=================================================================================================================
    //Hàm Đăng Nhập bằng Gmail=========================================================================================
    private void gmail() {
        Intent intent =new Intent(this, GoogleAccount.class);
        intent.putExtra("getToken",false);
        startActivityForResult(intent,10);
    }
    //Hàm Đăng Nhập bằng FaceBook=======================================================================================
    private void faceboook() {
        Intent intent =new Intent(this, FacebookAccount.class);
        intent.putExtra("getToken",false);
        startActivityForResult(intent,10);
    }


}


