package com.creative.share.apps.ebranch.activities_fragments.activity_department.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_department.DepartmentActivity;
import com.creative.share.apps.ebranch.adapters.SlidingImage_Adapter;
import com.creative.share.apps.ebranch.adapters.Work_Adapter;
import com.creative.share.apps.ebranch.adapters.department_Adapter;
import com.creative.share.apps.ebranch.adapters.offer_Adapter;
import com.creative.share.apps.ebranch.databinding.FragmentDepartmentBinding;
import com.creative.share.apps.ebranch.databinding.FragmentSearchBinding;
import com.creative.share.apps.ebranch.models.Slider_Model;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_department extends Fragment {
    private DepartmentActivity activity;
    private FragmentDepartmentBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String current_lang;
    private department_Adapter depart_adapter;

    public static Fragment_department newInstance() {
        return new Fragment_department();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_department, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {

        activity = (DepartmentActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
binding.recDeparment.setLayoutManager(new GridLayoutManager(activity,2));

        binding.setLang(current_lang);
        binding.recDeparment.setNestedScrollingEnabled(true);
setdata();

    }
    private void setdata() {
        List<Slider_Model.Data> dataArrayList=new ArrayList<>();
        depart_adapter=new department_Adapter(dataArrayList,activity);

        binding.recDeparment.setAdapter(depart_adapter);

        dataArrayList.add(new Slider_Model.Data());
        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());

        dataArrayList.add(new Slider_Model.Data());

    depart_adapter.notifyDataSetChanged();

    }



}
