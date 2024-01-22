package com.example.swiftems_;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class feedback extends Fragment {


    EditText name,email,feedback;
    Button submit;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_feedback, container, false);

        name=root.findViewById(R.id.fname);
        email=root.findViewById(R.id.femail);
        feedback=root.findViewById(R.id.ffeedback);
        submit=root.findViewById(R.id.fsubmit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fname = name.getText().toString();
                String femail = email.getText().toString();
                String feed = feedback.getText().toString();

                // Create a new user with a first and last name
                Map<String, Object> user = new HashMap<>();
                user.put("name", fname);
                user.put("email", femail);
                user.put("feedback", feed);

// Add a new document with a generated ID
                db.collection("feedback")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                Toast.makeText(getContext(), "Thank You", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error adding document", e);
                            }
                        });



            }
        });



        return root;




    }
}