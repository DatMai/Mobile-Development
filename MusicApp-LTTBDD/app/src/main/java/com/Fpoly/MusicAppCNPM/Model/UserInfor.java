package com.Fpoly.MusicAppCNPM.Model;

import java.util.ArrayList;

public class UserInfor {
    private String ID ;
    private String Username;
    private ArrayList<String> Favorites = null;
    private String Password;
    private String Email;
    private Boolean linkGmail;
    private Boolean linkFaceBook;
    private ArrayList<String> userPlaylist;


    private String kindID;
    //Biến xác định có phải là playlist hay không
    private Boolean isPlayList;
    //Biến xác định có phải là favorites hay không
    private Boolean isFavorites;

    private String TempID;
    //Biến xác định playlist hiện tại người dùng đang xem
    private String TempPlayListID;
    //Biến Xác định album hiện tại người dùng đang xem
    private ArrayList<String> CurrentAlbum;

    private static UserInfor instance = new UserInfor();
    private UserInfor() { }

    public static  UserInfor getInstance(){
        return instance;
    }



    public UserInfor(String ID, String username, ArrayList<String> favorites, String password, String email, Boolean linkGmail, Boolean linkFaceBook) {
        this.ID = ID;
        this.Username = username;
        this.Favorites = favorites;
        this.Password = password;
        this.Email = email;
        this.linkGmail = linkGmail;
        this.linkFaceBook = linkFaceBook;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        this.Username = username;
    }

    public ArrayList<String> getFavorites() {
        return Favorites;
    }

    public void setFavorites(ArrayList<String> favorites) {
        this.Favorites = favorites;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        this.Email = email;
    }

    public String getKindID() {
        return kindID;
    }

    public void setKindID(String kindID) {
        this.kindID = kindID;
    }

    public Boolean getLinkGmail() {
        return linkGmail;
    }

    public void setLinkGmail(Boolean linkGmail) {
        this.linkGmail = linkGmail;
    }

    public Boolean getLinkFaceBook() {
        return linkFaceBook;
    }

    public void setLinkFaceBook(Boolean linkFaceBook) {
        this.linkFaceBook = linkFaceBook;
    }

    //==================================================================================================================
//Danh Sách Mã Bài Hát được truyền từ accoutfragment-favorite hoặc playlist fragment
    public ArrayList<String> getUserPlaylist() {
        return userPlaylist;
    }

    public void setUserPlaylist(ArrayList<String> playlist) {
        userPlaylist = playlist;
    }

    public Boolean getisPlayList() {
        return isPlayList;
    }

    public void setisPlayList(Boolean playList) {
        isPlayList = playList;
    }

    public String getTempID() {
        return TempID;
    }

    public void setTempID(String tempID) {
        TempID = tempID;
    }

    //Biến tạm của ID playlist được get khi thao tác xóa phần tử trong playlist từ playlistsongid_adaoter
    public String getTempPlayListID() {
        return TempPlayListID;
    }
    //Biến tạm của ID playlist được set khi nhấn vào playlist bất kỳ, gọi trong playlist adapter
    public void setTempPlayListID(String tempPlayListID) {
        TempPlayListID = tempPlayListID;
    }

    public ArrayList<String> getCurrentAlbum() {
        return CurrentAlbum;
    }

    public void setCurrentAlbum(ArrayList<String> currentAlbum) {
        CurrentAlbum = currentAlbum;
    }

    public void setisFavorites(Boolean favorites) {
        isFavorites = favorites;
    }

    public Boolean getisFavorites(){
        return isFavorites;
    }
}