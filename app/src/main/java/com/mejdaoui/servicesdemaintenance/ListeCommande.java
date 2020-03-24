package com.mejdaoui.servicesdemaintenance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class ListeCommande extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<ModelCommande> commandeList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_commande);

        recyclerView = findViewById(R.id.rv);

        commandeList = new ArrayList<>();
        commandeList.add(new ModelCommande(R.drawable.image,"ffff","fffff","fffff"));
        commandeList.add(new ModelCommande(R.drawable.image,"ffff","fffff","fffff"));
        commandeList.add(new ModelCommande(R.drawable.image,"ffff","fffff","fffff"));
        commandeList.add(new ModelCommande(R.drawable.image,"ffff","fffff","fffff"));
        commandeList.add(new ModelCommande(R.drawable.image,"ffff","fffff","fffff"));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView.LayoutManager rvLayoutManager = layoutManager;

        recyclerView.setLayoutManager(rvLayoutManager);

        CommandeAdapter adapter = new CommandeAdapter(this,commandeList);
        recyclerView.setAdapter(adapter);

    }
}
