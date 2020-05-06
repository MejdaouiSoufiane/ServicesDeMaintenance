package com.mejdaoui.servicesdemaintenance.ViewHolder;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.mejdaoui.servicesdemaintenance.R;

import static androidx.core.content.ContextCompat.startActivity;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView srv;
    public TextView desc;
    public TextView clt;
    public TextView timeville;
    public CardView cardView;
    public ImageView newDemande;


    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        cardView = itemView.findViewById(R.id.parentLayout);
        imageView = itemView.findViewById(R.id.clientimg);
        srv = itemView.findViewById(R.id.serviceName);
        desc = itemView.findViewById(R.id.serviceDesc);
        clt = itemView.findViewById(R.id.clientName);
        timeville = itemView.findViewById(R.id.time_ville);
        newDemande = itemView.findViewById(R.id.newDemande);


    }
}
