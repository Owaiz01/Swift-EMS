package com.example.swiftems_;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Feed extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseUser auth;

    RecyclerView recyclerView;
    Feedadapter feedadapter;
    List<Feedmodel> feedmodelList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        db =FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance().getCurrentUser();
        recyclerView=findViewById(R.id.feeedrec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        LocalBroadcastManager.getInstance(getApplicationContext());
        //  .registerReceiver(mMessageReceiver,new IntentFilter("MyTotalAmount"));

        feedmodelList = new ArrayList<>();
        feedadapter = new Feedadapter(getApplicationContext(),feedmodelList);
        recyclerView.setAdapter(feedadapter);

        /*  db.collection("feedback")*//*.whereEqualTo("accptedBy","")*//**//*.whereEqualTo("sendedBy",auth.getEmail())*//*
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult() ){
                                Feedmodel feedmodel = document.toObject(Feedmodel.class);
                                feedmodelList.add(feedmodel);
                                // product_adapter.notifyDataSetChanged();
                                feedadapter.notifyDataSetChanged();
                            }
                        }else {
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
        db.collection("feedback")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Feedmodel feedmodel = document.toObject(Feedmodel.class);
                                feedmodelList.add(feedmodel);
                                // product_adapter.notifyDataSetChanged();
                                feedadapter.notifyDataSetChanged();
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });




    }
}