package com.example.swiftems_.ui.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.swiftems_.DetailActivity;
import com.example.swiftems_.R;
import com.example.swiftems_.databinding.FragmentHomeBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment  implements OnMapReadyCallback{
    private GoogleMap mMap;
    private FirebaseFirestore db;

    String hhhh = "";
    FirebaseFirestore objectFirebaseFirestore;
    StorageReference objectStorageReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root= inflater.inflate(R.layout.fragment_home, container, false);




        objectStorageReference = FirebaseStorage.getInstance().getReference("imageFolder"); // Create folder to Firebase Storage
        objectFirebaseFirestore = FirebaseFirestore.getInstance();

        db = FirebaseFirestore.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
       /* SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);*/
        mapFragment.getMapAsync(this);


        return root;
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;



        // Retrieve geopoints from Firestore and display them on the map
        retrieveAndDisplayGeopoints();
    }

    private void retrieveAndDisplayGeopoints() {



        CollectionReference geoPointsRef = db.collection("Ambulance");

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Currentlocation", MODE_PRIVATE);

        // Retrieve data from SharedPreferences
        String name = sharedPreferences.getString("area", "");

        db.collection("Ambulance").whereEqualTo("area",name).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<GeoPoint> geoPointList = new ArrayList<>();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    GeoPoint geoPoint = documentSnapshot.getGeoPoint("location");
                    geoPointList.add(geoPoint);

                    String data = documentSnapshot.getString("ambulancename");



                    if (geoPoint != null && data != null) {
                        LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(data).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancemap))).setTag(documentSnapshot.getId());

                    }

                }

                // After retrieving all geopoints, display them on the map
                displayGeopointsOnMap(geoPointList);
            }
        });
    }

    private void displayGeopointsOnMap(List<GeoPoint> geoPointList) {
        for (GeoPoint geoPoint : geoPointList) {
            LatLng latLng = new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title(hhhh).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancemap)));
        }

        // Move camera to the first geopoint (you can customize this based on your needs)
        if (!geoPointList.isEmpty()) {
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (GeoPoint geoPoint : geoPointList) {
                builder.include(new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()));

            }
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                // Check if the tag is not null
                Object tag = marker.getTag();
                if (tag != null) {
                    // Open a new page or activity with full details
                    openDetailPage(tag.toString());
                }
                return true;
            }
        });

    }

    private void openDetailPage(String string) {
        //  showBottomSheet();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Student", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Add data to SharedPreferences
        editor.putString("rollnumber",string);
        // Save the data to SharedPreferences
        editor.apply();


        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra("documentId", string);
        startActivity(intent);


    }

/*    private void showBottomSheet()  {



        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.activity_detail);












    }*/


}