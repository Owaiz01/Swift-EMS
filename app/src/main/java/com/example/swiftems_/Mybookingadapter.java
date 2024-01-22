package com.example.swiftems_;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import java.util.ArrayList;
import java.util.List;

public class Mybookingadapter extends RecyclerView.Adapter<Mybookingadapter.ViewHolder> implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {

    private Context context;
    private List<Mybookingmodel> mybookingmodelList;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    private GeoApiContext geoApiContext; // Initialize this in the constructor

    public Mybookingadapter(Context context, List<Mybookingmodel> mybookingmodelList) {
        this.context = context;
        this.mybookingmodelList = mybookingmodelList;

        geoApiContext = new GeoApiContext.Builder()
                .apiKey("AIzaSyBtAubRGSlTZanGLTPT3JrKWsRCFAXZzrE")
                .build();


    }

    @NonNull
    @Override
    public Mybookingadapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.mybooking, parent, false));


    }

    @Override
    public void onBindViewHolder(@NonNull Mybookingadapter.ViewHolder holder, int position) {
        holder.name.setText(mybookingmodelList.get(position).getAmbulancename());



        getDirectionsAndDrawRoute(holder, position);

    }

    private void getDirectionsAndDrawRoute(ViewHolder holder, int position) {
        Mybookingmodel bookingModel = mybookingmodelList.get(position);
        LatLng origin = new LatLng(bookingModel.getAmbulancelocation().getLatitude(), bookingModel.getAmbulancelocation().getLongitude());
        LatLng destination = new LatLng(bookingModel.getAddress().getLatitude(), bookingModel.getAddress().getLongitude());

        DirectionsApiRequest request = new DirectionsApiRequest(geoApiContext);
        request.origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude));
        request.destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));
        request.mode(TravelMode.DRIVING); // You can change the mode as needed

        request.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                // Handle the result and draw the route on the map
                if (result.routes != null && result.routes.length > 0) {
                    PolylineOptions polylineOptions = new PolylineOptions();

                    for (int i = 0; i < result.routes[0].overviewPolyline.decodePath().size(); i++) {
                        com.google.maps.model.LatLng point = result.routes[0].overviewPolyline.decodePath().get(i);
                        polylineOptions.add(new LatLng(point.lat, point.lng));
                    }

                    holder.googleMap.addPolyline(polylineOptions);
                }
            }

            @Override
            public void onFailure(Throwable e) {
                // Handle failure, e.g., show an error message
            }
        });

}


    @Override
    public int getItemCount() {
        return mybookingmodelList.size();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

    }

    private void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements OnMapReadyCallback {
        TextView name;
        private GoogleMap googleMap;
        private MapView mapView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.busname);
            mapView = itemView.findViewById(R.id.mapView);
            mapView.onCreate(null);
            mapView.getMapAsync(this);




        }

        public void bind(Mybookingmodel map) {
            if (googleMap != null) {

            }

        }

        public void bindData(Mybookingmodel location) {
            // Use the location data to set up the map marker or other map interactions
            if (googleMap != null) {
                LatLng latLng = new LatLng(location.getAmbulancelocation().getLatitude(), location.getAmbulancelocation().getLongitude());
                LatLng latL = new LatLng(location.getAddress().getLatitude(), location.getAddress().getLongitude());

            /*    googleMap.addMarker(new MarkerOptions().position(latLng).title("Location Marker"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));*/

                MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(mybookingmodelList.get(getAdapterPosition()).ambulancename).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancemap));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                googleMap.addMarker(markerOptions).showInfoWindow();


                MarkerOptions markerOption = new MarkerOptions().position(latL).title(mybookingmodelList.get(getAdapterPosition()).ambulancename).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulancemap));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latL, 10));
                googleMap.addMarker(markerOption).showInfoWindow();





// Add a Polyline between the two markers
                PolylineOptions polylineOptions = new PolylineOptions()
                        .add(latLng, latL)  // Add the two marker positions to the Polyline
                        .width(5)           // Set the width of the polyline
                        .color(Color.RED);  // Set the color of the polyline

                googleMap.addPolyline(polylineOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(latLng);
                builder.include(latL);
                LatLngBounds bounds = builder.build();
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));


            }
        }


        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            this.googleMap = googleMap;
            Mybookingmodel location = mybookingmodelList.get(getAdapterPosition());
            bindData(location);

            Mybookingmodel map = mybookingmodelList.get(getAdapterPosition());
            bind(map);


        }

    }


}

