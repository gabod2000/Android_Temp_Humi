package io.terasyshub.screens;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.terasyshub.R;
import io.terasyshub.controllers.AuthController;
import io.terasyshub.models.Device;
import io.terasyshub.models.DeviceData;
import io.terasyshub.services.RestService;
import io.terasyshub.utils.TerasysAlert;

public class Home extends AppCompatActivity  {

    private AuthController authController;
    private RestService restService;

    private Spinner devicesSpinner;
    private LineChart tempChart,humChart;

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
        humChart = (LineChart)findViewById(R.id.humidityChart);
        tempChart = (LineChart)findViewById(R.id.temperatureChart);

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
            public void onResponse(final List<Device> devices) {
                ArrayAdapter<Device> adapter = new ArrayAdapter<Device>(getApplicationContext(),  R.layout.spinner, devices);
                adapter.setDropDownViewResource( R.layout.spinner_item);
                devicesSpinner.setAdapter(adapter);


                devicesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fetchDeviceData(devices.get(position).getMac());
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onError(String message) {
                TerasysAlert.show(message, false, Home.this);
            }

        });
    }

    private void fetchDeviceData(String deviceMac) {

        restService.getDeviceHumidityData(deviceMac, new RestService.DeviceDataRESTCallback() {
            @Override
            public void onResponse(List<DeviceData> dataList) {

                List<Entry> entries = new ArrayList<>();
                List<String> xAxisLabels = new ArrayList<>();

                for(int x=0; x<dataList.size(); x++){
                    DeviceData dataUnit = dataList.get(x);
                    entries.add(new BarEntry(x, dataUnit.getValue()));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm aa");
                    Date netDate = (new Date(dataUnit.getTimestamp()* 1000L));
                    xAxisLabels.add(sdf.format(netDate));
                }


                LineDataSet setComp1 = new LineDataSet(entries, "Humidity");
                setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                setComp1.setLineWidth(2f);
                setComp1.setColor(Color.BLUE);
                setComp1.setCircleColor(Color.BLUE);

                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(setComp1);

                LineData data = new LineData(dataSets);
                humChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
                humChart.getXAxis().setLabelRotationAngle(90);
                humChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                humChart.getXAxis().setLabelCount(xAxisLabels.size(), true);
                humChart.setData(data);
                humChart.getLegend().setEnabled(false);
                humChart.getDescription().setEnabled(false);
                humChart.invalidate();


            }

            @Override
            public void onError(String message) {
                TerasysAlert.show(message, false, Home.this);
            }
        });

        restService.getDeviceTemperatureData(deviceMac, new RestService.DeviceDataRESTCallback() {
            @Override
            public void onResponse(List<DeviceData> dataList) {

                List<Entry> entries = new ArrayList<>();
                List<String> xAxisLabels = new ArrayList<>();

                for(int x=0; x<dataList.size(); x++){
                    DeviceData dataUnit = dataList.get(x);
                    entries.add(new Entry(x, dataUnit.getValue()));

                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM hh:mm aa");
                    Date netDate = (new Date(dataUnit.getTimestamp()* 1000L));
                    xAxisLabels.add(sdf.format(netDate));
                }

                LineDataSet setComp1 = new LineDataSet(entries, "Temperature");
                setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
                setComp1.setLineWidth(2f);
                setComp1.setColor(Color.RED);
                setComp1.setCircleColor(Color.RED);

                List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                dataSets.add(setComp1);

                LineData data = new LineData(dataSets);
                tempChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
                tempChart.getXAxis().setLabelRotationAngle(90);
                tempChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                tempChart.getXAxis().setLabelCount(xAxisLabels.size(), true);
                tempChart.setData(data);
                tempChart.getLegend().setEnabled(false);
                tempChart.getDescription().setEnabled(false);
                tempChart.invalidate();
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
