package com.example.cryptocurrency.Activitis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptocurrency.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    private ImageView editImageView;
    private TextView nameTextView;
    private TextView countryTextView;
    private TextView emailTextView;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private DatabaseReference mDatabaseReference;

    // Variables to store current name and country
    private String currentName = "";
    private String currentCountry = "";
    private String currentEmail = "";

    private static final int REQUEST_EDIT_PROFILE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, handle this scenario
            return;
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("usersProfileInf").child(currentUser.getUid());

        nameTextView = findViewById(R.id.nameTextView);
        countryTextView = findViewById(R.id.countryTextView);
        emailTextView = findViewById(R.id.emailTextView);
        editImageView = findViewById(R.id.editImageView); // Initialize editImageView

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String country = dataSnapshot.child("country").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);

                    // Check if name and country have changed before updating UI
                    if (name != null && !name.equals(currentName)) {
                        currentName = name;
                        nameTextView.setText(name);
                    }

                    if (country != null && !country.equals(currentCountry)) {
                        currentCountry = country;
                        countryTextView.setText(country);
                    }

                    if (email != null && !email.equals(currentEmail)) {
                        currentEmail = email;
                        emailTextView.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        editImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(ProfileActivity.this, EditProfilePopupActivity.class), REQUEST_EDIT_PROFILE);
            }
        });

        // Navigate to other activities
        ImageView walletImageView = findViewById(R.id.Wallet);
        ImageView homeImageView = findViewById(R.id.Home);
        ImageView settingsImageView = findViewById(R.id.Sittings);
        TextView logoutBtnTextView = findViewById(R.id.logoutBtn);

        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, WalletActivity.class));
            }
        });

        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            }
        });

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SittingsActivity.class));
            }
        });

        logoutBtnTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(ProfileActivity.this, IntroActivity.class));
                finish(); // Finish current activity to prevent user from coming back using back button
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            if (data != null) {
                // Retrieve the updated name, email, and country from the intent
                String updatedName = data.getStringExtra("updatedName");
                String updatedEmail = data.getStringExtra("updatedEmail");
                String updatedCountry = data.getStringExtra("updatedCountry");

                // Update the UI with the updated data
                nameTextView.setText(updatedName);
                emailTextView.setText(updatedEmail);
                countryTextView.setText(updatedCountry);

                // Update the current values
                currentName = updatedName;
                currentEmail = updatedEmail;
                currentCountry = updatedCountry;
            }
        }
    }
}
