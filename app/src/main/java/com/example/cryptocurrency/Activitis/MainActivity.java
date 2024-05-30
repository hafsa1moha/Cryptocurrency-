package com.example.cryptocurrency.Activitis;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cryptocurrency.Adapter.CryptoWalletAdapter;
import com.example.cryptocurrency.Domain.CryptoWallet;
import com.example.cryptocurrency.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private ImageView walletImageView; // Add ImageView for wallet
    private TextView nameTextView;

    private static final int REQUEST_EDIT_PROFILE = 1;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // User is not signed in, handle this scenario
            return;
        }

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("usersProfileInf").child(currentUser.getUid());

        recyclerViewWallet();

        // Find wallet ImageView and name TextView
        walletImageView = findViewById(R.id.Wallet);
        nameTextView = findViewById(R.id.nameTextView);

        // Set click listener for wallet ImageView
        walletImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to WalletActivity when wallet ImageView is clicked
                Intent intent = new Intent(MainActivity.this, WalletActivity.class);
                startActivity(intent);
            }
        });

        ImageView personImageView = findViewById(R.id.Person);
        personImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivityForResult(intent, REQUEST_EDIT_PROFILE);
            }
        });

        ImageView Sittings = findViewById(R.id.Sittings);
        Sittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(MainActivity.this, SittingsActivity.class);
                startActivity(intent);
            }
        });

        // Listen for changes in user data
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("name").getValue(String.class);
                    nameTextView.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to fetch user data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void recyclerViewWallet(){
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView=findViewById(R.id.view);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<CryptoWallet> cryptoWalletArrayList=new ArrayList<>();
        ArrayList<Integer> lineData=new ArrayList<>();
        lineData.add(1000);
        lineData.add(1100);
        lineData.add(1200);
        lineData.add(1100);

        ArrayList<Integer> lineData2=new ArrayList<>();
        lineData2.add(2100);
        lineData2.add(2000);
        lineData2.add(1900);
        lineData2.add(2000);

        ArrayList<Integer> lineData3=new ArrayList<>();
        lineData3.add(900);
        lineData3.add(1100);
        lineData3.add(1200);
        lineData3.add(1000);
        lineData3.add(1150);

        cryptoWalletArrayList.add(new CryptoWallet("bitcoin","BTX",1234.12,2.13,lineData,1234.12,0.12345));
        cryptoWalletArrayList.add(new CryptoWallet("etherium","ETH",2134.21,-1.13,lineData2,6545.65,0.01245));
        cryptoWalletArrayList.add(new CryptoWallet("trox","ROX",6543.21,0.73,lineData3,31234.12,0.02154));

        adapter=new CryptoWalletAdapter(cryptoWalletArrayList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_PROFILE && resultCode == RESULT_OK) {
            if (data != null) {
                // Retrieve the updated name from the intent
                String updatedName = data.getStringExtra("name");
                // Update the nameTextView with the new name
                nameTextView.setText(updatedName);
            }
        }
    }
}
