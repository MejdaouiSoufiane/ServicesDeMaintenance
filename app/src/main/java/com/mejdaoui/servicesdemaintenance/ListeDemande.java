package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListeDemande extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageButton imageButton;
    ArrayList<Demande> demandeList;
    DatabaseReference reference;
    DemandeAdapter adapter;
    FirebaseUser firebaseUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_demande);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        imageButton = (ImageButton)findViewById(R.id.img_button_add);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("Demandes");
       // System.out.println("im here "+reference);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                demandeList = new ArrayList<Demande>();
                for (DataSnapshot dds: dataSnapshot.getChildren())
                {
                    Demande d = dds.getValue(Demande.class);
                    if(d.getIdClient().equals(firebaseUser.getProviderId()))
                    demandeList.add(d);

                }
                LinearLayoutManager layoutManager = new LinearLayoutManager(ListeDemande.this);
                RecyclerView.LayoutManager rvLayoutManager = layoutManager;

                recyclerView.setLayoutManager(rvLayoutManager);

                adapter = new DemandeAdapter(ListeDemande.this,demandeList,ListeDemande.this);
                recyclerView.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        enableSwipeToDeleteAndUndo();




    }

    public void btn_add(View view) {
        Intent intent = new Intent(ListeDemande.this, AddDemande.class);
        startActivity(intent);
        finish();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Demande item = adapter.getData().get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListeDemande.this);

                if(item.getEtat().equals("En Attente")){
                    alertDialog.setTitle("Alerte")
                            .setMessage("voulez-vous le supprimer ?")
                            .setIcon(R.drawable.ic_delete)
                            .setPositiveButton("Supprimer", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    adapter.removeItem(position);
                                    DatabaseReference demandeDeleted = FirebaseDatabase.getInstance().getReference("Demandes").child(item.getIdDemande());
                                    demandeDeleted.removeValue();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());
                                }
                            })
                            .create();
                    alertDialog.show();
                }else{
                    alertDialog.setTitle("Alerte")
                            .setMessage("Impossible de supprimer cette demande")
                            .setIcon(R.drawable.ic_delete)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    adapter.notifyItemChanged(viewHolder.getAdapterPosition());

                                }
                            })
                            .create();
                    alertDialog.show();
                }
               /* Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.restoreItem(item, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

             //   snackbar.setActionTextColor(Color.black);
                snackbar.show();*/

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }

}
