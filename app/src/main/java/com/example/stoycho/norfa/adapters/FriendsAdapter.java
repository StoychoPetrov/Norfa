package com.example.stoycho.norfa.adapters;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.fragments.FriendsFragment;
import com.example.stoycho.norfa.models.CustomDialogWindow;
import com.example.stoycho.norfa.models.Friend;
import com.example.stoycho.norfa.tasks.LoadImage;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;

import java.util.List;

/**
 * Created by stoycho on 11/18/16.
 */

public class FriendsAdapter extends BaseAdapter implements Dialog.OnDismissListener {

    private Context mContext;
    private List<Friend> mFriends;
    private LayoutInflater mInflater;
    private CustomDialogWindow customDialogWindow;
    private boolean mHasSecondStatus;
    private Fragment fragment;
    private AlertDialog.Builder alert;
    private int mPosition;

    public FriendsAdapter(Context context,List<Friend> friends,boolean hasSecondStatus,Fragment fragment)
    {
        this.mContext = context;
        this.mFriends = friends;
        this.mHasSecondStatus = hasSecondStatus;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.fragment = fragment;
        customDialogWindow = new CustomDialogWindow(context);
        alert = new AlertDialog.Builder(context);
        alert.setMessage(context.getString(R.string.deleteFriend));
        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFriend(mPosition);
                dialog.dismiss();
            }
        });
        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getCount() {
        return mFriends.size();
    }

    @Override
    public Object getItem(int position) {
        return mFriends.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Friend friend = mFriends.get(position);
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.friend_item, parent, false);

        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView joined = (TextView) convertView.findViewById(R.id.joined);
        TextView tagged = (TextView) convertView.findViewById(R.id.tagged);
        TextView confirmed = (TextView) convertView.findViewById(R.id.confirmed);
        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profile_picture);
        ImageView secondStatus = (ImageView) convertView.findViewById(R.id.second_status);
        customDialogWindow.setOnDismissListener(this);
        final ImageView status = (ImageView) convertView.findViewById(R.id.status);
        if(!mHasSecondStatus)
            secondStatus.setVisibility(View.GONE);
        switch (friend.getmState())
        {
            case 0:
                status.setImageDrawable(ContextCompat.getDrawable(mContext,android.R.drawable.checkbox_on_background));
                break;
            case 1:
                status.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.check_2));
                break;
            case 3:
                status.setImageDrawable(ContextCompat.getDrawable(mContext,R.mipmap.check_1));
                break;
        }

        status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogWindow.setmFirstImageUrl(friend.getmPicSrc1());
                customDialogWindow.setmSecondImageUrl(friend.getmPicSrc2());
                customDialogWindow.setmFriend(friend);
                customDialogWindow.show();
            }
        });

        secondStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
                mPosition = position;
            }
        });
        String imageUrl = ConnectionURL.IMAGE_URL + friend.getmPicSrc();
        LoadImage loadImage = new LoadImage(mContext,imageUrl,profilePicture);
        loadImage.execute();

        name.setText(friend.getmName());
        joined.setText(friend.getmJDate());
        tagged.setText(friend.getmTDate());
        confirmed.setText(friend.getmCDate());

        return convertView;
    }

    private void deleteFriend(final int position)
    {
        String url = ConnectionURL.IMAGE_URL + mFriends.get(position).getmAction() + "&action=del&apikey=" + SharedPref.getAppKey(mContext);
        Task task = new Task(mContext,url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                mFriends.remove(position);
                notifyDataSetChanged();
            }
        };
        task.execute();
    }

    public void setmFriends(List<Friend> friends)
    {
        this.mFriends = friends;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(fragment != null && fragment instanceof FriendsFragment)
        {
            ((FriendsFragment)fragment).downloadFriends();
        }
    }
}
