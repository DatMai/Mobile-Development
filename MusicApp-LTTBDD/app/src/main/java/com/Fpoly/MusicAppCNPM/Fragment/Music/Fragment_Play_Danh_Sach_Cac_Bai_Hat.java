package com.Fpoly.MusicAppCNPM.Fragment.Music;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Fragment.Music.Adapter.PlaynhacAdapter;
import com.Fpoly.MusicAppCNPM.R;

public class Fragment_Play_Danh_Sach_Cac_Bai_Hat extends Fragment {
    View view;

    public static RecyclerView recyclerViewPlayNhac;
    public static PlaynhacAdapter playnhacAdapter;


    @Override
    public void onStart() {
        super.onStart();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_play_danh_sach_cac_bai_hat,container,false);
        recyclerViewPlayNhac = view.findViewById(R.id.recycleviewplaybaihat);
        if (PlayMusicFragment.mangbaihat.size()>0){
            playnhacAdapter = new PlaynhacAdapter(getActivity(), PlayMusicFragment.mangbaihat);
            recyclerViewPlayNhac.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerViewPlayNhac.setAdapter(playnhacAdapter);
        }
        return view;
    }
}
