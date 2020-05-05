package com.mejdaoui.servicesdemaintenance;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.mejdaoui.servicesdemaintenance.Helpers.FetchURL;
import com.mejdaoui.servicesdemaintenance.Helpers.TaskLoadedCallback;

public class PositionFonct extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    private GoogleMap mMap;
    private MarkerOptions place1, place2;
    private Polyline currentPolyline;
    Bundle b;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_fonct);
        b = getIntent().getExtras();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.positionFonct);
        mapFragment.getMapAsync(this);

        place1 = new MarkerOptions().position(new LatLng(b.getDouble("lat"), b.getDouble("lang"))).title("CLIENT"); //ClientLocation


    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("mylog", "Added Markers");
        //GPS
        mMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        enableMyLocationIfPermitted();
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.setMinZoomPreference(8);
        mMap.addMarker(place1);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener(){
            @Override
            public void onMapClick(LatLng latLng){
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                Double lat_location =latLng.latitude;
                Double long_location =latLng.longitude;

                mMap.clear();
                mMap.addMarker(place1);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,17));
                mMap.addMarker(markerOptions);

                place2 = new MarkerOptions().position(new LatLng(lat_location, long_location)).title("FONCT"); //FonctLocatinon

                mMap.addMarker(place2);

                //Drawing direction
                new FetchURL(PositionFonct.this).execute(getUrl(place1.getPosition(), place2.getPosition(), "driving"), "driving");


            }
        });


    }



    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        System.out.println("--PARAM: "+parameters);
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        //String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + "AIzaSyCJgcZa41QsmMpXRglDEacAR6ENosEW3wM";
        System.out.println("-- URL_MAP = "+url);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        Toast.makeText(this, "OnTaskDONE !", Toast.LENGTH_SHORT).show();
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }


    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            mMap.setMyLocationEnabled(true);
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
}
