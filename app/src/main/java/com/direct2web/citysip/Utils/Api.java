package com.direct2web.citysip.Utils;


import com.direct2web.citysip.Model.DoctorModels.DoctorAboutYou.ResponseDoctorAboutYou;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ImageAndVedio.ResponseDoctorAddImageAndVedio;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ResponseDoctorAddImage;
import com.direct2web.citysip.Model.DoctorModels.DoctorAddImage.ResponseDoctorAddVedio;
import com.direct2web.citysip.Model.DoctorModels.DoctorAppointment.ResponseDoctorAppointment;
import com.direct2web.citysip.Model.DoctorModels.DoctorBusinessDetails.ResponseDoctorBusinessDetails;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.ResponseDoctorAddCoupons;
import com.direct2web.citysip.Model.DoctorModels.DoctorCouponsOffers.ResponseDoctorCoupons;
import com.direct2web.citysip.Model.DoctorModels.DoctorDeleteData.ResponseDoctorDelete;
import com.direct2web.citysip.Model.DoctorModels.DoctorDeshboard.ResponseDoctorDeshboard;
import com.direct2web.citysip.Model.DoctorModels.DoctorSaveProfile.ResponseDoctorSaveProfile;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseDoctorAddService;
import com.direct2web.citysip.Model.DoctorModels.DoctorServices.ResponseDoctorServices;
import com.direct2web.citysip.Model.DoctorModels.ResponseDoctorGetTiming;
import com.direct2web.citysip.Model.DoctorModels.ResponseDoctorSendTiming;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceAboutYou.ResponseInsuranceAboutYou;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceAppointment.ResponseInsuranceAppointment;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceBusinessDetails.ResponseInsuranceBusinessDetails;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceDeleteData.ResponseInsuranceDelete;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceDeshboard.ResponseInsuranceDeshboard;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceSaveProfile.ResponseInsuranceSaveProfile;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ResponseInsuranceAddService;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ResponseInsuranceServiceAndCompanyList;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceServices.ResponseInsuranceServices;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceTiming.ResponseInsuranceGetTiming;
import com.direct2web.citysip.Model.InsuranceModel.InsuranceTiming.ResponseInsuranceSendTiming;
import com.direct2web.citysip.Model.LawyerModels.LawyerAboutYou.ResponseLawyerAboutYou;
import com.direct2web.citysip.Model.LawyerModels.LawyerAddImage.ImageAndVedio.ResponseLawyerAddImageAndVedio;
import com.direct2web.citysip.Model.LawyerModels.LawyerAddImage.ResponseLawyerAddImage;
import com.direct2web.citysip.Model.LawyerModels.LawyerAddImage.ResponseLawyerAddVedio;
import com.direct2web.citysip.Model.LawyerModels.LawyerAppointment.ResponseLawyerAppointment;
import com.direct2web.citysip.Model.LawyerModels.LawyerBusinessDetails.ResponseLawyerBusinessDetails;
import com.direct2web.citysip.Model.LawyerModels.LawyerDeleteData.ResponseLawyerDelete;
import com.direct2web.citysip.Model.LawyerModels.LawyerDeshboard.ResponseLawyerDeshboard;
import com.direct2web.citysip.Model.LawyerModels.LawyerSaveProfile.ResponseLawyerSaveProfile;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.ResponseLawyerAddService;
import com.direct2web.citysip.Model.LawyerModels.LawyerServices.ResponseLawyerServices;
import com.direct2web.citysip.Model.LawyerModels.ResponseLawyerSendTiming;
import com.direct2web.citysip.Model.RestaurentModels.BasicDetails.ResponseGetBasicDetails;
import com.direct2web.citysip.Model.RestaurentModels.BusinessDetails.ResponseBusinessDetails;
import com.direct2web.citysip.Model.RestaurentModels.Category.ResponseCategoryList;
import com.direct2web.citysip.Model.RestaurentModels.CreateAccount.ResponseCreateAccount;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.ResponseCuisinesList;
import com.direct2web.citysip.Model.RestaurentModels.Cuisines.ResponseImageDishNameTypeList;
import com.direct2web.citysip.Model.RestaurentModels.CustomerList.ResponseCustomerList;
import com.direct2web.citysip.Model.RestaurentModels.Dashboard.ResponseDashboard;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseDelete;
import com.direct2web.citysip.Model.RestaurentModels.Delete.ResponseStatus;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.ResponseAddImages;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.ResponseAddVideo;
import com.direct2web.citysip.Model.RestaurentModels.ImageVideo.ResponseImageVideoList;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.ResponseMenuList;
import com.direct2web.citysip.Model.RestaurentModels.MenuList.ResponseNewMenuList;
import com.direct2web.citysip.Model.RestaurentModels.OrderList.ResponseOrderList;
import com.direct2web.citysip.Model.RestaurentModels.OtpSend.ResponseOtpSend;
import com.direct2web.citysip.Model.RestaurentModels.OtpSend.ResponseVerifyMobile;
import com.direct2web.citysip.Model.RestaurentModels.OtpSend.ResponseVerifyPassword;
import com.direct2web.citysip.Model.RestaurentModels.Profile.ResponseSendProfile;
import com.direct2web.citysip.Model.RestaurentModels.SetUpMenu.ResponseSetMenu;
import com.direct2web.citysip.Model.RestaurentModels.Timming.ResponseGetTimingList;
import com.direct2web.citysip.Model.RestaurentModels.Timming.ResponseHoursTitleList;
import com.direct2web.citysip.Model.RestaurentModels.Timming.ResponseSendTiming;
import com.direct2web.citysip.Model.RestaurentModels.aboutrestaurant.AboutResturant;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseAddOffer;
import com.direct2web.citysip.Model.RestaurentModels.offer.ResponseOfferList;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface Api {

    //String url = "http://direct2web.in/app_demo/kabir_fresh/ws/";
    String imageUrl = "http://www.direct2web.in/city_slip/";
    String url = "http://www.direct2web.in/city_slip/api-firebase/";

    @POST("restaurant/business_side/fcm_token.php")
    @FormUrlEncoded
    Call<JsonObject> setToken(@Header("Authorization") String authHeader,
                              @Field("accesskey") String accesskey,
                              @Field("token") String token,
                              @Field("uid") String uid,
                              @Field("device_id") String device_id);

    @POST("business_side/get-categories.php")
    @FormUrlEncoded
    Call<ResponseCategoryList> getCategory(@Header("Authorization") String authHeader,
                                           @Field("accesskey") String accesskey,
                                           @Field("city_id") String city_id,
                                           @Field("mobile") String mobile);

    @POST("business_side/verify-mobile.php")
    @FormUrlEncoded
    Call<ResponseVerifyMobile> sendVerifiMobile(@Header("Authorization") String authHeader,
                                                @Field("accesskey") String accesskey,
                                                @Field("mobile") String mobile);


    @POST("business_side/send-otp.php")
    @FormUrlEncoded
    Call<ResponseOtpSend> sendOtp(@Header("Authorization") String authHeader,
                                  @Field("accesskey") String accesskey,
                                  @Field("mobile") String mobile,
                                  @Field("otp") String otp);

    @POST("business_side/create-account.php")
    @FormUrlEncoded
    Call<ResponseCreateAccount> sendCreateAccount(@Header("Authorization") String authHeader,
                                                  @Field("accesskey") String accesskey,
                                                  @Field("mobile") String mobile,
                                                  @Field("cat_id") String cat_id);

    @POST("business_side/verify-password.php")
    @FormUrlEncoded
    Call<ResponseVerifyPassword> sendVerifyPassword(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("mobile") String mobile,
                                                    @Field("password") String password);

    @POST("restaurant/business_side/save_profile.php")
    @FormUrlEncoded
    Call<ResponseSendProfile> sendProfile(@Header("Authorization") String authHeader,
                                          @Field("accesskey") String accesskey,
                                          @Field("user_id") String user_id,
                                          @Field("address_line_1") String address_line_1,
                                          @Field("address_line_2") String address_line_2,
                                          @Field("city_id") String city_id,
                                          @Field("email_id") String email_id,
                                          @Field("business_name") String business_name,
                                          @Field("business_phone_no") String business_phone_no,
                                          @Field("phone_no") String phone_no,
                                          @Field("website") String website,
                                          @Field("description") String description,
                                          @Field("latitude") String latitude,
                                          @Field("longitude") String longitude,
                                          @Field("password") String password);

    @POST("restaurant/business_side/get-cuisines-dish.php")
    @FormUrlEncoded
    Call<ResponseCuisinesList> getCuisine(@Header("Authorization") String authHeader,
                                          @Field("accesskey") String accesskey);


    //Add Offers
    @Multipart
    @POST("restaurant/business_side/add-offer.php")
    Call<ResponseAddOffer> sendSetUpOffers(@Header("Authorization") String authHeader,
                                           @Part("accesskey") RequestBody accesskey,
                                           @Part("business_id") RequestBody businessId,
                                           @Part("coupn_code") RequestBody coupnCode,
                                           @Part("min_amount") RequestBody minAmount,
                                           @Part("max_amount") RequestBody maxAmount,
                                           @Part("terms_condition") RequestBody termsCondition,
                                           @Part("percentage") RequestBody percentage);


    // Offers List
    @POST("restaurant/business_side/offer-list.php")
    @FormUrlEncoded
    Call<ResponseOfferList> getOffers(@Header("Authorization") String authHeader,
                                      @Field("accesskey") String accesskey,
                                      @Field("business_id") String id);

    //get-busigness details
    @POST("restaurant/business_side/get-business-detail.php")
    @FormUrlEncoded
    Call<AboutResturant> getBusignessDetails(@Header("Authorization") String authHeader,
                                             @Field("accesskey") String accesskey,
                                             @Field("business_id") String business_id);

    @POST("restaurant/business_side/get_offer_dish.php")
    @FormUrlEncoded
    Call<ResponseImageDishNameTypeList> getDishName(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("business_id") String business_id);

    @Multipart
    @POST("restaurant/business_side/set-up-menu.php")
    Call<ResponseSetMenu> sendSetUpMenu(@Header("Authorization") String authorization,
                                        @Part("accesskey") RequestBody accesskey,
                                        @Part("business_id") RequestBody business_id,
                                        @Part("title") RequestBody title,
                                        @Part("cuisines") RequestBody cuisines,
                                        @Part("dish_type") RequestBody dish_type,
                                        @Part("amount") RequestBody amount,
                                        @Part("max_dish") RequestBody max_dish,
                                        @Part("description") RequestBody description,
                                        @Part("category") RequestBody category,
                                        @Part("offer") RequestBody offer,
                                        @Part MultipartBody.Part image);


    @Multipart
    @POST("restaurant/business_side/add-image.php")
    Call<ResponseAddImages> sendImages(@Header("Authorization") String authorization,
                                       @Part("accesskey") RequestBody accesskey,
                                       @Part("business_id") RequestBody s1,
                                       @Part MultipartBody.Part[] s12);

    @Multipart
    @POST("restaurant/business_side/add-image.php")
    Call<ResponseAddImages> sendImagesFromList(@Header("Authorization") String authorization,
                                               @Part("accesskey") RequestBody accesskey,
                                               @Part("business_id") RequestBody s1,
                                               @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("restaurant/business_side/add-video.php")
    Call<ResponseAddVideo> sendVideo(@Header("Authorization") String authorization,
                                     @Part("accesskey") RequestBody accesskey,
                                     @Part("business_id") RequestBody s1,
                                     @Part MultipartBody.Part[] s12);


    @POST("restaurant/business_side/menu_list.php")
    @FormUrlEncoded
    Call<ResponseMenuList> getMenuList(@Header("Authorization") String authHeader,
                                       @Field("accesskey") String accesskey,
                                       @Field("business_id") String business_id);

    @POST("restaurant/business_side/media-list.php")
    @FormUrlEncoded
    Call<ResponseImageVideoList> getMedia(@Header("Authorization") String authHeader,
                                          @Field("accesskey") String accesskey,
                                          @Field("business_id") String business_id);

    @POST("restaurant/business_side/get-order.php")
    @FormUrlEncoded
    Call<ResponseOrderList> getOrderList(@Header("Authorization") String authHeader,
                                         @Field("accesskey") String accesskey,
                                         @Field("business_id") String business_id);

    @POST("restaurant/business_side/get-basic-detail.php")
    @FormUrlEncoded
    Call<ResponseGetBasicDetails> getBasicDetails(@Header("Authorization") String authHeader,
                                                  @Field("accesskey") String accesskey,
                                                  @Field("business_id") String business_id,
                                                  @Field("request") String request);

    @POST("restaurant/business_side/get-business-detail.php")
    @FormUrlEncoded
    Call<ResponseBusinessDetails> getBusinessDetails(@Header("Authorization") String authHeader,
                                                     @Field("accesskey") String accesskey,
                                                     @Field("business_id") String business_id);

    @POST("restaurant/business_side/get-customer.php")
    @FormUrlEncoded
    Call<ResponseCustomerList> getCustomerList(@Header("Authorization") String authHeader,
                                               @Field("accesskey") String accesskey,
                                               @Field("business_id") String business_id);

    @POST("restaurant/business_side/menu_list_new.php")
    @FormUrlEncoded
    Call<ResponseNewMenuList> getNewMenuList(@Header("Authorization") String authHeader,
                                             @Field("accesskey") String accesskey,
                                             @Field("business_id") String business_id,
                                             @Field("dish_type") String dish_type);

    @POST("restaurant/business_side/get-hour-title.php")
    @FormUrlEncoded
    Call<ResponseHoursTitleList> getHoursTitleList(@Header("Authorization") String authHeader,
                                                   @Field("accesskey") String accesskey,
                                                   @Field("cat_id") String cat_id);

    @POST("restaurant/business_side/save_business_hours.php")
    @FormUrlEncoded
    Call<ResponseSendTiming> sendTiming(@Header("Authorization") String authHeader,
                                        @Field("accesskey") String accesskey,
                                        @Field("business_id") String business_id,
                                        @Field("title_id") String title_id,
                                        @Field("working_day") String working_day,
                                        @Field("restaurent_time") String restaurent_time);

    @POST("restaurant/business_side/get-timing.php")
    @FormUrlEncoded
    Call<ResponseGetTimingList> getTimingList(@Header("Authorization") String authHeader,
                                              @Field("accesskey") String accesskey,
                                              @Field("business_id") String business_id);

    @POST("restaurant/business_side/dashboard.php")
    @FormUrlEncoded
    Call<ResponseDashboard> getDashboardData(@Header("Authorization") String authHeader,
                                             @Field("accesskey") String accesskey,
                                             @Field("business_id") String business_id);

    @POST("restaurant/business_side/delete_data.php")
    @FormUrlEncoded
    Call<ResponseDelete> sendDelete(@Header("Authorization") String authHeader,
                                    @Field("accesskey") String accesskey,
                                    @Field("business_id") String business_id,
                                    @Field("type") String type,
                                    @Field("id") String id);

    @POST("restaurant/business_side/manage_status.php")
    @FormUrlEncoded
    Call<ResponseStatus> sendStatus(@Header("Authorization") String authHeader,
                                    @Field("accesskey") String accesskey,
                                    @Field("business_id") String business_id,
                                    @Field("type") String type,
                                    @Field("id") String id,
                                    @Field("status") String status);


//===========================DOCTOR APP API========================================


    @POST("doctor/business_side/about_you.php")
    @FormUrlEncoded
    Call<ResponseDoctorAboutYou> sendAboutDetails(@Header("Authorization") String authHeader,
                                                  @Field("accesskey") String accesskey,
                                                  @Field("user_id") String user_id,
                                                  @Field("name") String name,
                                                  @Field("mobile") String mobile,
                                                  @Field("d_o_b") String d_o_b,
                                                  @Field("nationality") String nationality);


    @POST("doctor/business_side/save_profile.php")
    @FormUrlEncoded
    Call<ResponseDoctorSaveProfile> sendSaveProfile(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("user_id") String user_id,
                                                    @Field("address_line_1") String address_line_1,
                                                    @Field("address_line_2") String address_line_2,
                                                    @Field("city_id") String city_id,
                                                    @Field("email_id") String email_id,
                                                    @Field("business_name") String business_name,
                                                    @Field("phone_no") String phone_no,
                                                    @Field("website") String website,
                                                    @Field("description") String description,
                                                    @Field("latitude") String latitude,
                                                    @Field("longitude") String longitude,
                                                    @Field("password") String password);


    @POST("doctor/business_side/dashboard.php")
    @FormUrlEncoded
    Call<ResponseDoctorDeshboard> getDeshboardDetails(@Header("Authorization") String authHeader,
                                                      @Field("accesskey") String accesskey,
                                                      @Field("business_id") String business_id);


    @POST("doctor/business_side/get-order.php")
    @FormUrlEncoded
    Call<ResponseDoctorAppointment> getOrder(@Header("Authorization") String authHeader,
                                             @Field("accesskey") String accesskey,
                                             @Field("business_id") String business_id);


    @POST("doctor/business_side/service_list.php")
    @FormUrlEncoded
    Call<ResponseDoctorServices> getServices(@Header("Authorization") String authHeader,
                                             @Field("accesskey") String accesskey,
                                             @Field("business_id") String business_id);

    @Multipart
    @POST("doctor/business_side/add_services.php")
    Call<ResponseDoctorAddService> sendService(@Header("Authorization") String authorization,
                                               @Part("accesskey") RequestBody accesskey,
                                               @Part("business_id") RequestBody business_id,
                                               @Part("doctor_name") RequestBody doctor_name,
                                               @Part("service_name") RequestBody service_name,
                                               @Part("amount") RequestBody amount,
                                               @Part("description") RequestBody description,
                                               @Part("offer") RequestBody offer,
                                               @Part MultipartBody.Part image);


    @POST("doctor/business_side/offer-list.php")
    @FormUrlEncoded
    Call<ResponseDoctorCoupons> getCouponsList(@Header("Authorization") String authHeader,
                                               @Field("accesskey") String accesskey,
                                               @Field("business_id") String business_id);


    @POST("doctor/business_side/add-offer.php")
    @FormUrlEncoded
    Call<ResponseDoctorAddCoupons> sendCouponsDetails(@Header("Authorization") String authHeader,
                                                      @Field("accesskey") String accesskey,
                                                      @Field("business_id") String business_id,
                                                      @Field("coupn_code") String coupn_code,
                                                      @Field("min_amount") String min_amount,
                                                      @Field("max_amount") String max_amount,
                                                      @Field("terms_condition") String terms_condition,
                                                      @Field("percentage") String percentage);


    @Multipart
    @POST("doctor/business_side/add-image.php")
    Call<ResponseDoctorAddImage> sendImageFromDoctor(@Header("Authorization") String authorization,
                                                     @Part("accesskey") RequestBody accesskey,
                                                     @Part("business_id") RequestBody s1,
                                                     @Part MultipartBody.Part[] s12);


    @Multipart
    @POST("doctor/business_side/add-video.php")
    Call<ResponseDoctorAddVedio> sendVedioFromDoctor(@Header("Authorization") String authorization,
                                                     @Part("accesskey") RequestBody accesskey,
                                                     @Part("business_id") RequestBody s1,
                                                     @Part MultipartBody.Part[] s12);


    @POST("doctor/business_side/media-list.php")
    @FormUrlEncoded
    Call<ResponseDoctorAddImageAndVedio> getImageAndVedioListDoctor(@Header("Authorization") String authHeader,
                                                                    @Field("accesskey") String accesskey,
                                                                    @Field("business_id") String business_id);


    @POST("doctor/business_side/get-business-detail.php")
    @FormUrlEncoded
    Call<ResponseDoctorBusinessDetails> getDoctorBusinessDetails(@Header("Authorization") String authHeader,
                                                                 @Field("accesskey") String accesskey,
                                                                 @Field("business_id") String business_id);


    @POST("doctor/business_side/delete_data.php")
    @FormUrlEncoded
    Call<ResponseDoctorDelete> sendDeleteDataDoctor(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("business_id") String business_id,
                                                    @Field("type") String type,
                                                    @Field("id") String id);

    @POST("doctor/business_side/manage_status.php")
    @FormUrlEncoded
    Call<ResponseStatus> sendDoctorStatus(@Header("Authorization") String authHeader,
                                          @Field("accesskey") String accesskey,
                                          @Field("business_id") String business_id,
                                          @Field("type") String type,
                                          @Field("id") String id,
                                          @Field("status") String status);

    @POST("doctor/business_side/get-customer.php")
    @FormUrlEncoded
    Call<ResponseCustomerList> getDoctorCustomerList(@Header("Authorization") String authHeader,
                                               @Field("accesskey") String accesskey,
                                               @Field("business_id") String business_id);


    @POST("doctor/business_side/save_time_slot.php")
    @FormUrlEncoded
    Call<ResponseDoctorSendTiming> sendDoctorTiming(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("hospital_services_id") String hospital_services_id,
                                                    @Field("working_day") String working_day,
                                                    @Field("time") String time);


    @POST("doctor/business_side/get-time-slot.php")
    @FormUrlEncoded
    Call<ResponseDoctorGetTiming> getDoctorTiming(@Header("Authorization") String authHeader,
                                                   @Field("accesskey") String accesskey,
                                                   @Field("hospital_services_id") String hospital_services_id);


    //===========================LAWYER APP API========================================


    @POST("lawyer/business_side/about_you.php")
    @FormUrlEncoded
    Call<ResponseLawyerAboutYou> sendLawyerAboutDetails(@Header("Authorization") String authHeader,
                                                        @Field("accesskey") String accesskey,
                                                        @Field("user_id") String user_id,
                                                        @Field("name") String name,
                                                        @Field("mobile") String mobile,
                                                        @Field("d_o_b") String d_o_b,
                                                        @Field("nationality") String nationality);


    @POST("lawyer/business_side/save_profile.php")
    @FormUrlEncoded
    Call<ResponseLawyerSaveProfile> sendLawyerSaveProfile(@Header("Authorization") String authHeader,
                                                          @Field("accesskey") String accesskey,
                                                          @Field("user_id") String user_id,
                                                          @Field("address_line_1") String address_line_1,
                                                          @Field("address_line_2") String address_line_2,
                                                          @Field("city_id") String city_id,
                                                          @Field("email_id") String email_id,
                                                          @Field("business_name") String business_name,
                                                          @Field("phone_no") String phone_no,
                                                          @Field("website") String website,
                                                          @Field("description") String description,
                                                          @Field("latitude") String latitude,
                                                          @Field("longitude") String longitude,
                                                          @Field("password") String password);


    @POST("lawyer/business_side/dashboard.php")
    @FormUrlEncoded
    Call<ResponseLawyerDeshboard> getLawyerDashboardDetails(@Header("Authorization") String authHeader,
                                                            @Field("accesskey") String accesskey,
                                                            @Field("business_id") String business_id);


    @POST("lawyer/business_side/get-order.php")
    @FormUrlEncoded
    Call<ResponseLawyerAppointment> geLawyerOrder(@Header("Authorization") String authHeader,
                                                   @Field("accesskey") String accesskey,
                                                   @Field("business_id") String business_id);


    @POST("lawyer/business_side/service_list.php")
    @FormUrlEncoded
    Call<ResponseLawyerServices> getLawyerServices(@Header("Authorization") String authHeader,
                                                   @Field("accesskey") String accesskey,
                                                   @Field("business_id") String business_id);

    @Multipart
    @POST("lawyer/business_side/add_services.php")
    Call<ResponseLawyerAddService> sendLawyerService(@Header("Authorization") String authorization,
                                                     @Part("accesskey") RequestBody accesskey,
                                                     @Part("business_id") RequestBody business_id,
                                                     @Part("doctor_name") RequestBody doctor_name,
                                                     @Part("service_name") RequestBody service_name,
                                                     @Part("amount") RequestBody amount,
                                                     @Part("description") RequestBody description,
                                                     @Part("offer") RequestBody offer,
                                                     @Part MultipartBody.Part image);


    @Multipart
    @POST("lawyer/business_side/add-image.php")
    Call<ResponseLawyerAddImage> sendLawyerImageFrom(@Header("Authorization") String authorization,
                                                     @Part("accesskey") RequestBody accesskey,
                                                     @Part("business_id") RequestBody s1,
                                                     @Part MultipartBody.Part[] s12);


    @Multipart
    @POST("lawyer/business_side/add-video.php")
    Call<ResponseLawyerAddVedio> sendLawyerVideoFrom(@Header("Authorization") String authorization,
                                                     @Part("accesskey") RequestBody accesskey,
                                                     @Part("business_id") RequestBody s1,
                                                     @Part MultipartBody.Part[] s12);


    @POST("lawyer/business_side/media-list.php")
    @FormUrlEncoded
    Call<ResponseLawyerAddImageAndVedio> getLawyerImageAndVideoList(@Header("Authorization") String authHeader,
                                                                    @Field("accesskey") String accesskey,
                                                                    @Field("business_id") String business_id);


    @POST("lawyer/business_side/get-business-detail.php")
    @FormUrlEncoded
    Call<ResponseLawyerBusinessDetails> getLawyerBusinessDetails(@Header("Authorization") String authHeader,
                                                                 @Field("accesskey") String accesskey,
                                                                 @Field("business_id") String business_id);


    @POST("lawyer/business_side/delete_data.php")
    @FormUrlEncoded
    Call<ResponseLawyerDelete> sendLawyerDeleteDataDoctor(@Header("Authorization") String authHeader,
                                                          @Field("accesskey") String accesskey,
                                                          @Field("business_id") String business_id,
                                                          @Field("type") String type,
                                                          @Field("id") String id);

    @POST("lawyer/business_side/manage_status.php")
    @FormUrlEncoded
    Call<ResponseStatus> sendLawyerStatus(@Header("Authorization") String authHeader,
                                          @Field("accesskey") String accesskey,
                                          @Field("business_id") String business_id,
                                          @Field("type") String type,
                                          @Field("id") String id,
                                          @Field("status") String status);

    @POST("lawyer/business_side/get-customer.php")
    @FormUrlEncoded
    Call<ResponseCustomerList> getLawyerCustomerList(@Header("Authorization") String authHeader,
                                                     @Field("accesskey") String accesskey,
                                                     @Field("business_id") String business_id);

    @POST("lawyer/business_side/save_time_slot.php")
    @FormUrlEncoded
    Call<ResponseLawyerSendTiming> sendLawyerTiming(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("hospital_services_id") String hospital_services_id,
                                                    @Field("working_day") String working_day,
                                                    @Field("time") String time);


    //===========================Insurance APP API========================================


    @POST("insurance/business_side/about_you.php")
    @FormUrlEncoded
    Call<ResponseInsuranceAboutYou> sendInsuranceAboutDetails(@Header("Authorization") String authHeader,
                                                              @Field("accesskey") String accesskey,
                                                              @Field("user_id") String user_id,
                                                              @Field("name") String name,
                                                              @Field("mobile") String mobile,
                                                              @Field("d_o_b") String d_o_b,
                                                              @Field("nationality") String nationality,
                                                              @Field("intro") String intro);


    @POST("insurance/business_side/save_profile.php")
    @FormUrlEncoded
    Call<ResponseInsuranceSaveProfile> sendInsuranceSaveProfile(@Header("Authorization") String authHeader,
                                                                @Field("accesskey") String accesskey,
                                                                @Field("user_id") String user_id,
                                                                @Field("address_line_1") String address_line_1,
                                                                @Field("address_line_2") String address_line_2,
                                                                @Field("city_id") String city_id,
                                                                @Field("email_id") String email_id,
                                                                @Field("business_name") String business_name,
                                                                @Field("phone_no") String phone_no,
                                                                @Field("website") String website,
                                                                @Field("description") String description,
                                                                @Field("latitude") String latitude,
                                                                @Field("longitude") String longitude,
                                                                @Field("password") String password);


    @POST("insurance/business_side/dashboard.php")
    @FormUrlEncoded
    Call<ResponseInsuranceDeshboard> getInsuranceDashboardDetails(@Header("Authorization") String authHeader,
                                                                  @Field("accesskey") String accesskey,
                                                                  @Field("business_id") String business_id);

    @POST("insurance/business_side/service_list.php")
    @FormUrlEncoded
    Call<ResponseInsuranceServices> getInsuranceServices(@Header("Authorization") String authHeader,
                                                         @Field("accesskey") String accesskey,
                                                         @Field("business_id") String business_id);

    @POST("insurance/business_side/get-order.php")
    @FormUrlEncoded
    Call<ResponseInsuranceAppointment> getInsuranceOrder(@Header("Authorization") String authHeader,
                                                         @Field("accesskey") String accesskey,
                                                         @Field("business_id") String business_id);


    @POST("insurance/business_side/add_services.php")
    @FormUrlEncoded
    Call<ResponseInsuranceAddService> sendInsuranceService(@Header("Authorization") String authorization,
                                                           @Field("accesskey") String accesskey,
                                                           @Field("business_id") String business_id,
                                                           @Field("service_id") String service_id,
                                                           @Field("company_id") String company_id);

    @POST("insurance/business_side/company_service_list.php")
    @FormUrlEncoded
    Call<ResponseInsuranceServiceAndCompanyList> getInsuranceServiceList(@Header("Authorization") String authorization,
                                                                         @Field("accesskey") String accesskey);

    @POST("insurance/business_side/get-customer.php")
    @FormUrlEncoded
    Call<ResponseCustomerList> getInsuranceCustomerList(@Header("Authorization") String authHeader,
                                                        @Field("accesskey") String accesskey,
                                                        @Field("business_id") String business_id);

    @POST("insurance/business_side/get-business-detail.php")
    @FormUrlEncoded
    Call<ResponseInsuranceBusinessDetails> getInsuranceBusinessDetails(@Header("Authorization") String authHeader,
                                                                       @Field("accesskey") String accesskey,
                                                                       @Field("business_id") String business_id);

    @POST("insurance/business_side/delete_data.php")
    @FormUrlEncoded
    Call<ResponseInsuranceDelete> sendInsuranceDeleteData(@Header("Authorization") String authHeader,
                                                          @Field("accesskey") String accesskey,
                                                          @Field("business_id") String business_id,
                                                          @Field("type") String type,
                                                          @Field("id") String id);

    @POST("insurance/business_side/manage_status.php")
    @FormUrlEncoded
    Call<ResponseStatus> sendInsuranceStatus(@Header("Authorization") String authHeader,
                                             @Field("accesskey") String accesskey,
                                             @Field("business_id") String business_id,
                                             @Field("type") String type,
                                             @Field("id") String id,
                                             @Field("status") String status);

    @POST("insurance/business_side/save_time_slot.php")
    @FormUrlEncoded
    Call<ResponseInsuranceSendTiming> sendInsuranceTiming(@Header("Authorization") String authHeader,
                                                          @Field("accesskey") String accesskey,
                                                          @Field("hospital_services_id") String hospital_services_id,
                                                          @Field("working_day") String working_day,
                                                          @Field("time") String time);


    @POST("insurance/business_side/get-time-slot.php")
    @FormUrlEncoded
    Call<ResponseInsuranceGetTiming> getInsuranceTiming(@Header("Authorization") String authHeader,
                                                        @Field("accesskey") String accesskey,
                                                        @Field("hospital_services_id") String hospital_services_id);



    /*@POST("fcm_token.php")
    @FormUrlEncoded
    Call<JsonObject> setToken(@Header("Authorization") String authHeader,
                              @Field("accesskey") String accesskey,
                              @Field("token") String token,
                              @Field("uid") String uid,
                              @Field("device_id") String device_id);

    @POST("login.php")
    @FormUrlEncoded
    Call<ResponseLogin> setLogin(@Header("Authorization") String authHeader,
                                 @Field("accesskey") String accesskey,
                                 @Field("username") String username,
                                 @Field("password") String password);

    @POST("send_otp.php")
    @FormUrlEncoded
    Call<ResponseOtpSend> senduserData(@Header("Authorization") String authHeader,
                                       @Field("accesskey") String accesskey,
                                       @Field("mobile") String mobile);

    @POST("update_password.php")
    @FormUrlEncoded
    Call<ResponseUpdatePassword> sendUpdatePassword(@Header("Authorization") String authHeader,
                                                    @Field("accesskey") String accesskey,
                                                    @Field("mobile") String mobile,
                                                    @Field("password") String password);

    @POST("visit_list.php")
    @FormUrlEncoded
    Call<ResponseVisitList> setVisitList(@Header("Authorization") String authHeader,
                                         @Field("accesskey") String accesskey,
                                         @Field("employee_id") String employee_id);*/


    //===========================BUSINESS APP API========================================

    /*@Multipart
    @POST("add_product.php")
    Call<JsonObject> sendProductData(@Part("name") RequestBody s1,
                                     @Part("qty") RequestBody s2,
                                     @Part("unit") RequestBody s3,
                                     @Part("mrp") RequestBody s4,
                                     @Part("selling_price") RequestBody s5,
                                     @Part("category_id") RequestBody s6,
                                     @Part("category") RequestBody s7,
                                     @Part("brand_id") RequestBody s8,
                                     @Part("brand") RequestBody s9,
                                     @Part("product_discription") RequestBody s10,
                                     @Part MultipartBody.Part s13LOGO);

    @Multipart
    @POST("add_category.php")
    Call<JsonObject> sendCategoryData(@Part("name") RequestBody s1,
                                      @Part MultipartBody.Part s13LOGO);

    @POST("app_status.php")
    Call<JsonObject> getstatus();

    @GET("product_list.php")
    Call<ResponseProductList> getProductList();

    @GET("category_list.php")
    Call<ResponseCategoryList> getCategoryList();

    @Multipart
    @POST("edit_product.php")
    Call<JsonObject> sendEditProductData(@Part("name") RequestBody s1,
                                         @Part("qty") RequestBody s2,
                                         @Part("unit") RequestBody s3,
                                         @Part("mrp") RequestBody s4,
                                         @Part("product_id") RequestBody s5,
                                         @Part("icon") RequestBody s6,
                                         @Part("selling_price") RequestBody s7,
                                         @Part("category_id") RequestBody s8,
                                         @Part("category") RequestBody s9,
                                         @Part("brand_id") RequestBody s10,
                                         @Part("brand") RequestBody s11,
                                         @Part("product_discription") RequestBody s12,
                                         @Part MultipartBody.Part s13LOGO);

    @Multipart
    @POST("edit_category.php")
    Call<JsonObject> sendEditCategoryData(@Part("name") RequestBody s1,
                                          @Part("product_id") RequestBody s5,
                                          @Part("icon") RequestBody s6,
                                          @Part MultipartBody.Part s13LOGO);

    @POST("discription.php")
    @FormUrlEncoded
    Call<JsonObject> addDiscriptionProducts(@Field("disc") String s1,
                                            @Field("product_id") String s2);

    @POST("delete.php")
    @FormUrlEncoded
    Call<JsonObject> deleteproducts(@Field("id") String s1,
                                    @Field("table") String s2);

    @GET("area_list.php")
    Call<ResponseBussArea> getBusinessAreaDetails();

    @POST("set_product_status.php")
    @FormUrlEncoded
    Call<JsonObject> setStatusChange(@Field("status") String status,
                                     @Field("product_id") String product_id);

    @POST("set_category_status.php")
    @FormUrlEncoded
    Call<JsonObject> setCategoryStatusChange(@Field("status") String status,
                                             @Field("product_id") String product_id);

    @POST("set_app_status.php")
    @FormUrlEncoded
    Call<JsonObject> setAppStatusChange(@Field("status") String status);

    @GET("order_list.php")
    Call<ResponseOrderList> getorderlist();

    @POST("order_item.php")
    @FormUrlEncoded
    Call<ResponseOrderItem> getOrderDetails(@Field("order_id") String order_id);

    @POST("set_order_status.php")
    @FormUrlEncoded
    Call<JsonObject> setOrderStatus(@Field("id") String id,
                                    @Field("status") String status);

    @POST("order_product.php")
    @FormUrlEncoded
    Call<ResponseOrderProductList> getOrderProduct(@Field("delivery_date") String delivery_date);

    @POST("admin_login.php")
    @FormUrlEncoded
    Call<JsonObject> setsignup(@Field("password") String password,
                               @Field("contact") String contact);

    @POST("fcm_token.php")
    @FormUrlEncoded
    Call<JsonObject> setToken(@Field("token") String token,
                              @Field("uid") String uid,
                              @Field("device_id") String device_id);*/


}
