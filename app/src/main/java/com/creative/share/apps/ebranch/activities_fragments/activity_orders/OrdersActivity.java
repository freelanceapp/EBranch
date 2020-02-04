package com.creative.share.apps.ebranch.activities_fragments.activity_orders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_orders.fragments.Fragment_Current_Order;
import com.creative.share.apps.ebranch.activities_fragments.activity_orders.fragments.Fragment_Finshied_Order;
import com.creative.share.apps.ebranch.adapters.ViewPagerAdapter;
import com.creative.share.apps.ebranch.databinding.ActivityOrderBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.OrderModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class OrdersActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityOrderBinding binding;
    private String lang;
    private List<Fragment> fragmentList;
    private List<String> titles;
    private ViewPagerAdapter adapter;
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
        EventBus.getDefault().register(this);
        getdatafromintent();

    }
    private void getdatafromintent() {
        if(getIntent().getSerializableExtra("data")!=null){
            OrderModel orderModel= (OrderModel) getIntent().getSerializableExtra("data");
            if(orderModel.getStatus()==9){
                if(fragmentList!=null){
                    Fragment_Finshied_Order fragment_finshied_order= (Fragment_Finshied_Order) fragmentList.get(1);
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fragment_finshied_order.getOrder();
                                    binding.pager.setCurrentItem(1);}
                            },1);
                }}
            else {
                if(fragmentList!=null){
                    Fragment_Current_Order fragment_current_order= (Fragment_Current_Order) fragmentList.get(0);
                    new Handler()
                            .postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    fragment_current_order.getOrder();
                                    binding.pager.setCurrentItem(0);}
                            },1);
                }
            }
                 }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void ListenNotificationChange(OrderModel order_model)
    {
if(order_model.getStatus()==9){
    if(fragmentList!=null){
    Fragment_Finshied_Order fragment_finshied_order= (Fragment_Finshied_Order) fragmentList.get(1);
        new Handler()
                    .postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            fragment_finshied_order.getOrder();
                        binding.pager.setCurrentItem(1);}
                    },1);
}}
else {
    if(fragmentList!=null){
        Fragment_Current_Order fragment_current_order= (Fragment_Current_Order) fragmentList.get(0);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        fragment_current_order.getOrder();
                        binding.pager.setCurrentItem(0);}
                },1);
    }
}

//        if (fragment_myorders!=null&&fragment_myorders.isAdded()&&fragment_myorders.isVisible())
//        {
//            new Handler()
//                    .postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            fragment_myorders.getOrders();                        }
//                    },1);
//        }
//        else {
//            new Handler()
//                    .postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if(fragment_myorders!=null){
//                                fragment_myorders.getOrders();
//                            }
//
//                            DisplayFragmentMyorders();
//                        }
//                    },1);
//        }


    }
    private void initView() {

        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        fragmentList = new ArrayList<>();
        titles = new ArrayList<>();
binding.setBackListener(this);
       binding.tab.setupWithViewPager(binding.pager);
        addFragments_Titles();
        binding.pager.setOffscreenPageLimit(fragmentList.size());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragments(fragmentList);
        adapter.addTitles(titles);
        binding.pager.setAdapter(adapter);







    }

    private void addFragments_Titles() {
        fragmentList.add(Fragment_Current_Order.newInstance());
        fragmentList.add(Fragment_Finshied_Order.newInstance());


        titles.add(getString(R.string.current_order));
        titles.add(getString(R.string.finish_order));



    }

    @Override
    public void back() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this))
        {
            EventBus.getDefault().unregister(this);
        }
    }

}
