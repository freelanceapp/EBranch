package com.creative.share.apps.ebranch.activities_fragments.activitymarketprofile;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_departmnet_detials.DepartmentDetialsActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_product_detials.ProductDetialsActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.activities_fragments.chat_activity.ChatActivity;
import com.creative.share.apps.ebranch.adapters.Market_Department_Adapter;
import com.creative.share.apps.ebranch.adapters.Offer_Adapter;
import com.creative.share.apps.ebranch.adapters.Products_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityMarketProfileBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.ChatUserModel;
import com.creative.share.apps.ebranch.models.Products_Model;
import com.creative.share.apps.ebranch.models.RoomIdModel;
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

public class MarketProfileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityMarketProfileBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private int NUM_PAGES, current_page = 0;
    private Market_Department_Adapter market_department_adapter;
    private List<Single_Market_Model.Categories> categoriesList;

    private String lang;
    private String marketid;
    private Products_Adapter products_adapter;
    private Offer_Adapter offer_adapter;
    private List<Products_Model.Data> products;
    private List<Products_Model.Data> offerproducts;
    private LinearLayoutManager manager,manager2;
    private boolean isLoading = false;
    private int current_page2 = 1;
private Single_Market_Model single_market_model;
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
        getproducts();
        getproductsoffer();
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
        products=new ArrayList<>();
        offerproducts=new ArrayList<>();
        Paper.init(this);

        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recBestseler.setNestedScrollingEnabled(false);
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
        manager2=new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false);
        binding.recOffer.setNestedScrollingEnabled(false);

        binding.recOffer.setLayoutManager(manager2);
        binding.recDepartment.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        products_adapter = new Products_Adapter(products, this,null);
        binding.recBestseler.setItemViewCacheSize(25);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        manager = new GridLayoutManager(this, 2);
        binding.recBestseler.setLayoutManager(manager);
        binding.recBestseler.setNestedScrollingEnabled(true);
        binding.recBestseler.setAdapter(products_adapter);
        binding.recOffer.setItemViewCacheSize(25);
        binding.recOffer.setHasFixedSize(true);
        offer_adapter=new Offer_Adapter(offerproducts,this);
        binding.recOffer.setAdapter(offer_adapter);
        binding.arrow3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("data",manager2.findFirstVisibleItemPosition()+"");
                binding.arrow2.setVisibility(View.VISIBLE);

                if(manager2.findFirstVisibleItemPosition()>0){
                  // binding.recOffer.scrollToPosition(manager2.findFirstVisibleItemPosition()-1);
                    binding.recOffer.smoothScrollToPosition(manager2.findFirstVisibleItemPosition() - 1);
                    if(manager2.findFirstVisibleItemPosition()<1){
                       binding.arrow3.setVisibility(View.GONE);
                   }
                }
                else {
                    binding.recOffer.smoothScrollToPosition(0);
                    binding.arrow3.setVisibility(View.GONE);

                }

            }
        });
        binding.arrow2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.arrow3.setVisibility(View.VISIBLE);
                if(manager2.findLastVisibleItemPosition()<offerproducts.size()){
                    binding.recOffer.smoothScrollToPosition(manager2.findLastVisibleItemPosition() + 1);
                    if(manager2.findLastVisibleItemPosition()>=offerproducts.size()-1){
                        binding.arrow2.setVisibility(View.GONE);

                    }
                }
            }
        });
        binding.recBestseler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
