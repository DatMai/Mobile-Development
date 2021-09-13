package com.Fpoly.MusicAppCNPM.Fragment.Search;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Fpoly.MusicAppCNPM.Fragment.Search.Adapter.SearchAdapter;
import com.Fpoly.MusicAppCNPM.Database.DAO.SearchDAO;
import com.Fpoly.MusicAppCNPM.Database.Services.CallBack.SongCallBack;
import com.Fpoly.MusicAppCNPM.Model.Song;
import com.Fpoly.MusicAppCNPM.Model.UserInfor;
import com.Fpoly.MusicAppCNPM.R;
import java.util.ArrayList;

public class SearchFragment extends Fragment {
    Toolbar toolbar;
    RecyclerView rcvsearch;
    SearchAdapter adapter;
    TextView tvannotation;
    LinearLayout gridLayout;
    CardView cvVietNam,cvUS,cvChina,cvKorea;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        toolbar = root.findViewById(R.id.toolbar);
        rcvsearch = root.findViewById(R.id.rcvsearch);
        tvannotation = root.findViewById(R.id.tvannotation);
        gridLayout = root.findViewById(R.id.gridlayout);
        cvVietNam = root.findViewById(R.id.cvVietNam);
        cvUS = root.findViewById(R.id.cvUS);
        cvChina = root.findViewById(R.id.cvChina);
        cvKorea = root.findViewById(R.id.cvKorea);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        cvVietNam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment("T01");
            }
        });

        cvUS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment("T02");
            }
        });

        cvKorea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment("T03");
            }
        });

        cvChina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment("T04");
            }
        });
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                SearchMusic(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                tvannotation.setVisibility(View.GONE);
                rcvsearch.setVisibility(View.GONE);
                gridLayout.setVisibility(View.VISIBLE);
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    // Tìm kiếm theo tên bài hát
    private void SearchMusic(String query) {
    final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.loading);
        dialog.show();
        final SearchDAO searchDAO = new SearchDAO(getContext());
        searchDAO.searchMusic(UpperCase(query),new SongCallBack() {
            @Override
            public void getCallBack(ArrayList<Song> song) {
                if(!song.isEmpty()){
                    adapter = new SearchAdapter(getContext(),song,SearchFragment.this);
                    rcvsearch.setLayoutManager(new LinearLayoutManager(getActivity()));
                    rcvsearch.setAdapter(adapter);
                    rcvsearch.setVisibility(View.VISIBLE);
                    tvannotation.setVisibility(View.INVISIBLE);
                    gridLayout.setVisibility(View.GONE);
                }else{
                    tvannotation.setVisibility(View.VISIBLE);
                    rcvsearch.setVisibility(View.GONE);
                    gridLayout.setVisibility(View.GONE);
                }
                dialog.dismiss();
            }
        });
    }

    // Kiểm tra dữ liệu nhập vào
    private String UpperCase(String input){
        StringBuffer output=new StringBuffer(input);
        for(int i=0;i<output.length();i++)
            if(i==0 || output.charAt(i-1)==' ')
                output.setCharAt(i, Character.toUpperCase(output.charAt(i)));
        return output.toString();
    }

    private void changeFragment(String kindID){
        UserInfor userInfor = UserInfor.getInstance();
        userInfor.setKindID(kindID);
        FragmentTransaction ft  = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_out_left,R.anim.slide_in_right);
        ft.replace(R.id.nav_host_fragment,new SongByKindFragment());
        ft.addToBackStack(null);
        ft.commit();
    }
}