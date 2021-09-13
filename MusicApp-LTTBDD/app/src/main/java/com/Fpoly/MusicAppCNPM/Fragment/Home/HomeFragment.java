package com.Fpoly.MusicAppCNPM.Fragment.Home;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import com.Fpoly.MusicAppCNPM.Database.DAO.AlbumDAO;
import com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter.AlbumAdapter;
import com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter.RankAdapter;
import com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter.SongNewAdapter;
import com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter.SuggestAdapter;
import com.Fpoly.MusicAppCNPM.Database.DAO.SongsDAO;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.AlbumCallBack;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.SongCallBack;
import com.Fpoly.MusicAppCNPM.Model.Album;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    RecyclerView rcvalbum, rcvsuggest, rcvtop;
    ArrayList<Album> albumInRank;
    ArrayList<Song> SongList;
    ArrayList<Song> songsInRank;
    ArrayList<Song> songNew;
    AlbumAdapter albumAdapter;
    SuggestAdapter suggestAdapter;
    RankAdapter rankAdapter;
    SongNewAdapter songNewAdapter ;

    DotsIndicator dotsIndicator;
    ViewPager viewPager ;
    Handler handler ;
    Runnable runnable ;
    int currentSong ;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);
        init(root) ;
        autoSwipe() ;
        return root;
    }

    private void init(View root) {
        rcvalbum = root.findViewById(R.id.rcvalbum);
        rcvsuggest = root.findViewById(R.id.rcvsuggest);
        rcvtop = root.findViewById(R.id.rcvtop);
        dotsIndicator =  root.findViewById(R.id.dots_indicator);
        viewPager =  root.findViewById(R.id.viewPager);
    }

    @Override
    public void onStart() {
        getData();
        super.onStart();
    }

    private void getData() {
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        final SongsDAO songsDAO = new SongsDAO(getContext());
        final AlbumDAO albumDAO = new AlbumDAO(getContext());


        songsDAO.getRankSongs(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                songsInRank = song;
                rankAdapter = new RankAdapter(getContext(), song, HomeFragment.this);
                rcvtop.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvtop.setAdapter(rankAdapter);
            }
        });
        albumDAO.getAlbum(new AlbumCallBack() {
            @Override
            public void getCallBack(ArrayList<Album> album) {
                albumInRank = album;
                albumAdapter = new AlbumAdapter(getContext(), albumInRank, HomeFragment.this);
                rcvalbum.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
                rcvalbum.setHasFixedSize(true);
                rcvalbum.setAdapter(albumAdapter);
            }
        });

        songsDAO.getSuggest(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                SongList = song;
                suggestAdapter = new SuggestAdapter(getContext(), SongList, HomeFragment.this);
                rcvsuggest.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcvsuggest.setAdapter(suggestAdapter);
            }
        });

        songsDAO.getNewSongs(new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                songNew = song ;
                Log.d("songNew", String.valueOf(songNew.size())) ;
                songNewAdapter = new SongNewAdapter( getContext(), songNew, HomeFragment.this);
                viewPager.setAdapter(songNewAdapter);
                dotsIndicator.setViewPager(viewPager);
            }
        });
        dialog.dismiss();
    }

    private void autoSwipe() {
            handler = new Handler() ;
            runnable = new Runnable() {
                @Override
                public void run() {
                    currentSong = viewPager.getCurrentItem() ;
                    currentSong++ ;
                    try {
                    if (currentSong >= viewPager.getAdapter().getCount()) {
                        currentSong = 0 ;
                    } viewPager.setCurrentItem(currentSong, true);
                    handler.postDelayed(runnable,4500) ;
                    } catch (Exception e) {}
                }
            } ; handler.postDelayed(runnable,4500) ;

    }


}