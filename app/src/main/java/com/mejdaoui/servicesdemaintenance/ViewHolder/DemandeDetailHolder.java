package com.mejdaoui.servicesdemaintenance.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mejdaoui.servicesdemaintenance.R;

public class DemandeDetailHolder extends RecyclerView.ViewHolder {

    public ImageView imageView;

    public DemandeDetailHolder(@NonNull View itemView) {
        super(itemView);

        imageView = itemView.findViewById(R.id.dd_images);
    }


}
