package com.creative.share.apps.ebranch.activities_fragments.activity_home;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.FragmentMapTouchListener;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_ContactUs;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_Search;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_Views;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments.Fragment_department;
import com.creative.share.apps.ebranch.activities_fragments.activity_orders.OrdersActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_profile.profileActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_terms.TermsActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_cart.CartActivity;
import com.creative.share.apps.ebranch.databinding.DialogLanguageBinding;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.Markets_Model;
import com.creative.share.apps.ebranch.models.Single_Market_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements OnMapReadyCallback {
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
    private LinearLayout ll_profile,ll_terms,ll_orders,ll_lang,ll_logout;
    private NestedScrollView nestedScrollView;
    private String current_lang;
    private float zoom = 15.6f;

    private Marker marker;
    private GoogleMap mMap;
    private FragmentMapTouchListener fragment;

    private List<Single_Market_Model> maDataList;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView( R.layout.activity_home);
        initView();
        updateUI();
        getmarkets();

        if (savedInstanceState == null) {
            displayFragmentDepartment();
        }


    }

    private void initView() {
        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
maDataList=new ArrayList<>();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        fragmentManager = getSupportFragmentManager();
ahBottomNav=findViewById(R.id.ah_bottom_nav);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ll_profile=findViewById(R.id.ll_profile);
        ll_terms=findViewById(R.id.ll_terms);
        ll_orders=findViewById(R.id.ll_orders);
        ll_lang=findViewById(R.id.ll_lang);
        ll_logout=findViewById(R.id.ll_logout);
//ll_home=findViewById(R.id.ll_home);
        imagemenu=findViewById(R.id.imagemenu);
        im_cart =findViewById(R.id.cart);
nestedScrollView=findViewById(R.id.nestedScrollView);
        String visitTime = preferences.getVisitTime(this);
        Calendar calendar = Calendar.getInstance();
        long timeNow = calendar.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        String date = dateFormat.format(new Date(timeNow));

        if (!date.equals(visitTime)) {
            addVisit(date);
        }
im_cart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(HomeActivity.this, CartActivity.class);
        startActivity(intent);
    }
});
ll_lang.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
CreateLanguageDialog();
    }
});
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Logout();
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
     /*   ll_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(HomeActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });*/
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

    public void displayFragmentDepartment() {
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




    private void updateUI() {

        fragment = (FragmentMapTouchListener) getSupportFragmentManager().findFragmentById(R.id.map);

        if (fragment != null) {
            fragment.getMapAsync(this);

        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            // mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getActivity(), R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);
            mMap.setInfoWindowAdapter(new WindowInfo());
            mMap.setOnInfoWindowClickListener(marker -> {
                Single_Market_Model adModel = (Single_Market_Model) marker.getTag();
                if (adModel!=null)
                {
                  marker.showInfoWindow();


                }
            });
            fragment.setListener(new FragmentMapTouchListener.OnTouchListener() {
                @Override
                public void onTouch() {
                    nestedScrollView.requestDisallowInterceptTouchEvent(true);

                }
            });



            // AddMarker();


        }
    }

    private void AddMarker() {


        // IconGenerator iconGenerator = new IconGenerator(getActivity());
        //iconGenerator.setBackground(null);
        //View view = LayoutInflater.from(getActivity()).inflate(R.layout.search_map_icon, null);
        //iconGenerator.setContentView(view);
        //iconGenerator.setBackground(null);
        //   iconGenerator.setContentView(view);

        //  LatLngBounds.Builder builder = new LatLngBounds.Builder();
       // Log.e("data",maDataList.size()+"");
        for (int i = 0; i < maDataList.size(); i++) {
            //   LatLng ll = new LatLng(x[i], ys[i]);

            // bld.include(ll);
            //Log.e("dd", x[i] + "");
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(Double.parseDouble(maDataList.get(i).getLatitude()), Double.parseDouble(maDataList.get(i).getLongitude())));
            marker=mMap.addMarker(markerOptions);
            //  builder.include(marker[i].getPosition());
            // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(maDataList.get(i).getLatitude()), Double.parseDouble(maDataList.get(i).getLongitude())), zoom));
marker.setTag(maDataList.get(i));

        }

        // CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(),200);

        // mMap.animateCamera(cameraUpdate);


    }
    private void getmarkets() {
        Api.getService(Tags.base_url).getmarkets().enqueue(new Callback<Markets_Model>() {
            @Override
            public void onResponse(Call<Markets_Model> call, Response<Markets_Model> response) {
                if (response.isSuccessful()) {
                    maDataList.clear();
                    if (response.body() != null && response.body().getData() != null && response.body().getData().size() > 0) {
                        maDataList.addAll(response.body().getData());
                        //Log.e("erorr",response.code()+"");

                        AddMarker();
                    }
                    else {
                        Log.e("erorr",response.code()+"");
                    }

                }
            }

            @Override
            public void onFailure(Call<Markets_Model> call, Throwable t) {
                Log.e("erorr",t.getMessage()+"");

            }
        });
    }

    @Override
    public void onBackPressed() {

Back();    }

    private void Back() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            finish();
        }
    }
    private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setCancelable(true)
                .create();

        DialogLanguageBinding binding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_language, null, false);



        if (current_lang.equals("ar"))
        {
            binding.rbAr.setChecked(true);

        }else if (current_lang.equals("en"))
        {
            binding.rbEn.setChecked(true);

        }
        binding.rbAr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshActivity("ar");


            }
        });

        binding.rbEn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                refreshActivity("en");


            }
        });


        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //  dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(binding.getRoot());
        dialog.show();
    }
    private void refreshActivity(String lang) {
        preferences.create_update_language(this, lang);
        Paper.book().write("lang", lang);
        LanguageHelper.setNewLocale(this, lang);
        Intent intent = getIntent();
        finish();
        startActivity(intent);}
    private void addVisit(final String timeNow) {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService(Tags.base_url)
                .updateVisit("1", timeNow)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {

                            try {
                                preferences.saveVisitTime(HomeActivity.this, timeNow);

                            } catch (Exception e) {
                                // e.printStackTrace();
                            }
                            // Log.e("msg",response.body().toString());

                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        dialog.dismiss();
                        try {
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


    private void Logout() {

        if (userModel==null)
        {
            navigateToSignInActivity();
        }else
        {
            preferences.clear(this);
            navigateToSignInActivity();

        }

    }

    private void navigateToSignInActivity() {

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
            finish();
        },200);
    }
    public  class WindowInfo implements GoogleMap.InfoWindowAdapter
    {

        @Override
        public View getInfoWindow(Marker marker) {

            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            Single_Market_Model adModel = (Single_Market_Model) marker.getTag();
            if (adModel==null)
            {
                return null;
            }else
            {
                View view = LayoutInflater.from(HomeActivity.this).inflate(R.layout.window_info_view,null);
                TextView tvTitle = view.findViewById(R.id.tvTitle);
                TextView tvPrice = view.findViewById(R.id.tvPrice);
                TextView tvDetails = view.findViewById(R.id.tvDetails);
                TextView tvAddress = view.findViewById(R.id.tvAddress);
                ImageView image = view.findViewById(R.id.image);

                ProgressBar progBar = view.findViewById(R.id.progBar);

                try {

                    if (adModel.getName()!=null&&!adModel.getName().isEmpty())
                    {
                        tvTitle.setText(adModel.getName());

                    }else
                    {
                        tvTitle.setText(getString(R.string.no_name));

                    }






                            tvAddress.setText(adModel.getAddress());








                    Picasso.with(HomeActivity.this).load(Uri.parse(Tags.base_url+adModel.getLogo())).fit().into(image, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progBar.setVisibility(View.GONE);

                        }
                    });

                }catch (Exception e)
                {
                    if (e!=null&&e.getMessage()!=null)
                    {
                        Log.e("error",e.getMessage());
                    }
                }

                return view;
            }

        }


    }
}
