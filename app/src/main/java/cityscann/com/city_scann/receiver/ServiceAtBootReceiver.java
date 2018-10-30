package cityscann.com.city_scann.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import cityscann.com.city_scann.activity.Home;

/**
 * Created by shuser on 20-01-2017.
 */

public class ServiceAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            Intent serviceIntent = new Intent(context, Home.class);
            serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(serviceIntent);
        }
    }
}
