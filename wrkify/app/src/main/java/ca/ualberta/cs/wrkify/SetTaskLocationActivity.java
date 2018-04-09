/*
 * Copyright 2018 CMPUT301W18T18
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package ca.ualberta.cs.wrkify;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;

public class SetTaskLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private GoogleMap mMap;
    private CameraPosition cameraPosition;
    private LatLng defaultLocation = new LatLng(53.509,-113.5);
//    private LatLng defaultTestLocation = new LatLng(53.50)
    private Location lastKnownLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int DEFAULT_ZOOM = 14;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    public static final String TASK_EXTRA = "task";
    public static final String LOCATION_EXTRA = "location_extra";
    private static int RESULT_LOCATION_SAVED = 25;
    private TaskLocation taskLocation = new TaskLocation(defaultLocation.latitude,defaultLocation.longitude);
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        setContentView(R.layout.activity_maps);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
//        if(this.getIntent().getSerializableExtra(TASK_EXTRA)==null) {
            inflater.inflate(R.menu.set_task_location_menu, menu);
//        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.saveLocation:
                save();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void save(){
        Intent intent = new Intent(this,EditTaskActivity.class);
        intent.putExtra(LOCATION_EXTRA,taskLocation);
        Log.d("location --->",Double.valueOf(taskLocation.getLatitude()).toString());
        setResult(RESULT_LOCATION_SAVED,intent);
        finish();
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(this);
        Intent intent = this.getIntent();
        Task task = (Task)intent.getSerializableExtra(TASK_EXTRA);
        marker = mMap.addMarker(new MarkerOptions().position(defaultLocation));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
               marker.setPosition(latLng);
               taskLocation = new TaskLocation(latLng.latitude,latLng.longitude);
            }
        });
//        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(defaultLocation.latitude,defaultLocation.longitude)));
//        marker.setDraggable(true);


        if(task!=null){
            LatLng coordinates = new LatLng(task.getLocation().getLatitude(),task.getLocation().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinates,DEFAULT_ZOOM));
           marker.setPosition(coordinates);
           taskLocation = new TaskLocation(coordinates.latitude,coordinates.longitude);
           Log.d("--->","non null task set loc");
            return;
        }
        else {
            getLocationPermission();
            Log.d("Permission acquired--->","yay");
            getCurrentLocation();
            Log.d("LOCATION ACQUIRED--->","yes");
            updateLocationUI();
            Log.d("UI UPDATED --->","yes");
            Log.d("ON MARKER DRAG SET--->","yes");
            if(lastKnownLocation!=null){
                Log.d("lastKnownLocation--->","not null");
               marker.setPosition(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
               taskLocation = new TaskLocation(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());
                Log.d("MARKER ADDED--->","yes");
                return;
            }
            Log.d("lastKnownLocation--->","null");
            marker.setPosition(defaultLocation);
            taskLocation = new TaskLocation(defaultLocation.latitude,defaultLocation.longitude);
        }

    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    public void getCurrentLocation(){
        try {
            if (mLocationPermissionGranted) {
                com.google.android.gms.tasks.Task<Location> locationResult = mFusedLocationClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<Location> task) {
                        lastKnownLocation = task.getResult();
                        if (task.isSuccessful()&&lastKnownLocation!=null) {
//                            Log.d("Location nonNull-->","yea");
                            // Set the map's camera position to the current location of the devices
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(lastKnownLocation.getLatitude(),
                                            lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                          marker.setPosition(new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude()));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                           marker.setPosition(defaultLocation);
                        }
                    }
                });
            }
            else{
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation,DEFAULT_ZOOM));
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                mMap.setOnMarkerDragListener(this);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setOnMarkerDragListener(this);
//                lastKnownLocation.setLatitude(defaultLocation.latitude);
//                lastKnownLocation.setLongitude(defaultLocation.longitude);
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
        Log.d("DRAG START--->","yes");
    }

    @Override
    public void onMarkerDrag(Marker marker) {
        Log.d("DRAG --->","yes");
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        Log.d("DRAG END--->","yes");
        mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        taskLocation = new TaskLocation(marker.getPosition().latitude,marker.getPosition().longitude);
    }
}
