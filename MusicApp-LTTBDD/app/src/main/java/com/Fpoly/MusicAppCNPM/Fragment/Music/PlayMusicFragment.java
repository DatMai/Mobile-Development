package com.Fpoly.MusicAppCNPM.Fragment.Music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.viewpager.widget.ViewPager;

import com.Fpoly.MusicAppCNPM.Fragment.UserPlayList.AddItemPlaylistFragment;
import com.Fpoly.MusicAppCNPM.Activity.MainActivity;
import com.Fpoly.MusicAppCNPM.Fragment.Music.Adapter.ViewPagerPlayListNhac;
import com.Fpoly.MusicAppCNPM.Database.DAO.FavoritesDAO;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.SucessCallBack;
import com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.ActionPlaying;
import com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.MusicService;
import com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.NotificationReceiver;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

import static android.content.Context.BIND_AUTO_CREATE;
import static android.content.Context.NOTIFICATION_SERVICE;
import static com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.ApplicationClass.ACTION_NEXT;
import static com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.ApplicationClass.ACTION_PLAY;
import static com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.ApplicationClass.ACTION_PREV;
import static com.Fpoly.MusicAppCNPM.Fragment.Music.Notification.ApplicationClass.CHANNEL_ID_2;

public class PlayMusicFragment extends BottomSheetDialogFragment implements ActionPlaying, ServiceConnection {
    public static ArrayList<Song> mangbaihat = new ArrayList<>();
    public static ViewPagerPlayListNhac adapternhac;
    TextView txttimesong,txttotaltimesong,songs_title,songs_artist_name ;
    SeekBar sktime;
    ImageButton imgbtnsuffle,imgbtnpre,imgbtnplay,imgbtnnext,imgbtnrepeat,imgbtnlike,imgbtnplaylist,play_button;
    ImageView songs_cover_one ;
//    ViewPager
    ViewPager viewpagerplaynhac;
    PlayMp3 playMp3 = new PlayMp3();
    Fragment_Dia_Nhac fragment_dia_nhac;
    Fragment_Play_Danh_Sach_Cac_Bai_Hat fragment_play_danh_sach_cac_bai_hat;
    UserInfor userInfor = UserInfor.getInstance();
//    Music
    public static MediaPlayer mediaPlayer = new MediaPlayer() ;
    public static boolean play = true ;
    public static boolean stop = true ;
    public static int position = 0 ;
    boolean repeat = false;
    boolean checkrandom = false;
    boolean next = false;
    Boolean isFavorites=false;
    MediaSessionCompat mediaSession;
    MusicService musicService = new MusicService();
    BottomNavigationView navBar ;
    private DotsIndicator dotsIndicator_music;
    private String TAG = "Lifecycle" ;
    public static  AddItemPlaylistFragment bottomSheetFragment = new AddItemPlaylistFragment();
    ProgressBar progressBarMusic ;



    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView") ;
        View root = inflater.inflate(R.layout.activity_play_music,container,false);
        RelativeLayout notclick = root.findViewById(R.id.notClick);
        notclick.setOnClickListener(new View.OnClickListener() { @Override public void onClick(View view) { MainActivity.slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED); }});
        getDataFromIntent();
        init(root);
        eventClick();

        return root;

    }
