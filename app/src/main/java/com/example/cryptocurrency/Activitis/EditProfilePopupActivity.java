package com.example.cryptocurrency.Activitis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.cryptocurrency.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfilePopupActivity extends AppCompatActivity {

    private EditText emailEditText;
    private EditText nameEditText;
    private Spinner countrySpinner;
    private Button saveButton;

    // Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_popup);

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("usersProfileInf").child(mAuth.getCurrentUser().getUid());

        emailEditText = findViewById(R.id.editTextTextEmailAddress2);
        nameEditText = findViewById(R.id.nameEditText);
        countrySpinner = findViewById(R.id.countrySpinner);
        saveButton = findViewById(R.id.saveButton);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String name = nameEditText.getText().toString();
                String country = countrySpinner.getSelectedItem().toString();

                // Save user data to Realtime Database
                saveUserData(email, name, country);
            }
        });
    }

    private void saveUserData(String email, String name, String country) {
        String userId = mAuth.getCurrentUser().getUid();

        // Create a map to store user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("userId", userId);
        userData.put("email", email);
        userData.put("name", name);
        userData.put("country", country);

        // Save the user data to Firebase Realtime Database under "usersProfileInf" node
        mDatabaseReference.setValue(userData)
                .addOnSuccessListener(task -> {
                    Toast.makeText(EditProfilePopupActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    // Set result and finish activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedName", name);
                    resultIntent.putExtra("updatedEmail", email);
                    resultIntent.putExtra("updatedCountry", country);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(EditProfilePopupActivity.this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
