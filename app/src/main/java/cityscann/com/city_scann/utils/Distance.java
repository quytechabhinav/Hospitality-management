package cityscann.com.city_scann.utils;

import android.content.Context;
import android.location.Location;

public class Distance {
    private Context _context;
    GPSTracker gps;
    double latitude;
    double longitude;

    public Distance(Context context){
        this._context = context;
    }

    public float getDistanceInMeter(double lat1, double lng1) {

        gps = new GPSTracker(_context);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, lat1, lng1, dist);
        return dist[0];
    }
}