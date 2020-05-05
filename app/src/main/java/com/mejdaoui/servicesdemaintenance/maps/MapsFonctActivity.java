package com.mejdaoui.servicesdemaintenance.maps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.ui.IconGenerator;
import com.mejdaoui.servicesdemaintenance.Model.Demande;
import com.mejdaoui.servicesdemaintenance.R;

import java.util.ArrayList;

public class MapsFonctActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ClusterManager<ClusterMarker> mClusterManager;
    private MyClusterManagerRenderer myClusterManagerRenderer;
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();
    private IconGenerator iconGenerator;
    private ImageView imageView;
    ArrayList<Demande> demandeList;
    DatabaseReference reference;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    FirebaseUser user;
    String uid_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_fonct);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

     /*   user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            for (UserInfo profile : user.getProviderData()) {
                // UID specific to the provider
                uid_user = profile.getUid();
            }
        }*/

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        //GPS
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(8);



        reference = FirebaseDatabase.getInstance().getReference().child("Demandes");
        reference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                demandeList = new ArrayList<Demande>();
                for (DataSnapshot dds: dataSnapshot.getChildren())
                {
                    iconGenerator = new IconGenerator(MapsFonctActivity.this);
                    imageView = new ImageView(MapsFonctActivity.this);
                    Demande d = dds.getValue(Demande.class);
                    // if(d.getIdClient().equals(uid_user))
                    demandeList.add(d);
                    LatLng p = new LatLng(d.getLat_loc(), d.getLong_loc());
                    MarkerOptions marker = new MarkerOptions().position(p).title(d.getTitre());

                }

               addMapMarkers();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


      //  addMapMarkers();
        LatLng sydney = new LatLng(-34, 151);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }


    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    mMap.setMinZoomPreference(8);
                    return false;
                }
            };

    private void addMapMarkers(){
        if (mMap != null){
            if (mClusterManager == null){
                mClusterManager = new ClusterManager<ClusterMarker>(MapsFonctActivity.this,mMap);
            }
            if (myClusterManagerRenderer == null){
                myClusterManagerRenderer =new MyClusterManagerRenderer(MapsFonctActivity.this,mMap,mClusterManager);
                mClusterManager.setRenderer(myClusterManagerRenderer);
            }

            for (Demande d: demandeList){
                try{
                    int avatar = R.drawable.image;

                    ClusterMarker newClusterMarker = new ClusterMarker(
                        new LatLng(d.getLat_loc(), d.getLong_loc()),
                        d.getTitre(),
                        d.getDescription(),
                        d.getAdr_picture()
                    );
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);
                }catch (NullPointerException e){
                    Log.e("tag", "addMapMarkers: NullPointerException: " + e.getMessage() );
                }
            }
            mClusterManager.cluster();

        }

    }
}
