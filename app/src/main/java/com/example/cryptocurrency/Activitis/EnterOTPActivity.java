package com.example.cryptocurrency.Activitis;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocurrency.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class EnterOTPActivity extends AppCompatActivity {

    private EditText inputCode1, inputCode2, inputCode3, inputCode4, inputCode5, inputCode6;
    private Button verifyButton;
    private FirebaseAuth mAuth;
    private String verificationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        mAuth = FirebaseAuth.getInstance();

        TextView textMobile = findViewById(R.id.textMobile);
        textMobile.setText(String.format(
                "+968%s", getIntent().getStringExtra("mobile")
        ));

        inputCode1 = findViewById(R.id.inputCode1);
        inputCode2 = findViewById(R.id.inputCode2);
        inputCode3 = findViewById(R.id.inputCode3);
        inputCode4 = findViewById(R.id.inputCode4);
        inputCode5 = findViewById(R.id.inputCode5);
        inputCode6 = findViewById(R.id.inputCode6);
        verifyButton = findViewById(R.id.buttonVerify);

        setupOTPinputs();

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = inputCode1.getText().toString().trim() +
                        inputCode2.getText().toString().trim() +
                        inputCode3.getText().toString().trim() +
                        inputCode4.getText().toString().trim() +
                        inputCode5.getText().toString().trim() +
                        inputCode6.getText().toString().trim();
                if (!code.isEmpty()) {
                    verifyCode(code);
                } else {
                    Toast.makeText(EnterOTPActivity.this, "Please enter the OTP code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Get the verification ID from the intent
        verificationId = getIntent().getStringExtra("verificationId");
    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(EnterOTPActivity.this, "Verification successful", Toast.LENGTH_SHORT).show();
                            // Proceed to the next activity
                            // For example: startActivity(new Intent(EnterOTPActivity.this, HomeActivity.class));
                        } else {
                            // Sign in failed, display a message to the user.
                            Toast.makeText(EnterOTPActivity.this, "Verification failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void setupOTPinputs() {
        // Set up the logic for moving focus to the next EditText when the user inputs a digit
        // Similar to what you already have in your code
    }
}

