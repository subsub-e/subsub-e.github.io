package com.example.a7thhw;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity implements WeeklyFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new CurrentFragment(), "CURRENT")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        getMenuInflater().inflate(R.menu.spinner_menu, menu);
        MenuItem item = menu.findItem(R.id.spinner);
        final Spinner spinner = (Spinner) MenuItemCompat.getActionView(item);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_menu_item, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if("Current".equalsIgnoreCase(item.toString())) {
                    CurrentFragment newFragment = new CurrentFragment();

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.container, newFragment, "CURRENT");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
                else if("Weekly".equalsIgnoreCase(item.toString())) {
                    WeeklyFragment newFragment = new WeeklyFragment();

                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                    transaction.replace(R.id.container, newFragment, "WEEKLY");
                    transaction.addToBackStack(null);
                    transaction.commit();
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.change_city){
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Write the name of your city");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }

    public void changeCity(String city){
        Fragment myFragment = getSupportFragmentManager().findFragmentByTag("WEEKLY");

        if(myFragment != null && myFragment.isVisible()) {
            WeeklyFragment weeklyFragment = (WeeklyFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            weeklyFragment.changeCity(city);
        }
        else {
            CurrentFragment currentFragment = (CurrentFragment)getSupportFragmentManager()
                    .findFragmentById(R.id.container);
            currentFragment.changeCity(city);
        }
        new CityPreference(this).setCity(city);
    }

    @Override
    public void onWeeklyFragmentInteraction() {

        WeeklyFragment weeklyFrag = (WeeklyFragment)
                getSupportFragmentManager().findFragmentById(R.id.container);

        if (weeklyFrag == null) {
            WeeklyFragment newFragment = new WeeklyFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}