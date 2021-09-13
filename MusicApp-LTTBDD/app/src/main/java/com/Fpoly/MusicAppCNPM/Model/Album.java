package com.Fpoly.MusicAppCNPM.Model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Album implements Parcelable {
    private String Image;
    private String Name;
    private String Singer;
    private String ID;
    private ArrayList<String> Song = null;

    public Album(String image, String name, String singer, String ID, ArrayList<String> song) {
        Image = image;
        Name = name;
        Singer = singer;
        this.ID = ID;
        Song = song;
    }

    public Album() {
    }

    protected Album(Parcel in) {
        Image = in.readString();
        Name = in.readString();
        Singer = in.readString();
        ID = in.readString();
        Song = in.createStringArrayList();
    }

    public static final Creator<Album> CREATOR = new Creator<Album>() {
        @Override
        public Album createFromParcel(Parcel in) {
            return new Album(in);
        }

        @Override
        public Album[] newArray(int size) {
            return new Album[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Image);
        parcel.writeString(Name);
        parcel.writeString(Singer);
        parcel.writeString(ID);
        parcel.writeStringList(Song);
    }

    public String getImage() {
        return Image;
    }
    public void setImage(String image) {
        Image = image;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getSinger() {
        return Singer;
    }
    public void setSinger(String singer) {
        Singer = singer;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public ArrayList<String> getSong() {
        return Song;
    }
    public void setSong(ArrayList<String> song) {
        Song = song;
    }

    public static Creator<Album> getCREATOR() {
        return CREATOR;
    }
}







//package com.Fpoly.music143.Model;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;
//
//public class Album {
//    @SerializedName("Image")
//    @Expose
//    private String image;
//    @SerializedName("Name")
//    @Expose
//    private String name;
//    @SerializedName("Singer")
//    @Expose
//    private String singer;
//    @SerializedName("ID")
//    @Expose
//    private String iD;
//    @SerializedName("Song")
//    @Expose
//    private ArrayList<String> song = null;
//
//    /**
//     * No args constructor for use in serialization
//     *
//     */
//    public Album() {
//    }
//    /**
//     *
//     * @param song
//     * @param image
//     * @param singer
//     * @param name
//     * @param iD
//     */
//    public Album(String image, String name, String singer, String iD, ArrayList<String> song) {
//        super();
//        this.image = image;
//        this.name = name;
//        this.singer = singer;
//        this.iD = iD;
//        this.song = song;
//    }
//
//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getSinger() {
//        return singer;
//    }
//
//    public void setSinger(String singer) {
//        this.singer = singer;
//    }
//
//    public String getID() {
//        return iD;
//    }
//
//    public void setID(String iD) {
//        this.iD = iD;
//    }
//
//    public ArrayList<String> getSong() {
//        return song;
//    }
//
//    public void setSong(ArrayList<String> song) {
//        this.song = song;
//    }
//
//}
//
//
