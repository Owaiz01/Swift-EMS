package com.example.swiftems_;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Driver_Login extends AppCompatActivity {
    Activity mContext = this;

    TextView gotoregister;
    EditText phone,ppassword;
    Button loginbtn;
    TextView textView,test;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        gotoregister=findViewById(R.id.gotoregister);
        phone=findViewById(R.id.uphonenumber);
        ppassword=findViewById(R.id.upassword);
        loginbtn=findViewById(R.id.ulogin);
        firestore = FirebaseFirestore.getInstance();
        textView=findViewById(R.id.ppp);

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = phone.getText().toString().trim();
                String password = ppassword.getText().toString();


                if (number.isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setMessage("Name cannot be empty");
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    alert.show();
                }
                else if(password.isEmpty()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                    alert.setMessage("Email cannot be empty");
                    alert.setCancelable(false);
                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }else {

                    firestore.collection("Driver").whereEqualTo("user","1")
                            .whereEqualTo("number", number) // Replace "Item 1" with the value you want to query
                            .whereEqualTo("password", password)
                            .get()
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document :task.getResult()) {

                                        String name = document.getString("name");


                                        // Matching document found, go to the next activity
                                        SharedPreferences sharedPreferences = getSharedPreferences("Farmer", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        // Add data to SharedPreferences
                                        editor.putString("number", number);
                                        editor.putString("name",name);
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.apply();
                                        Intent intent = new Intent(Driver_Login.this, Driver_Dashboard.class);
                                        // Pass any data you want to send to the next activity using Intent extras
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else {
                                    // No matching document found, show an error message or handle the case accordingly
                                    Toast.makeText(Driver_Login.this, "No matching item found", Toast.LENGTH_SHORT).show();
                                }
                            });


                }





            }
        });


        gotoregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Login.this, Driver_Register.class));

            }
        });








    }
}