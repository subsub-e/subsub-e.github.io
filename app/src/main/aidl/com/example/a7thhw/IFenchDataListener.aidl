// IFenchDataListener.aidl
package com.example.a7thhw;

// Declare any non-default types here with import statements

interface IFenchDataListener {
    void onWeatherDataRetrieved(out String[] data);
}
