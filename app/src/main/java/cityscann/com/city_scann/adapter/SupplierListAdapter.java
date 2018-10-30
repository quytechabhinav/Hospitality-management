package cityscann.com.city_scann.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.SupplierItem;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.Distance;
import cityscann.com.city_scann.utils.GPSTracker;

public class SupplierListAdapter extends RecyclerView.Adapter<SupplierListAdapter.ViewHolder>{
    private Context mContext;
    private List<SupplierItem> listOfSupplier;

    GPSTracker gps;
    double latitude, longitude;
    Distance d;
    private ConnectionDetector cd;
    boolean isGPSPresent = false;

    public SupplierListAdapter(Context mContext, List<SupplierItem> listOfSupplier){
        this.mContext = mContext;
        this.listOfSupplier = listOfSupplier;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        ImageView sup_img;
        ImageButton linkBut;
        TextView sup_name, sup_rate, sup_dist, sup_pri, desc, gap, gap2;
        RelativeLayout rel;
        LinearLayout lay_dist, sup_phone, lay_rat, image_lay, lay_disc;

        public ViewHolder(View v) {
            super(v);
            sup_img=(ImageView)v.findViewById(R.id.sup_img);
            sup_name=(TextView)v.findViewById(R.id.sup_name);
            sup_rate=(TextView)v.findViewById(R.id.sup_rate);
            sup_dist=(TextView)v.findViewById(R.id.sup_dist);
            sup_pri=(TextView)v.findViewById(R.id.sup_pri);
            desc = (TextView) v.findViewById(R.id.desc);
            gap = (TextView) v.findViewById(R.id.gap);
            gap2 = (TextView) v.findViewById(R.id.gap2);
            image_lay = (LinearLayout) v.findViewById(R.id.sup_img_lay);
            lay_disc = (LinearLayout) v.findViewById(R.id.lay_description);
            sup_phone = (LinearLayout) v.findViewById(R.id.sup_phone);
            linkBut = (ImageButton)v.findViewById(R.id.link_button);
            rel = (RelativeLayout) v.findViewById(R.id.rel);
            lay_dist = (LinearLayout) v.findViewById(R.id.sup_distance);
            lay_rat = (LinearLayout) v.findViewById(R.id.sup_rating);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.supplierlist_recycleritem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final SupplierItem supplierItem = listOfSupplier.get(position);

        if (supplierItem.getCat_label().equals("hotelitem")){
            holder.lay_rat.setVisibility(View.GONE);
            holder.lay_dist.setVisibility(View.GONE);
        }

        if (supplierItem.getCat_label().equals("google")){
            holder.lay_rat.setVisibility(View.GONE);
        }

        if(supplierItem.getCat_label().equals("cpn")){
            holder.lay_rat.setVisibility(View.INVISIBLE);
            holder.lay_dist.setVisibility(View.GONE);
            holder.image_lay.setVisibility(View.VISIBLE);
            holder.gap.setVisibility(View.VISIBLE);
            holder.sup_name.setTextSize(TypedValue.COMPLEX_UNIT_DIP,18);
            holder.gap2.setVisibility(View.VISIBLE);
            holder.sup_phone.setVisibility(View.GONE);
            holder.sup_name.setText(supplierItem.getBrand());
        }else {
            if (supplierItem.getCat_label().equals("places")) {
                holder.lay_rat.setVisibility(View.GONE);
                holder.image_lay.setVisibility(View.VISIBLE);
                holder.linkBut.setVisibility(View.GONE);
                holder.lay_disc.setVisibility(View.VISIBLE);
                String upToNCharacters = supplierItem.getAbt_sup().substring(0, Math.min(supplierItem.getAbt_sup().length(), 70)); //cat_detail.substring(0, Math.min(cat_detail.length(), 1));
                holder.desc.setText(upToNCharacters + "...");

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

        Picasso.with(mContext).load(mContext.getResources().getString(R.string.ip_address_images) + supplierItem.getSup_img()).into(holder.sup_img);
    }

    private void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

        alertDialog.setTitle("Location");

        alertDialog.setMessage("Location is not enabled. Enable Now?");

        alertDialog.setPositiveButton("Enable", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
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

    private boolean isGPSon() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            return true;
        }
        return false;
    }

    public float getDistanceInMeter(double lat2, double lng2) {

        gps = new GPSTracker(mContext);

        if(gps.canGetLocation()){
            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

        }else{
            gps.showSettingsAlert();
        }

        float [] dist = new float[1];
        Location.distanceBetween(latitude, longitude, lat2, lng2, dist);
        return dist[0];
    }

    @Override
    public int getItemCount() {
        return listOfSupplier.size();
    }
}
