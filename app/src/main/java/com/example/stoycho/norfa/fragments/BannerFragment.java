package com.example.stoycho.norfa.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.activitites.HomeActivity;
import com.example.stoycho.norfa.adapters.FriendsAdapter;
import com.example.stoycho.norfa.models.Friend;
import com.example.stoycho.norfa.models.User;
import com.example.stoycho.norfa.tasks.LoadImage;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;
import com.example.stoycho.norfa.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class BannerFragment extends Fragment implements View.OnClickListener {

    private ImageView mBannerImage;
    private TextView mName;
    private TextView mAddress;
    private TextView mEmail;
    private TextView mCreatedOn;
    private TextView mFriendCount;
    private TextView mConfirmedFriendCount;
    private ListView mFriendList;
    private ImageView mReload;
    private ImageView mBrowser;
    private FriendsAdapter mFriendsAdapter;
    private User mUser;
    private boolean isReload;
    private int isFavorite;
    private Handler handler;
    private Runnable runnable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_banner, container, false);
        initUI(root);
        setListeners();
        handler = new Handler();
        mFriendsAdapter = new FriendsAdapter(getActivity(),new ArrayList<Friend>(),false,this);
        mFriendList.setAdapter(mFriendsAdapter);
        if(getArguments() != null && getArguments().containsKey("own"))
        {
            mReload.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_reload_icon));
            mBrowser.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_browser_icon));
        }
        else
        {
            mReload.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_location_icon));
            mBrowser.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_favorite_star_icon));
        }
        loadBanner();
        handler = new Handler();
        // Inflate the layout for this fragment
        runnable = new Runnable() {
            @Override
            public void run() {
                loadBanner();
                handler.postDelayed(this,mUser.getmReload());
            }
        };
        return root;
    }

    private void initUI(View root)
    {
        mBannerImage = (ImageView) root.findViewById(R.id.image);
        mName = (TextView) root.findViewById(R.id.name);
        mAddress = (TextView) root.findViewById(R.id.address);
        mEmail = (TextView) root.findViewById(R.id.email);
        mCreatedOn = (TextView) root.findViewById(R.id.createdOn);
        mFriendCount = (TextView) root.findViewById(R.id.friend_count);
        mConfirmedFriendCount = (TextView) root.findViewById(R.id.confirmed_friend_count);
        mFriendList = (ListView) root.findViewById(R.id.friends);
        mReload = (ImageView) root.findViewById(R.id.reload);
        mBrowser = (ImageView) root.findViewById(R.id.browser);
    }

    private void setListeners()
    {
        mReload.setOnClickListener(this);
        mBrowser.setOnClickListener(this);
    }

    public void loadBanner()
    {
        String url = null;
        if(getArguments() != null && getArguments().containsKey("banner"))
            url = ConnectionURL.IMAGE_URL +  getArguments().getString("banner") + "&apikey=" + SharedPref.getAppKey(getActivity()) + "&sz=" + Util.getSize(getActivity());
        else
            url = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=banner&sz=" + Util.getSize(getActivity()) + "&" + ConnectionURL.APIKEY + "=" + SharedPref.getAppKey(getActivity());
        Task task = new Task(getActivity(),url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s != null)
                {
                    try {
                        JSONObject result = new JSONObject(s);
                        if(result.getString("status").equals("OK"))
                        {
                            if(result.has("friends")) {
                                JSONArray friendsArray = result.getJSONArray("friends");
                                List<Friend> friends = new ArrayList<>();
                                for (int i = 0; i < friendsArray.length(); i++) {
                                    JSONObject friendJson = friendsArray.getJSONObject(i);
                                    Friend friend = new Friend();
                                    if (friendJson.has("pic_src"))
                                        friend.setmPicSrc(friendJson.getString("pic_src"));
                                    if (friendJson.has("pic_click"))
                                        friend.setmPicClick(friendJson.getString("pic_click"));
                                    if (friendJson.has("pic1_src"))
                                        friend.setmPicSrc1(friendJson.getString("pic1_src"));
                                    if (friendJson.has("pic2_src"))
                                        friend.setmPicSrc2(friendJson.getString("pic2_src"));
                                    if (friendJson.has("name"))
                                        friend.setmName(friendJson.getString("name"));
                                    if (friendJson.has("jdate"))
                                        friend.setmJDate(friendJson.getString("jdate"));
                                    if (friendJson.has("tdate"))
                                        friend.setmTDate(friendJson.getString("tdate"));
                                    if (friendJson.has("cdate"))
                                        friend.setmCDate(friendJson.getString("cdate"));
                                    if (friendJson.has("status"))
                                        friend.setmStatus(friendJson.getString("status"));
                                    if (friendJson.has("action"))
                                        friend.setmAction(friendJson.getString("action"));
                                    if(friendJson.has("state"))
                                        friend.setmState(friendJson.getInt("state"));
                                    else
                                        friend.setmState(-1);
                                    friends.add(friend);
                                }
                                mFriendsAdapter.setmFriends(friends);
                                mFriendsAdapter.notifyDataSetChanged();
                            }
                        }

                        JSONObject userJson = result.getJSONObject("user");
                        mUser = new User();
                        if(userJson.has("pic_src"))
                            mUser.setmPicSrc(userJson.getString("pic_src"));
                        if(userJson.has("pic_click"))
                            mUser.setmPickClick(userJson.getString("pic_click"));
                        if(userJson.has("name"))
                            mUser.setmName(userJson.getString("name"));
                        if(userJson.has("crdate"))
                            mUser.setmCrDate(userJson.getString("crdate"));
                        if(userJson.has("frcnt"))
                            mFriendCount.setText(userJson.getString("frcnt"));
                        if(userJson.has("cfrcnt"))
                            mConfirmedFriendCount.setText(userJson.getString("cfrcnt"));
                        if(userJson.has("address"))
                            mUser.setmAddress(userJson.getString("address"));
                        if(userJson.has("email"))
                            mUser.setmEmail(userJson.getString("email"));
                        if(userJson.has("reload"))
                            mUser.setmReload(userJson.getInt("reload"));
                        if(userJson.has("next"))
                            mUser.setmNext(userJson.getString("next"));
                        if(getArguments() != null && !getArguments().containsKey("own")) {
                            if (userJson.has("favorite")) {
                                if (userJson.getInt("favorite") == 0) {
                                    mBrowser.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_not_favorite_star_icon));
                                    mUser.setmFavourites(true);
                                } else {
                                    mBrowser.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_favorite_star_icon));
                                    mUser.setmFavourites(false);
                                }
                                isFavorite = userJson.getInt("favorite");
                            }
                        }
                        if(userJson.has("fav_change"))
                            mUser.setmFavChange(userJson.getString("fav_change"));

                        mName.setText(mUser.getmName());
                        mEmail.setText(mUser.getmEmail());
                        mAddress.setText(mUser.getmAddress());
                        mCreatedOn.setText(mUser.getmCrDate());


                        String imageUrl = ConnectionURL.IMAGE_URL + mUser.getmPicSrc();
                        LoadImage loadImage = new LoadImage(getActivity(),imageUrl,mBannerImage);
                        loadImage.execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        task.execute();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.reload:
                onReload();
                break;
            case R.id.browser:
                if(getArguments() != null && getArguments().containsKey("own"))
                    ((HomeActivity)getActivity()).setBarHidden();
                else
                    onFavorite();
                break;
        }
    }

    private void onReload()
    {

        if(getArguments() != null && getArguments().containsKey("own")) {
            if (isReload) {
                mReload.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_reload_icon));
                isReload = false;
                handler.removeCallbacks(runnable);

            } else {
                mReload.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.mipmap.ic_dont_reload_icon));
                isReload = true;
                handler.postDelayed(runnable,mUser.getmReload());
            }
        }
        else
        {
            Uri gmmIntentUri = Uri.parse("google.navigation:q=Sofia");
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
    }

    private void onFavorite()
    {
        String url = ConnectionURL.IMAGE_URL + mUser.getmFavChange() + "&apikey=" + SharedPref.getAppKey(getActivity()) + "&what=" + (isFavorite == 0?1:0);
        Task task = new Task(getActivity(),url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    if(new JSONObject(s).getString("status").equals("OK"))
                    {
                        if(isFavorite == 1)
                        {
                            mBrowser.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_not_favorite_star_icon));
                            isFavorite = 0;
                        }
                        else
                        {
                            mBrowser.setImageDrawable(ContextCompat.getDrawable(getActivity(),R.mipmap.ic_favorite_star_icon));
                            isFavorite = 1;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        task.execute();
    }
}
