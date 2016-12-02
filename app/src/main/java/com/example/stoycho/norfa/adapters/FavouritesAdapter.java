package com.example.stoycho.norfa.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.models.Friend;
import com.example.stoycho.norfa.models.User;
import com.example.stoycho.norfa.tasks.LoadImage;
import com.example.stoycho.norfa.utils.ConnectionURL;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stoycho on 11/21/16.
 */

public class FavouritesAdapter extends BaseAdapter {

    private Context mContext;
    private List<User> mUsers;
    private FriendsAdapter friendsAdapter;
    private LayoutInflater mInflater;
    private Fragment mFragment;

    public FavouritesAdapter(Context context, List<User> users,Fragment fragment)
    {
        this.mContext = context;
        this.mUsers = users;
        this.mFragment = fragment;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final User user = mUsers.get(position);
        if(convertView == null)
            convertView = mInflater.inflate(R.layout.friend_item, parent, false);

        TextView name = (TextView)convertView.findViewById(R.id.name);
        TextView createdOn = (TextView) convertView.findViewById(R.id.joined);
        TextView friendsCount = (TextView) convertView.findViewById(R.id.tagged);
        TextView confirmedFriendsCount = (TextView) convertView.findViewById(R.id.confirmed);
        ImageView profilePicture = (ImageView) convertView.findViewById(R.id.profile_picture);
        ImageView statusImage = (ImageView) convertView.findViewById(R.id.status);
        ImageView secondStatus = (ImageView) convertView.findViewById(R.id.second_status);
        final ListView friendsList = (ListView) convertView.findViewById(R.id.friends_list);
        LinearLayout userLayout = (LinearLayout) convertView.findViewById(R.id.user);

        userLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(friendsList.getLayoutParams().height == 0) {
                    if (user.getFriends().size() > 3)
                        friendsList.getLayoutParams().height = (int) (3.5 * 80 * mContext.getResources().getDisplayMetrics().density);
                    else
                        friendsList.getLayoutParams().height = (int) (user.getFriends().size() * 80 * mContext.getResources().getDisplayMetrics().density);
                }
                else
                {
                    friendsList.getLayoutParams().height = 0;
                }
                friendsList.requestLayout();
            }
        });
        friendsAdapter = new FriendsAdapter(mContext,user.getFriends(),false,mFragment);
        friendsList.setAdapter(friendsAdapter);
        friendsList.getLayoutParams().height = 0;
        statusImage.setVisibility(View.GONE);
        secondStatus.setVisibility(View.GONE);
        name.setText(user.getmName());
        createdOn.setText(user.getmCrDate());
        String friendsCountTxt = mContext.getString(R.string.friends_count) + " " + user.getmFrCnt();
        String confirmedFriendsCountTxt = mContext.getString(R.string.coinfirmed_friends_count) + " " + user.getmCfrCnt();

        friendsCount.setText(friendsCountTxt);
        confirmedFriendsCount.setText(confirmedFriendsCountTxt);

        String imageUrl = ConnectionURL.IMAGE_URL + user.getmPicSrc();
        LoadImage loadImage = new LoadImage(mContext,imageUrl,profilePicture);
        loadImage.execute();

        return convertView;
    }

    public Fragment getmFragment() {
        return mFragment;
    }

    public void setmFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public void setmUsers(List<User> users)
    {
        this.mUsers = users;
    }
}
