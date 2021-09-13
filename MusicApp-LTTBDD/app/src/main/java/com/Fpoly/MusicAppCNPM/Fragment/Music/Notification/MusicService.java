package com.Fpoly.MusicAppCNPM.Fragment.Music.Notification;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.Fpoly.MusicAppCNPM.Activity.MainActivity;
import com.Fpoly.MusicAppCNPM.Model.Song;


public class MusicService extends Service   {
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_PREV = "PREVIOUS";
    public static final String ACTION_PLAY = "PLAY";
    ActionPlaying actionPlaying;
    public static Song song   ;
    private IBinder mBinder = new MyBinder() ;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        MainActivity.slidingUpPanelLayout();
        Log.e("Bind","Method");
        return mBinder;
    }

    public class MyBinder extends Binder {
        public MusicService getService(){
            return MusicService.this;

        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String actionName = intent.getStringExtra("myActionName");
        if (intent !=null && intent.getExtras()!=null) {
            song = intent.getParcelableExtra("Songs");
        }
        if (actionName != null){
            switch (actionName){
                case ACTION_PLAY:
                    if (actionPlaying != null){
                        actionPlaying.playClicked();
                    }
                    break;
                case ACTION_NEXT:
                    if (actionPlaying != null){
                        actionPlaying.nextClicked();
                    }
                    break;
                case ACTION_PREV:
                    if (actionPlaying != null){
                        actionPlaying.prevClicked();
                    }
                    break;

            }
        }
        return START_STICKY;
    }
    public void setCallBack(ActionPlaying actionPlaying){
            this.actionPlaying = actionPlaying;
    }
}
