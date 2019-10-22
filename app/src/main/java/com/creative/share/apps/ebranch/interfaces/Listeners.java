package com.creative.share.apps.ebranch.interfaces;


import com.creative.share.apps.ebranch.models.ContactUsModel;

public interface Listeners {

    interface LoginListener {
        void checkDataLogin(String phone_code, String phone, String password);
    }
    interface ForgetpasswordListner {
        void checkDataForget(String phone_code, String phone);
    }
    interface PasswordListner {
        void checkDatapass(String pass);
    }

    interface ForgetListner
    {
        void forget();
    }
    interface CreateAccountListener
    {
        void createNewAccount();
    }

    interface ShowCountryDialogListener
    {
        void showDialog();
    }

    interface SignUpListener
    {
        void checkDataSignUp(String name, String phone_code, String phone,String email, String password,String confirmpassword);

    }

    interface EditProfileListener
    {
        void checkDataEditProfile(String name);

    }
    interface RatingListener
    {
        void checkDataRating(String desc);

    }
    interface BackListener
    {
        void back();
    }


    interface ContactListener
    {
        void sendContact(ContactUsModel contactUsModel);
    }




}
