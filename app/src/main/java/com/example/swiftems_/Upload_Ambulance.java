package com.example.swiftems_;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class Upload_Ambulance extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener{

    Activity mContext = this;

    ImageView uploadPicIV;
    private ProgressBar uploadProgressBar;
    final int IMAGE_REQUEST = 71;
    Uri imageLocationPath;
    StorageReference objectStorageReference;
    FirebaseFirestore objectFirebaseFirestore;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    com.google.android.gms.location.LocationRequest mLocationRequest;
    private int REQUEST_CODE = 11;
    SupportMapFragment mapFragment;
    EditText mFullName;
    Button mSubmitBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    public static final String TAG = "TAG";
    TextView area;
    EditText SuctionUnit,Ventilators,Bavalvemask,Defibrillator,Infusionpumps,Stretcher,MedicationBag,Monitoringequipment;
    private String[] options;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ambulance);
        options = new String[]{"Yes", "No"};



area=findViewById(R.id.namedirtick);
SuctionUnit=findViewById(R.id.suctionunit);
Ventilators=findViewById(R.id.ventilators);
Bavalvemask=findViewById(R.id.bavalvemask);
Defibrillator=findViewById(R.id.defibrillator);
Infusionpumps=findViewById(R.id.infusionpumps);
Stretcher=findViewById(R.id.stretcher);
MedicationBag=findViewById(R.id.medicationBag);
Monitoringequipment=findViewById(R.id.monitor);

