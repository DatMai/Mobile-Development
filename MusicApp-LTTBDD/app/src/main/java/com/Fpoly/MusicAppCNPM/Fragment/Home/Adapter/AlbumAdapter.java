package com.Fpoly.MusicAppCNPM.Fragment.Home.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Interface.ItemClickListener;
import com.Fpoly.MusicAppCNPM.Fragment.SongsList.SongsListFragment;
import com.Fpoly.MusicAppCNPM.Fragment.Home.HomeFragment;
import com.Fpoly.MusicAppCNPM.Model.Album;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{
    Context context;
    ArrayList<Album> mangalbum;
    HomeFragment homeFragment;
    public AlbumAdapter(Context context, ArrayList<Album> mangalbum,HomeFragment homeFragment) {
        this.context = context;
        this.mangalbum = mangalbum;
        this.homeFragment = homeFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_album,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         final Album album = mangalbum.get(position);
         holder.txtcasialbum.setText(album.getName());
         holder.txttenalbum.setText(album.getSinger());
         Picasso.get().load(album.getImage()).into(holder.imghinhalbum);
         holder.setItemClickListener(new ItemClickListener() {
             @Override
             public void onClick(View view, int position, boolean isLongClick) {
                 UserInfor userInfor = UserInfor.getInstance();
                 //Xác định danh sách gửi tới không là từ playlist người dùng
                 userInfor.setisPlayList(false);
                 userInfor.setCurrentAlbum(mangalbum.get(position).getSong());
                 userInfor.setisPlayList(false);
                 userInfor.setisFavorites(false);
                 ChangeFragment(mangalbum.get(position),new SongsListFragment());
             }
         });
    }

    @Override
    public int getItemCount() {
        return mangalbum.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imghinhalbum;
        TextView txttenalbum, txtcasialbum;
        ItemClickListener itemClickListener;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imghinhalbum = itemView.findViewById(R.id.imageviewalbum);
            txttenalbum = itemView.findViewById(R.id.textviewtenalbum);
            txtcasialbum = itemView.findViewById(R.id.textviewtencasialbum);
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
    private void ChangeFragment(Album album, Fragment fragment){
        Bundle bundle = new Bundle();
        bundle.putParcelable("Album", album);
        bundle.putInt("fragment",1);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager = homeFragment.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.commit();
    }
}
