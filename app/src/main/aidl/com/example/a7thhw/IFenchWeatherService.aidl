// IFenchWeatherService.aidl
package com.example.a7thhw;

// Declare any non-default types here with import statements
import com.loyid.weatherforecast.IFetchDataListener;

interface IFetchWeatherService {
    void retrieveWeatherData();
    void registerFetchDataListener(IFetchDataListener listener);
    void unregisterFetchDataListener(IFetchDataListener listener);
}
