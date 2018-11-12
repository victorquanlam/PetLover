package com.example.a.customproject.posthome;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a.customproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class GpsTracker extends Fragment implements OnMapReadyCallback{


    SupportMapFragment mapFragment;
    public GpsTracker() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static GpsTracker newInstance() {
        GpsTracker fragment = new GpsTracker();

        return fragment;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_gps_tracker,container,false);
        mapFragment =(SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if(mapFragment==null){
            FragmentManager fm= getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map,mapFragment);
            ft.addToBackStack(null);//add the transaction to the back stack
            ft.commit();
        }
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng pet1= new LatLng(-37.827031, 145.027523);
        googleMap.addMarker(new MarkerOptions().position(pet1)
                .title("Your Pet Location"));

        LatLng pet2= new LatLng(-37.527031, 145.027523);
        googleMap.addMarker(new MarkerOptions().position(pet2)
                .title("Your Pet Location"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pet1));
    }
}
