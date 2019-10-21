package com.creative.share.apps.ebranch.activities_fragments.activity_splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;


import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.activities_fragments.activity_sign_in.activities.SignInActivity;
import com.creative.share.apps.ebranch.databinding.ActivitySplashBinding;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.tags.Tags;

import io.paperdb.Paper;

public class Splash_Activity extends AppCompatActivity {

    private ActivitySplashBinding binding;
    private Animation animation;
    private Preferences preferences;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        preferences = Preferences.newInstance();

        animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.lanuch);
        binding.cons.startAnimation(animation);

        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                String session = preferences.getSession(Splash_Activity.this);
                if (session.equals(Tags.session_login))
                {

                }else
                {
                    Intent intent=new Intent(Splash_Activity.this, SignInActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
