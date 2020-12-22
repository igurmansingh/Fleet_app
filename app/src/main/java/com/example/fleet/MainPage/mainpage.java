package com.example.fleet.MainPage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Path;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.fleet.LoginAndSignup.LoginActivity;
import com.example.fleet.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.lang.reflect.Array;
import java.util.Arrays;

public class mainpage extends FragmentActivity implements OnMapReadyCallback {

    Button back, search;
    PlacesClient placesClient;
    public static String[] loc = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        back = findViewById(R.id.back_button);
        search = findViewById(R.id.search_button);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent b = new Intent(mainpage.this, LoginActivity.class);
                startActivity(b);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                display_track(loc[0],loc[1]);
            }
        });

        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }



    @Override
    public void onMapReady(final GoogleMap googleMap) {

        LatLng location1,location2;

        if (!Places.isInitialized()) {
            Places.initialize(this, "AIzaSyCV_a-zF70Ks_yqv-6Qvd58WgQwSGdgA-c");
        }

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autoComplete);

        final AutocompleteSupportFragment autocompleteSupportFragment2 =
                (AutocompleteSupportFragment)
                        getSupportFragmentManager().findFragmentById(R.id.autoComplete2);

        placesClient = Places.createClient(this);

        autocompleteSupportFragment.setHint("Source");
        autocompleteSupportFragment2.setHint("Destination");

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));
        autocompleteSupportFragment2.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG,Place.Field.ADDRESS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        final LatLng latLng = place.getLatLng();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 13.0f));
                        loc[0]=place.getName();
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(mainpage.this, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        autocompleteSupportFragment2.setOnPlaceSelectedListener(
                new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        final LatLng latLng = place.getLatLng();
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom((latLng), 13.0f));
                        loc[1]=place.getName();
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(mainpage.this, ""+status.getStatusMessage(), Toast.LENGTH_SHORT).show();
                    }
                });





    }

    private void display_track(String source, String destination){
        Log.e("TAG", "locationsssssssssssssss"+loc[0] + "" +loc[1] );
        try{
            Uri uri = Uri.parse("http://www.google.co.in/maps/dir/"+ source + "/" + destination);
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setPackage("com.google.android.apps.maps");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }catch (ActivityNotFoundException e){
            Uri uri = Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.apps.maps");
            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }
}