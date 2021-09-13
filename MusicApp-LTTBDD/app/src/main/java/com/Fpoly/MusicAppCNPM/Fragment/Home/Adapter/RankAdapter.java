package com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Fragment.Music.PlayMusicFragment;
import com.Fpoly.MusicAppCNPM.Interface.ItemClickListener;
import com.Fpoly.MusicAppCNPM.Fragment.Home.HomeFragment;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder>{
    Context context;
    ArrayList<Song> songArrayList;
    HomeFragment homeFragment;
    public RankAdapter(Context context, ArrayList<Song> songArrayList,HomeFragment homeFragment) {
        this.context = context;
        this.songArrayList = songArrayList;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_song_info,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            Song song = songArrayList.get(position);
            switch (position) {
                case 0:
                    holder.imgrank.setImageResource(R.drawable.top1);
                    break;
                case 1:
                    holder.imgrank.setImageResource(R.drawable.top2);
                    break;
                case 2:
                    holder.imgrank.setImageResource(R.drawable.top3);
                    break;
                case 3:
                    holder.imgrank.setImageResource(R.drawable.top4);
                    break;
                default:
                    holder.imgrank.setImageResource(R.drawable.top5);
            }

            holder.tvsongsinger.setText(song.getSinger());
            holder.tvsongname.setText(song.getName());
            if (song.getImage().isEmpty()){
               Toast.makeText(context,"flasf",Toast.LENGTH_SHORT).show();
            }
            else{
                Picasso.get().load(song.getImage()).into(holder.imghinh);
            }

            holder.tvlike.setText(song.getLike().toString());
            holder.setItemClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    ChangeFragment(position, view);
                }
            });
        }catch (Exception e){
            Toast.makeText(context,"Có Lỗi Khi Load Dữ Liệu",Toast.LENGTH_SHORT).show();
            Log.e("Error",e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return songArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvsongname, tvsongsinger,tvlike,tvindex;
        ImageView imghinh, imgrank;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvsongname = itemView.findViewById(R.id.tvsongname);
            tvsongsinger = itemView.findViewById(R.id.tvsongsinger);
            imghinh = itemView.findViewById(R.id.imgsong);
            imgrank = itemView.findViewById(R.id.imgrank);
            tvlike = itemView.findViewById(R.id.tvLike);
            tvindex = itemView.findViewById(R.id.tvindex);
            tvindex.setVisibility(View.GONE);
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
}



 /*  Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = intent.getExtras();
        bundle.putParcelable("songs",songArrayList.get(position));*/
      /*  Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",songArrayList.get(position));
        bundle.putInt("fragment",4);
        PlayMusicFragment playMusicFragment = new PlayMusicFragment() ;
        playMusicFragment.setArguments(bundle);
        FragmentManager fragmentManager = homeFragment.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, playMusicFragment, null);
        fragmentTransaction.commit();
        sendMessage() ;*/




//        Intent intent = new Intent(context, BackgroundSoundService.class);
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("Songs",songArrayList.get(position));
//        bundle.putInt("fragment",4);
//        intent.putExtra("song",songArrayList.get(position).getLink()) ;
//        intent.putExtra("song",bundle) ;
//        context.startService(intent);

     /*   Bundle bundle = new Bundle();
        bundle.putParcelable("Songs",songArrayList.get(position));
        bundle.putInt("fragment",4);
        Fragment fragment = new PlayMusicFragment();
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = homeFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();*/


  /*  String action = "ACTION_GETDATA";
    Intent intent = new Intent(context, MusicService.class);
        System.out.println(intent.getAction());
                intent.putExtra("myActionName",action);
                intent.putExtra("Songs",songArrayList.get(position));
                intent.setAction(action);
                context.startService(intent);*/