//    ================== 1 =====================
    //Hàm lấy dữ liệu
    public void getDataFromIntent() {
        Log.d(TAG,"getDataFromIntent") ;
        Bundle bundle = getArguments();
        //Nhận dữ liệu từ các fragment truyền qua
        Song song = bundle.getParcelable("Songs");
        if(song==null){
            mangbaihat = bundle.getParcelableArrayList("MultipleSongs");
        } else{
            mangbaihat.clear();
            mangbaihat.add(song);
        }
        // get position hiện tại của bài hát
        if (bundle.getInt("position") != 0) {
            position = bundle.getInt("position");
        } else {
            position = 0;
        }
    }
    //Hàm ánh xạ, và chơi nhạc ban đầu
    private void init(View root) {
        play = true ;
        progressBarMusic = root.findViewById(R.id.progressBarMusic) ;
        progressBarMusic.setProgress(0);
        dotsIndicator_music = root.findViewById(R.id.dotsIndicator_music) ;
        navBar = root.findViewById(R.id.nav_view);
        mediaSession = new MediaSessionCompat(getContext(),"PlayerAudio");
        //Các nút thao tác với danh sách bài hát
        imgbtnlike = root.findViewById(R.id.imgbtnlike);
        imgbtnplaylist = root.findViewById(R.id.imgbtnplaylist);
        //Các nút thao tác với bài hát
        txttimesong = root.findViewById(R.id.tvtimesong);
        txttotaltimesong = root.findViewById(R.id.tvtotaltimesong);
        sktime = root.findViewById(R.id.sbsong);
        imgbtnsuffle = root.findViewById(R.id.imgbtnsuffle);
        imgbtnpre = root.findViewById(R.id.imgbtnpre);
        imgbtnplay = root.findViewById(R.id.imgbtnplay);
        imgbtnnext=root.findViewById(R.id.imgbtnnext);
        imgbtnrepeat=root.findViewById(R.id.imgbtnrepeat);
        viewpagerplaynhac= root.findViewById(R.id.vpPlaynhac);
        songs_cover_one = root.findViewById(R.id.songs_cover_one)  ;
        songs_title = root.findViewById(R.id.songs_title)  ;
        songs_artist_name = root.findViewById(R.id.songs_artist_name)  ;
        play_button = root.findViewById(R.id.play_button)  ;

        // ViewPagerPlaymusic
        fragment_dia_nhac = new Fragment_Dia_Nhac();
        fragment_play_danh_sach_cac_bai_hat = new Fragment_Play_Danh_Sach_Cac_Bai_Hat();
        adapternhac = new ViewPagerPlayListNhac(getChildFragmentManager());
        adapternhac.addFragment(fragment_dia_nhac);
        adapternhac.addFragment(fragment_play_danh_sach_cac_bai_hat);
        viewpagerplaynhac.setAdapter(adapternhac);
        dotsIndicator_music.setViewPager(viewpagerplaynhac);
        fragment_dia_nhac = (Fragment_Dia_Nhac) adapternhac.getItem(0);

        // Gán ảnh và text
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(adapternhac.getItem(0)!=null){
                    if(mangbaihat.size()>0){
                        fragment_dia_nhac.Playnhac(mangbaihat.get(0).getImage());
                        handler.removeCallbacks(this);
                    }else{
                        handler.postDelayed(this,300);
                    }
                }
            }
        },500);
        Picasso.get().load(mangbaihat.get(position).getImage()).into(songs_cover_one);
        songs_title.setText(mangbaihat.get(position).getName());
        songs_artist_name.setText(mangbaihat.get(position).getSinger());

        //Kiểm tra có nằm trong danh sách bài hát yêu thích hay không
        checkDuplicate(mangbaihat.get(position).getID());
        if(isFavorites){
            imgbtnlike.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            imgbtnlike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }

        //Chạy bài hát và đổi hình ảnh nút play
        playMp3.execute(mangbaihat.get(position).getLink());
        imgbtnplay.setImageResource(R.drawable.ic_play);
        play_button.setImageResource(R.drawable.ic_baseline_play_arrow_24);
        showNotification(R.drawable.ic_pause_circle_filled_black_24dp);


    }
    //Hàm bắt các sự kiện nhấn của các nút
    private void eventClick() {
        //Nút Chơi Nhạc
        imgbtnplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playClicked();
            }
        });
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playClicked();
            }
        });

        //Khi nhấn vào nút yêu thích(trái tim)
        imgbtnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFavorites){
                    ItemFavorites(mangbaihat.get(position).getID(),false);
                }else{
                    ItemFavorites(mangbaihat.get(position).getID(),true);
                }
            }
        });
        imgbtnplaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPlayList(mangbaihat.get(position).getID());
            }
        });

        //Nút Lặp
        imgbtnrepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat == false){
                    if (checkrandom == true){
                        //chỉ được sử dụng 1 trong 2 hoặc repeat hoặc random
                        checkrandom = false;
                        imgbtnrepeat.setImageResource(R.drawable.ic_loop_black_24dp);
                        imgbtnsuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                    }
                    //set lại giá trị repeat và hình ảnh nút tương ứng
                    imgbtnrepeat.setImageResource(R.drawable.ic_loop_black_24dp2);
                    repeat = true;
                }else {
                    imgbtnrepeat.setImageResource(R.drawable.ic_loop_black_24dp);
                    repeat = false;
//                    imgbtnrepeat.setBackgroundColor("#00000000");
                }
            }
        });

        //nút nghe Ngẫu Nhiên
        imgbtnsuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkrandom == false) {
                    if (repeat == true) {
                        repeat = false;
                        imgbtnsuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                        imgbtnrepeat.setImageResource(R.drawable.ic_loop_black_24dp);
                    }
                    imgbtnsuffle.setImageResource(R.drawable.ic_shuffle_black_24dp2);
                    checkrandom = true;
                } else {
                    imgbtnsuffle.setImageResource(R.drawable.ic_shuffle_black_24dp);
                    checkrandom = false;

                }
            }
        });

        //Hàm Khi Kéo Seekbar bài hát sẽ tua đến vị trí tương ứng
        sktime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaPlayer.seekTo(seekBar.getProgress());
            }
        });

        //Nút Tiến
        imgbtnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarMusic.setVisibility(View.VISIBLE);
                imgbtnplay.setVisibility(View.GONE);
                nextClicked();
                play = true ;
                Fragment_Play_Danh_Sach_Cac_Bai_Hat.playnhacAdapter.notifyDataSetChanged();

            }
        });

        //Nút Lùi
        imgbtnpre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBarMusic.setVisibility(View.VISIBLE);
                imgbtnplay.setVisibility(View.GONE);
                prevClicked();
                play = true ;
                Fragment_Play_Danh_Sach_Cac_Bai_Hat.playnhacAdapter.notifyDataSetChanged();

            }
        });
    }

