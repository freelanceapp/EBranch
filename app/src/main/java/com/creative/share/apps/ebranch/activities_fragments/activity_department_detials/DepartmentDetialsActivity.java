package com.creative.share.apps.ebranch.activities_fragments.activity_department_detials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_ContactUs;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_Search;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_Views;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_department;
import com.creative.share.apps.ebranch.databinding.ActivityDepartmentBinding;
import com.creative.share.apps.ebranch.databinding.ActivityDepartmentDetialsBinding;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import io.paperdb.Paper;

public class DepartmentDetialsActivity extends AppCompatActivity {
    private ActivityDepartmentDetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_department_detials);
        initView();



    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");


    }





}
