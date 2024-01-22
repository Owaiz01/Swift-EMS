package com.example.swiftems_;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class All_Booking extends AppCompatActivity {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    RecyclerView productView;
    List<Mybookingmodel> busmodelList;
    Mybookingadapter busadapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_booking);
        database =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.allrec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ;

        busmodelList = new ArrayList<>();
        busadapter = new Mybookingadapter(getApplicationContext(),busmodelList);
        recyclerView.setAdapter(busadapter);



        SharedPreferences sharedPreferences = getSharedPreferences("User", MODE_PRIVATE);

        // Retrieve data from SharedPreferences
        String doc = sharedPreferences.getString("phone", "");



        //if (type != null && type.equalsIgnoreCase("Email")) {
        database.collection("Mybooking")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Mybookingmodel feedmodel = document.toObject(Mybookingmodel.class);
                                busmodelList.add(feedmodel);
                                // product_adapter.notifyDataSetChanged();
                                busadapter.notifyDataSetChanged();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });





    }
}