//    ================== 2 =====================
    //Hàm play bài hát
    @Override
    public void playClicked() {
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            play = false ;
            Fragment_Play_Danh_Sach_Cac_Bai_Hat.playnhacAdapter.notifyDataSetChanged();
            imgbtnplay.setImageResource(R.drawable.ic_play);
            play_button.setImageResource(R.drawable.ic_baseline_play_arrow_24);
            showNotification(R.drawable.ic_play);
        }else {
            mediaPlayer.start();
            play = true;
            Fragment_Play_Danh_Sach_Cac_Bai_Hat.playnhacAdapter.notifyDataSetChanged();
            play_button.setImageResource(R.drawable.ic_baseline_pause_24_white);
            imgbtnplay.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            showNotification(R.drawable.ic_pause_circle_filled_black_24dp);
        }
    }
    //Hàm chuyển về bài hát
    @Override
    public void nextClicked() {
        if (mangbaihat.size()>0){
            //nếu có bài hát nào đang chạy sẽ dừng bài hát đó
            if (mediaPlayer.isPlaying()||mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            //Kiểm tra vị trí phải bé hơn mảng bài hát
            if (position < (mangbaihat.size())){
                //set lại hình ảnh cho nút chơi nhạc
                imgbtnplay.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                play_button.setImageResource(R.drawable.ic_baseline_pause_24_white);
                position++;
                // cap nhat lai Viewpager
                adapternhac.notifyDataSetChanged();
                //nếu người dùng đang chọn chế dộ lặp lại
                if (repeat == true){
                    if (position == 0 ){
                        position = mangbaihat.size();
                    }
                    position -=1;
                }
                if (checkrandom == true){
                    Random random = new Random();
                    int index = random.nextInt(mangbaihat.size());
                    if (index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                if (position > (mangbaihat.size() - 1)){
                    position = 0;
                }
                // phát nhạc
                new PlayMp3().execute(mangbaihat.get(position).getLink());
                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImage());
                Picasso.get().load(mangbaihat.get(position).getImage()).into(songs_cover_one);
                songs_title.setText(mangbaihat.get(position).getName());
                songs_artist_name.setText(mangbaihat.get(position).getSinger());
                //Kiểm tra bài hát có nằm trong danh sách yêu thích hay không
                checkDuplicate(mangbaihat.get(position).getID());
                //Đổi màu trái tim tùy theo bài hát
                if(isFavorites){
                    imgbtnlike.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else{
                    imgbtnlike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
                showNotification(R.drawable.ic_pause_circle_filled_black_24dp);
                UpdateTime();
            }

            imgbtnpre.setClickable(false);
            imgbtnnext.setClickable(false);
            Handler handler1 = new Handler();
            handler1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    imgbtnpre.setClickable(true);
                    imgbtnnext.setClickable(true);
                }
            },5000);
        }
    }
    //Hàm chuyển về bài hát trước đó trong danh sách
    @Override
    public void prevClicked() {
        if (mangbaihat.size()>0){
            if (mediaPlayer.isPlaying()||mediaPlayer!=null){
                mediaPlayer.stop();
                mediaPlayer.release();
                mediaPlayer = null;
            }
            // xét lại biến positon
            if (position < (mangbaihat.size())){
                imgbtnplay.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                imgbtnplay.setImageResource(R.drawable.ic_baseline_pause_24_white);
                position--;
                adapternhac.notifyDataSetChanged();
                if (position <0){
                    position = mangbaihat.size() - 1;
                }
                if (repeat == true){
                    position +=1;
                }
                if (checkrandom == true){
                    Random random = new Random();
                    int index = random.nextInt(mangbaihat.size());
                    if (index == position){
                        position = index - 1;
                    }
                    position = index;
                }
                // Phát nhạc và thay đổi dữ liệu
                new PlayMp3().execute(mangbaihat.get(position).getLink());
                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImage());
                Picasso.get().load(mangbaihat.get(position).getImage()).into(songs_cover_one);
                songs_title.setText(mangbaihat.get(position).getName());
                songs_artist_name.setText(mangbaihat.get(position).getSinger());
                //Kiêm tra bài hát có nằm trong danh sách yêu thích hay không
                checkDuplicate(mangbaihat.get(position).getID());
                //Đổi màu trái tim tùy theo danh sách bài hát
                if(isFavorites){
                    imgbtnlike.setImageResource(R.drawable.ic_favorite_black_24dp);
                }else{
                    imgbtnlike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
                showNotification(R.drawable.ic_pause_circle_filled_black_24dp);
                UpdateTime();
            }
        }
        imgbtnpre.setClickable(false);
        imgbtnnext.setClickable(false);
        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                imgbtnpre.setClickable(true);
                imgbtnnext.setClickable(true);
            }
        },5000);
    }

