package com.Fpoly.MusicAppCNPM.Fragment.Music.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Fragment.Music.PlayMusicFragment;
import com.Fpoly.MusicAppCNPM.Interface.ItemClickListener;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.R;

import java.util.ArrayList;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class PlaynhacAdapter extends RecyclerView.Adapter<PlaynhacAdapter.ViewHolder> {
    Context context;
    ArrayList<Song> mangbaihat;
    int CurrentPosition = 0 ;

    public PlaynhacAdapter(Context context, ArrayList<Song> mangbaihat) {
        this.context = context;
        this.mangbaihat = mangbaihat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_play_bai_hat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        Song song = mangbaihat.get(position);
        holder.txttencasi.setText(song.getSinger());
        holder.txtindex.setText(position + 1 + "");
        holder.txttenbaihat.setText(song.getName());
        if (position == PlayMusicFragment.position ) {
            if (PlayMusicFragment.play == true) {
                holder.equalizer.animateBars();
            } else {
                holder.equalizer.stopBars();
            }
        } else {
            holder.equalizer.setVisibility(View.GONE);
        }
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int vitri, boolean isLongClick) {
                ChangeFragment(vitri,view) ;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mangbaihat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtindex,txttenbaihat,txttencasi;
        EqualizerView equalizer ;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtindex = itemView.findViewById(R.id.textviewplaynhacindex);
            txttenbaihat = itemView.findViewById(R.id.textviewplaynhactenbaihat);
            txttencasi = itemView.findViewById(R.id.textviewplaynhactencasi);
            equalizer = itemView.findViewById(R.id.equalizer_view) ;
            itemView.setOnClickListener(this);


        }
        public void setItemClickListener(ItemClickListener itemClickListener){
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View view) {
            itemClickListener.onClick(view,getAdapterPosition(),false);
        }
    }

    private void ChangeFragment(int position, View view) {
        if (position != PlayMusicFragment.position) {
            Bundle bundle = new Bundle();
            if (mangbaihat.size() > 1) {
                bundle.putParcelableArrayList("MultipleSongs", mangbaihat);
                bundle.putInt("position", position);
            } else {
                bundle.putParcelable("Songs", mangbaihat.get(0));
            }
            AppCompatActivity activity = (AppCompatActivity) view.getContext();
            Fragment myFragment = new PlayMusicFragment();
            myFragment.setArguments(bundle);
            activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
        } else {
            Toast.makeText(context, "Nhạc đang phát", Toast.LENGTH_SHORT).show();
        }
    }


}
