package com.example.stoycho.norfa.activitites;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.fragments.LoginFragment;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if(SharedPref.getAppKey(this) == null)
            startLoginFragment();
        else {
            String url = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=get_apikey&apikey=" + SharedPref.getAppKey(this);
            Task getApikey = new Task(this,url,null)
            {
                @Override
                protected void onPostExecute(String s) {
                    super.onPostExecute(s);
                    try {
                        JSONObject result = new JSONObject(s);
                        if(result.getString("status").equals("OK"))
                        {
                            SharedPref.setApiKey(LoginActivity.this,result.getString("apikey"));
                            Intent startHomeActivity = new Intent(LoginActivity.this, HomeActivity.class);
                            startActivity(startHomeActivity);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            getApikey.execute();

        }
    }

    private void startLoginFragment()
    {
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, loginFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        startLoginFragment();
    }
}