package com.mejdaoui.servicesdemaintenance.Adapter;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.Model.Fonctionnaire;
import com.mejdaoui.servicesdemaintenance.R;

import java.util.ArrayList;

public class FctAdapter extends RecyclerView.ViewHolder {

    public final Button accept;
    public final ImageView img;
    public final TextView fullName;
    public final TextView profile;
    private ArrayList<Fonctionnaire> mList ;

    private Fonctionnaire fct;

    public FctAdapter(@NonNull View itemView) {
        super(itemView);

        accept = itemView.findViewById(R.id.accept);
        img = itemView.findViewById(R.id.img);
        fullName = itemView.findViewById(R.id.fullName);
        profile = itemView.findViewById(R.id.profile);

    }
}


   