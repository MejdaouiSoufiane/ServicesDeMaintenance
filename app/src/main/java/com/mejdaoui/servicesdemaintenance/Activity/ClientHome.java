package com.mejdaoui.servicesdemaintenance.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mejdaoui.servicesdemaintenance.Fragement.ListDemande;
import com.mejdaoui.servicesdemaintenance.Login;
import com.mejdaoui.servicesdemaintenance.R;

public class ClientHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //FOR DESIGN
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    //FOR FRAGMENTS
    // 1 - Declare fragment handled by Navigation Drawer
    private Fragment fragmentDemande;
    private Fragment fragmentNewDemande;
    private Fragment fragmentProfile;

    //FOR DATAS
    // 2 - Identify each fragment with a number
    private static final int FRAGMENT_DEMANDE = 0;
    private static final int FRAGMENT_NEW_DEMANDE = 1;
    private static final int FRAGMENT_DEMANDE_ATTENTE = 2;
    private static final int FRAGMENT_DEMANDE_COURS = 3;
    private static final int FRAGMENT_PROFILE = 4;
    private static final int FRAGMENT_LOGOUT = 5;

    DatabaseReference databaseReference;
    FirebaseUser user ;
    private String uid ;
    private TextView profileMail, profileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_home);

        header_view();

        // 6 - Configure all views

        this.configureToolBar();

        this.configureDrawerLayout();

        this.configureNavigationView();

        this.showDemandeFragment("all");



    }

    private void header_view() {
        NavigationView mNavigationView = findViewById(R.id.nav_view);
        View headerView = mNavigationView.getHeaderView(0);
        profileName = headerView.findViewById(R.id.homeName);
        profileMail = headerView.findViewById(R.id.homeEmail);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        DatabaseReference ref = databaseReference.child("clients").child(uid);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String nom = dataSnapshot.child("nom").getValue(String.class);
                String prenom = dataSnapshot.child("prenom").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);

                System.out.println(nom);
                String fullName = prenom+" "+ nom;

                profileName.setText(fullName);
                profileMail.setText(email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent(eventListener);

    }


    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // 4 - Handle Navigation Item Click
        int id = item.getItemId();

        switch (id){
            case R.id.nav_home:
                this.showFragment(FRAGMENT_DEMANDE);
                navigationView.setCheckedItem(R.id.Liste_demande);
                break;
            case R.id.nouv_demande:
                this.showFragment(FRAGMENT_NEW_DEMANDE);

                break;
            case R.id.Liste_demande_attente:
                this.showFragment(FRAGMENT_DEMANDE_ATTENTE);
                break;
            case R.id.Liste_demande_encours:
                this.showFragment(FRAGMENT_DEMANDE_COURS);
                break;
            case R.id.logout:
                this.showFragment(FRAGMENT_LOGOUT);
                break;
            case R.id.nav_profile:
                this.showFragment(FRAGMENT_PROFILE);
                break;

            default:
                break;
        }

        this.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }


    // ---------------------
    // CONFIGURATION
    // ---------------------

    // 1 - Configure Toolbar
    private void configureToolBar(){
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_navigation_drawer, R.string.close_navigation_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure NavigationView
    private void configureNavigationView(){
        this.navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // ---------------------
    // FRAGMENTS
    // ---------------------

    // 5 - Show fragment according an Identifier

    private void showFragment(int fragmentIdentifier){
        switch (fragmentIdentifier){
            case FRAGMENT_DEMANDE :
                this.showDemandeFragment("all");
                break;
            case FRAGMENT_DEMANDE_ATTENTE :
                this.showDemandeFragment("En Attente");
                break;
            case FRAGMENT_DEMANDE_COURS :
                this.showDemandeFragment("En Cours");
                break;

            case FRAGMENT_NEW_DEMANDE :
                startActivityForResult(new Intent(this, AddDemande.class),FRAGMENT_NEW_DEMANDE);
                break;
            case FRAGMENT_LOGOUT :
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                //finish();
                //startActivityForResult(new Intent(this, Login.class),FRAGMENT_LOGOUT);
            case FRAGMENT_PROFILE :
               // Intent intent = new Intent(this, ClientProfile.class);
               // startActivity(intent);
                this.showProfileClient();
                break;
            default:
                break;
        }
    }



    // 4 - Create each fragment page and show it

    private void showDemandeFragment(String type){
        if (this.fragmentDemande == null) this.fragmentDemande = ListDemande.newInstance();
        Bundle arguments = new Bundle();
        arguments.putString("TYPE", type);

        fragmentDemande.setArguments(arguments);
        this.startTransactionFragment(this.fragmentDemande);
        fragmentDemande=null;
    }

    private void showProfileClient() {
        if (this.fragmentProfile == null) this.fragmentProfile = ClientProfile.newInstance();
        this.startTransactionFragment(this.fragmentProfile);
    }



    // 3 - Generic method that will replace and show a fragment inside the MainActivity Frame Layout
    private void startTransactionFragment(Fragment fragment){

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragement_container, fragment).commit();

    }



}
