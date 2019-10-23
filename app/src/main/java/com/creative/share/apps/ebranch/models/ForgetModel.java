package com.creative.share.apps.ebranch.models;

import android.content.Context;
import android.text.TextUtils;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.creative.share.apps.ebranch.BR;
import com.creative.share.apps.ebranch.R;

public class ForgetModel extends BaseObservable {

    private String email;

    public ObservableField<String> error_email = new ObservableField<>();



    public ForgetModel() {
        this.email = "";

    }

    public ForgetModel(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);



    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }




    public boolean isDataValid(Context context)
    {
        if (!TextUtils.isEmpty(email) &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
        {
            error_email.set(null);

            return true;
        }else
            {
                if (email.isEmpty()) {
                    error_email.set(context.getString(R.string.field_req));
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    error_email.set(context.getString(R.string.inv_email));
                } else {
                    error_email.set(null);
                }

                return false;
            }
    }


}
