package com.Fpoly.MusicAppCNPM.Fragment.UserPlayList;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.Fpoly.MusicAppCNPM.Database.DAO.PlayListDAO;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.PlayListCallBack;
import com.Fpoly.MusicAppCNPM.Fragment.UserPlayList.Apdater.AddItemPlayListAdapter;
import com.Fpoly.MusicAppCNPM.Model.PlayList;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;


public class AddItemPlaylistFragment extends BottomSheetDialogFragment {
    RecyclerView rcvplaylist;
    AddItemPlayListAdapter add_adapter;
    UserInfor userInfor = UserInfor.getInstance();
    ArrayList<PlayList> myplayLists;


    public AddItemPlaylistFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_item_playlist, container, false) ;
        RoundedImageView btn_createPlaylist = root.findViewById(R.id.btn_createPlaylist);
        rcvplaylist = root.findViewById(R.id.rcvplaylist) ;
        PlayListDAO playListDAO = new PlayListDAO(getContext());
        playListDAO.getPlayList(userInfor.getID(), new PlayListCallBack() {
            @Override
            public void getCallBack(ArrayList<PlayList> playlist) {
                myplayLists = playlist ;
                Log.d("MyPlaylist",playlist.toString()) ;
                rcvplaylist.setLayoutManager(new LinearLayoutManager(getActivity()));
                Log.d("playlistTest", playlist.toString());
                add_adapter = new AddItemPlayListAdapter(getContext(),myplayLists,new AddItemPlaylistFragment());
                rcvplaylist.setAdapter(add_adapter);
            }
        });

        btn_createPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        UserInfor userInfor = UserInfor.getInstance();
                        PlayListDAO playListDAO = new PlayListDAO(getContext());
                        playListDAO.createPlaylist(userInfor.getID(),name.getText().toString(), new PlayListCallBack() {
                            @Override
                            public void getCallBack(ArrayList<PlayList> playLists) {
                                myplayLists.clear();
                                myplayLists.addAll(playLists);
                                add_adapter.notifyDataSetChanged();
                            }
                        });
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
        });

        return root;

    }
}