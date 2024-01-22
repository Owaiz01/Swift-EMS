package com.example.swiftems_;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{
    private SearchView searchView;
    private static final double DEFAULT_LATITUDE = 37.7749; // Default latitude (e.g., San Francisco)
    private static final double DEFAULT_LONGITUDE = -122.4194;
    AutoCompleteTextView searchAutoCompleteTextView;

    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    android.location.Location mLastLocation;
    com.google.android.gms.location.LocationRequest mLocationRequest;
    private int REQUEST_CODE = 11;
    SupportMapFragment mapFragment;
    Button mSubmitBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";
    private TextView addressTextView;
TextView SuctionUnit,Ventilators,Bagvalvemask,Defibrillator,Infusiopumps,Stretcher,MedicationBag,Monitoringequipment;
TextView ambulacemap;
    TextView doc, ambuname,ambulancearea;
    ImageView imageView;
    FirebaseFirestore objectFirebaseFirestore;
    StorageReference objectStorageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
SuctionUnit =findViewById(R.id.s);
Ventilators=findViewById(R.id.v);
Bagvalvemask=findViewById(R.id.bv);
Defibrillator=findViewById(R.id.d);
Infusiopumps=findViewById(R.id.ip);
Stretcher=findViewById(R.id.st);
MedicationBag=findViewById(R.id.me);
Monitoringequipment=findViewById(R.id.mo);
ambulacemap=findViewById(R.id.ambulanceloc);






ambulancearea=findViewById(R.id.alltroll);

        mSubmitBtn=findViewById(R.id.submit);
        addressTextView = findViewById(R.id.addressTextView);
        searchView = findViewById(R.id.searchView);
        searchAutoCompleteTextView = searchView.findViewById(R.id.searchAutoCompleteTextView);
        searchAutoCompleteTextView.requestFocus();

        searchAutoCompleteTextView.setHint("Search for a location");
        fAuth=FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();


        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapFragment.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<String>());
        searchAutoCompleteTextView.setAdapter(adapter);

        searchAutoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Handle item selection from auto-suggestions
                String selectedLocation = (String) parent.getItemAtPosition(position);
                searchLocation(selectedLocation);

            }
        });



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                updateAutoSuggestions(newText);

                return false;
            }
        });










        objectStorageReference = FirebaseStorage.getInstance().getReference("imageFolder"); // Create folder to Firebase Storage
        objectFirebaseFirestore = FirebaseFirestore.getInstance();


        doc = (TextView)findViewById(R.id.textView);
        imageView = (ImageView) findViewById(R.id.stimage);
        ambuname = (TextView) findViewById(R.id.alltname);


        SharedPreferences sharedPreferences = getSharedPreferences("Student", MODE_PRIVATE);

        // Retrieve data from SharedPreferences
        String name = sharedPreferences.getString("rollnumber", "");
        doc.setText(name);




        objectFirebaseFirestore.collection("Ambulance")
                .document(name)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String img = document.getString("uri");
                                // Set the EditText text with the fetched data
                                Glide.with(getApplicationContext())
                                        .load(img)
                                        .into(imageView);

                                // Retrieve the data field you want to display in the EditText
                                String data = document.getString("ambulancename");
                                // Set the EditText text with the fetched data
                                ambuname.setText(data);

                                // Retrieve the data field you want to display in the EditText
                                String darea = document.getString("area");
                                // Set the EditText text with the fetched data
                                ambulancearea.setText(darea);
                                String suc = document.getString("suctionUnit");
                                // Set the EditText text with the fetched data
                                SuctionUnit.setText(suc);

                                String ven = document.getString("ventilators");
                                // Set the EditText text with the fetched data
                                Ventilators.setText(ven);

                                String bag = document.getString("bavalvemask");
                                // Set the EditText text with the fetched data
                                Bagvalvemask.setText(bag);

                                String def = document.getString("defibrillator");
                                // Set the EditText text with the fetched data
                                Defibrillator.setText(def);


                                String inf = document.getString("infusionpumps");
                                // Set the EditText text with the fetched data
                                Infusiopumps.setText(inf);

                                String str = document.getString("stretcher");
                                // Set the EditText text with the fetched data
                                Stretcher.setText(str);

                                String medi = document.getString("medicationBag");
                                // Set the EditText text with the fetched data
                                MedicationBag.setText(medi);

                                String moni = document.getString("monitor");
                                // Set the EditText text with the fetched data
                                Monitoringequipment.setText(moni);

                                /*String loca = document.getString("location");
                                // Set the EditText text with the fetched data
                                ambulacemap.setText(loca);*/

                                GeoPoint geoPoint = document.getGeoPoint("location");
                                if (geoPoint != null) {
                                    double latitude = geoPoint.getLatitude();
                                    double longitude = geoPoint.getLongitude();

                                    String latitudeText = String.format("%.7f° %s", Math.abs(latitude), latitude >= 0 ? "N" : "S");
                                    String longitudeText = String.format("%.7f° %s", Math.abs(longitude), longitude >= 0 ? "E" : "W");

                                    String formattedLocation = "[" + latitudeText + ", " + longitudeText + "]";

                                    // Set the EditText text with the formatted location
                                    ambulacemap.setText(formattedLocation);
                                } else {
                                    // Handle the case where the "location" field is not present or is not a GeoPoint
                                    ambulacemap.setText("Location not available");
                                }



                            } else {
                                // Document does not exist
                            }
                        } else {
                            // Handle errors here
                        }
                    }
                });


    }


    private void updateAutoSuggestions(String newText) {

        // Implement logic to fetch and update auto-suggestions based on the query
        // You may use a web service or a local database to get suggestions
        // For simplicity, let's use a dummy list as an example
        List<String> suggestions = getDummySuggestions(newText);

        ArrayAdapter<String> adapter = (ArrayAdapter<String>) searchAutoCompleteTextView.getAdapter();
        adapter.clear();
        adapter.addAll(suggestions);
        adapter.notifyDataSetChanged();
    }

    private List<String> getDummySuggestions(String newText) {
        List<String> suggestions = new ArrayList<>();
        suggestions.add(newText + " suggestion 1");
        suggestions.add(newText + " suggestion 2");
        suggestions.add(newText + " suggestion 3");
        return suggestions;
    }

    private void searchLocation(String query) {


        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addressList = geocoder.getFromLocationName(query, 1);
            if (!addressList.isEmpty()) {
                Address address = addressList.get(0);
                LatLng searchedLocation = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(searchedLocation).title(query));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(searchedLocation, 15));

                // Display the selected address
                addressTextView.setText(address.getAddressLine(0));
                updateTextViewWithLatLng(searchedLocation);
            } else {
                Toast.makeText(this, "Location not found for: " + query, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error searching for location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        buildGoogleApiClient();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);

        // Set a marker at a default location (e.g., your current location)
        LatLng defaultLocation = new LatLng(DEFAULT_LATITUDE, DEFAULT_LONGITUDE);
        mMap.addMarker(new MarkerOptions().position(defaultLocation).title("Marker at Default Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 15));

        // Set the latitude and longitude in the TextView
        updateTextViewWithLatLng(defaultLocation);


    }

    private void updateTextViewWithLatLng(LatLng defaultLocation) {
        double latitude = defaultLocation.latitude;
        double longitude = defaultLocation.longitude;

        String latitudeText = String.format("%.7f° N", Math.abs(latitude));
        String longitudeText = String.format("%.7f° E", Math.abs(longitude));



        String formattedLocation = "[" + latitudeText + ", " + longitudeText + "]";

// Assuming you have a TextView named textViewLocation
        addressTextView.setText(formattedLocation);
      //  addressTextView.setText(latitudeText + "\n" + longitudeText);

    }




    protected synchronized void buildGoogleApiClient(){
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(@NonNull android.location.Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        //MarkerOptions markerOptions1 = new MarkerOptions().position(latLng).title("You are here");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.addMarker(markerOptions1).showInfoWindow();


        getAddressFromLocation(location);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        mMap.addMarker(markerOptions).showInfoWindow();








        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    userID = fAuth.getCurrentUser().getUid();
                //DocumentReference documentReference = fStore.collection("donate").document(userID);
                CollectionReference collectionReference = fStore.collection("user data");

                GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());

                String address = addressTextView.getText().toString();
                String ambula = ambulacemap.getText().toString();

// Define a pattern to match numeric values and direction indicators
                Pattern pattern = Pattern.compile("[-+]?\\d*\\.?\\d+|[NSWE]");

// Convert addressTextView's text to GeoPoint
                Matcher matcherAddress = pattern.matcher(address);

                double latitudeAddress = 0.0;
                double longitudeAddress = 0.0;
                boolean isLatitudeNegativeAddress = false;
                boolean isLongitudeNegativeAddress = false;

                while (matcherAddress.find()) {
                    String match = matcherAddress.group();
                    if (match.equals("N") || match.equals("S")) {
                        isLatitudeNegativeAddress = match.equals("S");
                    } else if (match.equals("E") || match.equals("W")) {
                        isLongitudeNegativeAddress = match.equals("W");
                    } else {
                        double value = Double.parseDouble(match);
                        if (latitudeAddress == 0.0) {
                            latitudeAddress = value;
                        } else {
                            longitudeAddress = value;
                        }
                    }
                }

// Adjust latitude and longitude based on direction
                if (isLatitudeNegativeAddress) {
                    latitudeAddress = -latitudeAddress;
                }

                if (isLongitudeNegativeAddress) {
                    longitudeAddress = -longitudeAddress;
                }

// Create GeoPoint object for address
                GeoPoint geoPointFromAddress = new GeoPoint(latitudeAddress, longitudeAddress);

// Convert ambulacemap's text to GeoPoint
                Matcher matcherAmbula = pattern.matcher(ambula);

                double latitudeAmbula = 0.0;
                double longitudeAmbula = 0.0;
                boolean isLatitudeNegativeAmbula = false;
                boolean isLongitudeNegativeAmbula = false;

                while (matcherAmbula.find()) {
                    String match = matcherAmbula.group();
                    if (match.equals("N") || match.equals("S")) {
                        isLatitudeNegativeAmbula = match.equals("S");
                    } else if (match.equals("E") || match.equals("W")) {
                        isLongitudeNegativeAmbula = match.equals("W");
                    } else {
                        double value = Double.parseDouble(match);
                        if (latitudeAmbula == 0.0) {
                            latitudeAmbula = value;
                        } else {
                            longitudeAmbula = value;
                        }
                    }
                }

// Adjust latitude and longitude based on direction
                if (isLatitudeNegativeAmbula) {
                    latitudeAmbula = -latitudeAmbula;
                }

                if (isLongitudeNegativeAmbula) {
                    longitudeAmbula = -longitudeAmbula;
                }

// Create GeoPoint object for ambula
                GeoPoint geoPointFromAmbula = new GeoPoint(latitudeAmbula, longitudeAmbula);
String amulancen = ambuname.getText().toString();
                String amula = ambulancearea.getText().toString();


                String uuid = UUID.randomUUID().toString();
                Map<String,Object> user = new HashMap<>();
                user.put("timestamp", FieldValue.serverTimestamp());

              //  user.put("location",geoPoint);
                user.put("userid",userID);
                user.put("docId", uuid);
                user.put("address", geoPointFromAddress);
                user.put("ambulancelocation", geoPointFromAmbula);
                user.put("ambulancename", amulancen);
                user.put("ambulancearea", amula);


                fStore.collection("Mybooking").document(uuid)
                        .set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                                startActivity(intent);
                                Toast.makeText(DetailActivity.this, "Booked Check in My Booking", Toast.LENGTH_SHORT).show();

                            }
                        });


            }
        });
    }

    private void getAddressFromLocation(android.location.Location location) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1); // You can specify the maximum results here
            if (addresses != null && addresses.size() > 0) {
                android.location.Address address = addresses.get(0);

                // Get the state from the address
                String state = address.getAdminArea();

                // Update the TextView with the state name
                // updateDirtickTextView(state);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateDirtickTextView(String state) {
        addressTextView.setText(state);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new com.google.android.gms.location.LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment.getMapAsync(this);
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
