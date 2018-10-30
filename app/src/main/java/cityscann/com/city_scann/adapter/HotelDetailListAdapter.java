package cityscann.com.city_scann.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.HotelItem;

public class HotelDetailListAdapter extends RecyclerView.Adapter<HotelDetailListAdapter.ViewHolder>{
    private static Context mContext;
    private List<HotelItem> hotelItemList;

    public HotelDetailListAdapter(Context mContext, List<HotelItem> hotelItemList){
        this.mContext = mContext;
        this.hotelItemList = hotelItemList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView img;
        private TextView myText;

        public ViewHolder(View v) {
            super(v);
            img = (ImageView)v.findViewById(R.id.img);
            final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
            img.startAnimation(myAnim);
            myText = (TextView)v.findViewById(R.id.text);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.hotel_item,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HotelItem hotelItem = hotelItemList.get(position);
        holder.myText.setText(hotelItem.getHotelItemName());
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.ip_address_images)+ hotelItem.getHotelItemImage()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return hotelItemList.size();
    }
}