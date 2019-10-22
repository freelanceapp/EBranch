package com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.DepartmentActivity;
import com.creative.share.apps.ebranch.databinding.FragmentDepartmentBinding;
import com.creative.share.apps.ebranch.databinding.FragmentViewsBinding;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Views extends Fragment {
    private DepartmentActivity activity;
    private FragmentViewsBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String current_lang;

    public static Fragment_Views newInstance() {
        return new Fragment_Views();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_views, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {

        activity = (DepartmentActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());


        binding.setLang(current_lang);


    }


}
