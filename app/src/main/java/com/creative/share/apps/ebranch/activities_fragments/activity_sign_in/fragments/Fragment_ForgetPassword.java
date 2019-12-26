package com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.databinding.DialogAlertBinding;
import com.creative.share.apps.ebranch.databinding.FragmentForgetpasswordBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.models.ForgetModel;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;
import com.mukesh.countrypicker.Country;
import com.mukesh.countrypicker.CountryPicker;
import com.mukesh.countrypicker.listeners.OnCountryPickerListener;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_ForgetPassword extends Fragment implements  Listeners.ForgetpasswordListner {
    private FragmentForgetpasswordBinding binding;
    private SignInActivity activity;
    private String lang;
    private Preferences preferences;
    private ForgetModel forgetModel;

    public static Fragment_ForgetPassword newInstance() {
        return new Fragment_ForgetPassword();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.fragment_forgetpassword, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        forgetModel = new ForgetModel();
        activity = (SignInActivity) getActivity();
        preferences = Preferences.newInstance();
        Paper.init(activity);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setForgetModel(forgetModel);
        binding.setForgetpasswordListner(this);





    }



    @Override
    public void checkDataForget(String emial) {

        forgetModel = new ForgetModel(emial);
        binding.setForgetModel(forgetModel);

        if (forgetModel.isDataValid(activity))
        {
            forget(forgetModel);
        }
    }
    private void forget(ForgetModel forgetModel)
    {
        ProgressDialog dialog = Common.createProgressDialog(activity,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .forget(forgetModel.getEmail())
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();

                            if (response.isSuccessful())
                            {
                              //  Log.e("data",response.body().getPassword_token());
                                CreateAlertDialog(response.body());

                            }
                            else
                            {
                                try {

                                    Log.e("error",response.code()+"_"+response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();



                            }
                        }

                        @Override
                        public void onFailure(Call<UserModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(activity,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(activity,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }
    }



    private void CreateAlertDialog(UserModel userModel)
    {
        final AlertDialog dialog = new AlertDialog.Builder(activity)
                .create();

        DialogAlertBinding binding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.dialog_alert, null, false);

        binding.tvMsg.setText(getString(R.string.you_will_receive_4_digit));

        binding.btnCancel.setOnClickListener(view -> {
            dialog.dismiss();
            activity.displayFragmentCodeVerification(userModel,1);


        });

        dialog.setCanceledOnTouchOutside(false);
        dialog.setView(binding.getRoot());
        dialog.show();
    }



}
