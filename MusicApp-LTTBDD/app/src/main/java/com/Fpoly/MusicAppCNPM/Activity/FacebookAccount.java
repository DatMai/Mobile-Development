package com.Fpoly.MusicAppCNPM.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.facebook.FacebookAuthorizationException;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

public class FacebookAccount extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Boolean getToken;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CallbackManager mCallbackManager;
    UserInfor userInfor = UserInfor.getInstance();
    // testcomit

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getToken=getIntent().getBooleanExtra("getToken",false);
        mAuth = FirebaseAuth.getInstance();
        FacebookSdk.sdkInitialize(getApplicationContext());


        mCallbackManager = CallbackManager.Factory.create();

//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email"));
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","public_profile"));
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        Log.d("devS", "onSuccess" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }
                    @Override
                    public void onCancel() {
                        finish();// App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        Log.e("error",exception.toString());
                        finish();
                    }
                });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken accessToken) {
//        if (BuildConfig.DEBUG) {
//            FacebookSdk.setIsDebugEnabled(true);
//            FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);
//        }
        Log.d("devS",accessToken.getToken()) ;
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
//        if(mAuth.getCurrentUser()!=null) {
//            mAuth.signOut();
//        }
//        mAuth.getCurrentUser().unlink(mAuth.getCurrentUser().getProviderId());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("devS","succsess") ;
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("devS", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUI(user);
                } else {
                    Log.d("devS","error") ;
//                    updateUI(null);

                }

            }
        });
    }

    private void updateUI(final FirebaseUser user){
            db.collection("Users")
                    .whereEqualTo( "email", user.getEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            Log.d("devH", String.valueOf(task.isSuccessful()));
                            if (task.isSuccessful()) {
                                if(task.getResult().getDocuments().size() == 0){
                                    userInfor = new UserInfor(user.getUid(),user.getDisplayName(),null,"",user.getEmail(),false,true) ;
                                    userInfor.setID(mAuth.getCurrentUser().getUid());
                                    Log.d("devH", userInfor.getID());
                                    db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(userInfor);
                                }

                            } else {
                                Log.d("devH", "Error getting documents: ", task.getException());
                            }
                        }
                    });
          Log.d("devS",user.getDisplayName()) ;
        finish();
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Log.d("devS",isLoggedIn + "") ;
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser!=null) {
            updateUI(currentUser);
        }
    }

}

