package com.mejdaoui.servicesdemaintenance;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.R;

import java.util.ArrayList;

public class ListDemande extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageButton imageButton;
    ArrayList<Demande> demandeList;
    DatabaseReference reference;
    DemandeAdapter adapter;
    FirebaseUser user;
    String uid_user;
    private Toolbar tool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_demande);
        /*Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListDemande.this, AddDemande.class);
                startActivity(intent);
                finish();
            }
        });

        tool = (Toolbar) this.findViewById(R.id.tool);

        recyclerView = (RecyclerView) findViewById(R.id.rv);
        //imageButton = (ImageButton)findViewById(R.id.img_button_add);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // UID specific to the provider
                uid_user = profile.getUid();
            }
        }
        reference = FirebaseDatabase.getInstance().getReference().child("Demandes");

        // System.out.println("im here "+reference);
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                demandeList = new ArrayList<Demande>();
                for (DataSnapshot dds: dataSnapshot.getChildren())
                {
                    Demande d = dds.getValue(Demande.class);
                    if(d.getIdClient().equals(uid_user))
                        demandeList.add(d);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(ListDemande.this);
                    RecyclerView.LayoutManager rvLayoutManager = layoutManager;

                    recyclerView.setLayoutManager(rvLayoutManager);

                    adapter = new DemandeAdapter(ListDemande.this,demandeList,ListDemande.this);
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        enableSwipeToDeleteAndUndo();

        this.setTitle("Accueil");
        setSupportActionBar(tool);

    }


    public void btn_add(View view) {
        Intent intent = new Intent(ListDemande.this, AddDemande.class);
        startActivity(intent);
        finish();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final Demande item = adapter.getData().get(position);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListDemande.this);

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

    public boolean onCreateOptionsMenu(Menu menu)
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
    }

}