SuctionUnit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showOptionsDialog();

    }
});
Ventilators.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                Ventilators.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
});
Bavalvemask.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                Bavalvemask.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
});
Defibrillator.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                Defibrillator.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();    }
});
Infusionpumps.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
               Infusionpumps.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();    }
});
Stretcher.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                Stretcher.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();    }
});
MedicationBag.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                MedicationBag.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();    }
});
Monitoringequipment.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                Monitoringequipment.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();    }
});



        mFullName = findViewById(R.id.donorname);
        mSubmitBtn=findViewById(R.id.submit);

        uploadPicIV = findViewById(R.id.imageID);




        uploadProgressBar = findViewById(R.id.progress_bar);

        objectStorageReference = FirebaseStorage.getInstance().getReference("imageFolder"); // Create folder to Firebase Storage
        objectFirebaseFirestore = FirebaseFirestore.getInstance();

        fAuth=FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mapFragment.getMapAsync(this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
    }

    private void showOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedOption = options[which];
                SuctionUnit.setText(selectedOption);
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void selectImage(View view) {
        try {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");

            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent, IMAGE_REQUEST);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == IMAGE_REQUEST
                    && resultCode == RESULT_OK
                    && data != null
                    && data.getData() != null) {
                imageLocationPath = data.getData();
                Bitmap objectBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageLocationPath);
                uploadPicIV.setImageBitmap(objectBitmap);

            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
    public void onLocationChanged(@NonNull Location location) {
        mLastLocation = location;
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());

        //MarkerOptions markerOptions1 = new MarkerOptions().position(latLng).title("You are here");
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //mMap.addMarker(markerOptions1).showInfoWindow();
        String busname = mFullName.getText().toString();
        getAddressFromLocation(location);
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("busname").snippet(busname).icon(BitmapDescriptorFactory.fromResource(R.drawable.ambulance));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
        mMap.addMarker(markerOptions).showInfoWindow();





        mSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (/*!uploadPicET.getText().toString().isEmpty() && */imageLocationPath != null) {
                        //String nameOfimage = "." + getExtension(imageLocationPath);
                        String uuid = UUID.randomUUID().toString();
                        String nameOfimage = uuid.toString() + "." + getExtension(imageLocationPath);
                        uploadProgressBar.setVisibility(View.VISIBLE);
                        uploadProgressBar.setIndeterminate(true);
                        final StorageReference imageRef = objectStorageReference.child(nameOfimage);

                        UploadTask objectUploadTask = imageRef.putFile(imageLocationPath);

                        objectUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {

                                    throw task.getException();

                                }
                                return imageRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {

                                    String saveCurrentDate, saveCurrenttime;
                                    Calendar calForDate = Calendar.getInstance();


                                    SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
                                    saveCurrentDate = currentDate.format(calForDate.getTime());
                                    SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                                    saveCurrenttime = currenttime.format(calForDate.getTime());


                                    String fullname = mFullName.getText().toString().trim();
                                    String districk = area.getText().toString().trim();
                                    String succ = SuctionUnit.getText().toString().trim();
                                    String ven = Ventilators.getText().toString().trim();
                                    String bag = Bavalvemask.getText().toString().trim();
                                    String def = Defibrillator.getText().toString().trim();
                                    String inf = Infusionpumps.getText().toString().trim();
                                    String str = Stretcher.getText().toString().trim();
                                    String med = MedicationBag.getText().toString().trim();
                                    String mon = Monitoringequipment.getText().toString().trim();





                                    //  userID = fAuth.getCurrentUser().getUid();
                                    //DocumentReference documentReference = fStore.collection("donate").document(userID);
                                    CollectionReference collectionReference = fStore.collection("user data");

                                    GeoPoint geoPoint = new GeoPoint(location.getLatitude(),location.getLongitude());
                                    String uuid = UUID.randomUUID().toString();



                                    Map<String, Object> objectMap = new HashMap<>();
                                    String docId = objectFirebaseFirestore.collection("data").document().getId();
                                    objectMap.put("uri", task.getResult().toString());
                                    objectMap.put("docId", uuid);
                                    objectMap.put("date", saveCurrentDate);
                                    objectMap.put("time", saveCurrenttime);
                                    objectMap.put("ambulancename",fullname);
                                    objectMap.put("ventilators",ven);
                                    objectMap.put("bavalvemask",bag);
                                    objectMap.put("defibrillator",def);
                                    objectMap.put("infusionpumps",inf);
                                    objectMap.put("stretcher",str);
                                    objectMap.put("medicationBag",med);
                                    objectMap.put("monitor",mon);
                                    objectMap.put("suctionUnit",succ);
                                    objectMap.put("area",districk);
                                    objectMap.put("location",geoPoint);
                                    objectMap.put("userid",userID);

                                    objectFirebaseFirestore.collection("Ambulance").document(uuid)
                                            .set(objectMap)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    uploadProgressBar.setVisibility(View.GONE);
                                                    uploadProgressBar.setIndeterminate(false);
                                                    uploadProgressBar.setProgress(0);
                                                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                                    alert.setMessage("Ambulance Upload Sucessful");
                                                    alert.setCancelable(false);
                                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();

                                                            /*    Intent intent = new Intent(Exam.this, Institute_Dashboard.class);
                                                                startActivity(intent);
                                                                finish();*/
                                                        }
                                                    });
                                                    alert.show();
                                                    //  Toast.makeText(Student_Registration.this, "Upload Sucessfully", Toast.LENGTH_SHORT).show();
                                                }
                                            });


                                } else if (!task.isSuccessful()) {
                                    Toast.makeText(Upload_Ambulance.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


                    } else {
                        //    Toast.makeText(this, "Please provide name for image", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    private void getAddressFromLocation(Location location) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1); // You can specify the maximum results here
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);

                // Get the district from the address
                String district = address.getSubAdminArea();

                // Update the TextView with the district name
                updateDistrictTextView(district);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void updateDistrictTextView(String district) {
        if (district != null) {
            // Display the district information in your TextView
            area.setText( district);
        } else {
            // Handle the case when district information is not available
            area.setText("District information not available");
        }
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

    private String getExtension(Uri uri) {
        try {
            ContentResolver objectContentResolver = getContentResolver();
            MimeTypeMap objectMimeTypeMap = MimeTypeMap.getSingleton();

            return objectMimeTypeMap.getExtensionFromMimeType(objectContentResolver.getType(uri));

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return null;


    }
}
