package com.example.swiftems_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Driver_Register extends AppCompatActivity {
    TextView gotologin;
    Activity mContext = this;
    EditText name,email,number,password;
    Button register;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        name=findViewById(R.id.pname);
        email=findViewById(R.id.pemail);
        number=findViewById(R.id.pnumber);
        password=findViewById(R.id.ppassword);
        register=findViewById(R.id.pregister);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pname = name.getText().toString();
                String pemail = email.getText().toString();
                String pnumber = number.getText().toString();
                String ppassword = password.getText().toString();

                String saveCurrentDate, saveCurrenttime;
                Calendar calForDate = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("MM, dd, yyyy");
                saveCurrentDate = currentDate.format(calForDate.getTime());

                SimpleDateFormat currenttime = new SimpleDateFormat("HH:mm:ss a");
                saveCurrenttime = currenttime.format(calForDate.getTime());



                String uuid = UUID.randomUUID().toString();
                Map<String, Object> objectMap = new HashMap<>();
                objectMap.put("name", pname);
                objectMap.put("email", pemail);
                objectMap.put("number", pnumber);
                objectMap.put("user","1");
                objectMap.put("password", ppassword);
                objectMap.put("date", saveCurrentDate);
                objectMap.put("time", saveCurrenttime);
                objectMap.put("docId",uuid);

                if (pname.isEmpty()){
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
                else if(pemail.isEmpty()){
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
                }
                else if(pnumber.isEmpty()){
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
                }
                else if(ppassword.isEmpty()){
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

                    db.collection("Driver").document(uuid)
                            .set(objectMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                                    alert.setMessage("Register Sucessful");
                                    alert.setCancelable(false);
                                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(Driver_Register.this, Driver_Login.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                    alert.show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
                                }
                            });


                }





            }
        });



        gotologin=findViewById(R.id.gotologin);
        gotologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Driver_Register.this, Driver_Login.class));
                finish();

            }
        });



    }
}