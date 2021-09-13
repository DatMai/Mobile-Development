package com.Fpoly.MusicAppCNPM.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class PlayList implements Parcelable {

    private ArrayList<String> songs = null;
    private String name;
    private String iD;
    private Boolean deleted;

    public PlayList() {
    }


    public PlayList(ArrayList<String> songs, String name, String iD, Boolean deleted) {
        super();
        this.songs = songs;
        this.name = name;
        this.iD = iD;
        this.deleted = deleted;
    }

    protected PlayList(Parcel in) {
        songs = in.createStringArrayList();
        name = in.readString();
        iD = in.readString();
        byte tmpDeleted = in.readByte();
        deleted = tmpDeleted == 0 ? null : tmpDeleted == 1;
    }

    public static final Creator<PlayList> CREATOR = new Creator<PlayList>() {
        @Override
        public PlayList createFromParcel(Parcel in) {
            return new PlayList(in);
        }

        @Override
        public PlayList[] newArray(int size) {
            return new PlayList[size];
        }
    };

    public ArrayList<String> getSongs() {
        return songs;
    }
    public void setSongs(ArrayList<String> songs) {
        this.songs = songs;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getID() {
        return iD;
    }
    public void setID(String iD) {
        this.iD = iD;
    }
    public Boolean getDeleted() {
        return deleted;
    }
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(songs);
        parcel.writeString(name);
        parcel.writeString(iD);
        parcel.writeByte((byte) (deleted == null ? 0 : deleted ? 1 : 2));
    }
}