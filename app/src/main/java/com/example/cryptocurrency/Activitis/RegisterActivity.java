package com.example.cryptocurrency.Activitis;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cryptocurrency.Models.User;
import com.example.cryptocurrency.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

public class RegisterActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText, confirmPasswordEditText, phoneNumberEditText;
    private Button registerButton;
    private TextView loginButton;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private static final int KEY_SIZE = 256;
    private static final int IV_SIZE = 12;
    private static final int TAG_LENGTH = 128;
    private static final int ITERATION_COUNT = 65536;
    private static final int SALT_LENGTH = 16;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        initializeUI();

        registerButton.setOnClickListener(v -> registerUser());
        loginButton.setOnClickListener(v -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private void initializeUI() {
        usernameEditText = findViewById(R.id.editTextTextPersonName2);
        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword2);
        confirmPasswordEditText = findViewById(R.id.editTextTextPassword3);
        phoneNumberEditText = findViewById(R.id.editTextPhone);
        registerButton = findViewById(R.id.registerBtn);
        loginButton = findViewById(R.id.loginBtn);
    }

    private void registerUser() {
        final String username = usernameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
        final String phoneNumber = phoneNumberEditText.getText().toString().trim();

        if (isInputValid(username, email, password, confirmPassword, phoneNumber)) {
            if (isStrongPassword(password)) {
                checkUsernameAvailability(username, email, password, phoneNumber);
            } else {
                showToast("Please use a stronger password.");
            }
        }
    }

    private boolean isInputValid(String username, String email, String password, String confirmPassword, String phoneNumber) {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(phoneNumber)) {
            showToast("Please fill all the fields");
            return false;
        }

        if (!password.equals(confirmPassword)) {
            showToast("Passwords do not match");
            return false;
        }

        return true;
    }

    private boolean isStrongPassword(String password) {
        return password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*[a-z].*") &&
                password.matches(".*\\d.*") &&
                password.matches(".*[!@#$%^&*()-+=].*");
    }

    private void checkUsernameAvailability(String username, String email, String password, String phoneNumber) {
        mDatabase.child("usernames").child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    showToast("Username is already taken");
                } else {
                    createFirebaseUser(username, email, password, phoneNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                showToast("Database error: " + databaseError.getMessage());
            }
        });
    }

    private void createFirebaseUser(String username, String email, String password, String phoneNumber) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            sendVerificationEmail(user);
                            showToast("Please verify your email by clicking the verification link sent to your email address.");
                            registerButton.setEnabled(false);
                            saveUserData(user.getUid(), username, email, phoneNumber, password);
                        } else {
                            showToast("User is null");
                        }
                    } else {
                        showToast("Registration failed: " + task.getException().getMessage());
                    }
                });
    }

    private void sendVerificationEmail(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showToast("Verification email sent. Please check your inbox.");
                    } else {
                        showToast("Failed to send verification email: " + task.getException().getMessage());
                    }
                });
    }

    private void saveUserData(String userId, String username, String email, String phoneNumber, String password) {
        try {
            byte[] salt = generateSalt();
            SecretKey aesKey = deriveKey(password, salt);
            byte[] iv = generateIV();
            String encryptedPassword = encryptPassword(password, aesKey, iv);

            User user = new User(userId, username, email, phoneNumber, encryptedPassword, encodeBase64(salt), encodeBase64(iv));

            DatabaseReference usersRef = mDatabase.child("users").child(userId);
            usersRef.setValue(user)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("RegisterActivity", "User data saved successfully.");
                        } else {
                            Log.e("RegisterActivity", "Failed to save user data: " + task.getException().getMessage());
                        }
                    });

            DatabaseReference usernamesRef = mDatabase.child("usernames").child(username);
            usernamesRef.setValue(userId)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("RegisterActivity", "Username saved successfully.");
                        } else {
                            Log.e("RegisterActivity", "Failed to save username: " + task.getException().getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            showToast("Encryption error: " + e.getMessage());
        }
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[SALT_LENGTH];
        new SecureRandom().nextBytes(salt);
        return salt;
    }

    private SecretKey deriveKey(String password, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATION_COUNT, KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private byte[] generateIV() {
        byte[] iv = new byte[IV_SIZE];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    private String encryptPassword(String password, SecretKey aesKey, byte[] iv) throws GeneralSecurityException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        GCMParameterSpec parameterSpec = new GCMParameterSpec(TAG_LENGTH, iv);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey, parameterSpec);
        byte[] encryptedBytes = cipher.doFinal(password.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    private String encodeBase64(byte[] data) {
        return Base64.encodeToString(data, Base64.DEFAULT);
    }

    private void showToast(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
