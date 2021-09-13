package com.Fpoly.MusicAppCNPM.Model;

import java.util.ArrayList;

public class SongIDList {
    ArrayList<String> data;

    public SongIDList (ArrayList<String> data) {
        this.data = data;
    }

    public ArrayList<String> getData() {
        return data;
    }

    public void setData(ArrayList<String> data) {
        this.data = data;
    }
}
