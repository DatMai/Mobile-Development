package com.Fpoly.MusicAppCNPM.Fragment.UserPlayList;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Fragment.Account.AccountFragment;
import com.Fpoly.MusicAppCNPM.Database.DAO.PlayListDAO;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.PlayListCallBack;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.Fpoly.MusicAppCNPM.Fragment.UserPlayList.Apdater.PlaylistAdapter;
import com.Fpoly.MusicAppCNPM.Model.PlayList;

import java.util.ArrayList;

public class PlaylistFragment extends Fragment {
    ArrayList<PlayList> myplayLists;
    RecyclerView rcvplaylist;
    ImageButton btn_createPlaylist;
    PlaylistAdapter adapter;
    Toolbar toolbar ;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_playlist, container, false);
        rcvplaylist = root.findViewById(R.id.rcvplaylist);
        btn_createPlaylist = root.findViewById(R.id.btn_createPlaylist);

        UserInfor userInfor = UserInfor.getInstance();
        getData(userInfor.getID());

        toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_keyboard_arrow_left_black_24dp);
        toolbarOnclick() ;
        btn_createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });

        return root;
    }

    private void toolbarOnclick() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new AccountFragment());
            }
        });
    }

    // get Data
    private void getData(String UID) {
        final Bundle bundle = getArguments();
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        PlayListDAO playListDAO = new PlayListDAO(getContext());
        playListDAO.getPlayList(UID, new PlayListCallBack() {
            @Override
            public void getCallBack(ArrayList<PlayList> playlist) {
                myplayLists = playlist;
                Log.d("MyPlaylist",playlist.toString()) ;
                rcvplaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.d("playlistTest", playlist.toString());
                adapter = new PlaylistAdapter(getContext(),myplayLists,PlaylistFragment.this);
                rcvplaylist.setAdapter(adapter);
                dialog.dismiss();
            }
        });
    }

    // tạo dialog
    private void showDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder= new androidx.appcompat.app.AlertDialog.Builder(getContext());
        LinearLayout linearLayout = new LinearLayout(getContext());
        final EditText name = new EditText(getContext());
        name.setHint("Nhập Tên PlayList");
        name.setMinEms(16);
        linearLayout.addView(name);
        linearLayout.setPadding(10,50,10,10);
        builder.setView(linearLayout);
        //button Rename
        builder.setPositiveButton("Tạo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //input email
                CreateNew(name.getText().toString());
                dialog.dismiss();
            }
        });
        //button Cancel
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        //Show Dialog
        builder.create().show();

    }

    // Tạo mới
    private void CreateNew(String name) {
        UserInfor userInfor = UserInfor.getInstance();
        PlayListDAO playListDAO = new PlayListDAO(getContext());
        playListDAO.createPlaylist(userInfor.getID(), name, new PlayListCallBack() {
            @Override
            public void getCallBack(ArrayList<PlayList> playLists) {
                myplayLists.clear();
                myplayLists.addAll(playLists);
                adapter.notifyDataSetChanged();
            }
        });
    }


    private void changeFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction =this.getFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_left,R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.nav_host_fragment,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}