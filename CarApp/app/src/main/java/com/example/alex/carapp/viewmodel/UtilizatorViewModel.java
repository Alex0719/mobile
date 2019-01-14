package com.example.alex.carapp.viewmodel;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModel;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.alex.carapp.LoginActivity;
import com.example.alex.carapp.MainActivity;
import com.example.alex.carapp.api.MasinaResource;
import com.example.alex.carapp.database.MasinaDatabase;
import com.example.alex.carapp.database.TokenDatabase;
import com.example.alex.carapp.database.UtilizatorDao;
import com.example.alex.carapp.repository.Provider;
import com.example.alex.carapp.vo.LoginStatus;
import com.example.alex.carapp.vo.Utilizator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UtilizatorViewModel extends ViewModel {
    private static final String TAG = UtilizatorViewModel.class.getCanonicalName();

    private static Retrofit retrofit = null;
    private TokenDatabase tokenDb;
    private UtilizatorDao utilizatorDao;
    private List<Utilizator> utilizatorList = new ArrayList<>();
    private Executor executor;

    public void login(final Utilizator utilizator, final LoginActivity loginActivity) {
        tokenDb = TokenDatabase.getInstance(loginActivity);
        retrofit = new Retrofit.Builder()
                .baseUrl(MasinaResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MasinaResource masinaResource = retrofit.create(MasinaResource.class);

        Call<LoginStatus> call = masinaResource.login(utilizator);
        Log.d(TAG, "login a.");
        call.enqueue(new Callback<LoginStatus>() {
            @Override
            public void onResponse(Call<LoginStatus> call, Response<LoginStatus> response) {

                Log.d(TAG, "raspuns ->>" + response.body().getToken());
                if (response.body().isFound()) {
                    tokenDb.insertToken(response.body().getToken());

                    MasinaDatabase masinaDatabase = MasinaDatabase.getMasinaDatabase(loginActivity);
                    utilizatorDao = masinaDatabase.utilizatorDao();
                    executor = Provider.getExecutor();
                    executor.execute(() -> {
                        utilizatorDao.insertUtilizator(utilizator);
                    });
                    Intent intent = new Intent(loginActivity, MainActivity.class);
                    intent.putExtra("nume", utilizator.getNume());
                    loginActivity.startActivity(intent);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                    builder.setMessage("Logarea a esuat!")
                            .setNegativeButton("Mai incearca", null)
                            .create().show();
                }

            }

            @Override
            public void onFailure(Call<LoginStatus> call, Throwable t) {
                Log.d(TAG, "oroare "+t.getMessage());
                MasinaDatabase masinaDatabase = MasinaDatabase.getMasinaDatabase(loginActivity);
                utilizatorDao = masinaDatabase.utilizatorDao();
                executor = Provider.getExecutor();
                executor.execute(() -> {
                    utilizatorList = utilizatorDao.findUtilizator(utilizator.getNume(),utilizator.getParola());

                    if(utilizatorList.size() > 0 ){
                        if(utilizatorList.get(0).getNume().equals(utilizator.getNume())){
                            Intent intent = new Intent(loginActivity, MainActivity.class);
                            intent.putExtra("nume", utilizator.getNume());
                            loginActivity.startActivity(intent);
                        }

                    }else{
                        Handler hand = new Handler(Looper.getMainLooper());
                        hand.post(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog.Builder builder = new AlertDialog.Builder(loginActivity);
                                builder.setMessage("Nu v-ati mai conectat cu acest user sau nu exista")
                                        .setNegativeButton("Mai incearca", null)
                                        .create()
                                        .show();
                            }
                        });
                    }
                });
            }
        });
    }

}
