package com.mejdaoui.servicesdemaintenance;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FirebaseViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;
    public TextView srv;
    public TextView desc;
    public TextView clt;
    public TextView timeville;

    public FirebaseViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.clientimg);
        srv = itemView.findViewById(R.id.serviceName);
        desc = itemView.findViewById(R.id.serviceDesc);
        clt = itemView.findViewById(R.id.clientName);
        timeville = itemView.findViewById(R.id.time_ville);

    }
}
