package com.example.alex.carapp;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.carapp.viewmodel.UtilizatorViewModel;
import com.example.alex.carapp.vo.Utilizator;

public class LoginActivity extends AppCompatActivity {

    EditText textNume;
    EditText textParola;
    UtilizatorViewModel utilizatorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        textNume = (EditText) findViewById(R.id.editTextNume);
        textParola = (EditText) findViewById(R.id.editTextParola);
    }

    protected void OnLogin(View view) {
        String nume = textNume.getText().toString();
        String parola = textParola.getText().toString();

        utilizatorViewModel = ViewModelProviders.of(this).get(UtilizatorViewModel.class);
        utilizatorViewModel.login(new Utilizator(nume, parola), LoginActivity.this);
    }
}
