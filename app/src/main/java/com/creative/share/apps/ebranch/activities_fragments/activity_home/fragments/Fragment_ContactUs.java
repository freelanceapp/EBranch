package com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.databinding.FragmentContactusBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.models.ContactUsModel;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_ContactUs extends Fragment implements Listeners.ContactListener {
    private HomeActivity activity;
    private FragmentContactusBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String current_lang;
private ContactUsModel contactUsModel;
    public static Fragment_ContactUs newInstance() {
        return new Fragment_ContactUs();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_contactus, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {
contactUsModel=new ContactUsModel();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
binding.setContactListener(this);
binding.setContactUs(contactUsModel);
        binding.setLang(current_lang);


    }


    @Override
    public void sendContact(ContactUsModel contactUsModel) {
        if(contactUsModel.isDataValid(activity)){

        }
    }
}
