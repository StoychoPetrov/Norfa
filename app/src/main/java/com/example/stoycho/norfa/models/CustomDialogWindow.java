package com.example.stoycho.norfa.models;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.tasks.LoadImage;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;

/**
 * Created by stoycho on 11/25/16.
 */

public class CustomDialogWindow extends Dialog implements View.OnClickListener {

    private ImageView mFirstImage;
    private ImageView mSecondImage;
    private ImageButton mYes;
    private ImageButton mNo;
    private String mFirstImageUrl;
    private String mSecondImageUrl;
    private Context mContext;
    private Friend mFriend;

    public CustomDialogWindow(Context context) {
        super(context);
        mContext = context;
    }

    public CustomDialogWindow(Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDialogWindow(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.verifying_images);
        if(getWindow() != null) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            getWindow().setAttributes(params);
        }
        initUI();
        setListeners();

        if(mFirstImageUrl != null && mSecondImageUrl != null)
        {
            String image = ConnectionURL.IMAGE_URL + mFirstImageUrl;
            LoadImage firstImageTask = new LoadImage(mContext,image,mFirstImage);
            firstImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            String secondImage = ConnectionURL.IMAGE_URL + mSecondImage;
            LoadImage secondImageTask = new LoadImage(mContext,secondImage,mSecondImage);
            secondImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    private void initUI()
    {
        mFirstImage = (ImageView)findViewById(R.id.first_image);
        mFirstImage.post(new Runnable() {
            @Override
            public void run() {
                mFirstImage.getLayoutParams().height = mFirstImage.getWidth();
                mFirstImage.postInvalidate();
            }
        });
        mSecondImage = (ImageView)findViewById(R.id.second_image);
        mSecondImage.post(new Runnable() {
            @Override
            public void run() {
                mSecondImage.getLayoutParams().height = mSecondImage.getWidth();
                mSecondImage.postInvalidate();
            }
        });
        mYes = (ImageButton) findViewById(R.id.yes_button);
        mNo = (ImageButton) findViewById(R.id.no_button);
    }

    private void setListeners()
    {
        mYes.setOnClickListener(this);
        mNo.setOnClickListener(this);
    }

    public String getmFirstImageUrl() {
        return mFirstImageUrl;
    }

    public void setmFirstImageUrl(String mFirstImageUrl) {
        this.mFirstImageUrl = mFirstImageUrl;
    }

    public String getmSecondImageUrl() {
        return mSecondImageUrl;
    }

    public void setmSecondImageUrl(String mSecondImageUrl) {
        this.mSecondImageUrl = mSecondImageUrl;
    }

    public Friend getmFriend() {
        return mFriend;
    }

    public void setmFriend(Friend mFriend) {
        this.mFriend = mFriend;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.yes_button:
                onYes();
                dismiss();
                break;
            case R.id.no_button:
                onNo();
                dismiss();
                break;
        }
    }

    private void onYes()
    {
        String url = null;
        String action = null;
        if(mFriend.getmState() == 0)
            action = "tag";
        else if(mFriend.getmState() == 1)
            action = "conf";
        else
            action = "unconf";

        url = ConnectionURL.IMAGE_URL + mFriend.getmAction() + "&action=" + action + "&apikey=" + SharedPref.getAppKey(mContext);
        Task task = new Task(mContext,url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        };
        task.execute();
    }

    private void onNo()
    {
        String url = ConnectionURL.IMAGE_URL + mFriend.getmAction() + "&action=unconf&apikey=" + SharedPref.getAppKey(mContext);
        Task task = new Task(mContext,url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
            }
        };
        task.execute();
    }
}
