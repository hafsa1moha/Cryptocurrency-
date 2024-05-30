package com.example.cryptocurrency.Domain;

import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class UserProfile {
    private ImageView profileImageView;
    private TextView nameTextView;
    private Spinner countrySpinner;
    private String name;
    private String country;

    public UserProfile(ImageView profileImageView, TextView nameTextView, Spinner countrySpinner, String name, String country) {
        this.profileImageView = profileImageView;
        this.nameTextView = nameTextView;
        this.countrySpinner = countrySpinner;
        this.name = name;
        this.country = country;
    }

    public ImageView getProfileImageView() {
        return profileImageView;
    }

    public TextView getNameTextView() {
        return nameTextView;
    }

    public Spinner getCountrySpinner() {
        return countrySpinner;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }
}
