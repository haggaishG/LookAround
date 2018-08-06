package com.example.haggai.lookaround;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    public final int MY_PERMISSION_ACCESS_COURSE_LOCATION = 33 ;
    public final int MY_PERMISSION_ACCESS_FINE_LOCATION =  MY_PERMISSION_ACCESS_COURSE_LOCATION+1;

    private GoogleMap mMap;
    private LocationManager mLocationManager ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_ACCESS_COURSE_LOCATION);
        }
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
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()
        {
            @Override
            public void onMapClick(LatLng arg0)
            {
                android.util.Log.i("onMapClick", "Horray!");
            }
        });
        SingleShotLocationProvider.requestSingleUpdate(this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(double lat, double lon) {
                        // Add a marker in Sydney and move the camera
                        LatLng center = new LatLng(lat, lon);
                        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(center));
                        if ( ContextCompat.checkSelfPermission( MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
                            mMap.setMyLocationEnabled(true);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10.0f));
                    }

                    @Override
                    public void error(String err) {
                        Toast.makeText(MapsActivity.this, err, Toast.LENGTH_LONG).show();
                    }
                });

    }
}
