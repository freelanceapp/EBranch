package com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.databinding.FragmentNewpasswordBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.models.OrderModel;
import com.creative.share.apps.ebranch.models.PasswordModel;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Newpass extends Fragment implements Listeners.PasswordListner {
    private FragmentNewpasswordBinding binding;
    private SignInActivity activity;
    private String lang;
    private Preferences preferences;
    private PasswordModel passwordModel;
    private UserModel userModel;
    private static final String TAG = "DATA";

    public static Fragment_Newpass newInstance(UserModel userModel) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TAG, userModel);
        Fragment_Newpass fragment_newpass = new Fragment_Newpass();
        fragment_newpass.setArguments(bundle);

        return fragment_newpass;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_newpassword, container, false);
        View view = binding.getRoot();
        initView();
        return view;
    }

    private void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            userModel = (UserModel) bundle.getSerializable(TAG);
        }
        passwordModel = new PasswordModel();
            activity = (SignInActivity) getActivity();
            Paper.init(activity);
            binding.btnLogin.setText(activity.getString(R.string.login));


        preferences = Preferences.newInstance();
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setPassModel(passwordModel);
        binding.setPassListener(this);


    }


    @Override
    public void checkDatapass(String pass) {
            if (passwordModel.isDataValid(activity)) {
                change_pass(pass);
            }


    }

    private void login(String password, UserModel userModel) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .login(userModel.getPhone(),userModel.getPhone_code(), password)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                //  activity.displayFragmentCodeVerification(response.body());
                                preferences.create_update_userdata(activity, response.body());
                                preferences.create_update_session(activity, Tags.session_login);
                                Intent intent = new Intent(activity, HomeActivity.class);
                                startActivity(intent);
                                activity.finish();
                            }else
                            {

                                if (response.code() == 422) {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    //  Log.e("error",response.code()+"_"+response.errorBody()+response.message()+password+phone+phone_code);
                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                else if(response.code()==405){
                                    try {

                                        JSONObject obj = null;

                                        try {
                                            String re=response.errorBody().string();
                                            Log.e("data",re);
                                            obj = new JSONObject(re);
                                            // Log.e("data",obj.stri);

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.e("data",e.getMessage());
                                        }
                                        UserModel userModel1=new UserModel();



                                        Log.e("data",obj.get("id").toString());
                                        userModel1.setId(Integer.parseInt(obj.get("id").toString()));
                                        activity.displayFragmentCodeVerification(userModel1,2);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //    Log.e("data",e.getMessage());
                                    }

                                }
                                else if (response.code()==404)
                                {
                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(activity, R.string.inc_phone_pas, Toast.LENGTH_SHORT).show();

                                }else if (response.code() == 500) {
                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                }
                                else if (response.code()==406)
                                {
                                    Toast.makeText(activity, R.string.user_not_active, Toast.LENGTH_SHORT).show();

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
            dialog.dismiss();

        }
    }

    private void change_pass(String pass) {
        ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .edit_pass(userModel.getId() + "", pass)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                userModel = response.body();
                              login(pass,response.body());


                            } else {
                                try {

                                    Log.e("error", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                if (response.code() == 500) {
                                    Toast.makeText(activity, "Server Error", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();


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
            dialog.dismiss();

        }
    }

}
