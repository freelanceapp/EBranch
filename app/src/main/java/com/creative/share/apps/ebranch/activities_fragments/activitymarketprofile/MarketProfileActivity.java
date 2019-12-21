package com.creative.share.apps.ebranch.activities_fragments.activitymarketprofile;

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

public class MarketProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketProfileBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private int NUM_PAGES, current_page = 0;
    private Market_Department_Adapter market_department_adapter;
    private List<Single_Market_Model.Categories> categoriesList;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_market_profile);
        getdatafromintent();
        initView();
        getSingleMarket();
//change_slide_image();


    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("marketid") != null) {
            marketid = getIntent().getStringExtra("marketid");
        }
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
        categoriesList = new ArrayList<>();
        Paper.init(this);

        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        if (lang.equals("ar")) {
            binding.tvOffer.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape1));
            binding.tvDepart.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape1));
            binding.tvBestseller.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape1));

        } else {
            binding.tvOffer.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape2));
            binding.tvDepart.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape2));
            binding.tvBestseller.setBackgroundDrawable(getResources().getDrawable(R.drawable.text_shape2));
        }
        binding.progBarSlider.setVisibility(View.GONE);
        binding.recOffer.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.recDepartment.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recBestseler.setLayoutManager(new GridLayoutManager(this, 2));
        market_department_adapter = new Market_Department_Adapter(categoriesList, this);
        binding.recDepartment.setAdapter(market_department_adapter);


    }

    public void displayproduct() {
        Intent intent = new Intent(MarketProfileActivity.this, ProductDetialsActivity.class);
        startActivity(intent);
    }


    @Override
    public void back() {
        finish();
    }

    public void getSingleMarket() {
        //  binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getsinglemarket(marketid)
                .enqueue(new Callback<Single_Market_Model>() {
                    @Override
                    public void onResponse(Call<Single_Market_Model> call, Response<Single_Market_Model> response) {
                        // binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            updateddata(response.body());

                        } else {

                            Toast.makeText(MarketProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Single_Market_Model> call, Throwable t) {
                        try {


                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updateddata(Single_Market_Model body) {
        binding.setMarketmodel(body);
        if (body.getCategories() != null) {
            categoriesList.clear();
            categoriesList.addAll(body.getCategories());
            market_department_adapter.notifyDataSetChanged();
        }
    }

}
