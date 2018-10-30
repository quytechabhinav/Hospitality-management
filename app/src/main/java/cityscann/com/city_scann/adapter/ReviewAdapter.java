package cityscann.com.city_scann.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cityscann.com.city_scann.R;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<HashMap<String, String>> mDataSet;

    public ReviewAdapter(Context context, ArrayList<HashMap<String, String>> list){
        mDataSet = list;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView rev_sub, sup_rate, rev_det;

        public ViewHolder(View v) {
            super(v);
            rev_sub=(TextView)v.findViewById(R.id.rev_sub);
            sup_rate=(TextView)v.findViewById(R.id.sup_rate);
            rev_det=(TextView)v.findViewById(R.id.rev_det);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.reviews_recycleritem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final HashMap<String, String> hm;
        hm = mDataSet.get(position);

        String rate = hm.get("rat");
        String heading = hm.get("rev_head");
        String detail = hm.get("rev_sub");

        holder.rev_sub.setText(heading);
        holder.sup_rate.setText(rate);
        holder.rev_det.setText(detail);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
