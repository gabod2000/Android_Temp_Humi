package io.terasyshub.screens;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import io.terasyshub.R;
import io.terasyshub.controllers.AuthController;
import io.terasyshub.models.Device;
import io.terasyshub.services.RestService;
import io.terasyshub.utils.TerasysAlert;

public class Home extends AppCompatActivity  {

    private AuthController authController;
    private RestService restService;

    private Spinner devicesSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Casting
        devicesSpinner = (Spinner)findViewById(R.id.home_devices);

        //Initialize
        authController = AuthController.getInstance();
        restService  = RestService.getInstance(this);

        //Actions
        checkAuthentication();
    }

    private void checkAuthentication() {
        if(!authController.checkAuth()){
            startActivity(new Intent(this, Login.class));
            finish();
        } else {
            //Fetch Data
            fetchDevices();
        }
    }

    private void fetchDevices() {
        restService.getDevices(new RestService.DevicesRESTCallback() {
            @Override
            public void onResponse(List<Device> devices) {
                ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(getApplicationContext(),  android.R.layout.simple_spinner_dropdown_item, devices);
                adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
                devicesSpinner.setAdapter(adapter);
            }

            @Override
            public void onError(String message) {
                TerasysAlert.show(message, false, Home.this);
            }

        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            authController.logout();
            startActivity(new Intent(Home.this, Login.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
