package com.example.obdandroid.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Iterator;

/**
 * 作者：Jealous
 * 日期：2021/1/29
 * 描述：
 */
public class LocationHelper {
    private static final String TAG = "Car_LocationHelper";
    public static final int MSG_GPS_SIGNAL = 115;
    public static final int MSG_GPS_SPEED = 116;

    private static LocationHelper mLocationHelper;
    private LocationManager mLocationManager;
    private Context mContext;
    private Double longitude_last_time = 0.0;
    private Double latitude_last_time = 0.0;
    private Double longitude_last_location = 0.0;
    private Double latitude_last_location = 0.0;
    private float speed_last = 0;
    private int satellite_count = 0;
    private int minTime = 1000;
    private int minDistance = 1;
    private static final double EARTH_RADIUS = 6378137.0;
    private double distance;

    private LocationHelper(Context context) {
        this.mContext = context;
    }


    private final GpsStatus.Listener statusListener = new GpsStatus.Listener() {
        public void onGpsStatusChanged(int event) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            GpsStatus status = mLocationManager.getGpsStatus(null);
            Log.i(TAG, "onGpsStatusChanged   MaxSatellites=" + status.getMaxSatellites());
            if (status != null) {
                updateGpsStatus(event, status);
            } else {
                Log.i(TAG, "onGpsStatusChanged   status=null");
            }
        }
    };

    /**
     * @param event
     * @param status
     */
    private void updateGpsStatus(int event, GpsStatus status) {
        if (status == null) {
        } else if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator it = status.getSatellites().iterator();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = (GpsSatellite) it.next();
                if (s.getSnr() >= 35) {
                    count++;
                    Log.i(TAG, "updateGpsStatus   snr=" + s.getSnr());
                }
            }

            satellite_count = count;
            Log.i(TAG, "updateGpsStatus: satellite_count=" + satellite_count);
//            CarApplication.getInstance().notifyListener(MSG_GPS_SIGNAL, satellite_count);

            if (satellite_count == 0) {
               // AppManager.getInstance().notifyListener(MSG_GPS_SPEED, 0);
            }

        }
    }

    public static LocationHelper getInstance(Context context) {
        if (mLocationHelper == null) {
            mLocationHelper = new LocationHelper(context);
        }
        return mLocationHelper;
    }

    public boolean requestLocationUpdate() {
        Log.i(TAG, "requestLocationUpdate: ");
        LocationManager locationManager = getLocationManager();
        if (locationManager == null) {
            return false;
        }
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        Log.i(TAG, "requestLocationUpdate bestProvider:" + bestProvider);
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, mLocationListener);
        } catch (SecurityException e) {
            Log.e(TAG, "requestLocationUpdate: error=" + e.toString());
            e.printStackTrace();
        }
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.e(TAG, "requestLocationUpdate: gps disable");
            return false;
        }

        return true;
    }

    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        criteria.setSpeedRequired(true);
        criteria.setBearingRequired(false);
        criteria.setAltitudeRequired(true);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        return criteria;
    }

    public Location getLocation() {
        LocationManager locationManager = getLocationManager();
        if (locationManager == null) {
            return null;
        }
        String bestProvider = locationManager.getBestProvider(getCriteria(), true);
        Location loc = null;
        try {
            loc = locationManager.getLastKnownLocation(bestProvider);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return loc;
    }

    private LocationManager getLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (mLocationListener == null) {
                Log.e(TAG, "requestLocationUpdate: create LocationManager intance fail");
            }
        }
        return mLocationManager;
    }

    public void releaseLocation() {
        Log.d(TAG, "releaseLocation: ");
        if (mLocationManager != null) {
            try {
                mLocationManager.removeUpdates(mLocationListener);
                mLocationManager.removeGpsStatusListener(statusListener);
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        mLocationManager = null;
        Log.d(TAG, "releaseLocation: success");
    }


    public static double getDistance(double longitude1, double latitude1, double longitude2, double latitude2) {
        double Lat1 = rad(latitude1);
        double Lat2 = rad(latitude2);
        double a = Lat1 - Lat2;
        double b = rad(longitude1) - rad(longitude2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(Lat1) * Math.cos(Lat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private LocationListener mLocationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            mLocationManager.addGpsStatusListener(statusListener);
            Log.i(TAG, "onLocationChanged: satellite_count=" + satellite_count);

            Log.i(TAG, "onLocationChanged: float speed=" + location.getSpeed());
            int speed = (int) (location.getSpeed() * 3.6);// m/s ---> km/h
            Log.i(TAG, "onLocationChanged: speed=" + speed);
            Log.i(TAG, "onLocationChanged: satellite_count=" + satellite_count);

  //          CarApplication.getInstance().notifyListener(MSG_GPS_SPEED, speed);
            if (satellite_count >= 1) {
                //当有3颗15强度的卫星时，才显示速度
               // AppManager.getInstance().notifyListener(MSG_GPS_SPEED, speed);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d(TAG, "gps status: available");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d(TAG, "gps status: out of service");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d(TAG, "gps status: temporarily unavailable");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "GPS onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "GPS onProviderDisabled");
        }
    };
}