package com.example.swiftems_;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Driver_Dashboard extends AppCompatActivity {
CardView uploadambulance,allbooking,feedback,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_dashboard);
        uploadambulance=findViewById(R.id.uploadambu);
        allbooking=findViewById(R.id.allbooking);
        feedback=findViewById(R.id.allfeedback);
        logout=findViewById(R.id.adminlogout);
logout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Driver_Dashboard.this, Dashboard.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
});

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Dashboard.this, Feed.class));

            }
        });
allbooking.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(Driver_Dashboard.this, All_Booking.class));

    }
});
        uploadambulance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Dashboard.this, Upload_Ambulance.class));

            }
        });



    }
}