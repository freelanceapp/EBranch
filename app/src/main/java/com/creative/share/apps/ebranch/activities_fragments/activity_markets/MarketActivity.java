package com.creative.share.apps.ebranch.activities_fragments.activity_markets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.ActivityMarketProfile.MarketProfileActivity;
import com.creative.share.apps.ebranch.adapters.Markets_Adapter;
import com.creative.share.apps.ebranch.adapters.SlidingImage_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityMarketsBinding;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class MarketActivity extends AppCompatActivity {
    private ActivityMarketsBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private int NUM_PAGES,current_page=0;
    private SlidingImage_Adapter slidingImage__adapter;
    private Markets_Adapter markets_adapter;

    private String lang;
private Catogries_Model.Data data;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_markets);
        getdatafromintent();

        initView();

    }

    private void getdatafromintent() {
        if(getIntent().getStringExtra("cat")!=null){
            data = (Catogries_Model.Data) getIntent().getSerializableExtra("cat");
        }
    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.recMarket.setLayoutManager(new GridLayoutManager(this,2));
binding.setLang(lang);
if(data!=null) {
    binding.setMarketmodel(data);
}
    }



    public void displaymarketprofile() {
        Intent intent=new Intent(MarketActivity.this, MarketProfileActivity.class);
        startActivity(intent);
    }
}
