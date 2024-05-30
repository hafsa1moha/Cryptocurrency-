package com.example.cryptocurrency.Domain;

import java.util.ArrayList;

public class CryptoWallet {
    private String cryptoNme;
    private String cryptoSymbol;
    private Double cryptoPrice;
    private  Double changePercent;
    private ArrayList<Integer> lineData;
    private Double propertyAmount;
    private Double propertySize;

    public CryptoWallet(String cryptoNme, String cryptoSymbol, Double cryptoPrice, Double changePercent, ArrayList<Integer> lineData, Double propertyAmount, Double propertySize) {
        this.cryptoNme = cryptoNme;
        this.cryptoSymbol = cryptoSymbol;
        this.cryptoPrice = cryptoPrice;
        this.changePercent = changePercent;
        this.lineData = lineData;
        this.propertyAmount = propertyAmount;
        this.propertySize = propertySize;
    }

    public String getCryptoNme() {
        return cryptoNme;
    }

    public void setCryptoNme(String cryptoNme) {
        this.cryptoNme = cryptoNme;
    }

    public String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public Double getCryptoPrice() {
        return cryptoPrice;
    }

    public void setCryptoPrice(Double cryptoPrice) {
        this.cryptoPrice = cryptoPrice;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public ArrayList<Integer> getLineData() {
        return lineData;
    }

    public void setLineData(ArrayList<Integer> lineData) {
        this.lineData = lineData;
    }

    public Double getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(Double propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public Double getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(Double propertySize) {
        this.propertySize = propertySize;
    }
}