//    ================== 3 =====================
    //Hàm format thời gian
    private void TimeSong(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        txttotaltimesong.setText(simpleDateFormat.format(mediaPlayer.getDuration()));
        sktime.setMax(mediaPlayer.getDuration());
    }
    //Hàm cập nhập thời gian bài hát
    private void UpdateTime(){
        //Handler cập nhập thời gian bài hát, được gọi mỗi 300ms
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //kiểm tra biến stop, biến này true khi bấm thoát khỏi fragment này,luồng lấy thời gian bài hát chỉ chạy khi biến này false
                if(play){
                    //kiểm tra sự tồn tại của mediaplayer
                    if (mediaPlayer !=null){
                        try {
                            sktime.setProgress(mediaPlayer.getCurrentPosition());
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                            txttimesong.setText(simpleDateFormat.format(mediaPlayer.getCurrentPosition()));
                            //chạy lại hàm trên
                            handler.postDelayed(this,300);
                            //khi bài hát kết thúc
                            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    if (mangbaihat.size()==1) {
                                        if (repeat == true) {
                                            next = true;
                                        } else {
                                            next = false ;
                                            Fragment_Play_Danh_Sach_Cac_Bai_Hat.playnhacAdapter.notifyDataSetChanged();
                                            if (position < (mangbaihat.size())) {
                                                imgbtnplay.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                                                play_button.setImageResource(R.drawable.ic_baseline_pause_24_white);
                                                position++;
                                                if (repeat == true) {
                                                    if (position == 0) {
                                                        position = mangbaihat.size();
                                                    }
                                                    position -= 1;
                                                }
                                                // phát ngẫu nhiên
                                                if (checkrandom == true) {
                                                    Random random = new Random();
                                                    int index = random.nextInt(mangbaihat.size());
                                                    if (index == position) {
                                                        position = index - 1;
                                                    }
                                                    position = index;
                                                }
                                                if (position > (mangbaihat.size() - 1)) {
                                                    position = 0;
                                                }
                                                new PlayMp3().execute(mangbaihat.get(position).getLink());

                                                fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImage());
                                                Picasso.get().load(mangbaihat.get(position).getImage()).into(songs_cover_one);
                                                songs_title.setText(mangbaihat.get(position).getName());
                                                songs_artist_name.setText(mangbaihat.get(position).getSinger());
                                            }
                                            //sau khi click xong vô hiệu 2 nút;
                                            imgbtnpre.setClickable(false);
                                            imgbtnnext.setClickable(false);
                                            //tạo một luồng set lại 2 nút
                                            Handler handler1 = new Handler();
                                            handler1.postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    imgbtnpre.setClickable(true);
                                                    imgbtnnext.setClickable(true);
                                                }
                                            }, 5000);
                                        }
                                    }else  {
                                        next = true ;
                                        play = true ;
                                        Fragment_Play_Danh_Sach_Cac_Bai_Hat.playnhacAdapter.notifyDataSetChanged();
                                    }
                                    try{
                                        //tạm ngủ thread này 1s
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        } catch (Exception e) {}
                        }

                }else {
                    handler.removeCallbacks(this);
                }
            }
        },300);
        //Kiểm tra nếu next = true
        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (next == true) {

                    if (position < (mangbaihat.size())) {
                        imgbtnplay.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                        play_button.setImageResource(R.drawable.ic_baseline_pause_24_white);
                        position++;
                        if (repeat == true) {
                            if (position == 0) {
                                position = mangbaihat.size();
                            }
                            position -= 1;
                        }
                        // phát ngẫu nhiên
                        if (checkrandom == true) {
                            Random random = new Random();
                            int index = random.nextInt(mangbaihat.size());
                            if (index == position) {
                                position = index - 1;
                            }
                            position = index;
                        }
                        if (position > (mangbaihat.size() - 1)) {
                            position = 0;
                        }
                        new PlayMp3().execute(mangbaihat.get(position).getLink());

                        fragment_dia_nhac.Playnhac(mangbaihat.get(position).getImage());
                        Picasso.get().load(mangbaihat.get(position).getImage()).into(songs_cover_one);
                        songs_title.setText(mangbaihat.get(position).getName());
                        songs_artist_name.setText(mangbaihat.get(position).getSinger());
                    }
                    //sau khi click xong vô hiệu 2 nút;
                    imgbtnpre.setClickable(false);
                    imgbtnnext.setClickable(false);
                    //tạo một luồng set lại 2 nút
                    Handler handler1 = new Handler();
                    handler1.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            imgbtnpre.setClickable(true);
                            imgbtnnext.setClickable(true);
                        }
                    }, 5000);
                    next = false;
                    handler1.removeCallbacks(this);
                }else {
                    handler1.postDelayed(this,1000);
                }
            }
        },1000);
    }

