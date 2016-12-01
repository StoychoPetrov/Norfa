package com.example.stoycho.norfa.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.activitites.HomeActivity;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginFragment extends Fragment implements View.OnClickListener {

    private Button login;
    private Button register;
    private EditText mUsername;
    private EditText mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_login, container, false);
        initUI(root);
        setListeners();
        return root;
    }

    private void initUI(View root)
    {
        login = (Button) root.findViewById(R.id.login);
        register = (Button) root.findViewById(R.id.register);
        mUsername = (EditText) root.findViewById(R.id.username);
        mPassword = (EditText) root.findViewById(R.id.password);
    }

    private void setListeners()
    {
        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.register:
                onRegister();
                break;
            case R.id.login:
                onLogin();
                break;
        }
    }

    private void onLogin()
    {
        String login = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=" + ConnectionURL.LOGIN + "&" + "login=" + mUsername.getText().toString() + "&password=" + mPassword.getText().toString();
        Task task = new Task(getActivity(),login,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject result = new JSONObject(s);
                    if(result.getString("status").equals("OK"))
                    {
                        SharedPref.setApiKey(getActivity(),result.getString("apikey"));
                        Intent startHomeActivity = new Intent(getActivity(), HomeActivity.class);
                        startActivity(startHomeActivity);
                        getFragmentManager().popBackStack();
                    }
                    else
                        Toast.makeText(getActivity(),result.getString("status"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        task.execute();
    }

    private void onRegister()
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.content, CameraFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
