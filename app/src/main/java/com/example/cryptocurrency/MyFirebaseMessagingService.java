package com.example.cryptocurrency;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle FCM messages here
    }

    @Override
    public void onNewToken(String token) {
        // Get updated InstanceID token.
        Log.d(TAG, "Refreshed token: " + token);

        // Send the token to your server or store it locally.
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        // Implement code to send the token to your server or store it locally.
        // This token can be used to send messages to this device or to manage device groups.
    }

}