//    ================== 4 =====================
    //Hàm thêm vào danh sách playlist cá nhân
    private void addPlayList(final String ID){
        userInfor.setTempID(ID);
        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }
    //Hàm kiểm tra bài hát yêu thích
    private void checkDuplicate(String ID){
        UserInfor userInfor = UserInfor.getInstance();
        ArrayList<String> songid = userInfor.getFavorites();
        if(songid!=null){
            for(int i = 0; i<songid.size(); i++){
                if(songid.get(i).equals(ID)){
                    isFavorites = true;
                    break;
                }
            }
        }
    }
    //Hàm thêm vào danh sách bài hát yêu thích
    private void ItemFavorites(final String ID, boolean Add) {
        if(userInfor.getUsername()!=null){
            String UserID = userInfor.getID();
            FavoritesDAO favoritesDAO = new FavoritesDAO(getContext());
            if (Add) {
                favoritesDAO.addItemFavorites(UserID, ID, new SucessCallBack() {
                    @Override
                    public void getCallBack(Boolean isSucees) {
                        if (isSucees) {
                            //Khi thành công đổi màu trái tim
                            imgbtnlike.setImageResource(R.drawable.ic_favorite_black_24dp);
                            isFavorites = true;
                            //Sửa lại danh sách bài hát yêu thích của client
                            UserInfor userInfor = UserInfor.getInstance();
                            ArrayList<String>list = userInfor.getFavorites();
                            try {
                                list.add(ID);
                                userInfor.setFavorites(list);
                            } catch (Exception e) {
                                System.out.println("erorr" + e);
                            }
                        }
                    }
                });
            } else {
                favoritesDAO.removeItemFavorites(UserID, ID, new SucessCallBack() {
                    @Override
                    public void getCallBack(Boolean isSucees) {
                        if (isSucees) {
                            imgbtnlike.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                            isFavorites = false;
                            UserInfor userInfor = UserInfor.getInstance();
                            try{
                                ArrayList<String>list = userInfor.getFavorites();
                                list.remove(ID);
                                userInfor.setFavorites(list);
                            }catch (Exception e) {
                                Log.d("catch", e.toString()) ;
                            }
                        }
                    }
                });
            }
        }else{
            Toast.makeText(getContext(),"Bạn Cần Đăng Nhập Để Thực Hiện Chức Năng Này",Toast.LENGTH_SHORT).show();
        }
    }

