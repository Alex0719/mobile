package com.example.alex.carapp.repository;

import android.content.Context;
import android.util.Log;

import com.example.alex.carapp.api.MasinaResource;
import com.example.alex.carapp.database.MasinaDao;
import com.example.alex.carapp.database.MasinaDatabase;
import com.example.alex.carapp.vo.Page;

import java.util.concurrent.Executor;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MasinaRepo {
    private static final String TAG = MasinaRepo.class.getCanonicalName();
    private MasinaResource masinaResource;
    private MasinaDao masinaDao;
    private Executor executor;

    private static MasinaRepo instance;

    private MasinaRepo(Context context) {
        MasinaDatabase masinaDatabase = MasinaDatabase.getMasinaDatabase(context);
        this.masinaDao = masinaDatabase.masinaDao();

        this.executor = Provider.getExecutor();

        OkHttpClient http = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request.Builder ongoing = chain.request().newBuilder();

                    ongoing.addHeader("Accept", "application/json");
//                    String token = tokenDatabase.getToken();
                    return chain.proceed(ongoing.build());
                }).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MasinaResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(http)
                .build();
        masinaResource = retrofit.create(MasinaResource.class);
    }

    public void synchronizeMasini() {
        executor.execute(() ->{
            Call<Page> call = masinaResource.getMAsini();
            Log.d(TAG, "load devices");
        });
    }
}
