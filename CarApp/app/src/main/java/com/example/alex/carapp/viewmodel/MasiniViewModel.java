package com.example.alex.carapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.alex.carapp.MainActivity;
import com.example.alex.carapp.api.MasinaResource;
import com.example.alex.carapp.database.MasinaDao;
import com.example.alex.carapp.database.MasinaDatabase;
import com.example.alex.carapp.repository.Provider;
import com.example.alex.carapp.vo.Masina;
import com.example.alex.carapp.vo.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MasiniViewModel extends ViewModel {
    private  static final String TAG = MasiniViewModel.class.getCanonicalName();
    private MutableLiveData<List<Masina>> masini;
    private List<Masina> masiniDaoList = new ArrayList<>();
    private MasinaDao masinaDao;
    private Executor executor;

    public LiveData<List<Masina>> getMasini(Context context) {
        //preapares data from offline storage(database)
        MasinaDatabase masinaDatabase = MasinaDatabase.getMasinaDatabase(context);
        this.masinaDao = masinaDatabase.masinaDao();
        this.executor = Provider.getExecutor();
        executor.execute(() -> {
            masiniDaoList.addAll(masinaDao.load());
        });

        if(masini == null){
            masini = new MutableLiveData<List<Masina>>();
            loadMasini();
        }
        return masini;
    }

    public void loadMasini() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MasinaResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MasinaResource api = retrofit.create(MasinaResource.class);
        Call<Page> call = api.getMAsini();
        Log.d(TAG, "loadMasini");
        call.enqueue(new Callback<Page>() {
            @Override
            public void onResponse(Call<Page> call, Response<Page> response) {
                Log.d(TAG, "loadMasini succeeded");
                System.out.println(response.body().getMasini());
                masini.setValue(response.body().getMasini());
                //updating database as well
                executor.execute(() ->{
                   masinaDao.save(response.body().getMasini());
                });
            }

            @Override
            public void onFailure(Call<Page> call, Throwable t) {
                Log.e(TAG, "loadMasini failed", t);
                masini.setValue(masiniDaoList);
            }
        });
    }

    public LiveData<List<Masina>> updateMasini(Masina masina, Context context) {
            if(masini == null){
                masini = new MutableLiveData<List<Masina>>();
            }
            MasinaDatabase masinaDatabase = MasinaDatabase.getMasinaDatabase(context);
            this.masinaDao = masinaDatabase.masinaDao();
            this.executor = Provider.getExecutor();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MasinaResource.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            MasinaResource api = retrofit.create(MasinaResource.class);

            executor.execute(() -> {
                masinaDao.update(masina);
                masiniDaoList = masinaDao.load();
                for(Masina m: masiniDaoList){
                    Call<Page> call = api.updateMasina(m);
                    call.enqueue(new Callback<Page>() {
                        @Override
                        public void onResponse(Call<Page> call, Response<Page> response) {

                        }

                        @Override
                        public void onFailure(Call<Page> call, Throwable t) {

                        }
                    });
                }
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Call<Page> call = api.updateMasina(masina);
                        Log.d(TAG, "updateMasina");
                        call.enqueue(new Callback<Page>() {
                            @Override
                            public void onResponse(Call<Page> call, Response<Page> response) {
                                Log.d(TAG, "updateMasina succeeded");
                                System.out.println(response.body().getMasini());

                                masini.setValue(response.body().getMasini());
                            }

                            @Override
                            public void onFailure(Call<Page> call, Throwable t) {
                                Log.e(TAG, "updateMasina failed");
                                executor.execute(() -> {
                                    masinaDao.update(masina);
                                    masiniDaoList = masinaDao.load();
                                    Handler handler = new Handler(Looper.getMainLooper());
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            masini.setValue(masiniDaoList);
                                        }
                                    });
                                });

                            }
                        });
                    }
                });
            });



        return masini;
    }
}
