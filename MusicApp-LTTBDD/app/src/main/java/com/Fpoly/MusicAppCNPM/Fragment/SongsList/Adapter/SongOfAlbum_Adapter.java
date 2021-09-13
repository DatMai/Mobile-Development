package com.Fpoly.MusicAppCNPM.Fragment.SongsList.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Fragment.Music.PlayMusicFragment;
import com.Fpoly.MusicAppCNPM.Fragment.SongsList.SongsListFragment;
import com.Fpoly.MusicAppCNPM.Interface.ItemClickListener;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SongOfAlbum_Adapter extends RecyclerView.Adapter<SongOfAlbum_Adapter.ViewHolder> {
    Context context;
    ArrayList<Song> songArrayList;
    SongsListFragment songsListFragment;

    public SongOfAlbum_Adapter(Context context, ArrayList<Song> songArrayList, SongsListFragment songsListFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.songsListFragment = songsListFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song_info, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Song song = songArrayList.get(position);
        holder.tvsongsinger.setText(song.getSinger());
        holder.tvsongname.setText(song.getName());
        if (song.getImage().isEmpty()) {
            Toast.makeText(context, "Không có hình", Toast.LENGTH_SHORT).show();
        } else {
            Picasso.get().load(song.getImage()).into(holder.imghinh);
        }
        holder.tvindex.setText(position + 1 + "");
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                ChangeFragment(position, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {
        LinearLayout like_layout;
        TextView tvsongname, tvsongsinger, tvindex;
        ImageView imghinh, imgrank;
        ItemClickListener itemClickListener;

        public ViewHolder(@NonNull View itemView)  {
            super(itemView);
            tvsongname = itemView.findViewById(R.id.tvsongname);
            tvsongsinger = itemView.findViewById(R.id.tvsongsinger);
            imghinh = itemView.findViewById(R.id.imgsong);
            imgrank = itemView.findViewById(R.id.imgrank);
            tvindex = itemView.findViewById(R.id.tvindex);
            like_layout = itemView.findViewById(R.id.like_layout);
            like_layout.setVisibility(View.GONE);
            imgrank.setVisibility(View.GONE);
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

    private void ChangeFragment(int position, View view){
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",songArrayList.get(position));
        bundle.putInt("fragment",4);
        Fragment myFragment = new PlayMusicFragment();
        myFragment.setArguments(bundle);
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment).addToBackStack(null).commit();
    }

   /* private void DoDelete(final int position){
        final UserInfor UserInfor = UserInfor.getInstance();
        AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(context)
                .setMessage("Bạn Có Muốn Xóa Không")
                .setTitle("Thông Báo")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final Dialog dialog = new Dialog(context);
                        dialog.setContentView(R.layout.loading);
                        dialog.show();
                        PlayListDAO playListDAO = new PlayListDAO(context);
                        playListDAO.removeItemPlayList(UserInfor.getID(),UserInfor.getTempPlayListID(),songArrayList.get(position).getID(), new SongCallBack() {
                            @Override
                            public void getCallBack(ArrayList<Song> songs) {
                                songArrayList.clear();
                                songArrayList.addAll(songs);
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.parseColor("#00ACC1"));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.parseColor("#00ACC1"));
    }*/
}

