package com.creative.share.apps.ebranch.activities_fragments.activity_home.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_home.HomeActivity;
import com.creative.share.apps.ebranch.databinding.FragmentMainBinding;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;


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

public class Fragment_Main extends Fragment {
    private HomeActivity activity;
    private FragmentMainBinding binding;
    private Preferences preferences;
    private UserModel userModel;
    private String current_lang;

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        initView();


        return binding.getRoot();
    }


    private void initView() {

        activity = (HomeActivity) getActivity();
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        binding.progBarSlider.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        binding.setLang(current_lang);
        if(current_lang.equals("ar")){
            binding.tvOffer.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.text_shape1));
            binding.tvDepart.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.text_shape1));
            binding.tvBestseller.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.text_shape1));

        }
        else {
            binding.tvOffer.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.text_shape2));
            binding.tvDepart.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.text_shape2));
            binding.tvBestseller.setBackgroundDrawable(activity.getResources().getDrawable(R.drawable.text_shape2));
        }

binding.cons.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        activity.DisplayDepartment();
    }
});

    }


}
