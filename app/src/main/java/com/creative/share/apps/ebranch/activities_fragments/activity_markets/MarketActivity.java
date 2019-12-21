package com.creative.share.apps.ebranch.activities_fragments.activity_markets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activitymarketprofile.MarketProfileActivity;
import com.creative.share.apps.ebranch.adapters.Markets_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityMarketsBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Catogries_Market_Model;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MarketActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketsBinding binding;

    private Preferences preferences;
    private UserModel userModel;

    private Markets_Adapter markets_adapter;
private List<Catogries_Market_Model.Data.Users> usersList;
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
        if(data!=null){
        getMarkets();}
    }

    private void getdatafromintent() {
        if(getIntent().getSerializableExtra("cat")!=null){
            data = (Catogries_Model.Data) getIntent().getSerializableExtra("cat");
        }
    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        usersList=new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
markets_adapter=new Markets_Adapter(usersList,this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

binding.recMarket.setItemViewCacheSize(25);
binding.recMarket.setDrawingCacheEnabled(true);
binding.recMarket.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.recMarket.setLayoutManager(new GridLayoutManager(this,2));
        binding.recMarket.setAdapter(markets_adapter);
binding.setLang(lang);
binding.setBackListener(this);
if(data!=null) {
    binding.setMarketmodel(data);
}
    }



    public void displaymarketprofile(Catogries_Market_Model.Data.Users users) {
        Intent intent=new Intent(MarketActivity.this, MarketProfileActivity.class);
        intent.putExtra("marketid",users.getId()+"");
        startActivity(intent);
    }
    public void getMarkets() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getmarketsbycat(data.getId()+"")
                .enqueue(new Callback<Catogries_Market_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Market_Model> call, Response<Catogries_Market_Model> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            usersList.addAll(response.body().getData().getUsers());
                            if (response.body().getData().getUsers().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                binding.llNoStore.setVisibility(View.GONE);
                                markets_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                binding.llNoStore.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(MarketActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Catogries_Market_Model> call, Throwable t) {
                        try {

                            binding.progBar.setVisibility(View.GONE);

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    @Override
    public void back() {
        finish();
    }
}
