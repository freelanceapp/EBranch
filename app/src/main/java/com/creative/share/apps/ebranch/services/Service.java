package com.creative.share.apps.ebranch.services;


import com.creative.share.apps.ebranch.models.App_Data_Model;
import com.creative.share.apps.ebranch.models.Catogries_Market_Model;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.Markets_Model;
import com.creative.share.apps.ebranch.models.Products_Model;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("api/contact-us")
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
    @FormUrlEncoded
    @POST("api/catWithMarkets")
    Call<Catogries_Market_Model> getmarketsbycat(
            @Field("cat_id") String cat_id

    );
    @FormUrlEncoded
    @POST("api/search-market-by-name")
    Call<Markets_Model> getmarketsbyname(
            @Field("name") String name,
            @Header(value = "page") int page

    );
    @GET("api/all-products")
    Call<Products_Model> getproducts(@Header(value = "page") int page
    );
    @FormUrlEncoded
    @POST("api/visit")
    Call<ResponseBody> updateVisit(@Field("type") String type, @Field("date") String date);

}