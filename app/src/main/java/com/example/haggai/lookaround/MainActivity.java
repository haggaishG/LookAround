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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    public final int PERMISSION_ACCESS_LOCATION = 33 ; //arbitrary
    public final int MAP_PAGE = 0 ;
    public final int LIST_PAGE = 1 ;
    private static final int NUM_PAGES = 2;

    private TextView tapMessage;
    private GoogleMap mMap;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    protected List<PointOfInterest> pointsList;
    protected SupportMapFragment mapFragment = null ;
    protected ListFragment listFragment = null ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen_layout);
        tapMessage = (TextView)findViewById(R.id.tap_message);
        if ( ContextCompat.checkSelfPermission( this, android.Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_LOCATION);
        }
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            public void onPageSelected(int position) {
                Log.d("PAGE_SELECTED", "page selected "+position);
                if(position == MAP_PAGE){
                    if(mMap == null || pointsList == null){
                        return;
                    }
                    mMap.clear();
                    for(PointOfInterest poi : pointsList){
                        if(poi.isSelectedForDisplay()){
                            Marker marker = mMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(poi.getPosition()[0], poi.getPosition()[1]))
                                    .anchor(0.5f, 0.5f)
                                    .title(poi.getName())
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.compass_marker))
                                    );
                        }
                    }

                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public void onMapClick(LatLng position)
    {
        tapMessage.setVisibility(View.GONE);

        String key = getString(R.string.google_maps_key) ;
        String geoPoint = position.latitude+","+position.longitude ;
        String queryUrl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + geoPoint+ "&rankby=distance&" + "key="+key ;
        JsonReader reader = new JsonReader(){
            @Override
            protected void onPostExecute(List<PointOfInterest> result) {
                super.onPostExecute(result);
                MainActivity.this.pointsList = result;
                MainActivity.this.listFragment.setList(result);
                MainActivity.this.mPager.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.this.mPager.setCurrentItem(LIST_PAGE);
                    }
                }, 100);
            }
        };
        reader.execute(queryUrl);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        tapMessage.setVisibility(View.VISIBLE);
        googleMap.setOnMapClickListener(this);
        SingleShotLocationProvider.requestSingleUpdate(this,
                new SingleShotLocationProvider.LocationCallback() {
                    @Override public void onNewLocationAvailable(double lat, double lon) {
                        LatLng center = new LatLng(lat, lon);
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

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case MAP_PAGE:
                    if (mapFragment == null) {
                        mapFragment = new SupportMapFragment();
                        mapFragment.getMapAsync(MainActivity.this);

                    }
                    return mapFragment;
                case LIST_PAGE:
                default:
                    if(listFragment == null){
                        listFragment = new ListFragment();
                        listFragment.setList(MainActivity.this.pointsList);
                    }
                    return listFragment ;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }



}
