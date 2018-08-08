package com.example.haggai.lookaround;

import android.Manifest;
import android.content.pm.PackageManager;
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

import org.json.JSONArray;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private final String TAG = MapsActivity.class.getSimpleName();

    public final int PERMISSION_ACCESS_LOCATION = 33 ;
    private GoogleMap mMap;
    protected List<PointOfInterest> pointsList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_LOCATION);
        }
    }
    @Override
    public void onMapClick(LatLng position)
    {
        Log.i("onMapClick", "Horray!");

        String key = "AIzaSyDfik3ovAz5-61v2h9lVLgkQZoaq5HguhU" ; //getString(R.string.google_maps_key) ;
        String geoPoint = position.latitude+","+position.longitude ;
        String queryUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + geoPoint+ "&rankby=distance&" + "key="+key ;
        Log.d("onMapClick", "URL = "+queryUrl);

        JsonReader reader = new JsonReader(){
            @Override
            protected void onPostExecute(List<PointOfInterest> result) {
                super.onPostExecute(result);
                MapsActivity.this.pointsList = result;            }
        };
        reader.execute(queryUrl);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        googleMap.setOnMapClickListener(this);
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
