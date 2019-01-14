package com.example.alex.carapp.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.alex.carapp.R;
import com.example.alex.carapp.viewmodel.MasiniViewModel;
import com.example.alex.carapp.vo.Masina;

import java.util.List;

public class MasinaListAdapter extends RecyclerView.Adapter<MasinaListAdapter.MasiniViewHolder>{
    private Context mContext;
    private List<Masina> mMasini;

    public MasinaListAdapter(Context context, List<Masina> masini) {
        this.mContext = context;
        this.mMasini = masini;
    }

    @NonNull
    @Override
    public MasiniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.masina_layout, parent, false);
        return new MasiniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MasiniViewHolder holder, int position) {
        Masina masina = mMasini.get(position);
        holder.textView.setText(masina.toString());
    }

    @Override
    public int getItemCount() { return mMasini.size(); }

    class MasiniViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        MasiniViewHolder(View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.text_view);
        }
    }
}
