package com.example.alex.carapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.alex.carapp.database.TokenDatabase;
import com.example.alex.carapp.ui.MasinaListAdapter;
import com.example.alex.carapp.ui.MasiniListFragment;
import com.example.alex.carapp.viewmodel.MasiniViewModel;
import com.example.alex.carapp.vo.Masina;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText textId;
    EditText textMarca;
    EditText textCombustibil;
    MasiniViewModel masiniViewModel;
    RecyclerView masiniList;
    TokenDatabase tokenDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MasiniListFragment.newInstance())
                    .commitNow();
        }

        textId = (EditText) findViewById(R.id.editTextId);
        textMarca = (EditText) findViewById(R.id.editTextMarca);
        textCombustibil = (EditText) findViewById(R.id.editTextCombustibil);

    }

    public void OnUpdate(View view) {
        System.out.println("DATE$$$$"+textId.getText().toString()+" "+textMarca.getText().toString());
        Integer id = Integer.parseInt(textId.getText().toString());
        String marca = textMarca.getText().toString();
        String combustibil = textCombustibil.getText().toString();


        masiniList = findViewById(R.id.masina_list);
        masiniList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        masiniViewModel = ViewModelProviders.of(this).get(MasiniViewModel.class);
        masiniViewModel.updateMasini(new Masina(id, marca, combustibil), MainActivity.this).observe(
                this, new Observer<List<Masina>>() {
                    @Override
                    public void onChanged(@Nullable List<Masina> masinas) {
                        MasinaListAdapter masiniAdapter = new MasinaListAdapter(MainActivity.this, masinas);
                        masiniList.setAdapter(masiniAdapter);
                    }
                }
        );

    }

    public void onLogout(View view){
        tokenDb = TokenDatabase.getInstance(MainActivity.this);
        System.out.println("will logout "+tokenDb.getToken());
        tokenDb.deleteToken();
        System.out.println("after logout "+tokenDb.getToken());
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivity(intent);
    }
}
