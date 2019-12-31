package com.creative.share.apps.ebranch.activities_fragments.activity_cart;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_copoun.CopounActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_map.MapActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_orders.OrdersActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.adapters.Cart_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityCartBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Add_Order_Model;
import com.creative.share.apps.ebranch.models.Copuon_Model;
import com.creative.share.apps.ebranch.models.SelectedLocation;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
private List<Add_Order_Model.Products> order_details;
private Cart_Adapter cart_adapter;
private double totalcost;
    private Add_Order_Model add_order_model;
    private SelectedLocation selectedLocation;
    private Copuon_Model copun;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        initView();
getorders();

    }

    private void getorders() {
        if(preferences.getUserOrder(this)!=null){
            order_details.clear();
            order_details.addAll(preferences.getUserOrder(this).getProducts());
            cart_adapter.notifyDataSetChanged();
        gettotal();
        }
        else {
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.radio.setVisibility(View.GONE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);
            binding.tvcopon.setVisibility(View.GONE);

        }
    }

    private void gettotal() {

        double total=0;
        for(int i=0;i<order_details.size();i++){
            total+=order_details.get(i).getTotal_price();

        }
        if(copun==null){
        totalcost=total;}
        else {
            totalcost=total-((total*Integer.parseInt(copun.getValue()))/100);
            Log.e("copun",copun.getName());

        }

        binding.tvTotal.setText(getResources().getString(R.string.total)+totalcost+"");
    }

    @SuppressLint("RestrictedApi")
    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.toolbar.setTitle("");
order_details=new ArrayList<>();
cart_adapter=new Cart_Adapter(order_details,this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.recCart.setLayoutManager(new GridLayoutManager(this,1));
        binding.recCart.setAdapter(cart_adapter);
binding.btCom.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if(userModel!=null){
            checkdata();
        }
        else {
            Common.CreateNoSignAlertDialog(CartActivity.this);
        }
    }
});
binding.tvcopon.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        copun();
    }
});
    }

    private void checkdata() {
         add_order_model=preferences.getUserOrder(this);
           add_order_model.setUser_id(userModel.getId());
           add_order_model.setTotal_cost(totalcost);
           if(binding.rBranch.isChecked()){
               add_order_model.setOrder_type(3);
           }
           else if(binding.rHome.isChecked()){
               add_order_model.setOrder_type(1);
           }
        selectlocation();

    }


    public void removeitem(int layoutPosition) {
        order_details.remove(layoutPosition);
        if(order_details.size()>0){
            Add_Order_Model add_order_model=preferences.getUserOrder(this);
            add_order_model.setProducts(order_details);
            preferences.create_update_order(this,add_order_model);
            gettotal();
        }
        else {
            preferences.create_update_order(this,null);
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.radio.setVisibility(View.GONE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);
            binding.tvcopon.setVisibility(View.GONE);

        }

        cart_adapter.notifyDataSetChanged();

    }
    public void additem(int layoutPosition) {
        Add_Order_Model.Products products1 =order_details.get(layoutPosition);
products1.setTotal_price((products1.getTotal_price()/ products1.getAmount())*(products1.getAmount()+1));
        products1.setAmount(products1.getAmount()+1);
        order_details.remove(layoutPosition);
        order_details.add(layoutPosition, products1);
        Add_Order_Model add_order_model=preferences.getUserOrder(this);
        add_order_model.setProducts(order_details);
        preferences.create_update_order(this,add_order_model);
        cart_adapter.notifyDataSetChanged();
        gettotal();
    }
    public void minusitem(int layoutPosition) {

        Add_Order_Model.Products products1 =order_details.get(layoutPosition);
        if(products1.getAmount()>1){
        products1.setTotal_price((products1.getTotal_price()/ products1.getAmount())*(products1.getAmount()-1));
        products1.setAmount(products1.getAmount()-1);
        order_details.remove(layoutPosition);
        order_details.add(layoutPosition, products1);
        Add_Order_Model add_order_model=preferences.getUserOrder(this);
        add_order_model.setProducts(order_details);
        preferences.create_update_order(this,add_order_model);
        cart_adapter.notifyDataSetChanged();
            gettotal();

        }
    }

    @Override
    public void back() {
        finish();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("location")) {
                selectedLocation = (SelectedLocation) data.getSerializableExtra("location");

                add_order_model.setAddress(selectedLocation.getAddress());
                add_order_model.setLatitude(selectedLocation.getLat());
                add_order_model.setLongitude(selectedLocation.getLng());
                if(copun!=null){
                add_order_model.setCoupon_serial(copun.getCoupon_serial()+"");}
                else {
                    add_order_model.setCoupon_serial("");
                }

accept_order();
            }
        }
        else  if (requestCode == 2 && resultCode == RESULT_OK && data != null) {
            if (data.hasExtra("copun")) {
                copun = (Copuon_Model) data.getSerializableExtra("copun");
totalcost=totalcost-((totalcost*Integer.parseInt(copun.getValue()))/100);
binding.tvTotal.setText(totalcost+"");
            }
        }

    }

    public void selectlocation() {
        Intent intent = new Intent(CartActivity.this, MapActivity.class);
        startActivityForResult(intent, 1);
    }
    private void accept_order() {

        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url).accept_orders(add_order_model).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                dialog.dismiss();
                if (response.isSuccessful()) {
showorders();
                    // Common.CreateSignAlertDialog(activity, getResources().getString(R.string.sucess));

                  //  activity.refresh(Send_Data.getType());
                } else {
                    Common.CreateDialogAlert(CartActivity.this, getString(R.string.failed));

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(CartActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void showorders() {

        preferences.create_update_order(CartActivity.this, null);
        order_details.clear();
        cart_adapter.notifyDataSetChanged();
        binding.llNoStore.setVisibility(View.VISIBLE);
        binding.radio.setVisibility(View.GONE);
        binding.tvTotal.setVisibility(View.GONE);
        binding.btCom.setVisibility(View.GONE);
        binding.tvcopon.setVisibility(View.GONE);
      Common.CreateDialogAlert2(this,getResources().getString(R.string.suc));
    }


    public void copun() {
        Intent intent = new Intent(CartActivity.this, CopounActivity.class);
        add_order_model=preferences.getUserOrder(this);
        intent.putExtra("marketid",add_order_model.getMarket_id()+"");

        startActivityForResult(intent, 2);
    }
}
