package com.creative.share.apps.ebranch.activities_fragments.activity_home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_ContactUs;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_Search;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_Views;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_department;
import com.creative.share.apps.ebranch.activities_fragments.activity_markets.MarketActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_orders.OrdersActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_profile.profileActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.ebranch.activity_cart.CartActivity;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Slider_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private Fragment_Search fragment_search;
    private Fragment_department fragment_department;
    private Fragment_Views fragment_views;
    private Fragment_ContactUs fragment_contactUs;
    private Preferences preferences;
    private UserModel userModel;
private  AHBottomNavigation ahBottomNav;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ImageView imagemenu, im_cart;
    private LinearLayout ll_profile,ll_terms,ll_orders,ll_home;
    private String current_lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.activity_home);
        initView();
        if (savedInstanceState == null) {
            displayFragmentDepartment();
        }


    }

    private void initView() {
        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = getSupportFragmentManager();
ahBottomNav=findViewById(R.id.ah_bottom_nav);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ll_profile=findViewById(R.id.ll_profile);
        ll_terms=findViewById(R.id.ll_terms);
        ll_orders=findViewById(R.id.ll_orders);
ll_home=findViewById(R.id.ll_home);
        imagemenu=findViewById(R.id.imagemenu);
        im_cart =findViewById(R.id.cart);

im_cart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }
});
        setUpBottomNavigation();
       ahBottomNav.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case 0:

                    displayFragmentSearch();
                    break;
                case 1:

                    displayFragmentDepartment();


                    break;
                case 2:
                    displayFragmentViews();

                    break;
                case 3:
                    displayFragmentContactUS();
                    break;

            }
            return false;
        });

        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(HomeActivity.this, profileActivity.class);
                startActivity(intent);

            }
        });
        ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });
        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(HomeActivity.this, TermsActivity.class);
                startActivity(intent);

            }
        });
        ll_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent);

            }
        });
        imagemenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        setdata();
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem("", R.drawable.ic_search);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("", R.drawable.ic_menudepert);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("", R.drawable.ic_views);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("", R.drawable.ic_email);

       ahBottomNav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_HIDE);
       ahBottomNav.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.white));
       ahBottomNav.setTitleTextSizeInSp(14, 12);
       ahBottomNav.setForceTint(true);
       ahBottomNav.setAccentColor(ContextCompat.getColor(this, R.color.colorAccent));
       ahBottomNav.setInactiveColor(ContextCompat.getColor(this, R.color.gray5));

       ahBottomNav.addItem(item1);
       ahBottomNav.addItem(item2);
       ahBottomNav.addItem(item3);
       ahBottomNav.addItem(item4);

       ahBottomNav.setCurrentItem(1);


    }

    public void updateBottomNavigationPosition(int pos) {

       ahBottomNav.setCurrentItem(pos, false);
    }


    private void displayFragmentSearch() {
        try {
            if (fragment_search == null) {
                fragment_search = Fragment_Search.newInstance();
            }
            if (fragment_department != null && fragment_department.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_department).commit();
            }
            if (fragment_views != null && fragment_views.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_views).commit();

            }
            if (fragment_contactUs != null && fragment_contactUs.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_contactUs).commit();

            }
            if (fragment_search.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_search).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_main_app_container, fragment_search, "fragment_search").addToBackStack("fragment_search").commit();

            }
           setTitle(getResources().getString(R.string.search));

            updateBottomNavigationPosition(0);
        } catch (Exception e) {
        }
    }

    private void displayFragmentDepartment() {
        try {
            if (fragment_department == null) {
                fragment_department = Fragment_department.newInstance();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_views != null && fragment_views.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_views).commit();

            }
            if (fragment_contactUs != null && fragment_contactUs.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_contactUs).commit();

            }

            if (fragment_department.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_department).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_main_app_container, fragment_department, "fragment_department").addToBackStack("fragment_department").commit();

            }
            setTitle(getResources().getString(R.string.departments));
            updateBottomNavigationPosition(1);
        } catch (Exception e) {
        }
    }

    private void displayFragmentViews() {
        try {
            if (fragment_views == null) {
                fragment_views = Fragment_Views.newInstance();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_department != null && fragment_department.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_department).commit();

            }
            if (fragment_contactUs != null && fragment_contactUs.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_contactUs).commit();

            }

            if (fragment_views.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_views).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_main_app_container, fragment_views, "fragment_views").addToBackStack("fragment_views").commit();

            }
           setTitle(getResources().getString(R.string.views));

            updateBottomNavigationPosition(2);
        } catch (Exception e) {
        }
    }

    private void displayFragmentContactUS
            () {
        try {
            if (fragment_contactUs == null) {
                fragment_contactUs = Fragment_ContactUs.newInstance();
            }
            if (fragment_search != null && fragment_search.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_search).commit();
            }
            if (fragment_department != null && fragment_department.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_department).commit();

            }
            if (fragment_views != null && fragment_views.isAdded()) {
                fragmentManager.beginTransaction().hide(fragment_views).commit();

            }

            if (fragment_contactUs.isAdded()) {
                fragmentManager.beginTransaction().show(fragment_contactUs).commit();

            } else {
                fragmentManager.beginTransaction().add(R.id.fragment_main_app_container, fragment_contactUs, "fragment_contactUs").addToBackStack("fragment_contactUs").commit();

            }
           setTitle(getResources().getString(R.string.contact_us));

            updateBottomNavigationPosition(3);
        } catch (Exception e) {
        }
    }

    private void setdata() {
        List<Slider_Model.Data> dataArrayList = new ArrayList<>();

        dataArrayList.add(new Slider_Model.Data());
        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());
    }

    public void displaydetials() {
        Intent intent=new Intent(HomeActivity.this, MarketActivity.class);
        startActivity(intent);
    }
}
