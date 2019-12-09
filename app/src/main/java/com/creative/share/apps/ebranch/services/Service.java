package com.creative.share.apps.ebranch.services;


import com.creative.share.apps.ebranch.models.App_Data_Model;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.Markets_Model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface Service {

    @FormUrlEncoded
    @POST("api/contact_us")
    Call<ResponseBody> sendContact(@Field("name") String name,
                                   @Field("email") String email,
                                   @Field("phone") String phone,
                                   @Field("message") String message
    );
    @FormUrlEncoded
    @POST("api/terms")
    Call<App_Data_Model> getterms(@Field("type") int type

    );
    @GET("api/mainCategories")
    Call<Catogries_Model> getDepartment(


    );
    @GET("api/all-markets")
    Call<Markets_Model> getmarkets(


    );
}