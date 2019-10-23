package com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;


import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments.Fragment_ForgetPassword;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments.Fragment_Language;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments.Fragment_Sign_In;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.fragments.Fragment_Sign_Up;
import com.creative.share.apps.ebranch.databinding.ActivitySignInBinding;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.preferences.Preferences;


import java.util.Locale;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ActivitySignInBinding binding;

    private int fragment_count = 0;
    private Fragment_Sign_In fragment_sign_in;
    private Fragment_Sign_Up fragment_sign_up;
    private Fragment_Language fragment_language;
    private String cuurent_language;
    private Preferences preferences;
    private Fragment_ForgetPassword fragment_forgetpass;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        Paper.init(this);


        initView();
        if (savedInstanceState == null) {
            if (!preferences.isLanguageSelected(this))
            {
                DisplayFragmentLanguage();
            }else
            {
                DisplayFragmentSignIn();

            }
            DisplayFragmentSignIn();

        }


    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.newInstance();
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();

    }

    public void DisplayFragmentSignIn() {
        fragment_count += 1;
        fragment_sign_in = Fragment_Sign_In.newInstance();
        if (fragment_sign_in.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_in).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_in, "fragment_sign_in").addToBackStack("fragment_sign_in").commit();
        }
    }

    public void DisplayFragmentSignUp() {
        fragment_count += 1;
        fragment_sign_up = Fragment_Sign_Up.newInstance();
        if (fragment_sign_up.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_sign_up).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_sign_up, "fragment_sign_up").addToBackStack("fragment_sign_up").commit();
        }
    }

    public void DisplayFragmentLanguage() {
        fragment_language = Fragment_Language.newInstance();
        if (fragment_language.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_language).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_language, "fragment_language").addToBackStack("fragment_language").commit();
        }
    }
    public void displayFragmentForgetpass() {
        fragment_count ++;
        fragment_forgetpass = Fragment_ForgetPassword.newInstance();

        fragmentManager.beginTransaction().add(R.id.fragment_sign_in_container, fragment_forgetpass, "fragment_forgetpass").addToBackStack("fragment_forgetpass").commit();

    }

    public void RefreshActivity(String selected_language) {
        Paper.book().write("lang", selected_language);
        LanguageHelper.setNewLocale(this, selected_language);
        preferences.setIsLanguageSelected(this);


        Intent intent = getIntent();
        finish();
        startActivity(intent);


    }
    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_language!=null&&fragment_language.isAdded()&&fragment_language.isVisible())
        {
            finish();

        }else
        {
            if (fragment_count >1) {
                fragment_count -= 1;
                super.onBackPressed();


            } else  {

                finish();

            }
        }


    }
}