Log.e("dy",dy+"");
                if (dy > 0) {
                    int totalItems = products_adapter.getItemCount();
                    int lastVisiblePos = manager.findLastCompletelyVisibleItemPosition();
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
        market_department_adapter = new Market_Department_Adapter(categoriesList, this);
        binding.recDepartment.setAdapter(market_department_adapter);
binding.consChat.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        getChatRoomId();
    }
});

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
        this.single_market_model=body;
        if (body.getCategories() != null) {
            categoriesList.clear();
            categoriesList.addAll(body.getCategories());
            market_department_adapter.notifyDataSetChanged();
        }
    }
    private void getproductsoffer() {
        offerproducts.clear();
        offer_adapter.notifyDataSetChanged();
      //  binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getofferproductbymarket(marketid)
                    .enqueue(new Callback<Products_Model>() {
                        @Override
                        public void onResponse(Call<Products_Model> call, Response<Products_Model> response) {
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                offerproducts.clear();
                                offerproducts.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                      Log.e("data",response.body().getData().size()+"");

                                    binding.llNoOffer.setVisibility(View.GONE);
                                    offer_adapter.notifyDataSetChanged();
                                    if(offerproducts.size()>1){
updateui();}
                                    else {
                                        binding.arrow2.setVisibility(View.GONE);
                                        binding.arrow3.setVisibility(View.GONE);
                                    }
                                //    binding.recOffer.smoothScrollToPosition(0);
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    offer_adapter.notifyDataSetChanged();

                                    binding.llNoOffer.setVisibility(View.VISIBLE);
                                    binding.arrow2.setVisibility(View.GONE);
                                    binding.arrow3.setVisibility(View.GONE);

                                }
                            } else {
                                offer_adapter.notifyDataSetChanged();

                                binding.llNoOffer.setVisibility(View.VISIBLE);
                                binding.arrow2.setVisibility(View.GONE);
                                binding.arrow3.setVisibility(View.GONE);
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

                                binding.llNoOffer.setVisibility(View.VISIBLE);
                                binding.arrow2.setVisibility(View.GONE);
                                binding.arrow3.setVisibility(View.GONE);
                                Toast.makeText(MarketProfileActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


                                Log.e("error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.llNoOffer.setVisibility(View.VISIBLE);

        }
    }

    private void updateui() {

        new Handler().postDelayed(() -> {

          //  binding.recOffer.smoothScrollToPosition(0);
            manager2.setSmoothScrollbarEnabled(true);

            binding.recOffer.smoothScrollToPosition(manager2.findLastVisibleItemPosition()-1);
            manager2.scrollToPosition(0);
            binding.arrow3.setVisibility(View.GONE);
            Log.e("datas",manager2.findLastVisibleItemPosition()+"");
        }, 10);

    }

    private void getproducts() {
        products.clear();
        products_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getproductbymarket(marketid, 1)
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
                                Toast.makeText(MarketProfileActivity.this, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();


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
                    .getproductbymarket(marketid, page)
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

    public void DisplayDepartdetials(Single_Market_Model.Categories id) {
        Intent intent = new Intent(MarketProfileActivity.this, DepartmentDetialsActivity.class);
        intent.putExtra("cat_id",id.getId()+"");

        intent.putExtra("markt_id",marketid);

        startActivity(intent);
    }

        public void displayproduct(String s) {
            Intent intent = new Intent(MarketProfileActivity.this, ProductDetialsActivity.class);
            Log.e("p",s);
            intent.putExtra("productid",s);
            startActivity(intent);

    }
    private void getChatRoomId() {

        ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        try {

            Api.getService(Tags.base_url)
                    .getRoomId(userModel.getId(),single_market_model.getId())
                    .enqueue(new Callback<RoomIdModel>() {
                        @Override
                        public void onResponse(Call<RoomIdModel> call, Response<RoomIdModel> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()&&response.body()!=null)
                            {
                                ChatUserModel chatUserModel = new ChatUserModel(single_market_model.getFull_name(),single_market_model.getLogo(),single_market_model.getId(),response.body().getRoom_id(),"",single_market_model.getPhone());
                                Intent intent = new Intent(MarketProfileActivity.this, ChatActivity.class);
                                intent.putExtra("chat_user_data",chatUserModel);
                                startActivity(intent);
                            }else
                            {
                                if (response.code() == 500) {
                                    Toast.makeText(MarketProfileActivity.this, "Server Error", Toast.LENGTH_SHORT).show();


                                }else
                                {
                                    Toast.makeText(MarketProfileActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                                    try {

                                        Log.e("error",response.code()+"_"+response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<RoomIdModel> call, Throwable t) {
                            try {
                                dialog.dismiss();
                                if (t.getMessage()!=null)
                                {
                                    Log.e("error",t.getMessage());
                                    if (t.getMessage().toLowerCase().contains("failed to connect")||t.getMessage().toLowerCase().contains("unable to resolve host"))
                                    {
                                        Toast.makeText(MarketProfileActivity.this,R.string.something, Toast.LENGTH_SHORT).show();
                                    }else
                                    {
                                        Toast.makeText(MarketProfileActivity.this,t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }catch (Exception e){}
                        }
                    });
        }catch (Exception e){
            dialog.dismiss();

        }

    }

}
