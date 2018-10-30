package cityscann.com.city_scann.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class ConnectionDetector {
    private Context _context;

    public ConnectionDetector(Context context){
        this._context = context;
    }

    public boolean isConnectingToInternet(){
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {

                return true;
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                return true;
            }
        } else {
            Toast.makeText(_context, "Please enable your internet connection", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public boolean isGPSon(){
        LocationManager locationManager = (LocationManager) _context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        } else if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            return true;
        }
        return false;
    }

}
