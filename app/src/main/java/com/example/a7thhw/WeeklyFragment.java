package com.example.a7thhw;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;



public class WeeklyFragment extends Fragment {

    private Handler handler;

    private TextView cityField;
    private TextView updatedField;

    private ImageView[] weatherIcon;
    private TextView[] detailsField;
    private TextView[] currentTemperatureField;

    private OnFragmentInteractionListener mListener;

    public WeeklyFragment() {
        handler = new Handler();
        weatherIcon = new ImageView[7];
        detailsField = new TextView[7];
        currentTemperatureField = new TextView[7];
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        updateWeatherData(new CityPreference(getActivity()).getCity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weekly, container, false);

        cityField = (TextView) rootView.findViewById(R.id.city_field);
        updatedField = (TextView) rootView.findViewById(R.id.updated_field);

        for(int i=0; i<7; i++) {
            switch (i) {
                case 0: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon0);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field0);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field0);
                    break;
                }
                case 1: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon1);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field1);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field1);
                    break;
                }
                case 2: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon2);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field2);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field2);
                    break;
                }
                case 3: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon3);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field3);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field3);
                    break;
                }
                case 4: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon4);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field4);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field4);
                    break;
                }
                case 5: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon5);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field5);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field5);
                    break;
                }
                case 6: {
                    weatherIcon[i] = (ImageView) rootView.findViewById(R.id.weather_icon6);
                    detailsField[i] = (TextView) rootView.findViewById(R.id.details_field6);
                    currentTemperatureField[i] = (TextView) rootView.findViewById(R.id.current_temperature_field6);
                    break;
                }
            }
        }

        return rootView;
    }

    public void changeCity(String city) {
        updateWeatherData(city);
    }

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = FetchWeeklyWeather.getJSON(getActivity(), city);
                if(json == null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), getActivity().getText(R.string.place_not_found),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
                else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    private void renderWeather(JSONObject json) {
        try {
            cityField.setText(json.getJSONObject("city").getString("name").toUpperCase(Locale.US)
                    + ", "
                    + json.getJSONObject("city").getString("country"));

            DateFormat df = DateFormat.getDateTimeInstance();
            String updatedOn = df.format(new Date(json.getJSONArray("list").getJSONObject(0).getLong("dt")*1000));
            updatedField.setText("Last update: " + updatedOn);


            JSONArray list = json.getJSONArray("list");
            for(int i=0; i<7; i++) {

                JSONObject obj = list.getJSONObject(i);
                JSONObject weather = obj.getJSONArray("weather").getJSONObject(0);

                detailsField[i].setText("   " +
                        weather.getString("description").toUpperCase(Locale.US) +
                        "\n" + "   Humidity: " + obj.getString("humidity") + "%" +
                        "\n" + "   Pressure: " + obj.getString("pressure") + " hPa");

                currentTemperatureField[i].setText(String.format("       %.2f", obj.getJSONObject("temp").getDouble("day"))+ " ℃");

                String iconCode = weather.getString("icon");
                String tmpURL = "http://openweathermap.org/img/w/" + iconCode + ".png";
                new DownloadImageTask(weatherIcon[i]).execute(tmpURL);
            }

        } catch (Exception e) {
            Log.e("WeeklyWeather", "One or more fields not found in the JSON data");
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onWeeklyFragmentInteraction();
    }
}