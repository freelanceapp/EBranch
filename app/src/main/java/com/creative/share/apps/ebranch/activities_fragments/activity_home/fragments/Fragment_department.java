package com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.activities_fragments.activity_markets.MarketActivity;
import com.creative.share.apps.ebranch.adapters.Department_Adapter;
import com.creative.share.apps.ebranch.databinding.FragmentDepartmentBinding;
import com.creative.share.apps.ebranch.models.Catogries_Model;
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

public class Fragment_department extends Fragment {
    private HomeActivity activity;
    private FragmentDepartmentBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String current_lang;
    private Department_Adapter depart_adapter;
    private List<Catogries_Model.Data> dataList;

    public static Fragment_department newInstance() {
        return new Fragment_department();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_department, container, false);
        initView();
getDepartments();

        return binding.getRoot();
    }


    private void initView() {
dataList=new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.recDeparment.setLayoutManager(new GridLayoutManager(activity,2));
depart_adapter=new Department_Adapter(dataList,activity,this);
        binding.recDeparment.setItemViewCacheSize(25);
        binding.recDeparment.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recDeparment.setDrawingCacheEnabled(true);
binding.recDeparment.setAdapter(depart_adapter);

        binding.setLang(current_lang);
        binding.recDeparment.setNestedScrollingEnabled(true);

    }
    public void getDepartments() {
        binding.progBar.setVisibility(View.VISIBLE);

        Api.getService(Tags.base_url)
                .getDepartment()
                .enqueue(new Callback<Catogries_Model>() {
                    @Override
                    public void onResponse(Call<Catogries_Model> call, Response<Catogries_Model> response) {
                        binding.progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {

                            dataList.addAll(response.body().getData());
                            if (response.body().getData().size() > 0) {
                                // rec_sent.setVisibility(View.VISIBLE);

                                binding.llNoStore.setVisibility(View.GONE);
                                depart_adapter.notifyDataSetChanged();
                                //   total_page = response.body().getMeta().getLast_page();

                            } else {
                                 binding.llNoStore.setVisibility(View.VISIBLE);

                            }
                        } else {

                            Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            try {
                                Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Catogries_Model> call, Throwable t) {
                        try {

                            binding.progBar.setVisibility(View.GONE);

                            //    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }


    public void DisplayDepartmentMarket(Catogries_Model.Data data) {
        Intent intent=new Intent(activity, MarketActivity.class);
        intent.putExtra("cat",data);
        startActivity(intent);
    }
}
