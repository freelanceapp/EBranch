package com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;


import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.adapters.CityAdapter;
import com.creative.share.apps.ebranch.databinding.FragmentSignUpBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.models.Cities_Model;
import com.creative.share.apps.ebranch.models.SelectedLocation;
import com.creative.share.apps.ebranch.models.SignUpModel;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment_Sign_Up extends Fragment implements Listeners.SignUpListener,Listeners.BackListener,Listeners.ShowCountryDialogListener, OnCountryPickerListener {
    private SignInActivity activity;
    private String current_language;
    private FragmentSignUpBinding binding;
    private CountryPicker countryPicker;
    private SignUpModel signUpModel;
    private Preferences preferences;
    private CityAdapter adapter;
    private List<Cities_Model.Data> dataList;
    private String city_id = "";
    private SelectedLocation selectedLocation;


    @Override
    public View onCreateView(@androidx.annotation.NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container, @androidx.annotation.Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false);

        initView();
        return binding.getRoot();
    }

    private void initView() {
        dataList=new ArrayList<>();
        signUpModel = new SignUpModel();
        activity = (SignInActivity) getActivity();
        Paper.init(activity);
        preferences = Preferences.newInstance();
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(current_language);
        binding.setSignUpModel(signUpModel);
        binding.setBackListener(this);
        binding.setSignUpListener(this);
        binding.setShowCountryListener(this);
        createCountryDialog();
adapter=new CityAdapter(dataList,activity);
binding.spinnerCity.setAdapter(adapter);
        binding.tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               activity.selectlocation();
            }
        });








        getCities();

        binding.spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "";
                    signUpModel.setCity_id(city_id);
                    binding.setSignUpModel(signUpModel);
                } else {
                    city_id = String.valueOf(dataList.get(i).getId_city());
                    signUpModel.setCity_id(city_id);
                    binding.setSignUpModel(signUpModel);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    private void getCities() {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .getAllCities()
                    .enqueue(new Callback<Cities_Model>() {
                        @Override
                        public void onResponse(Call<Cities_Model> call, Response<Cities_Model> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                if(response.body().getData()!=null){
                                    updateCityAdapter(response.body());}
                                else {
                                    Log.e("error",response.code()+"_"+response.errorBody());

                                }

                            } else {

                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Cities_Model> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }

    }

    private void updateCityAdapter(Cities_Model body) {

        dataList.add(new Cities_Model.Data("إختر المدينه ","choose city"));
if(body.getData()!=null){
        dataList.addAll(body.getData());
        adapter.notifyDataSetChanged();}
    }





    public static Fragment_Sign_Up newInstance() {
        return new Fragment_Sign_Up();
    }

    private void createCountryDialog()
    {
        countryPicker = new CountryPicker.Builder()
                .canSearch(true)
                .listener(this)
                .theme(CountryPicker.THEME_NEW)
                .with(activity)
                .build();

        TelephonyManager telephonyManager = (TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE);

        if (countryPicker.getCountryFromSIM()!=null)
        {
            updatePhoneCode(countryPicker.getCountryFromSIM());
        }else if (telephonyManager!=null&&countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByISO(telephonyManager.getNetworkCountryIso()));
        }else if (countryPicker.getCountryByLocale(Locale.getDefault())!=null)
        {
            updatePhoneCode(countryPicker.getCountryByLocale(Locale.getDefault()));
        }else
        {
            String code = "+966";
            binding.tvCode.setText(code);
            signUpModel.setPhone_code(code.replace("+","00"));

        }

    }

    @Override
    public void showDialog() {

        countryPicker.showDialog(activity);
    }

    @Override
    public void onSelectCountry(Country country) {
        updatePhoneCode(country);

    }

    private void updatePhoneCode(Country country)
    {
        binding.tvCode.setText(country.getDialCode());
        signUpModel.setPhone_code(country.getDialCode().replace("+","00"));

    }

    @Override
    public void checkDataSignUp(String name, String phone_code, String phone,String email, String password,String confirmpassword) {
        if (phone.startsWith("0")) {
            phone = phone.replaceFirst("0", "");
        }
        signUpModel = new SignUpModel(name,city_id,phone_code,phone,email,password,confirmpassword);
        signUpModel.setAddress(selectedLocation.getAddress());
        signUpModel.setLatitude(selectedLocation.getLat()+"");
        signUpModel.setLongitude(selectedLocation.getLng()+"");
        binding.setSignUpModel(signUpModel);

        if (signUpModel.isDataValid(activity))
        {
            signUp(signUpModel);
        }
    }

    private void signUp(SignUpModel signUpModel) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url)
                    .signUp(signUpModel.getName(),signUpModel.getCity_id(),signUpModel.getEmail(),signUpModel.getPassword(),signUpModel.getPhone(),signUpModel.getPhone_code(),"1")
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                activity.displayFragmentCodeVerification(response.body(),2);

                            } else {
                                if (response.code() == 422) {
                                    Toast.makeText(activity,getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                } else if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                }else if (response.code() == 406) {
                                    Toast.makeText(activity,getString(R.string.em_exist), Toast.LENGTH_SHORT).show();

                                }else
                                {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage() != null) {
                                    Log.e("error", t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect") || t.getMessage().toLowerCase().contains("unable to resolve host")) {
                                        Toast.makeText(activity, R.string.something, Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(activity, t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
        }
    }


    @Override
    public void back() {
        activity.Back();
    }


    public void setlocation(SelectedLocation selectedLocation) {
        this.selectedLocation=selectedLocation;
        binding.setLocation(selectedLocation);
        signUpModel.setLatitude(String.valueOf(selectedLocation.getLat()));
        signUpModel.setLongitude(String.valueOf(selectedLocation.getLng()));
        signUpModel.setAddress(selectedLocation.getAddress());
        binding.setSignUpModel(signUpModel);
    }
}
