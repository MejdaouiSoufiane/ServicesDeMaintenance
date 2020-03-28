package com.mejdaoui.servicesdemaintenance;

import android.content.Context;
import android.graphics.ColorSpace;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CommandeAdapter  extends RecyclerView.Adapter<CommandeAdapter.ViewHolder> {
    @NonNull

    private Context mContext;
    private ArrayList<ModelCommande> mList ;
    CommandeAdapter(Context context, ArrayList<ModelCommande> list){
        mContext = context;
        mList = list;
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
        ModelCommande commandeItem = mList.get(position);

        ImageView image = holder.item_image;
        TextView name,description,etat;
        name = holder.item_name;
        description = holder.item_description;
        etat = holder.item_etat;

        image.setImageResource(commandeItem.getImage());
        name.setText(commandeItem.getName());
        description.setText(commandeItem.getDescreption());
        etat.setText(commandeItem.getEtat());

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        ImageView item_image;
        TextView item_name,item_description,item_etat;
        public ViewHolder(View itemView){
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_titre);
            item_description = itemView.findViewById(R.id.item_description);
            item_etat = itemView.findViewById(R.id.item_etat);
        }
    }
}