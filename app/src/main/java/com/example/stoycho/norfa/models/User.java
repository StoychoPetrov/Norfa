package com.example.stoycho.norfa.models;

import java.util.List;

/**
 * Created by stoycho on 11/21/16.
 */

public class User {

    private String mPicSrc;
    private String mPickClick;
    private String mName;
    private String mCrDate;
    private int mFrCnt;
    private int mCfrCnt;
    private String mEmail;
    private String mAddress;
    private int mReload;
    private String mNext;
    private boolean mFavourite;
    private String mFavChange;
    private List<Friend> friends;

    public User(){}

    public User(String mPicSrc, String mPickClick, String mName, String mCrDate, int mFrCnt, List<Friend> friends) {
        this.mPicSrc = mPicSrc;
        this.mPickClick = mPickClick;
        this.mName = mName;
        this.mCrDate = mCrDate;
        this.mFrCnt = mFrCnt;
        this.friends = friends;
    }

    public String getmPicSrc() {
        return mPicSrc;
    }

    public void setmPicSrc(String mPicSrc) {
        this.mPicSrc = mPicSrc;
    }

    public String getmPickClick() {
        return mPickClick;
    }

    public void setmPickClick(String mPickClick) {
        this.mPickClick = mPickClick;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmCrDate() {
        return mCrDate;
    }

    public void setmCrDate(String mCrDate) {
        this.mCrDate = mCrDate;
    }

    public int getmFrCnt() {
        return mFrCnt;
    }

    public void setmFrCnt(int mFrCnt) {
        this.mFrCnt = mFrCnt;
    }

    public int getmCfrCnt() {
        return mCfrCnt;
    }

    public void setmCfrCnt(int mCfrCnt) {
        this.mCfrCnt = mCfrCnt;
    }

    public List<Friend> getFriends() {
        return friends;
    }

    public void setFriends(List<Friend> friends) {
        this.friends = friends;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getmReload() {
        return mReload;
    }

    public void setmReload(int mReload) {
        this.mReload = mReload;
    }

    public String getmNext() {
        return mNext;
    }

    public void setmNext(String mNext) {
        this.mNext = mNext;
    }

    public boolean ismFavourites() {
        return mFavourite;
    }

    public void setmFavourites(boolean mFavourites) {
        this.mFavourite = mFavourites;
    }

    public String getmFavChange() {
        return mFavChange;
    }

    public void setmFavChange(String mFavChange) {
        this.mFavChange = mFavChange;
    }

}
