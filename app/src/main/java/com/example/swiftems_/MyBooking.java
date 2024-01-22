package com.example.swiftems_;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MyBooking extends Fragment {
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    RecyclerView productView;
    List<Mybookingmodel> busmodelList;
    Mybookingadapter busadapter;
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_my_booking, container, false);

        database =FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        recyclerView=root.findViewById(R.id.allrec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ;

        busmodelList = new ArrayList<>();
        busadapter = new Mybookingadapter(getContext(),busmodelList);
        recyclerView.setAdapter(busadapter);



        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", MODE_PRIVATE);

        // Retrieve data from SharedPreferences
        String doc = sharedPreferences.getString("phone", "");



        //if (type != null && type.equalsIgnoreCase("Email")) {
        database.collection("Mybooking").whereEqualTo("userid",mAuth.getUid())
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
                            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                        }
                    }

                });




        return root;





    }
}