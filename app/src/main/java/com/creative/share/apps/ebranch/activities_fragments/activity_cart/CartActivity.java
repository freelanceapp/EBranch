package com.creative.share.apps.ebranch.activities_fragments.activity_cart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.adapters.Cart_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityCartBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Add_Order_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class CartActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCartBinding binding;

    private Preferences preferences;
    private UserModel userModel;
    private String lang;
private List<Add_Order_Model.Order_details> order_details;
private Cart_Adapter cart_adapter;

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
            order_details.addAll(preferences.getUserOrder(this).getOrder_details());
            cart_adapter.notifyDataSetChanged();
            double total=0;
            for(int i=0;i<order_details.size();i++){
                total+=order_details.get(i).getTotal_price();
            }
            binding.tvTotal.setText(getResources().getString(R.string.total)+total+"");
        }
        else {
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.radio.setVisibility(View.GONE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);
        }
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


    }




    public void removeitem(int layoutPosition) {
        order_details.remove(layoutPosition);
        if(order_details.size()>0){
            Add_Order_Model add_order_model=preferences.getUserOrder(this);
            add_order_model.setOrder_details(order_details);
            preferences.create_update_order(this,add_order_model);
        }
        else {
            preferences.create_update_order(this,null);
            binding.llNoStore.setVisibility(View.VISIBLE);
            binding.radio.setVisibility(View.GONE);
            binding.tvTotal.setVisibility(View.GONE);
            binding.btCom.setVisibility(View.GONE);
        }
        cart_adapter.notifyDataSetChanged();

    }

    @Override
    public void back() {
        finish();
    }
}
