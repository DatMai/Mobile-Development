package com.Fpoly.MusicAppCNPM.Database.DAO;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.SongCallBack;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SearchDAO {
    Context context;
    private ArrayList<Song> songArrayList  = new ArrayList<>();
    private Song song = new Song();

    public SearchDAO(Context context) {
        this.context = context;
    }

    public void searchMusic( String name, final SongCallBack songCallBack) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").orderBy("Name").startAt(name).endAt(name+"\uf8ff").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
//                                Log.d("AAA", snapshot.getId() + " => " + snapshot.getData());
                                song = snapshot.toObject(Song.class) ;
                                songArrayList.add(song) ;
                            }
                            songCallBack.getCallBack(songArrayList);
                        } else {
//                            Log.w("AAA", "Error getting documents.", task.getException());
//                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    public void getMusicByKind( String kindID, final SongCallBack songCallBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").orderBy("ID", Query.Direction.ASCENDING).whereEqualTo("Type",kindID).limit(7).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                song = snapshot.toObject(Song.class) ;
                                songArrayList.add(song) ;
                            }
                            songCallBack.getCallBack(songArrayList);
                        } else {
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    public void getNextMusicByKind( String kindID,String musicID,final SongCallBack songCallBack) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Songs").orderBy("ID", Query.Direction.ASCENDING)
                .whereEqualTo("Type", kindID).startAfter(musicID).limit(7).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                song = snapshot.toObject(Song.class);
                                songArrayList.add(song);
                            }
                            songCallBack.getCallBack(songArrayList);
                        } else {
                            ToastAnnotation("Có Lỗi Xảy Ra");
                        }
                    }
                });
    }

    private void ToastAnnotation(String mesage){
        Toast.makeText(context,mesage,Toast.LENGTH_SHORT).show();
    }

}
