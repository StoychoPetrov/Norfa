package com.example.stoycho.norfa.activitites;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.fragments.BannerFragment;
import com.example.stoycho.norfa.fragments.CameraFragment;
import com.example.stoycho.norfa.fragments.FavoritesFragment;
import com.example.stoycho.norfa.fragments.FriendsFragment;
import com.example.stoycho.norfa.fragments.RegistrationFragment;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;
import com.example.stoycho.norfa.utils.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends FragmentActivity implements View.OnClickListener ,DrawerLayout.DrawerListener,AdapterView.OnItemClickListener {

    private DrawerLayout mDrawerLayout;
    private ImageButton mDrawerButton;
    private RelativeLayout mContentLayout;
    private ListView mDrawerList;
    private ImageButton mAddButton;
    private ImageButton mFindButton;
    private TextView mLogOut;
    private EditText mSearch;
    private TextView mTitle;
    private int mOnEditRegistration;
    private RelativeLayout mMainLayout;
    private RelativeLayout mBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initUI();
        setListeners();
        String[] drawerItems = getResources().getStringArray(R.array.drawer);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.drawer_item,R.id.drawerLabel,drawerItems);
        mDrawerList.setAdapter(adapter);
        mOnEditRegistration = -1;
        onFriends();
    }

    private void initUI()
    {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mDrawerButton = (ImageButton) findViewById(R.id.drawer_button);
        mContentLayout = (RelativeLayout) findViewById(R.id.push_leyout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);
        mLogOut = (TextView) findViewById(R.id.log_out);
        mAddButton = (ImageButton) findViewById(R.id.add);
        mFindButton = (ImageButton) findViewById(R.id.find);
        mMainLayout = (RelativeLayout) findViewById(R.id.replace);
        mSearch = (EditText) findViewById(R.id.search);
        mTitle = (TextView) findViewById(R.id.title);
        mBar = (RelativeLayout) findViewById(R.id.bar);
    }

    private void setListeners()
    {
        mDrawerButton.setOnClickListener(this);
        mDrawerLayout.addDrawerListener(this);
        mDrawerList.setOnItemClickListener(this);
        mLogOut.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
        mFindButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id)
        {
            case R.id.drawer_button:
                onDrawerButton();
                break;
            case R.id.log_out:
                finish();
                SharedPref.setApiKey(this,null);
                break;
            case R.id.find:
                if(mSearch.getVisibility() == View.VISIBLE)
                    showSearchBox(false);
                else
                    showSearchBox(true);
                break;
            case R.id.add:
                addFriends();
                break;
        }
    }

    private void addFriends()
    {
        CameraFragment cameraFragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("add_friends",true);
        cameraFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, cameraFragment,"camera");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onDrawerButton()
    {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START))
            mDrawerLayout.closeDrawer(GravityCompat.START);
        else
            mDrawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        mContentLayout.setTranslationX(drawerView.getWidth() * slideOffset);
    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
       switch (mOnEditRegistration)
       {
           case 0:
               onFriends();
               break;
           case 1:
               onFavourites();
               break;
           case 2:
               onBanner(true);
               break;
           case 3:
               onEditProfile();
               break;
       }
        mOnEditRegistration = -1;
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    public void onBackPressed()
    {
        if(getFragmentManager().getBackStackEntryCount() > 1)
            super.onBackPressed();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int listId = parent.getId();
        switch (listId)
        {
            case R.id.drawer_list:
                onDrawerList(position);
                break;
        }
    }

    private void onDrawerList(int position)
    {
        switch (position)
        {
            case 0:
                FriendsFragment friendsFragment = (FriendsFragment) getFragmentManager().findFragmentByTag("friends");
                if(friendsFragment == null || !friendsFragment.isVisible()) {
                    mOnEditRegistration = position;
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case 1:
                FavoritesFragment favoritesFragment = (FavoritesFragment) getFragmentManager().findFragmentByTag("favourites");
                if(favoritesFragment == null || !favoritesFragment.isVisible()) {
                    mOnEditRegistration = position;
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case 2:
                BannerFragment bannerFragment = (BannerFragment) getFragmentManager().findFragmentByTag("banner");
                if(bannerFragment == null || !bannerFragment.isVisible()) {
                    mOnEditRegistration = position;
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
            case 3:
                RegistrationFragment registrationFragment = (RegistrationFragment) getFragmentManager().findFragmentByTag("edit_registration");
                if(registrationFragment == null || !registrationFragment.isVisible()) {
                    mOnEditRegistration = position;
                    mDrawerLayout.closeDrawer(GravityCompat.START);
                }
                break;
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void onFriends()
    {
        FriendsFragment friendsFragment = new FriendsFragment();
        Bundle bundle = new Bundle();
        friendsFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, friendsFragment,"friends");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onEditProfile()
    {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("edit_profile",true);
        registrationFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, registrationFragment,"edit_registration");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onFavourites()
    {
        FavoritesFragment favoritesFragment = new FavoritesFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, favoritesFragment,"favourites");
        transaction.addToBackStack("favourites");
        transaction.commit();
    }

    private void onBanner(boolean own)
    {
        BannerFragment bannerFragment = new BannerFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("own",own);
        bannerFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, bannerFragment,"banner");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showSearchBox(final boolean searchVisible)
    {
        Animation fadeIn = new AlphaAnimation(0,1);
        fadeIn.setDuration(250);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setStartOffset(250);

        Animation fadeOut = new AlphaAnimation(1,0);
        fadeOut.setDuration(250);
        fadeOut.setInterpolator(new AccelerateInterpolator());

        fadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(!searchVisible)
                    mSearch.setVisibility(View.INVISIBLE);
                else
                    mTitle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(searchVisible)
                    mSearch.setVisibility(View.VISIBLE);
                else
                    mTitle.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if(searchVisible) {
            mTitle.startAnimation(fadeOut);
            mSearch.startAnimation(fadeIn);
        }
        else
        {
            mTitle.startAnimation(fadeIn);
            mSearch.startAnimation(fadeOut);
        }

        if(!searchVisible && !mSearch.getText().toString().equals(""))
        {
            String ur = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=search&" + ConnectionURL.APIKEY + "=" + SharedPref.getAppKey(this) + "&sz=" + Util.getSize(this) + "&text=" + mSearch.getText().toString();
            Task search = new Task(this,ur,null)
            {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    if(s != null) {
                        try {
                            JSONObject result = new JSONObject(s);
                            if(result.getString("status").equals("OK")) {
                                JSONArray users = result.getJSONArray("users");
                                FavoritesFragment fragment = (FavoritesFragment) getFragmentManager().findFragmentByTag("favourites");
                                if(fragment != null)
                                {
                                    getFragmentManager().popBackStackImmediate("favourites",0);
                                    fragment.createUsers(users);
                                }
                                else {
                                    FavoritesFragment favoritesFragment = new FavoritesFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putString("users", users.toString());
                                    favoritesFragment.setArguments(bundle);
                                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                    transaction.replace(R.id.content, favoritesFragment, "favourites");
                                    transaction.addToBackStack(null);
                                    transaction.commit();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            search.execute();
        }
    }

    public void setBarHidden()
    {
        ValueAnimator animator;
        if(mBar.getLayoutParams().height == 50 * getResources().getDisplayMetrics().density)
        {
            animator = ObjectAnimator.ofInt(mBar.getHeight(),0);
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
        else {
            animator = ObjectAnimator.ofInt(mBar.getHeight(),(int)(50 * getResources().getDisplayMetrics().density));
            mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        animator.setDuration(250);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBar.getLayoutParams().height = (Integer) animation.getAnimatedValue();
                mBar.requestLayout();
            }
        });
        animator.start();
    }
}
