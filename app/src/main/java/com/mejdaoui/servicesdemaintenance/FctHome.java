package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Activity.DemandeDetails;
import com.mejdaoui.servicesdemaintenance.Fragement.MainFragmentTab;
import com.mejdaoui.servicesdemaintenance.Model.Fonctionnaire;

public class FctHome extends AppCompatActivity {

    private DrawerLayout drawer;
    public TextView fonctcname;
    public TextView fonctemail;
    public ImageView fonctimage;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fct_home);


                /******* navigation drawer tricks ***/
        final Toolbar toolbar = findViewById(R.id.toolbar);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        toolbar.setTitle("Liste des demandes");
        MainFragmentTab fragment = new MainFragmentTab();
        ft.replace(R.id.fragement_container, fragment);
        ft.commit();

        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_navigation_drawer,R.string.close_navigation_drawer);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawer.closeDrawer(Gravity.LEFT);
                int id = item.getItemId();
                fonctimage = findViewById(R.id.fonctimage);
                fonctemail = findViewById(R.id.fonctemail);
                fonctcname = findViewById(R.id.fonctname);
                /** Fonctionnaire data **/

                final String  currentFocnt = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference fonct = FirebaseDatabase.getInstance().getReference("fonctionnaires").child(currentFocnt);
                fonct.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            Fonctionnaire f = dataSnapshot.getValue(Fonctionnaire.class);
                            if(fonctemail != null && fonctcname != null){
                                fonctcname.setText(f.getNom()+" "+f.getPrenom());
                                fonctemail.setText(f.getEmail());
                            }

                        }else
                            Toast.makeText(FctHome.this, "Data not found.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                /**END fonct data**/

                if(id == R.id.nav_home){
                    toolbar.setTitle("Home");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    MainFragmentTab fragment = new MainFragmentTab();
                    ft.replace(R.id.fragement_container, fragment);
                    ft.commit();
                }else if(id == R.id.nav_message){
                    toolbar.setTitle("Messages");
                    //Toast.makeText(FctHome.this, "Messages", Toast.LENGTH_SHORT).show();
                    setTitle("Détails de demande");
                    startActivity(new Intent(FctHome.this, DemandeDetails.class));

                }
                else if(id == R.id.nav_profile){
                    toolbar.setTitle("Profile");
                    startActivity(new Intent(FctHome.this, Profile_fnct.class));
                    //Toast.makeText(FctHome.this, "Profles", Toast.LENGTH_SHORT).show();
                }

                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        /******* End navigation drawer tricks***/



    }


    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START))
            drawer.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }


}
