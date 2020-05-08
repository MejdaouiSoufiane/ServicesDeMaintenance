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

/*
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
            //holder.name.setText(Fct.getNom());
        }
        else
            holder.fullName.setText("Aucun candidat");
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final Button accept;
        private final TextView name;
        private final ImageView img;
        private final TextView fullName;

        private Fonctionnaire fct;


        public MyViewHolder(final View itemView) {
            super(itemView);

            accept = itemView.findViewById(R.id.accept);
            name = itemView.findViewById(R.id.name);
            img=  itemView.findViewById(R.id.img);
            fullName = itemView.findViewById(R.id.fullName);

            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Confirmer").setMessage("Etes vous sûr de choisir ce fonctionnaire ?")
                                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent intent = ((Activity)context).getIntent() ;
                                            String idDmd = intent.getStringExtra("idDmd");

                                        }
                                    })
                                    .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            builder.create();
                        }

            });

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle(fct.getNom()+" "+fct.getPrenom())
                            .show();
                }
            });*/


