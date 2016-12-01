package com.example.stoycho.norfa.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.adapters.FriendsAdapter;
import com.example.stoycho.norfa.models.Friend;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;
import com.example.stoycho.norfa.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FriendsFragment extends Fragment implements AdapterView.OnItemClickListener {

    private List<Friend> mFriends;
    private ListView mFriendsList;
    private FriendsAdapter mFriendAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_friends, container, false);
        initUI(root);
        setListeners();
        mFriends = new ArrayList<>();
        mFriendAdapter = new FriendsAdapter(getActivity(),new ArrayList<Friend>(),true,this);
        mFriendsList.setAdapter(mFriendAdapter);
        downloadFriends();
        // Inflate the layout for this fragment
        return root;
    }

    private void initUI(View root)
    {
        mFriendsList = (ListView)root.findViewById(R.id.friends_list);
    }

    private void setListeners()
    {
        mFriendsList.setOnItemClickListener(this);
    }

    public void downloadFriends()
    {
        mFriends = new ArrayList<>();
        String url = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=friends_list&sz=" + Util.getSize(getActivity()) + "&" + ConnectionURL.APIKEY + "=" + SharedPref.getAppKey(getActivity());
        Task getFriends = new Task(getActivity(),url,null)
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
                            JSONArray friendsArray = result.getJSONArray("friends");
                            for(int i = 0;i < friendsArray.length();i++)
                            {
                                JSONObject friendJson = friendsArray.getJSONObject(i);
                                Friend friend = new Friend();
                                if(friendJson.has("pic_src"))
                                    friend.setmPicSrc(friendJson.getString("pic_src"));
                                if(friendJson.has("pic_click"))
                                    friend.setmPicClick(friendJson.getString("pic_click"));
                                if(friendJson.has("pic1_src"))
                                    friend.setmPicSrc1(friendJson.getString("pic1_src"));
                                if(friendJson.has("pic2_src"))
                                    friend.setmPicSrc2(friendJson.getString("pic2_src"));
                                if(friendJson.has("name"))
                                    friend.setmName(friendJson.getString("name"));
                                if(friendJson.has("jdate"))
                                    friend.setmJDate(friendJson.getString("jdate"));
                                if(friendJson.has("tdate"))
                                    friend.setmTDate(friendJson.getString("tdate"));
                                if(friendJson.has("cdate"))
                                    friend.setmCDate(friendJson.getString("cdate"));
                                if(friendJson.has("state"))
                                    friend.setmState(friendJson.getInt("state"));
                                if(friendJson.has("action"))
                                    friend.setmAction(friendJson.getString("action"));
                                mFriends.add(friend);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mFriendAdapter.setmFriends(mFriends);
                    mFriendAdapter.notifyDataSetChanged();
                }
            }
        };
        getFriends.execute();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BannerFragment bannerFragment = new BannerFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("banner",mFriends.get(position).getmPicClick());
        bannerFragment.setArguments(bundle);
        transaction.replace(R.id.content, bannerFragment,"banner");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
