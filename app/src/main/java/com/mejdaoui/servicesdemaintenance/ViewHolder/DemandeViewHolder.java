package com.mejdaoui.servicesdemaintenance.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mejdaoui.servicesdemaintenance.R;

public class DemandeViewHolder extends RecyclerView.ViewHolder {
    public TextView srv;
    public TextView desc;
    public TextView clt;
    public TextView timeville;
    public ImageView imageView;

    public DemandeViewHolder(@NonNull View itemView) {
        super(itemView);
        srv = itemView.findViewById(R.id.serviceName);
        desc = itemView.findViewById(R.id.serviceDesc);
        clt = itemView.findViewById(R.id.clientName);
        timeville = itemView.findViewById(R.id.time_ville);
        imageView = itemView.findViewById(R.id.clientimg);


    }


}
