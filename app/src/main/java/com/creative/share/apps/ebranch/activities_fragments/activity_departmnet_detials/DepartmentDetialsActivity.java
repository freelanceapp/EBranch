package com.creative.share.apps.ebranch.activities_fragments.activity_departmnet_detials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.creative.share.apps.ebranch.adapters.Market_Department_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityDepartmentDetialsBinding;
import com.creative.share.apps.ebranch.databinding.ActivityMarketProfileBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Single_Market_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DepartmentDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityDepartmentDetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;


    private String lang;
    private String marketid;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_department_detials);
        getdatafromintent();
        initView();
//change_slide_image();


    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("cat_id") != null) {
            marketid = getIntent().getStringExtra("cat_id");
        }
    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");
        Paper.init(this);
        binding.setBackListener(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);


    }

    public void displayproduct() {
        Intent intent = new Intent(DepartmentDetialsActivity.this, ProductDetialsActivity.class);
        startActivity(intent);
    }


    @Override
    public void back() {
        finish();
    }


}
