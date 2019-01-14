package com.example.alex.carapp.api;

import com.example.alex.carapp.vo.LoginStatus;
import com.example.alex.carapp.vo.Masina;
import com.example.alex.carapp.vo.Page;
import com.example.alex.carapp.vo.Utilizator;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;


public interface MasinaResource {
//    String BASE_URL = "http://192.168.100.34:3000/api/";
    //String BASE_URL = "http://172.30.114.149:3000/api/";
    String BASE_URL = "http://172.20.10.5:3000/api/";

    @GET("masini/public")
    Call<Page> getMAsini();

    @POST("login")
    Call<LoginStatus> login(@Body Utilizator utilizator);

    @PUT("masini")
    Call<Page> updateMasina(@Body Masina masina);
}
