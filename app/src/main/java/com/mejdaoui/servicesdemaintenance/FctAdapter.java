package com.mejdaoui.servicesdemaintenance;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FctAdapter extends RecyclerView.Adapter<FctAdapter.MyViewHolder> {

    private Context context ;
    private List<Fonctionnaire> Fonctionnaires = new ArrayList<>();

    public FctAdapter(Context context , List<Fonctionnaire> Fonctionnaires){

        this.Fonctionnaires  = Fonctionnaires;
        this.context = context;

    }

    @Override
    public int getItemCount() {

        return Fonctionnaires.size();
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item , parent, false);
        return new MyViewHolder(view);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(Fonctionnaires.size() != 0) {
            Fonctionnaire Fct = (Fonctionnaire) Fonctionnaires.get(position);
            holder.fullName.setText(Fct.getNom()+" "+Fct.getPrenom());
            holder.name.setText(Fct.getNom());
        }
        else
            holder.fullName.setText("Aucun candidat");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final Button call;
        private final TextView name;
        private final ImageView img;
        private final TextView fullName;

        private Fonctionnaire fct;


        public MyViewHolder(final View itemView) {
            super(itemView);

            call = itemView.findViewById(R.id.call);
            name = itemView.findViewById(R.id.name);
            img=  itemView.findViewById(R.id.img);
            fullName = itemView.findViewById(R.id.fullName);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(fct.getNom()+" "+fct.getPrenom())
                            .show();
                }
            });
        }

    }
}
