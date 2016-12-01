package com.example.stoycho.norfa.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.adapters.FavouritesAdapter;
import com.example.stoycho.norfa.models.Friend;
import com.example.stoycho.norfa.models.User;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;
import com.example.stoycho.norfa.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class FavoritesFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mFavouritesList;
    private List<User> mUsers;
    private FavouritesAdapter mFavouritesAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_favourites, container, false);
        initUI(root);
        setListeners();
        mUsers = new ArrayList<>();
        mFavouritesAdapter = new FavouritesAdapter(getActivity(),mUsers);
        mFavouritesList.setAdapter(mFavouritesAdapter);
        if(getArguments() != null && getArguments().containsKey("users")) {
            try {
                createUsers(new JSONArray(getArguments().getString("users")));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else
            loadFavourites();

        return root;
    }

    private void initUI(View root)
    {
        mFavouritesList = (ListView) root.findViewById(R.id.favouritesList);
    }

    private void setListeners()
    {
        mFavouritesList.setOnItemClickListener(this);
    }

    private void loadFavourites()
    {
        String url = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=favs_list&sz=" + Util.getSize(getActivity()) + "&" + ConnectionURL.APIKEY + "=" + SharedPref.getAppKey(getActivity());

        Task getFavourites = new Task(getActivity(),url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s != null) {
                    try {
                        JSONObject result = new JSONObject(s);
                        if (result.getString("status").equals("OK")) {
                            JSONArray users = result.getJSONArray("users");
                            createUsers(users);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        getFavourites.execute();
    }

    public void createUsers(JSONArray users) {
        for (int i = 0; i < users.length(); i++) {
            JSONObject userJson = null;
            try {
                userJson = users.getJSONObject(i);
                User user = new User();
                if (userJson.has("pic_src"))
                    user.setmPicSrc(userJson.getString("pic_src"));
                if (userJson.has("pic_click"))
                    user.setmPickClick(userJson.getString("pic_click"));
                if (userJson.has("name"))
                    user.setmName(userJson.getString("name"));
                if (userJson.has("crdate"))
                    user.setmCrDate(userJson.getString("crdate"));
                if (userJson.has("frcnt"))
                    user.setmFrCnt(userJson.getInt("frcnt"));
                if (userJson.has("cfrcnt"))
                    user.setmCfrCnt(userJson.getInt("cfrcnt"));
                JSONArray friendsJson = userJson.getJSONArray("friends");
                List<Friend> friends = new ArrayList<>();
                for (int j = 0; j < friendsJson.length(); j++) {
                    JSONObject friendJson = friendsJson.getJSONObject(j);
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
                    if (friendJson.has("state"))
                        friend.setmState(friendJson.getInt("state"));
                    if (friendJson.has("action"))
                        friend.setmAction(friendJson.getString("action"));
                    friends.add(friend);
                }
                user.setFriends(friends);
                FavoritesFragment.this.mUsers.add(user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        mFavouritesAdapter.setmUsers(mUsers);
        mFavouritesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    }
}
