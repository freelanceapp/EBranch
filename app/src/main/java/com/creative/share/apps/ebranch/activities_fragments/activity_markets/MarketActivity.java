package com.creative.share.apps.ebranch.activities_fragments.activity_markets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.ActivityMarketProfile.MarketProfileActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.creative.share.apps.ebranch.adapters.Markets_Adapter;
import com.creative.share.apps.ebranch.adapters.SlidingImage_Adapter;
import com.creative.share.apps.ebranch.adapters.Work_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityMarketProfileBinding;
import com.creative.share.apps.ebranch.databinding.ActivityMarketsBinding;
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

public class MarketActivity extends AppCompatActivity {
    private ActivityMarketsBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private int NUM_PAGES,current_page=0;
    private SlidingImage_Adapter slidingImage__adapter;
    private Markets_Adapter markets_adapter;

    private String lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_markets);
        initView();
        setdata();
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
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.progBarSlider.setVisibility(View.GONE);
        binding.recDepartment.setLayoutManager(new GridLayoutManager(this,2));


        setdata();

    }

    private void setdata() {
        List<Slider_Model.Data> dataArrayList=new ArrayList<>();
        markets_adapter=new Markets_Adapter(dataArrayList,this);

        binding.recDepartment.setAdapter(markets_adapter);

        dataArrayList.add(new Slider_Model.Data());
        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());
        markets_adapter.notifyDataSetChanged();

        NUM_PAGES = dataArrayList.size();
        slidingImage__adapter = new SlidingImage_Adapter(this, dataArrayList);
        binding.pager.setAdapter(slidingImage__adapter);
    }


    public void displaymarketprofile() {
        Intent intent=new Intent(MarketActivity.this, MarketProfileActivity.class);
        startActivity(intent);
    }
}
