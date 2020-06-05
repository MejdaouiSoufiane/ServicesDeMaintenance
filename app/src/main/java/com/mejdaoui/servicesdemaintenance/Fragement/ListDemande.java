package com.mejdaoui.servicesdemaintenance.Fragement;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Activity.AddDemande;
import com.mejdaoui.servicesdemaintenance.Adapter.DemandeAdapter;
import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.R;
import com.mejdaoui.servicesdemaintenance.SwipeToDeleteCallback;


import java.util.ArrayList;

public class ListDemande extends Fragment {

    RecyclerView recyclerView;
    ImageButton imageButton;
    ArrayList<Demande> demandeList;
    DatabaseReference reference;
    DemandeAdapter adapter;
    FirebaseUser user;
    String uid_user, type;
    private Toolbar tool;

    public static ListDemande newInstance() {
        return (new ListDemande());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_list_demande, container, false);

        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getBaseContext(), AddDemande.class);
                startActivity(intent);

            }
        });

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            type = bundle.getString("TYPE");
            //Toast.makeText(getActivity().getBaseContext().getApplicationContext(), type , Toast.LENGTH_SHORT).show();

        }

        tool = (Toolbar) view.findViewById(R.id.tool);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        //imageButton = (ImageButton)findViewById(R.id.img_button_add);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            //for (UserInfo profile : user.getProviderData()) {
                // UID specific to the provider
                uid_user = user.getUid();
           // Toast.makeText(this,uid_user,Toast.LENGTH_LONG).show();
           // }
        }
        reference = FirebaseDatabase.getInstance().getReference().child("Demandes");

        // System.out.println("im here "+reference);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                demandeList = new ArrayList<Demande>();
                try {
                    for (DataSnapshot dds: dataSnapshot.getChildren())
                    {
                        Demande d = dds.getValue(Demande.class);
                        if(d.getIdClient().equals(uid_user)){
                        if(type.equals("all"))demandeList.add(d);
                        else if (type.equals(d.getEtat()))demandeList.add(d);

                        
                       }

                        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
                        RecyclerView.LayoutManager rvLayoutManager = layoutManager;

                        recyclerView.setLayoutManager(rvLayoutManager);

                        adapter = new DemandeAdapter(getActivity().getBaseContext(),demandeList,getActivity());
                        recyclerView.setAdapter(adapter);
                    }
                }catch(DatabaseException e){
                    //Log the exception and the key
                    dataSnapshot.getKey();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        enableSwipeToDeleteAndUndo();

        //this.setTitle("Accueil");
       // setSupportActionBar(tool);
        return view;
    }


    public void btn_add(View view) {
        Intent intent = new Intent(getActivity().getBaseContext(), AddDemande.class);
        startActivity(intent);
        //finish();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity().getBaseContext()) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Demande item = adapter.getData().get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

                if(item.getEtat().equals("En Attente")){
                    alertDialog.setTitle("Alerte")
                            .setMessage("voulez-vous la supprimer ?")
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

   /* public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {

            case R.id.item1:
                Intent intent = new Intent(ListDemande.this, ClientProfile.class);
                startActivity(intent);
                break;
            case R.id.item2:
                FirebaseAuth.getInstance().signOut();
                finish();
                Intent intent1 = new Intent(ListDemande.this, Login.class);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }*/

}
