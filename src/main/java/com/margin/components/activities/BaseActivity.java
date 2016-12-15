package com.margin.components.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.margin.components.utils.AndroidVersionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

/**
 * Created on Feb 19, 2016.
 *
 * @author Marta.Ginosyan
 */
public abstract class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = BaseActivity.class.getSimpleName();

    private static final int PERMISSION_REQUEST_ACCESS_LOCATION = 11;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    /**
     * Do some actions when user has declined location permission
     */
    protected abstract void onLocationPermissionDeclined();

    /**
     * Do some actions when user has granted location permission
     */
    protected abstract void onLocationPermissionGranted();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /**
     * App has been connected to Google Play services and the location services API.
     * The next step is checking location permissions and getting last known location.
     */
    @Override
    public void onConnected(Bundle bundle) {
        checkPermissionsAndSetLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_ACCESS_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // We can now safely use the API we requested access to
                    checkPermissionsAndSetLocation();
                    onLocationPermissionGranted();
                } else {
                    onLocationPermissionDeclined();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "connection was suspended with cause #" + cause);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, connectionResult.toString());
    }

    /**
     * @return last known {@link Location} object.
     */
    public Location getLastKnownLocation() {
        return mLastLocation;
    }

    /**
     * Check location permissions and get last known location.
     */
    private void checkPermissionsAndSetLocation() {
        if (AndroidVersionUtils.isHigherEqualToMarshmallow()) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                // check permissions now
                requestPermissions(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }, PERMISSION_REQUEST_ACCESS_LOCATION
                );
                return;
            }
        }
        // permission has been granted, continue as usual
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }
}
