package com.creative.share.apps.ebranch.activities_fragments.activity_profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_edit_profile.EditProfileActivity;
import com.creative.share.apps.ebranch.databinding.ActivityProfileBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class profileActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityProfileBinding binding;
    private String lang;
    private Preferences preferences;
    private UserModel userModel;
    private boolean isProfileUpdated = false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile);
        initView();

    }


    private void initView() {
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setLang(lang);
        binding.setBackListener(this);
        binding.setUserModel(userModel);
        binding.imageEdit.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivityForResult(intent,100);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==100&&resultCode==RESULT_OK&&data!=null)
        {
            isProfileUpdated = true;
            userModel = preferences.getUserData(this);
            binding.setUserModel(userModel);
        }
    }

    @Override
    public void onBackPressed() {
        back();
    }

    @Override
    public void back() {
        if (isProfileUpdated)
        {
            Intent intent = getIntent();
            setResult(RESULT_OK,intent);
        }
        finish();
    }
}
