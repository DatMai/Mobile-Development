package com.Fpoly.MusicAppCNPM.Fragment.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.Fpoly.MusicAppCNPM.Activity.LoginActivity;
import com.Fpoly.MusicAppCNPM.Activity.SplashActivity;
import com.Fpoly.MusicAppCNPM.Fragment.SongsList.SongsListFragment;
import com.Fpoly.MusicAppCNPM.Fragment.UserPlayList.PlaylistFragment;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.Fpoly.MusicAppCNPM.txt.blank;
import com.google.firebase.auth.FirebaseAuth;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

import static com.Fpoly.MusicAppCNPM.Activity.MainActivity.slidingUpPanelLayout;

public class AccountFragment extends Fragment {
    TextView tvUsername,tvUserEmail;
    ArrayList<String> list;
    UserInfor userInfor = UserInfor.getInstance();
    LinearLayout Favorites;
    LinearLayout Playlist;
    SwitchCompat swface, swgmail;
    SwitchCompat swdarkmode  ;
    Button btnSignOut ;
    // darkMode
    public static final String MyPREFERENCES = "nightModePrefs";
    public static final String KEY_ISNIGHTMODE = "isNightMode";
    SharedPreferences sharedPreferences;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);

        init(root) ;
        GetUser();
        onClick() ;

        return root;
    }

    private void init(View root) {
        tvUsername = root.findViewById(R.id.tvUserName);
        tvUserEmail = root.findViewById(R.id.tvUserEmail);
        swface =root.findViewById(R.id.swface);
        swgmail = root.findViewById(R.id.swgmail);
        swdarkmode = root.findViewById(R.id.swDarkMode);
        Favorites = root.findViewById(R.id.Favorites);
        Playlist = root.findViewById(R.id.PlayList);
        btnSignOut = root.findViewById(R.id.btnSignOut);
    }

    private void onClick() {
        //Khi nhấn vào bài hát yêu thích
        Favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userInfor.setisFavorites(true);
                userInfor.setisPlayList(true);
                changeFragment(new SongsListFragment());
            }
        });
        //Khi Nhấn Vào PlayList
        Playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle =new Bundle();
                bundle.putBoolean("AddMusic",false);
                Fragment fragment = new PlaylistFragment();
                fragment.setArguments(bundle);
                changeFragment(fragment);
            }
        });

        //DarkMode
        sharedPreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        checkNightModeActivated();
        swdarkmode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new blank()).addToBackStack(null).commit();
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    saveNightModeState(true);
                    startActivity(new Intent(getContext(), SplashActivity.class));
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, new blank()).addToBackStack(null).commit();
                    slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    saveNightModeState(false);
                    startActivity(new Intent(getContext(), SplashActivity.class));
                }
                getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
            }
        });

        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignOut();
            }
        });
    }

    // Save darkMode
    private void saveNightModeState(boolean nightMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(KEY_ISNIGHTMODE, nightMode);
        editor.apply();
    }

    // Check darkmode
    public void checkNightModeActivated(){
        if(sharedPreferences.getBoolean(KEY_ISNIGHTMODE, true)){
            swdarkmode.setChecked(true);
        }else {
            swdarkmode.setChecked(false);
        }
    }

    // Thay đổi Fragment
    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =this.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    // Check user
    private void GetUser() {
        UserInfor userInfor = UserInfor.getInstance();
                if(userInfor.getUsername()!=null){
                    tvUsername.setText(userInfor.getUsername());
                    tvUserEmail.setText(userInfor.getEmail());
                    list = userInfor.getFavorites();
                    swface.setChecked(userInfor.getLinkFaceBook()?true:false);
                    swgmail.setChecked(userInfor.getLinkGmail()?true:false);
                    swface.setClickable(false);
                    swgmail.setClickable(false);
                }else{
                    btnSignOut.setText("Đăng Nhập");
                    Favorites.setEnabled(false);
                    Playlist.setEnabled(false);
                    swface.setEnabled(false);
                    swgmail.setEnabled(false);
                }
            }

    // Đăng xuất
    private void SignOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }

}