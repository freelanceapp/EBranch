package com.creative.share.apps.ebranch.activities_fragments.activity_product_detials;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.adapters.SlidingImage_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityProductDetialsBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Add_Order_Model;
import com.creative.share.apps.ebranch.models.Single_Product_Model;
import com.creative.share.apps.ebranch.models.Slider_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
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

public class ProductDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProductDetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
    private int NUM_PAGES, current_page = 0;
    private SlidingImage_Adapter slidingImage__adapter;
    private String product_id;
    private int amount = 1;
    private int totalamount;
    private Single_Product_Model single_product_model;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_product_detials);
        getdatafromintent();
        initView();
        getSingleproduct();
        //setdata();

//change_slide_image();


    }

    private void getdatafromintent() {
        if (getIntent().getStringExtra("productid") != null) {
            product_id = getIntent().getStringExtra("productid");
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

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.imageDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount > 1) {
                    binding.tvAmount.setText((amount - 1) + "");
                    amount -= 1;
                }

            }
        });
        binding.imageIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (amount < totalamount) {
                    binding.tvAmount.setText((amount + 1) + "");
                    amount += 1;
                }
            }
        });
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Add_Order_Model add_order_model = preferences.getUserOrder(ProductDetialsActivity.this);
                if (add_order_model != null) {
                    Log.e("data", single_product_model.getMarket_id() + " " + add_order_model.getMarket_id());
                    if ((add_order_model.getMarket_id() + "").equals(single_product_model.getMarket_id())) {
                        List<Add_Order_Model.Products> order_details = add_order_model.getProducts();
                        Add_Order_Model.Products products1 = null;
                        int pos = 0;
                        for (int i = 0; i < order_details.size(); i++) {
                            if (single_product_model.getId() == order_details.get(i).getProduct_id()) {
                                products1 = order_details.get(i);
                                pos = i;
                            }
                        }
                        if (products1 != null) {
                            products1.setAmount(amount + order_details.get(pos).getAmount());
                            // Log.e("to",add_order_model.getTotal_cost()+(Double.parseDouble(single_product_model.getPrice())*amount)+""+((amount+order_details.get(pos).getAmount())*Double.parseDouble(single_product_model.getPrice())));
                            products1.setTotal_price(products1.getTotal_price() + (Double.parseDouble(single_product_model.getPrice()) * amount));
                            products1.setImage(single_product_model.getImage());

                            order_details.remove(pos);
                            order_details.add(pos, products1);

                        } else {
                            products1 = new Add_Order_Model.Products();
                            products1.setAmount(amount);
                            products1.setTotal_price(Double.parseDouble(single_product_model.getPrice()) * amount);
                            products1.setProduct_id(single_product_model.getId());
                            products1.setAr_desc(single_product_model.getAr_des());
                            products1.setEn_des(single_product_model.getEn_des());
                            products1.setAr_title(single_product_model.getAr_title());
                            products1.setEn_title(single_product_model.getEn_title());
                            products1.setImage(single_product_model.getImage());
                            order_details.add(products1);

                        }
                        add_order_model.setProducts(order_details);
                        Common.CreateDialogAlert3(ProductDetialsActivity.this, getResources().getString(R.string.suc));

                    } else {
                        Common.CreateDialogAlert(ProductDetialsActivity.this, getResources().getString(R.string.order_pref));
                    }
                } else {
                    add_order_model = new Add_Order_Model();
                    List<Add_Order_Model.Products> order_details = new ArrayList<>();
                    add_order_model.setMarket_id(Integer.parseInt(single_product_model.getMarket_id()));
                    Add_Order_Model.Products products1 = new Add_Order_Model.Products();
                    products1.setProduct_id(single_product_model.getId());
                    products1.setTotal_price(Double.parseDouble(single_product_model.getPrice()) * amount);
                    products1.setAmount(amount);
                    products1.setAr_desc(single_product_model.getAr_des());
                    products1.setEn_des(single_product_model.getEn_des());
                    products1.setAr_title(single_product_model.getAr_title());
                    products1.setEn_title(single_product_model.getEn_title());
                    products1.setImage(single_product_model.getImage());
                    order_details.add(products1);
                    add_order_model.setProducts(order_details);

                    Common.CreateDialogAlert3(ProductDetialsActivity.this, getResources().getString(R.string.suc));


                }
                preferences.create_update_order(ProductDetialsActivity.this, add_order_model);


            }
        });
    }


    private void setdata() {
        List<Slider_Model.Data> dataArrayList = new ArrayList<>();

        dataArrayList.add(new Slider_Model.Data());
        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());


        NUM_PAGES = dataArrayList.size();
        slidingImage__adapter = new SlidingImage_Adapter(this, dataArrayList);
        binding.pager.setAdapter(slidingImage__adapter);


    }


    @Override
    public void back() {
        finish();
    }

    public void getSingleproduct() {
        //  binding.progBar.setVisibility(View.VISIBLE);
        Log.e("pr", product_id);
        ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .getsingleproduct(product_id)
                .enqueue(new Callback<Single_Product_Model>() {
                    @Override
                    public void onResponse(Call<Single_Product_Model> call, Response<Single_Product_Model> response) {
                        dialog.dismiss();
                        // binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null) {
                            updateddata(response.body());

                        } else {

                            Toast.makeText(ProductDetialsActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Single_Product_Model> call, Throwable t) {
                        try {
                            dialog.dismiss();

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }

    private void updateddata(Single_Product_Model body) {
        binding.setProductmodel(body);
        single_product_model = body;
        totalamount = body.getAmount();

    }

}
