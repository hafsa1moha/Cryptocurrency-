package com.example.cryptocurrency.Activitis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocurrency.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthActionCodeException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

public class SendOTPActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_otp);

        mAuth = FirebaseAuth.getInstance();

        final EditText inputEmail = findViewById(R.id.inputEmail);
        Button sendOTP = findViewById(R.id.sendOTP);

        sendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = inputEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(SendOTPActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                sendVerificationEmail(email);
            }
        });
    }

    private void sendVerificationEmail(String email) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            user.updateEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mAuth.sendSignInLinkToEmail(email, getActionCodeSettings())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SendOTPActivity.this, "Verification email sent", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(getApplicationContext(), EnterOTPActivity.class);
                                                    intent.putExtra("email", email);
                                                    startActivity(intent);
                                                } else {
                                                    Exception e = task.getException();
                                                    if (e instanceof FirebaseAuthActionCodeException) {
                                                        Toast.makeText(SendOTPActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                                                    } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
                                                        Toast.makeText(SendOTPActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(SendOTPActivity.this, "Failed to send verification email", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(SendOTPActivity.this, "Failed to update email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } else {
            Toast.makeText(SendOTPActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private ActionCodeSettings getActionCodeSettings() {
        return ActionCodeSettings.newBuilder()
                .setUrl("https://example.com/emailVerification")
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                        getPackageName(),
                        true, /* installIfNotAvailable */
                        "12" /* minimumVersion */)
                .build();
    }
}

