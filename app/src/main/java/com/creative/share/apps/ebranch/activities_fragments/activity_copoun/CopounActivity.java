package com.creative.share.apps.ebranch.activities_fragments.activity_copoun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.creative.share.apps.ebranch.R;
import com.creative.share.apps.ebranch.databinding.ActivityCouponBinding;
import com.creative.share.apps.ebranch.databinding.ActivityMapBinding;
import com.creative.share.apps.ebranch.interfaces.Listeners;
import com.creative.share.apps.ebranch.language.LanguageHelper;
import com.creative.share.apps.ebranch.models.PlaceGeocodeData;
import com.creative.share.apps.ebranch.models.PlaceMapDetailsData;
import com.creative.share.apps.ebranch.models.SelectedLocation;
import com.creative.share.apps.ebranch.models.UserModel;
import com.creative.share.apps.ebranch.preferences.Preferences;
import com.creative.share.apps.ebranch.remote.Api;
import com.creative.share.apps.ebranch.share.Common;
import com.creative.share.apps.ebranch.tags.Tags;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CopounActivity extends AppCompatActivity implements Listeners.BackListener {
    private ActivityCouponBinding binding;
    private String current_lang;
    private Preferences preferences;
    private UserModel userModel;
    private String marketid;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(LanguageHelper.updateResources(newBase, Paper.book().read("lang", "ar")));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_coupon);
        getdattafromintent();

        initView();
    }

    private void getdattafromintent() {
        if(getIntent().getStringExtra("marketid")!=null){
            marketid=getIntent().getStringExtra("marketid");
        }
    }

    private void initView() {
        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        binding.setBackListener(this);
        preferences = Preferences.newInstance();
        userModel = preferences.getUserData(this);
        binding.setLang(current_lang);
        binding.btnUseCoupon.setOnClickListener(view -> {
            checkdata();
        });
    }

    private void checkdata() {
        String copun = binding.edtCoupon.getText().toString();
        Common.CloseKeyBoard(this, binding.edtCoupon);
        if (!TextUtils.isEmpty(copun)) {
            binding.edtCoupon.setError(null);

            findCopun(copun);
        } else {
            if (TextUtils.isEmpty(copun)) {
                binding.edtCoupon.setError(getResources().getString(R.string.field_req));
            } else {
                binding.edtCoupon.setError(null);
            }
        }
    }

    private void findCopun(String copun) {
        try {
            ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
            Api.getService(Tags.base_url).Foundcopun(userModel.getId() + "", copun,marketid).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        Intent intent = getIntent();
                        if (intent!=null)
                        {
                            intent.putExtra("copun",copun);
                            setResult(RESULT_OK,intent);
                        }
                        finish();
                    } else {
                        try {
                            Log.e("error_code",response.code()+"_"+response.errorBody().string());
                        }catch (Exception e){}

                        if (response.code() == 403) {
                            Toast.makeText(CopounActivity.this, getString(R.string.Copun_stoped), Toast.LENGTH_SHORT).show();


                        } else if (response.code() == 406) {
                            Toast.makeText(CopounActivity.this, getString(R.string.Copun_used), Toast.LENGTH_SHORT).show();


                        }

                        else if (response.code() == 422||response.code() == 405) {
                            Toast.makeText(CopounActivity.this, getString(R.string.copoun_notfound), Toast.LENGTH_SHORT).show();

                        } else if (response.code() == 500) {
                            Toast.makeText(CopounActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(CopounActivity.this, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        dialog.dismiss();
                        Toast.makeText(CopounActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    } catch (Exception e) {
                    }
                }
            });
        } catch (Exception e) {

        }
    }


    @Override
    public void back() {
        finish();
    }

}
