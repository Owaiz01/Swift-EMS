package com.example.swiftems_;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class profile extends Fragment {
    TextView name,email,number;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_profile, container, false);



        name=root.findViewById(R.id.username);
        email=root.findViewById(R.id.useremail);
        number=root.findViewById(R.id.usernumber);
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("User", MODE_PRIVATE);

        // Retrieve data from SharedPreferences
        String doc = sharedPreferences.getString("phone", "");


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").whereEqualTo("phone", doc)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Assuming "name" and "email" are fields in the document
                                String data = document.getString("name");
                                name.setText(data);

                                String useemail = document.getString("email");
                                email.setText(useemail);

                                String usenumber = document.getString("phone");
                                number.setText(usenumber);

                            }
                        } else {
                            // Handle the error
                        }
                    }
                });


        return root;


    }
}