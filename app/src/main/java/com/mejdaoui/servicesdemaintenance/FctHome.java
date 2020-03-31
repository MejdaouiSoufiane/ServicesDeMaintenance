package com.mejdaoui.servicesdemaintenance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mejdaoui.servicesdemaintenance.Fragement.FonctionnaireRecycler;
import com.mejdaoui.servicesdemaintenance.Fragement.MainFragmentTab;

import java.util.ArrayList;

public class FctHome extends AppCompatActivity {

    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fct_home);

        /******* navigation drawer tricks ***/
        final Toolbar toolbar = findViewById(R.id.toolbar);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        toolbar.setTitle("TESTT");
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

                if(id == R.id.nav_home){
                    toolbar.setTitle("Home");
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    MainFragmentTab fragment = new MainFragmentTab();
                    ft.replace(R.id.fragement_container, fragment);
                    ft.commit();
                }else if(id == R.id.nav_message){
                    toolbar.setTitle("Messages");
                    Toast.makeText(FctHome.this, "Messages", Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.nav_profile){
                    toolbar.setTitle("Profile");
                    Toast.makeText(FctHome.this, "Profles", Toast.LENGTH_SHORT).show();
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
