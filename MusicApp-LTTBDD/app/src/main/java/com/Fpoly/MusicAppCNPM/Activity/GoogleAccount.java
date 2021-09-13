package com.Fpoly.MusicAppCNPM.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class GoogleAccount extends AppCompatActivity {
    private int RC_SIGN_IN = 10;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    UserInfor userInfor = UserInfor.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken(), account.getEmail(), account.getDisplayName());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately

                // ..
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken , final String email, final String name) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        if(mAuth.getCurrentUser()!=null) {
            mAuth.signOut();
        }
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
//                                userInfor.setID(mAuth.getCurrentUser().getUid());
                                db.collection("Users")
                                        .whereEqualTo( "email",email)
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                Log.d("devH", String.valueOf(task.isSuccessful()));
                                                if (task.isSuccessful()) {
                                                    if(task.getResult().getDocuments().size() == 0){
                                                        userInfor = new UserInfor(mAuth.getCurrentUser().getUid(),name,null,"",email,true,false) ;
                                                        userInfor.setID(mAuth.getCurrentUser().getUid());
                                                        Log.d("devH", userInfor.getID());
                                                        db.collection("Users").document(mAuth.getCurrentUser().getUid()).set(userInfor);
                                                    }

                                                } else {
                                                    Log.d("devH", "Error getting documents: ", task.getException());
                                                }
                                            }
                                        });
                                finish();
                            }
                        }
                    });
        }
    }

