package com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.Fpoly.MusicAppCNPM.Fragment.Home.HomeFragment;
import com.Fpoly.MusicAppCNPM.Fragment.Music.PlayMusicFragment;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongNewAdapter extends PagerAdapter {
    Context context ;
    ArrayList<Song> Songs ;
    HomeFragment homeFragment ;


    public SongNewAdapter(Context context, ArrayList<Song> songs, HomeFragment homeFragment) {
        this.context = context;
        this.Songs = songs;
        this.homeFragment = homeFragment;

    }

    @Override
    public int getCount() {
        return Songs.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater inflater  = LayoutInflater.from(context) ;
        View view = inflater.inflate(R.layout.item_songnew,null) ;
        ImageView imgSongNew = view.findViewById(R.id.imgSongNew) ;
        Picasso.get().load(Songs.get(position).getImage()).into(imgSongNew);
        container.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangeFragment(position,view);
            }
        });
        return view ;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    private void ChangeFragment(int position, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",Songs.get(position));
        bundle.putInt("fragment",4);
        Fragment myFragment = new PlayMusicFragment();
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
        }

}
