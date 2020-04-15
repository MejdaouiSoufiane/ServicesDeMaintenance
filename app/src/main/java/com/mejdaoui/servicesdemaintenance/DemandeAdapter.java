package com.mejdaoui.servicesdemaintenance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DemandeAdapter  extends RecyclerView.Adapter<DemandeAdapter.ViewHolder> {
    @NonNull

    private Context mContext;
    private ArrayList<Demande> mList ;
    private Activity mactivity;

    DemandeAdapter(Context context, ArrayList<Demande> list,Activity activity){
        mContext = context;
        mList = list;
        mactivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.rv_commande_items,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Demande demandeItem = mList.get(position);

        ImageView image = holder.item_image;
        TextView name,description,etat;
        name = holder.item_name;
        description = holder.item_description;
        etat = holder.item_etat;
        String s = demandeItem.getAdr_picture();
        if(s.equals("")==false)
        Picasso.get().load(s).resize(50, 50).into(image);

        name.setText(demandeItem.getTitre());
        description.setText(demandeItem.getDescription());
        etat.setText(demandeItem.getEtat());

        holder.item_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent detail = new Intent(mactivity,DetailDemande.class);
                Intent detail = new Intent(mactivity,DetailDemandeClt.class);

                 detail.putExtra("id_demande",demandeItem.getIdDemande());

                mactivity.startActivity(detail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView item_image;
        TextView item_name,item_description,item_etat;
        RelativeLayout item_card;
        public ViewHolder(View itemView){
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_titre);
            item_description = itemView.findViewById(R.id.item_description);
            item_etat = itemView.findViewById(R.id.item_etat);
            item_card = itemView.findViewById(R.id.item_card);
        }
    }




    public void removeItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Demande item, int position) {
        mList.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Demande> getData() {
        return mList;
    }
}
