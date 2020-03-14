package com.creative.share.apps.ebranch.activities_fragments.activity_order_detials;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.creative.share.apps.ebranch.adapters.Order_Detials_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityOrderdetialsBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.OrderModel;
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

public class OrderDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderdetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;

    private Order_Detials_Adapter order_detials_adapter;
    private List<OrderModel.OrderDetails> orderDetails;
    private String lang;
    private String order_id;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_orderdetials);
        getdatafromintent();

        initView();
        if (order_id != null) {
            getOrderDetials();
        }
    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("orderid") != null) {
            order_id = getIntent().getStringExtra("orderid");
        }
    }


    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        orderDetails = new ArrayList<>();

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        order_detials_adapter = new Order_Detials_Adapter(orderDetails, this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recMarket.setItemViewCacheSize(25);
        binding.recMarket.setDrawingCacheEnabled(true);
        binding.recMarket.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        binding.recMarket.setLayoutManager(new GridLayoutManager(this, 1));
        binding.recMarket.setAdapter(order_detials_adapter);
        binding.setLang(lang);
        binding.setBackListener(this);

    }


    public void getOrderDetials() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getorderdetials(order_id)
                .enqueue(new Callback<OrderModel>() {
                    @Override
                    public void onResponse(Call<OrderModel> call, Response<OrderModel> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getOrder_details() != null) {

                            orderDetails.addAll(response.body().getOrder_details());
                            if (response.body().getOrder_details().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                binding.llNoStore.setVisibility(View.GONE);
                                order_detials_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                binding.llNoStore.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(OrderDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderModel> call, Throwable t) {
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
