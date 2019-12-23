package com.creative.share.apps.ebranch.services;


import com.creative.share.apps.ebranch.models.App_Data_Model;
import com.creative.share.apps.ebranch.models.Catogries_Market_Model;
import com.creative.share.apps.ebranch.models.Catogries_Model;
import com.creative.share.apps.ebranch.models.Cities_Model;
import com.creative.share.apps.ebranch.models.Markets_Model;
import com.creative.share.apps.ebranch.models.OrderDataModel;
import com.creative.share.apps.ebranch.models.OrderModel;
import com.creative.share.apps.ebranch.models.PlaceGeocodeData;
import com.creative.share.apps.ebranch.models.PlaceMapDetailsData;
import com.creative.share.apps.ebranch.models.Products_Model;
import com.creative.share.apps.ebranch.models.Single_Market_Model;
import com.creative.share.apps.ebranch.models.Single_Product_Model;
import com.creative.share.apps.ebranch.models.UserModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {
    @GET("place/findplacefromtext/json")
    Call<PlaceMapDetailsData> searchOnMap(@Query(value = "inputtype") String inputtype,
                                          @Query(value = "input") String input,
                                          @Query(value = "fields") String fields,
                                          @Query(value = "language") String language,
                                          @Query(value = "key") String key
    );

    @GET("geocode/json")
    Call<PlaceGeocodeData> getGeoData(@Query(value = "latlng") String latlng,
                                      @Query(value = "language") String language,
                                      @Query(value = "key") String key);
    @FormUrlEncoded
    @POST("api/client/register")
    Call<UserModel> signUp(@Field("full_name") String full_name,
                           @Field("city_id") String city_id,
                           @Field("email") String email,
                           @Field("password") String password,
                           @Field("phone") String phone,
                           @Field("phone_code") String phone_code,

                           @Field("software_type") String software_type
    );
    @FormUrlEncoded
    @POST("api/client/login")
    Call<UserModel> login(@Field("phone") String user_phone,
                          @Field("phone_code") String phone_code,
                          @Field("password") String user_pass);
    @FormUrlEncoded
    @POST("api/client/cofirm-code")
    Call<ResponseBody> confirmCode(@Field("user_id") int user_id,
                                   @Field("code") String code
    );
    @FormUrlEncoded
    @POST("api/client/code/send")
    Call<ResponseBody> resendCode(@Field("user_id") int user_id
    );
    @GET("api/ALl-Cities")
    Call<Cities_Model> getAllCities();
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
    @POST("api/single-market")
    Call<Single_Market_Model> getsinglemarket(
            @Field("market_id") String market_id

    );
    @FormUrlEncoded
    @POST("api/single-product")
    Call<Single_Product_Model> getsingleproduct(
            @Field("product_id") String product_id

    );
    @FormUrlEncoded
    @POST("api/search-market-by-name")
    Call<Markets_Model> getmarketsbyname(
            @Field("name") String name,
            @Header(value = "page") int page

    );
    @GET("api/all-products")
    Call<Products_Model> getproducts(@Query(value = "page") int page
    );
    @FormUrlEncoded
    @POST("api/productsByMarket")
    Call<Products_Model> getproductbymarket(@Field("market_id") String market_id,
                                          @Field("page") int page
    );
    @FormUrlEncoded
    @POST("api/products-filter")
    Call<Products_Model> getproductbyfilter(@Field("cat_id") String cat_id,
                                            @Field("name") String name,
                                            @Field("newest") int newest,
                                            @Field("best_sales") int best_sales,
                                            @Field("lowest_price") int lowest_price,
                                            @Field("page") int page
    );
    @FormUrlEncoded
    @POST("api/visit")
    Call<ResponseBody> updateVisit(@Field("type") String type, @Field("date") String date);
    @FormUrlEncoded
    @POST("api/client/currentOrders")
    Call<OrderDataModel> getcurrentOrders(@Field("user_id") int user_id,
                                          @Field("page") int page
    );
    @FormUrlEncoded
    @POST("api/client/previousOrders")
    Call<OrderDataModel> getfinshiorders(@Field("user_id") int user_id,
                                          @Field("page") int page
    );
    @FormUrlEncoded
    @POST("api/single-order")
    Call<OrderModel> getorderdetials(
            @Field("order_id") String order_id

    );
}