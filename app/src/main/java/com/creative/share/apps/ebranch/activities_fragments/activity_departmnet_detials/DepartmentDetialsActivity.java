package com.creative.share.apps.ebranch.activities_fragments.activity_departmnet_detials;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.creative.share.apps.ebranch.adapters.FilterAdapter;
import com.creative.share.apps.ebranch.adapters.Filter_Rec_Adapter;
import com.creative.share.apps.ebranch.adapters.Products_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityDepartmentDetialsBinding;
import com.creative.share.apps.ebranch.databinding.ActivityMarketProfileBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Filter_model;
import com.creative.share.apps.ebranch.models.Products_Model;
import com.creative.share.apps.ebranch.models.Single_Market_Model;
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

public class DepartmentDetialsActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityDepartmentDetialsBinding binding;

    private Preferences preferences;
    private UserModel userModel;
private List<Filter_model> filter_models;
private FilterAdapter filterAdapter;
private Filter_Rec_Adapter filter_rec_adapter;
    private String lang;
    private String marketid;
    private GridLayoutManager manager;
    private Products_Adapter products_adapter;
    private ArrayList<Products_Model.Data> products;
    private boolean isLoading = false;
    private Filter_model filter_model1,filter_model2,filter_model3;
    private int current_page2 = 1;
private String name="0";
private int newest=0,best=0,low=0;
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
        getproducts();
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
        filter_models=new ArrayList<>();
        products=new ArrayList<>();
     setfiltermodels();

     filterAdapter=new FilterAdapter(filter_models,this);
     filter_rec_adapter=new Filter_Rec_Adapter(filter_models,this);
     Log.e("data",filter_models.size()+"");
    // binding.spCountryfrom.setAdapter(filterAdapter);
        binding.toolbar.setTitle("");
        products=new ArrayList<>();
        Paper.init(this);
        binding.setBackListener(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        products_adapter=new Products_Adapter(products,this);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(this,2);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(products_adapter);
        binding.recView.setNestedScrollingEnabled(true);
        binding.recfilter.setLayoutManager(new GridLayoutManager(this,1));
        binding.recfilter.setAdapter(filter_rec_adapter);
        binding.recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int totalItems = products_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
                    Log.e("data",totalItems+" "+lastVisiblePos);
                    if (lastVisiblePos >= (totalItems - 10) && !isLoading) {
                        isLoading = true;
                        products.add(null);
                        products_adapter.notifyItemInserted(products.size() - 1);
                        int page = current_page2 + 1;
                        loadMore(page);


                    }
                }
            }
        });
      /*  binding.spCountryfrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              if(i==0){
                newest=1;
                best=0;
                low=0;

              }
              else if(i==1){
                  newest=0;
                  best=1;
                  low=0;
              }
              else if(i==2){
                  newest=0;
                  best=0;
                  low=1;
              }
getproducts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                name = binding.edtSearch.getText().toString();
                if (!TextUtils.isEmpty(name)) {

                    Common.CloseKeyBoard(this,binding.edtSearch);
                    getproducts();
                    return false;
                }
            }
            return false;
        });
*/
      binding.imFilter.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              if(binding.expandLayout.isExpanded()){
                  binding.expandLayout.collapse(true);
                  binding.recView.setVisibility(View.VISIBLE);
              }
              else {
                  binding.expandLayout.expand(true);
                  binding.recView.setVisibility(View.GONE);
              }
          }
      });
    }

    private void setfiltermodels() {
        filter_model1=new Filter_model();
        filter_model2=new Filter_model();
        filter_model3=new Filter_model();
        filter_model1.setAr_filter("الاحدث");
        filter_model1.setEn_filter("newest");
        filter_model2.setAr_filter("الاكثر مبيعا");
        filter_model2.setEn_filter("Best seller");
        filter_model3.setAr_filter("الاقل سعرا");
        filter_model3.setEn_filter("low price");
        filter_models.add(filter_model1);
        filter_models.add(filter_model2);
        filter_models.add(filter_model3);
    }

    private void getproducts() {
        products.clear();
        products_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getproductbyfilter(marketid,name,newest,best,low,1)
                    .enqueue(new Callback<Products_Model>() {
                        @Override
                        public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                products.clear();
                                products.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoStore.setVisibility(View.GONE);
                                    products_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    products_adapter.notifyDataSetChanged();

                                    binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {
                                products_adapter.notifyDataSetChanged();

                                binding.llNoStore.setVisibility(View.VISIBLE);

                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Products_Model> call, Throwable t) {
                            try {

                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);
                                Toast.makeText(DepartmentDetialsActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }

    private void loadMore(int page) {
        try {


            Api.getService(Tags.base_url)
                    .getproductbyfilter(marketid,name,newest,best,low,page)
                    .enqueue(new Callback<Products_Model>() {
                        @Override
                        public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                            products.remove(products.size() - 1);
                            products_adapter.notifyItemRemoved(products.size() - 1);
                            isLoading = false;
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                                products.addAll(response.body().getData());
                                // categories.addAll(response.body().getCategories());
                                current_page2 = response.body().getCurrent_page();
                                products_adapter.notifyDataSetChanged();

                            } else {
                                //Toast.makeText(this, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Products_Model> call, Throwable t) {
                            try {
                                products.remove(products.size() - 1);
                                products_adapter.notifyItemRemoved(products.size() - 1);
                                isLoading = false;
                                // Toast.makeText(this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            products.remove(products.size() - 1);
            products_adapter.notifyItemRemoved(products.size() - 1);
            isLoading = false;
        }
    }

    public void displayproduct(String s) {
        Intent intent = new Intent(DepartmentDetialsActivity.this, ProductDetialsActivity.class);
        Log.e("p",s);
        intent.putExtra("productid",s);
        startActivity(intent);
    }


    @Override
    public void back() {
        finish();
    }


    public void Selectitem(int layoutPosition) {
        if(layoutPosition==0){
            newest=1;
            best=0;
            low=0;

        }
        else if(layoutPosition==1){
            newest=0;
            best=1;
            low=0;
        }
        else if(layoutPosition==2){
            newest=0;
            best=0;
            low=1;
        }
        binding.recView.setVisibility(View.VISIBLE);

        binding.expandLayout.collapse(true);
        getproducts();
    }
}
