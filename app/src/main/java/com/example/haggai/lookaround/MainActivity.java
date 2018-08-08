package com.example.haggai.lookaround;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    public final int PERMISSION_ACCESS_LOCATION = 33 ; //arbitrary
    private GoogleMap mMap;
    protected List<PointOfInterest> pointsList;

    protected SupportMapFragment mapFragment = null ;


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 1;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_LOCATION);
        }
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
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
                MainActivity.this.pointsList = result;            }
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
                        if ( ContextCompat.checkSelfPermission( MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED ) {
                            mMap.setMyLocationEnabled(true);
                        }
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(center, 10.0f));
                    }

                    @Override
                    public void error(String err) {
                        Toast.makeText(MainActivity.this, err, Toast.LENGTH_LONG).show();
                    }
                });

    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(mapFragment == null){
                mapFragment = new SupportMapFragment();
                mapFragment.getMapAsync(MainActivity.this);
            }
            return mapFragment;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



}
