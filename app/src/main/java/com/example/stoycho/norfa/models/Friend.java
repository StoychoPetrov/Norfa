package com.example.stoycho.norfa.models;

/**
 * Created by stoycho on 11/18/16.
 */

public class Friend {

    private String mPicSrc;
    private String mPicClick;
    private String mPicSrc1;
    private String mPicSrc2;
    private String mName;
    private String mJDate;
    private String mTDate;
    private String mCDate;
    private int mState;
    private String mAction;
    private String mStatus;

    public Friend(){}

    public Friend(String mPicSrc, String mPicClick, String mPicSrc1, String mPicSrc2, String mName, String mJDate, String mTDate, String mCDate, int mState, String mAction) {
        this.mPicSrc = mPicSrc;
        this.mPicClick = mPicClick;
        this.mPicSrc1 = mPicSrc1;
        this.mPicSrc2 = mPicSrc2;
        this.mName = mName;
        this.mJDate = mJDate;
        this.mTDate = mTDate;
        this.mCDate = mCDate;
        this.mState = mState;
        this.mAction = mAction;
    }

    public String getmPicSrc() { return mPicSrc; }

    public void setmPicSrc(String mPicSrc) {
        this.mPicSrc = mPicSrc;
    }

    public String getmPicClick() {
        return mPicClick;
    }

    public void setmPicClick(String mPicClick) {
        this.mPicClick = mPicClick;
    }

    public String getmPicSrc1() {
        return mPicSrc1;
    }

    public void setmPicSrc1(String mPicSrc1) {
        this.mPicSrc1 = mPicSrc1;
    }

    public String getmPicSrc2() {
        return mPicSrc2;
    }

    public void setmPicSrc2(String mPicSrc2) {
        this.mPicSrc2 = mPicSrc2;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmJDate() {
        return mJDate;
    }

    public void setmJDate(String mJDate) {
        this.mJDate = mJDate;
    }

    public String getmTDate() {
        return mTDate;
    }

    public void setmTDate(String mTDate) {
        this.mTDate = mTDate;
    }

    public String getmCDate() {
        return mCDate;
    }

    public void setmCDate(String mCDate) {
        this.mCDate = mCDate;
    }

    public int getmState() {
        return mState;
    }

    public void setmState(int mState) { this.mState = mState; }

    public String getmAction() {
        return mAction;
    }

    public void setmAction(String mAction) {
        this.mAction = mAction;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
