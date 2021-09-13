package com.Fpoly.MusicAppCNPM.Database.DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


import androidx.annotation.NonNull;

import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.SucessCallBack;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.UserCallBack;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UserDAO {
    Context context;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<UserInfor> userInforArrayList = new ArrayList<>() ;
    UserInfor userInfor;

    public UserDAO(Context context) {
        this.context = context;
    }

//    Lấy thông tin người dùng khi đăng nhập thành công
    public void getUser(String UserID, final UserCallBack userCallBack){
        db.collection("Users")
                .whereEqualTo( "id",UserID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("TAG", document.getId() + " => " + document.getData());
                                userInfor = document.toObject(UserInfor.class) ;
                                Log.d("DDD",userInfor.toString()) ;
                                userInforArrayList.add(userInfor) ;
                            }
                            userCallBack.getCallback(userInforArrayList);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

//    Thêm người người dùng vào FireStore
    public void signUp(String ID, String Username, String Password, String Email, final SucessCallBack sucessCallBack){
        userInfor = new UserInfor(ID,Username,null,Password,Email,false,false) ;

        db.collection("Users").document(ID)
                .set(userInfor)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        sucessCallBack.getCallBack(true);
                        Log.d("test", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("test", "Error writing document", e);
                        sucessCallBack.getCallBack(false);
                    }
                });
    }


    private void ToastAnnotation(String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
