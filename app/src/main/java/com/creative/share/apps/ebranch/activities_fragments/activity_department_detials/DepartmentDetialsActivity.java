package com.creative.share.apps.ebranch.activities_fragments.activity_department_detials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_ContactUs;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_Search;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_Views;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments.Fragment_department;
import com.creative.share.apps.ebranch.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.creative.share.apps.ebranch.adapters.SlidingImage_Adapter;
import com.creative.share.apps.ebranch.adapters.Work_Adapter;
import com.creative.share.apps.ebranch.adapters.offer_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityDepartmentBinding;
import com.creative.share.apps.ebranch.databinding.ActivityDepartmentDetialsBinding;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Slider_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;

public class DepartmentDetialsActivity extends AppCompatActivity {
    private ActivityDepartmentDetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private Work_Adapter work_adapter;
    private int NUM_PAGES,current_page=0;
    private SlidingImage_Adapter slidingImage__adapter;
    private String current_lang;


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

change_slide_image();

    }
    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                binding.pager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }
    @SuppressLint("RestrictedApi")
    private void initView() {
        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");
binding.recDepartment.setLayoutManager(new GridLayoutManager(this,2));
        binding.setLang(current_lang);
        if(current_lang.equals("ar")){
            binding.tvDepart.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape1));

        }
        else {
            binding.tvDepart.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape2));
        }
setdata();
    }
    private void setdata() {
        List<Slider_Model.Data> dataArrayList=new ArrayList<>();
        work_adapter=new Work_Adapter(dataArrayList,this);

        binding.recDepartment.setAdapter(work_adapter);
        dataArrayList.add(new Slider_Model.Data());
        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());
        work_adapter.notifyDataSetChanged();
        NUM_PAGES = dataArrayList.size();
        slidingImage__adapter = new SlidingImage_Adapter(this, dataArrayList);
        binding.pager.setAdapter(slidingImage__adapter);
    }


    public void displayproduct() {
        Intent intent=new Intent(DepartmentDetialsActivity.this, ProductDetialsActivity.class);
        startActivity(intent);
    }
}
