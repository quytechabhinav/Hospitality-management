package cityscann.com.city_scann.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;
import cityscann.com.city_scann.R;

public class CategoryL2Adapter extends RecyclerView.Adapter<CategoryL2Adapter.ViewHolder>{

    private Context mContext;
    private ArrayList<HashMap<String, String>> mDataSet;

    public CategoryL2Adapter(Context context,ArrayList<HashMap<String, String>> list){
        mDataSet = list;
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
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.categoryl2_circle_recycleritem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CategoryL2Adapter.ViewHolder holder, int position) {
        final HashMap<String, String> hm;
        hm = mDataSet.get(position);

        String name = hm.get("cat_name");
        String cat_image = hm.get("cat_image");
        String stringforlength = "Cityscann ...";
        Log.d("length", "" + stringforlength.length());
        holder.nameText.setText(name);
        Picasso.with(mContext).load(mContext.getResources().getString(R.string.ip_address_images)+ cat_image).into(holder.imgText);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
