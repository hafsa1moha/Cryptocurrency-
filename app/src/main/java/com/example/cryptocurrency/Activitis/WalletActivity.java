package com.example.cryptocurrency.Activitis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.cryptocurrency.Adapter.CryptoWalletAdapter;
import com.example.cryptocurrency.Domain.CryptoWallet;
import com.example.cryptocurrency.R;
import com.google.firebase.FirebaseApp;

import java.util.ArrayList;

public class WalletActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CryptoWalletAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.view);

        // Set up RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Populate RecyclerView with data
        recyclerViewWallet();

        // Set OnClickListener for the ImageView
        ImageView homeImageView = findViewById(R.id.Home);
        homeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to MainActivity
                Intent intent = new Intent(WalletActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ImageView personImageView = findViewById(R.id.Person);
        personImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start MainActivity
                Intent intent = new Intent(WalletActivity.this, ProfileActivity.class);
                startActivity(intent);
                // Finish the current activity to prevent going back to it when pressing back button from MainActivity
                finish();
            }
        });

        ImageView Sittings = findViewById(R.id.Sittings);
        Sittings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to ProfileActivity
                Intent intent = new Intent(WalletActivity.this, SittingsActivity.class);
                startActivity(intent);
            }
        });
    }


    private void recyclerViewWallet() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<CryptoWallet> cryptoWalletArrayList = new ArrayList<>();
        ArrayList<Integer> lineData = new ArrayList<>();
        lineData.add(1000);
        lineData.add(1100);
        lineData.add(1200);
        lineData.add(1100);

        ArrayList<Integer> lineData2 = new ArrayList<>();
        lineData2.add(2100);
        lineData2.add(2000);
        lineData2.add(1900);
        lineData2.add(2000);

        ArrayList<Integer> lineData3 = new ArrayList<>();
        lineData3.add(900);
        lineData3.add(1100);
        lineData3.add(1200);
        lineData3.add(1000);
        lineData3.add(1150);

        cryptoWalletArrayList.add(new CryptoWallet("bitcoin", "BTX", 1234.12, 2.13, lineData, 1234.12, 0.12345));
        cryptoWalletArrayList.add(new CryptoWallet("etherium", "ETH", 2134.21, -1.13, lineData2, 6545.65, 0.01245));
        cryptoWalletArrayList.add(new CryptoWallet("trox", "ROX", 6543.21, 0.73, lineData3, 31234.12, 0.02154));

        adapter = new CryptoWalletAdapter(cryptoWalletArrayList);
        recyclerView.setAdapter(adapter);
    }
}
