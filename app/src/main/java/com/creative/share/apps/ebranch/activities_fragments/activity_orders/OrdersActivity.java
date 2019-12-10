package com.creative.share.apps.ebranch.activities_fragments.activity_orders;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.adapters.order_Adapter;
import com.creative.share.apps.ebranch.databinding.ActivityOrderBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Slider_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrdersActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderBinding binding;
    private String lang;
    private order_Adapter order_adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_order);
        initView();

    }


    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);

binding.setBackListener(this);
binding.recView.setLayoutManager(new GridLayoutManager(this,1));
setdata();
    }


    @Override
    public void back() {
        finish();
    }
    private void setdata() {
        List<Slider_Model.Data> dataArrayList=new ArrayList<>();
       order_adapter=new order_Adapter(dataArrayList,this);


        binding.recView.setAdapter(order_adapter);
        dataArrayList.add(new Slider_Model.Data());
        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());
        order_adapter.notifyDataSetChanged();


    }


}