//    ================== 5 =====================
    // AsyncTask
    class PlayMp3 extends AsyncTask<String,Void,String> {

    @Override
    protected String doInBackground(String... strings) {
        return strings[0];
    }
    protected void onPostExecute(String baihat) {
        super.onPostExecute(baihat);
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            }
        });
        try {
            mediaPlayer.setDataSource(baihat);
            mediaPlayer.prepare();
        }catch (IOException e){
            e.printStackTrace();
        }
        mediaPlayer.start();
        play = true ;
        progressBarMusic.setVisibility(View.GONE);
        imgbtnplay.setVisibility(View.VISIBLE);
        imgbtnplay.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
        play_button.setImageResource(R.drawable.ic_baseline_pause_24_white);
        TimeSong();
        UpdateTime();
    }
}

    // show notification
    public void showNotification(int playPauseBtn){
        try{
            Intent intent = new Intent(getContext(), getClass());
            PendingIntent contentIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);
            Intent prevIntent = new Intent(getContext(), NotificationReceiver.class).setAction(ACTION_PREV);
            PendingIntent prevPendingIntent = PendingIntent.getBroadcast(getContext(), 0, prevIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent playIntent = new Intent(getContext(), NotificationReceiver.class).setAction(ACTION_PLAY);
            PendingIntent playPendingIntent = PendingIntent.getBroadcast(getContext(), 0, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent nextIntent = new Intent(getContext(), NotificationReceiver.class).setAction(ACTION_NEXT);
            PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getContext(), 0, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            // design notifi
            if (playPauseBtn == R.drawable.ic_pause_circle_filled_black_24dp) {
                // design notification
                Bitmap picture = BitmapFactory.decodeResource(getResources(),R.drawable.hinhlogo);
                Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID_2)
                        .setSmallIcon(R.drawable.hinhlogo)
                        .setLargeIcon(picture)
                        .setContentTitle(mangbaihat.get(position).getName())
                        .setContentText(mangbaihat.get(position).getSinger())
                        .addAction(R.drawable.ic_skip_previous_black_24dp, "Previous",prevPendingIntent)
                        .addAction(R.drawable.ic_pause_circle_filled_black_24dp, "Play",playPendingIntent)
                        .addAction(R.drawable.ic_skip_next_black_24dp, "Next",nextPendingIntent)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                                setMediaSession(mediaSession.getSessionToken()))
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setContentIntent(contentIntent)
                        .setOnlyAlertOnce(true)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,notification);
            } else {
                // design notification
                Bitmap picture = BitmapFactory.decodeResource(getResources(),R.drawable.hinhlogo);
                Notification notification = new NotificationCompat.Builder(getContext(), CHANNEL_ID_2)
                        .setSmallIcon(R.drawable.hinhlogo)
                        .setLargeIcon(picture)
                        .setContentTitle(mangbaihat.get(position).getName())
                        .setContentText(mangbaihat.get(position).getSinger())
                        .addAction(R.drawable.ic_skip_previous_black_24dp, "Previous",prevPendingIntent)
                        .addAction(R.drawable.ic_play, "Pause",playPendingIntent)
                        .addAction(R.drawable.ic_skip_next_black_24dp, "Next",nextPendingIntent)
                        .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().
                                setMediaSession(mediaSession.getSessionToken()))
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setContentIntent(contentIntent)
                        .setOnlyAlertOnce(true)
                        .build();
                NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(0,notification);
            }
        } catch (Exception e) {}
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
        musicService = binder.getService();
        musicService.setCallBack(PlayMusicFragment.this);
        Log.e("Conected", musicService + "");
    }
    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
        Log.e("Disconected", musicService + "");
    }

//    ================== 6 =====================
    @Override
    public void onResume() {
        Log.d(TAG,"onResume") ;
        super.onResume();
        Intent intent = new Intent(getContext(), MusicService.class);
        getActivity().bindService(intent, this, BIND_AUTO_CREATE);
    }
    @Override
    public void onDestroyView() {
        Log.d(TAG,"onDestroyView") ;
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.release();
            play = false;
        }
        super.onDestroyView();
    }

}