package cityscann.com.city_scann.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.HotelItem;


public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.ViewHolder>{

    private Context mContext;
    private List<HotelItem> hotelItemList;

    public RestaurantListAdapter(Context context, List<HotelItem> hotelItemList){
        this.hotelItemList = hotelItemList;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView nameText, catdetail;
        ImageView imgText;

        public ViewHolder(View v) {
            super(v);
            nameText = (TextView) v.findViewById(R.id.tv_android);
            imgText = (ImageView) v.findViewById(R.id.img_android);
        }
    }

    @Override
    public RestaurantListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.categoryl2_recycleritem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RestaurantListAdapter.ViewHolder holder, int position) {
        final HotelItem hotelItem = hotelItemList.get(position);
        holder.nameText.setText(hotelItem.getHotelItemName());
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.ip_address_images)+ hotelItem.getHotelItemImage()).into(holder.imgText);

    }

    @Override
    public int getItemCount() {
        return hotelItemList.size();
    }
}
