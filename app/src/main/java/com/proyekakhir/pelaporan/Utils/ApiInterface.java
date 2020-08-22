package com.proyekakhir.pelaporan.Utils;

import com.proyekakhir.pelaporan.response.BaseResponse;
import com.proyekakhir.pelaporan.response.ContactResponse;
import com.proyekakhir.pelaporan.response.LocationResponse;
import com.proyekakhir.pelaporan.response.LogInResponse;
import com.proyekakhir.pelaporan.response.ReportResponse;
import com.proyekakhir.pelaporan.response.ScheduleResponse;
import com.proyekakhir.pelaporan.response.UpdateImageResponse;
import com.proyekakhir.pelaporan.response.UserResponse;
import com.proyekakhir.pelaporan.response.ValidateResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("auth.php")
    Call<LogInResponse> postSignin(@Field("email") String email,
                                   @Field("password") String password);

    @FormUrlEncoded
    @POST("isEmailExist.php")
    Call<ValidateResponse> postIsEmailExist(@Field("email") String email);

    @FormUrlEncoded
    @POST("addUser.php")
    Call<BaseResponse> postRegister(
            @Field("email") String email,
            @Field("role") int role,
            @Field("name") String name,
            @Field("address") String address,
            @Field("phone") String phone,
            @Field("img") String img,
            @Field("password") String password);

    @FormUrlEncoded
    @POST("updateUser.php")
    Call<BaseResponse> postEditUser(
            @Field("id_user") String id_user,
            @Field("name") String name,
            @Field("address") String address,
            @Field("phone") String phone
    );
    @FormUrlEncoded
    @POST("updateImgUser.php")
    Call<UpdateImageResponse> postUpdateImgUser(
            @Field("id_user") String id_user,
            @Field("img") String img
    );
    @FormUrlEncoded
    @POST("changePw.php")
    Call<BaseResponse> postChangePw(
            @Field("id_user") String id_user,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("checkOldPw.php")
    Call<ValidateResponse> postCheckPw(
            @Field("id_user") String id_user,
            @Field("password") String password
    );

    @GET("allContacts.php")
    Call<ContactResponse>getAllContacts();

    @GET("allUsers.php")
    Call<UserResponse>getAllUsers();

    @GET("allOfficers.php")
    Call<UserResponse>getAllOfficers();

    @GET("allReports.php")
    Call<ReportResponse>getAllReports();

    @GET("allSchedules.php")
    Call<ScheduleResponse>getAllSchedules();

    @FormUrlEncoded
    @POST("updateSchedule.php")
    Call<BaseResponse> postUpdateSchedule(
            @Field("id_schedule") String id_schedule,
            @Field("id_user") String id_user
    );

    @FormUrlEncoded
    @POST("addContact.php")
    Call<BaseResponse> postAddContact(
            @Field("phone") String phone,
            @Field("division") String division,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("updateContact.php")
    Call<BaseResponse> postUpdateContact(
            @Field("id_contact") String id_contact,
            @Field("phone") String phone,
            @Field("division") String division,
            @Field("address") String address
    );

    @FormUrlEncoded
    @POST("addReport.php")
    Call<BaseResponse> postAddReport(
            @Field("id_user") String id_user,
            @Field("report") String report,
            @Field("date") String date,
            @Field("img") String img
    );

    @FormUrlEncoded
    @POST("addLocation.php")
    Call<BaseResponse> postAddLocation(
            @Field("name") String name,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @FormUrlEncoded
    @POST("updateLocation.php")
    Call<BaseResponse> postUpdateLocation(
            @Field("id_location") String id_location,
            @Field("name") String name,
            @Field("address") String address,
            @Field("latitude") String latitude,
            @Field("longitude") String longitude
    );

    @GET("allLocations.php")
    Call<LocationResponse>getAllLocations();
}
