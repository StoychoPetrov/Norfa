package com.example.stoycho.norfa.fragments;

import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stoycho.norfa.R;
import com.example.stoycho.norfa.activitites.HomeActivity;
import com.example.stoycho.norfa.activitites.LoginActivity;
import com.example.stoycho.norfa.tasks.Task;
import com.example.stoycho.norfa.utils.ConnectionURL;
import com.example.stoycho.norfa.utils.SharedPref;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegistrationFragment extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener,TextWatcher,EditText.OnFocusChangeListener {

    private EditText login;
    private EditText password;
    private EditText repeatPassword;
    private EditText name;
    private EditText address;
    private EditText city;
    private EditText email;
    private TextView personal;
    private TextView business;
    private TextView createdOn;
    private TextView passwordTxt;
    private TextView confirmPasswordTxt;
    private EditText country;
    private RadioGroup typeGroup;
    private RadioGroup nameGroup;
    private RadioGroup addressGroup;
    private RadioGroup emailGroup;
    private RadioGroup friendsDetailGroup;
    private RadioGroup friendsCountGroup;
    private RadioGroup confirmedFriendCountGroup;
    private RadioGroup sortOrderByGroup;
    private RadioGroup confirmDeleteGroup;
    private RadioGroup bookmarkGroup;
    private RadioGroup deleteGroup;
    private Button submit;
    private Button profilePicture;
    private HashMap<String,String> registrationValues;
    private RelativeLayout createLayout;
    private RelativeLayout loginLayout;
    private Dialog mCountryDialog;
    private ListView coutrnyList;
    private ArrayAdapter<String> bannerAdapter;
    private ArrayAdapter<String> countryAdapter;
    private String[] countries;
    private String[] bannerValues;
    private TextView refreshBanner;
    private View currentEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_registration, container, false);
        initUI(root);
        setListeners();
        registrationValues = new HashMap<>();
        createCountryDialog();
        bannerValues = new String[]{getString(R.string.never),getString(R.string.thirtySec),getString(R.string.sixtySec),getString(R.string.oneHundredAndTwentySec),getString(R.string.hour)};
        bannerAdapter = new ArrayAdapter<>(getActivity(),R.layout.country_item,R.id.name,bannerValues);
        if(getArguments() != null && getArguments().containsKey("edit_profile"))
            setProfileinfo();

        return root;
    }

    private void initUI(View root)
    {
        login = (EditText)root.findViewById(R.id.login_edit);
        password = (EditText)root.findViewById(R.id.password_edit);
        repeatPassword = (EditText)root.findViewById(R.id.confirm_password_edit);
        name = (EditText)root.findViewById(R.id.name_edit);
        address = (EditText)root.findViewById(R.id.address_edit);
        city = (EditText)root.findViewById(R.id.city_edit);
        email = (EditText)root.findViewById(R.id.email_edit);
        personal = (TextView)root.findViewById(R.id.personal);
        business = (TextView)root.findViewById(R.id.business);
        submit = (Button)root.findViewById(R.id.submit);
        typeGroup = (RadioGroup) root.findViewById(R.id.typeGroup);
        nameGroup = (RadioGroup) root.findViewById(R.id.nameRadio);
        addressGroup = (RadioGroup) root.findViewById(R.id.addressRadio);
        emailGroup = (RadioGroup) root.findViewById(R.id.emailRadio);
        friendsDetailGroup = (RadioGroup) root.findViewById(R.id.friendsDetailsRadio);
        friendsCountGroup = (RadioGroup) root.findViewById(R.id.friendCountRadio);
        confirmedFriendCountGroup = (RadioGroup) root.findViewById(R.id.confirmedFriendRadio);
        sortOrderByGroup = (RadioGroup) root.findViewById(R.id.sortOrderRadio);
        confirmDeleteGroup = (RadioGroup) root.findViewById(R.id.confirmDeleteGroup);
        bookmarkGroup = (RadioGroup) root.findViewById(R.id.bookmarkGroup);
        deleteGroup = (RadioGroup) root.findViewById(R.id.deleteGroup);
        country = (EditText) root.findViewById(R.id.country);
        refreshBanner = (TextView) root.findViewById(R.id.refresh_banner_dialog);
        createLayout = (RelativeLayout) root.findViewById(R.id.create_layout);
        createdOn = (TextView) root.findViewById(R.id.createdOn);
        passwordTxt = (TextView) root.findViewById(R.id.passwordTxt);
        confirmPasswordTxt = (TextView) root.findViewById(R.id.confirmPasswordTxt);
        loginLayout = (RelativeLayout) root.findViewById(R.id.login);
        profilePicture = (Button) root.findViewById(R.id.profile_picture);
    }

    private void setListeners()
    {
        submit.setOnClickListener(this);
        typeGroup.setOnCheckedChangeListener(this);
        nameGroup.setOnCheckedChangeListener(this);
        addressGroup.setOnCheckedChangeListener(this);
        emailGroup.setOnCheckedChangeListener(this);
        friendsDetailGroup.setOnCheckedChangeListener(this);
        friendsCountGroup.setOnCheckedChangeListener(this);
        confirmedFriendCountGroup.setOnCheckedChangeListener(this);
        sortOrderByGroup.setOnCheckedChangeListener(this);
        confirmDeleteGroup.setOnCheckedChangeListener(this);
        bookmarkGroup.setOnCheckedChangeListener(this);
        deleteGroup.setOnCheckedChangeListener(this);
        login.addTextChangedListener(this);
        password.addTextChangedListener(this);
        repeatPassword.addTextChangedListener(this);
        city.addTextChangedListener(this);
        name.addTextChangedListener(this);
        address.addTextChangedListener(this);
        email.addTextChangedListener(this);
        login.setOnFocusChangeListener(this);
        password.setOnFocusChangeListener(this);
        repeatPassword.setOnFocusChangeListener(this);
        city.setOnFocusChangeListener(this);
        name.setOnFocusChangeListener(this);
        address.setOnFocusChangeListener(this);
        email.setOnFocusChangeListener(this);
        country.setOnClickListener(this);
        refreshBanner.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id)
        {
            case R.id.submit:
                onSubmit(getArguments().getString("pic_key"));
                break;
            case R.id.country:
                coutrnyList.setAdapter(countryAdapter);
                mCountryDialog.show();
                break;
            case R.id.refresh_banner_dialog:
                showDialogWithRefreshBanner();
                break;
            case R.id.profile_picture:
                startCamera();
                break;
        }
    }

    private void startCamera()
    {
        CameraFragment cameraFragment = new CameraFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("edit_profile",true);
        cameraFragment.setArguments(bundle);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.replace, cameraFragment,"camera_fragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onSubmit(String picKey)
    {
        HashMap<String,String> values = new HashMap<>(registrationValues);
        Iterator<Map.Entry<String, String>> it = values.entrySet().iterator();
        String params = "";
        StringBuilder builder = new StringBuilder();
        while (it.hasNext()) {
            Map.Entry<String, String> value = it.next();
            builder.append(value.getKey()).append("=").append(value.getValue()).append("&");
            it.remove(); // avoids a ConcurrentModificationException
        }
        String url = null;
        if(getActivity() instanceof LoginActivity)
            url = ConnectionURL.BASE_URL +  builder.toString() + ConnectionURL.PIC_KEY + "=" + picKey + "&" + ConnectionURL.CMD + "=acc_add";
        else
            url =  ConnectionURL.BASE_URL +  builder.toString() + ConnectionURL.CMD + "=acc_modify&apikey=" + SharedPref.getAppKey(getActivity());

        Task addAccount = new Task(getActivity(),url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject result = new JSONObject(s);
                    if(result.getString("status").equals("OK"))
                    {
                        if(getActivity() instanceof  LoginActivity) {
                            Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                            startActivity(homeIntent);
                        }
                        else
                            getFragmentManager().popBackStack();
                    }
                    else
                        Toast.makeText(getActivity(),result.getString("status"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        addAccount.execute();

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        int position = -1;
        for(int i =0;i<group.getChildCount();i++)
        {
            RadioButton button = (RadioButton) group.getChildAt(i);
            if(button.isChecked()) {
                button.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.barColor));
                button.setTextColor(ContextCompat.getColor(getActivity(),android.R.color.white));
                position = i;
            }
            else
            {
                button.setBackgroundColor(ContextCompat.getColor(getActivity(),android.R.color.transparent));
                button.setTextColor(ContextCompat.getColor(getActivity(),R.color.barColor));
            }
        }
        if(position != -1) {
            int id = group.getId();
            switch (id) {
                case R.id.typeGroup:
                    registrationValues.put("type",String.valueOf(position));
                    break;
                case R.id.nameRadio:
                    registrationValues.put("fname", String.valueOf(position));
                    break;
                case R.id.addressRadio:
                    registrationValues.put("faddress", String.valueOf(position));
                    break;
                case R.id.emailRadio:
                    registrationValues.put("femail", String.valueOf(position));
                    break;
                case R.id.friendsDetailsRadio:
                    registrationValues.put("friend_details", String.valueOf(position));
                    break;
                case R.id.friendCountRadio:
                    registrationValues.put("friend_cnt", String.valueOf(position));
                    break;
                case R.id.confirmedFriendRadio:
                    registrationValues.put("fconf_friend_cnt", String.valueOf(position));
                    break;
                case R.id.bookmarkGroup:
                    registrationValues.put("fbookmark_tag_pic", String.valueOf(position));
                    break;
                case R.id.confirmDeleteGroup:
                    registrationValues.put("fconfirm_delete", String.valueOf(position));
                    break;
                case R.id.sortOrderRadio:
                    registrationValues.put("fsort_order", String.valueOf(position));
                    break;
                case R.id.deleteGroup:
                    registrationValues.put("fbanner_fr_rot", String.valueOf(position));
                    break;
            }
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus)
            currentEditText = v;
    }

    private void createCountryDialog()
    {
        mCountryDialog = new Dialog(getActivity());
        mCountryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mCountryDialog.setContentView(R.layout.country_dialog);
        coutrnyList = (ListView) mCountryDialog.findViewById(R.id.country_list);
        JSONArray countriesArray = loadJSONFromAsset();
        countries = new String[countriesArray.length()];
        for(int i =0;i<countries.length;i++)
        {
            try {
                countries[i] = countriesArray.getJSONObject(i).getString("name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        countryAdapter = new ArrayAdapter<>(getActivity(),R.layout.country_item,R.id.name,countries);
        coutrnyList.setAdapter(countryAdapter);
        coutrnyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (((ListView) parent).getAdapter().getCount() > 10) {
                    country.setText(countries[position]);
                    registrationValues.put("country",countries[position]);
                }
                else {
                    refreshBanner.setText(bannerValues[position]);
                    registrationValues.put("frefresh_banner",String.valueOf(position));
                }
                mCountryDialog.dismiss();
            }
        });
    }

    public JSONArray loadJSONFromAsset() {
        String information = null;
        JSONArray countries = null;
        try {
            InputStream is = getActivity().getAssets().open("countries.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            information = new String(buffer, "UTF-8");
            countries = new JSONArray(information);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }

    private void showDialogWithRefreshBanner()
    {
        coutrnyList.setAdapter(bannerAdapter);
        mCountryDialog.show();
    }

    private void setProfileinfo()
    {
        String url = ConnectionURL.BASE_URL + ConnectionURL.CMD + "=acc_form&" + "apikey=" + SharedPref.getAppKey(getActivity());
        Task getProfileInfo = new Task(getActivity(),url,null)
        {
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                try {
                    JSONObject result = new JSONObject(s);
                    if(result.getString("status").equals("OK")) {
                        createLayout.setVisibility(View.VISIBLE);
                        loginLayout.setVisibility(View.GONE);
                        passwordTxt.setText(getString(R.string.new_password));
                        confirmPasswordTxt.setText(getString(R.string.confirm_new_password));
                        JSONArray values = result.getJSONArray("values");
                        for (int i = 0; i < values.length(); i++) {
                            JSONObject type = values.getJSONObject(i);
                            switch (i)
                            {
                                case 2:
                                    ((RadioButton) typeGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 3:
                                    name.setText(type.getString("value"));
                                    registrationValues.put("full_name",name.getText().toString());
                                    break;
                                case 4:
                                    address.setText(type.getString("value"));
                                    registrationValues.put("piaddress",address.getText().toString());
                                    break;
                                case 5:
                                    city.setText(type.getString("value"));
                                    registrationValues.put("city",city.getText().toString());
                                    break;
                                case 6:
                                    country.setText(type.getString("value"));
                                    registrationValues.put("country",type.getString("value"));
                                    break;
                                case 7:
                                    email.setText(type.getString("value"));
                                    registrationValues.put("email",email.getText().toString());
                                    break;
                                case 8:
                                    createdOn.setText(type.getString("value"));
                                    break;
                                case 9:
                                    ((RadioButton) nameGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 10:
                                    ((RadioButton) addressGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 11:
                                    ((RadioButton) emailGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 12:
                                    ((RadioButton) friendsDetailGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 13:
                                    ((RadioButton) friendsCountGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 14:
                                    ((RadioButton) confirmedFriendCountGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 15:
                                    ((RadioButton) bookmarkGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 16:
//                                    refreshBanner.setText(bannerValues[type.getInt("value")]);
//                                    registrationValues.put("frefresh_banner",String.valueOf(type.getInt("value")));
                                    break;
                                case 17:
                                    ((RadioButton) confirmDeleteGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 18:
                                    ((RadioButton) sortOrderByGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                                case 19:
//                                    ((RadioButton) deleteGroup.getChildAt(type.getInt("value"))).setChecked(true);
                                    break;
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                typeGroup.getChildAt(0).setEnabled(false);
                typeGroup.getChildAt(1).setEnabled(false);
                profilePicture.setVisibility(View.VISIBLE);
            }
        };
        getProfileInfo.execute();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if(currentEditText != null) {
            switch (currentEditText.getId()) {
                case R.id.login_edit:
                    registrationValues.put("login", login.getText().toString());
                    break;
                case R.id.password_edit:
                    registrationValues.put("password", password.getText().toString());
                    break;
                case R.id.confirm_password_edit:
                    registrationValues.put("password2", repeatPassword.getText().toString());
                    break;
                case R.id.name_edit:
                    registrationValues.put("full_name", name.getText().toString());
                    break;
                case R.id.address_edit:
                    registrationValues.put("piaddress", address.getText().toString());
                    break;
                case R.id.city_edit:
                    registrationValues.put("city", city.getText().toString());
                    break;
                case R.id.email_edit:
                    registrationValues.put("email", email.getText().toString());
                    break;
            }
        }
    }
}
