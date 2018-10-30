package cityscann.com.city_scann.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.Distance;
import cityscann.com.city_scann.utils.GPSTracker;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder>{
    private Context mContext;
    private List<SupplierItem> listOfSupplier;
    GPSTracker gps;
    double latitude, longitude;
    Distance d;
    private ConnectionDetector cd;
    boolean isGPSPresent = false;

    public SearchAdapter(Context mContext,List<SupplierItem> listOfSupplier){
        this.mContext = mContext;
        this.listOfSupplier = listOfSupplier;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sup_img;
        TextView sup_name, sup_rate, sup_dist, sup_pri, catname, use_link;
        ImageButton linkBut;
        RelativeLayout rel;
        LinearLayout lay_dist, sup_phone, lay_rat, lay_use_link;

        public ViewHolder(View v) {
            super(v);
            sup_img = (ImageView) v.findViewById(R.id.sup_img);
            sup_name = (TextView) v.findViewById(R.id.sup_name);
            sup_rate = (TextView) v.findViewById(R.id.sup_rate);
            sup_dist = (TextView) v.findViewById(R.id.sup_dist);
            sup_pri = (TextView) v.findViewById(R.id.sup_pri);
            catname = (TextView) v.findViewById(R.id.cat_name);
            linkBut = (ImageButton)v.findViewById(R.id.link_button);
            sup_phone = (LinearLayout) v.findViewById(R.id.sup_phone);
            rel = (RelativeLayout) v.findViewById(R.id.rel);
            lay_dist = (LinearLayout) v.findViewById(R.id.sup_distance);
            lay_rat = (LinearLayout) v.findViewById(R.id.sup_rating);
            use_link = (TextView) v.findViewById(R.id.use_link);
            lay_use_link = (LinearLayout) v.findViewById(R.id.lay_use_link);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.search_result_recycleritem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final SupplierItem supplierItem = listOfSupplier.get(position);

        if(supplierItem.getCat_label().equals("cpn")) {
            holder.lay_rat.setVisibility(View.INVISIBLE);
            holder.lay_dist.setVisibility(View.INVISIBLE);
            holder.sup_phone.setVisibility(View.GONE);
            holder.sup_name.setText(supplierItem.getBrand());
        }else if (supplierItem.getCat_label().equals("sos") || supplierItem.getCat_label().equals("uslinks")){
            holder.sup_name.setText(supplierItem.getSup_name());
            holder.lay_rat.setVisibility(View.GONE);
            holder.lay_dist.setVisibility(View.GONE);
            if (!supplierItem.getSup_phone1().equals("")) {
                holder.sup_phone.setVisibility(View.VISIBLE);
                holder.sup_pri.setText(supplierItem.getSup_phone1());
            } if (supplierItem.getCat_label().equals("uslinks")){
                holder.lay_use_link.setVisibility(View.VISIBLE);
                holder.use_link.setText(supplierItem.getSup_link());
            }
        } else {
            if (supplierItem.getCat_label().equals("places")) {
                holder.lay_rat.setVisibility(View.GONE);
            }
            if (supplierItem.getSup_phone1().equals("") && Double.parseDouble(supplierItem.getSup_dist()) >= 5000) {
                holder.sup_phone.setVisibility(View.INVISIBLE);
            }
            if (!supplierItem.getSup_phone1().equals("")) {
                holder.sup_phone.setVisibility(View.VISIBLE);
                holder.sup_pri.setText(supplierItem.getSup_phone1());
            }
            holder.sup_name.setText(supplierItem.getSup_name());
            if (Double.parseDouble(supplierItem.getSup_dist()) >= 5000) {
                holder.lay_dist.setVisibility(View.GONE);
            } else {
                holder.sup_dist.setText(supplierItem.getSup_dist());
            }
            holder.sup_rate.setText(supplierItem.getSup_rate());
        }
        holder.catname.setText(supplierItem.getCat_name());
        holder.sup_img.setImageResource(R.drawable.untitled);
    }

    private boolean isGPSon() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }
        return false;
    }

    private float getDistanceInMeter(double lat1, double lng1) {
        gps = new GPSTracker(mContext);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, lat1, lng1, dist);
        return dist[0];
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Location");
        alertDialog.setMessage("Location is not enabled. Enable Now?");
        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    @Override
    public int getItemCount() {
        return listOfSupplier.size();
    }
}