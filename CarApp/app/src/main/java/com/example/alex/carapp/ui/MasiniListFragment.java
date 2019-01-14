package com.example.alex.carapp.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.alex.carapp.R;
import com.example.alex.carapp.viewmodel.MasiniViewModel;
import com.example.alex.carapp.vo.Masina;

import java.util.List;

public class MasiniListFragment extends Fragment {
    private MasiniViewModel mMasiniViewModel;
    private RecyclerView mMasiniList;

    public static MasiniListFragment newInstance() {return new MasiniListFragment(); }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState){
        return inflater.inflate(R.layout.masina_list_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMasiniList = view.findViewById(R.id.masina_list);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMasiniList.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMasiniViewModel = ViewModelProviders.of(this).get(MasiniViewModel.class);
        mMasiniViewModel.getMasini(getContext()).observe(this, new Observer<List<Masina>>() {
            @Override
            public void onChanged(@Nullable List<Masina> masini) {
                MasinaListAdapter masiniAdapter = new MasinaListAdapter(getActivity(), masini);
                mMasiniList.setAdapter(masiniAdapter);
            }
        });

    }

}
