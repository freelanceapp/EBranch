package com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.adapters.Search_Markets_Adapter;
import com.creative.share.apps.ebranch.databinding.FragmentSearchBinding;
import com.creative.share.apps.ebranch.models.Markets_Model;
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

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Search extends Fragment {
    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String current_lang;
    private String query;
    private GridLayoutManager manager;
private List<Single_Market_Model> dataList;
private Search_Markets_Adapter search_markets_adapter;
    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        dataList=new ArrayList<>();
search_markets_adapter=new Search_Markets_Adapter(dataList,activity);
        binding.progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        binding.progBar.setVisibility(View.GONE);
        binding.recView.setItemViewCacheSize(25);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setDrawingCacheEnabled(true);
        manager=new GridLayoutManager(activity,2);
        binding.recView.setLayoutManager(manager);
        binding.recView.setAdapter(search_markets_adapter);
        binding.setLang(current_lang);
binding.iconSearch.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        query = binding.edtSearch.getText().toString();
        if (!TextUtils.isEmpty(query)) {

            Common.CloseKeyBoard(activity,binding.edtSearch);
            getmarkets(query);}
    }
});
        binding.edtSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                query = binding.edtSearch.getText().toString();
                if (!TextUtils.isEmpty(query)) {

                    Common.CloseKeyBoard(activity,binding.edtSearch);
                    getmarkets(query);
                    return false;
                }
            }
            return false;
        });


    }

    private void getmarkets(String query) {
        dataList.clear();
        search_markets_adapter.notifyDataSetChanged();
        binding.progBar.setVisibility(View.VISIBLE);

        try {


            Api.getService(Tags.base_url)
                    .getmarketsbyname(query,1)
                    .enqueue(new Callback<Markets_Model>() {
                        @Override
                        public void onResponse(Call<Markets_Model> call, Response<Markets_Model> response) {
                            binding.progBar.setVisibility(View.GONE);
                            if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                                dataList.clear();
                                dataList.addAll(response.body().getData());
                                if (response.body().getData().size() > 0) {
                                    // rec_sent.setVisibility(View.VISIBLE);
                                    //  Log.e("data",response.body().getData().get(0).getAr_title());

                                    binding.llNoStore.setVisibility(View.GONE);
                                    search_markets_adapter.notifyDataSetChanged();
                                    //   total_page = response.body().getMeta().getLast_page();

                                } else {
                                    search_markets_adapter.notifyDataSetChanged();

                                    binding.llNoStore.setVisibility(View.VISIBLE);

                                }
                            } else {
                                search_markets_adapter.notifyDataSetChanged();

                                binding.llNoStore.setVisibility(View.VISIBLE);

                                //Toast.makeText(activity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Markets_Model> call, Throwable t) {
                            try {
                                binding.progBar.setVisibility(View.GONE);
                                binding.llNoStore.setVisibility(View.VISIBLE);


                            } catch (Exception e) {
                            }
                        }
                    });
        } catch (Exception e) {
            binding.progBar.setVisibility(View.GONE);
            binding.llNoStore.setVisibility(View.VISIBLE);

        }
    }

}
