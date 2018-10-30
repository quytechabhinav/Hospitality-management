package cityscann.com.city_scann.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import cityscann.com.city_scann.R;

public class UsefulLinkAdapter extends RecyclerView.Adapter<UsefulLinkAdapter.ViewHolder>{
    private Context mContext;
    private ArrayList<HashMap<String, String>> mDataSet;

    public UsefulLinkAdapter(Context context,ArrayList<HashMap<String, String>> list){
        mDataSet = list;
        mContext = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView link_name, ulink, sos1, sos2, phone2;
        LinearLayout sos;

        public ViewHolder(View v) {
            super(v);
            link_name =(TextView)v.findViewById(R.id.link_name);
            ulink = (TextView)v.findViewById(R.id.ulink);
            sos = (LinearLayout)v.findViewById(R.id.sos);
            sos1 = (TextView)v.findViewById(R.id.sos1);
            sos2 = (TextView)v.findViewById(R.id.sos2);
            phone2 = (TextView)v.findViewById(R.id.phone2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.useful_link_recycleritem,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final HashMap<String, String> hm;
        hm = mDataSet.get(position);

        String name = hm.get("abt_link");
        final String link = hm.get("link");
        final String contact_no1 = hm.get("contact_no1");
        final String contact_no2 = hm.get("contact_no2");
        String str1 = null;

        Log.d("linkkk",link);
        if(!(link.equals("null") || link.equals(""))){
            holder.ulink.setVisibility(View.VISIBLE);
            holder.sos.setVisibility(View.INVISIBLE);
        }

        if(!(contact_no1.equals("null") || contact_no1.equals(""))){
            holder.ulink.setVisibility(View.INVISIBLE);
            holder.sos.setVisibility(View.VISIBLE);
            if(contact_no2.equals("null")){
                holder.sos2.setVisibility(View.INVISIBLE);
            }
        }

        if (!(contact_no2.equals("null") || contact_no2.equals(""))){
            holder.phone2.setVisibility(View.VISIBLE);
        }
        holder.link_name.setText(name);

        holder.ulink.setPaintFlags(holder.ulink.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if (!(link.equals("") || link.equals("null") || link.equals(null) || link.equals(" "))) {
            String str = link;
            String lastchar = str.substring(str.length() -1, str.length());
            if (lastchar.equals("/")) {
               str1 = str.substring(0, str.length() - 1);
            }else {
                str1 = link;
            }
        }
        holder.ulink.setText(str1);

        holder.sos1.setPaintFlags(holder.sos1.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.sos1.setText(contact_no1);
        holder.sos2.setPaintFlags(holder.sos2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.sos2.setText(contact_no2);

        holder.ulink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = mDataSet.get(position);
                Uri uri = Uri.parse(link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.sos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = mDataSet.get(position);
                try {
                    String phone_no = contact_no1.replaceAll("-", "");
                    phone_no = phone_no.replaceAll(" ", "");
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    callIntent.setData(Uri.parse("tel:" + phone_no));
                    mContext.startActivity(callIntent);
                }catch (Exception e){
                }

            }
        });

        holder.sos2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> hashMap = mDataSet.get(position);

                String phone_no= contact_no2.replaceAll("-", "");
                phone_no = phone_no.replaceAll(" ","");
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                callIntent.setData(Uri.parse("tel:" + phone_no));
                mContext.startActivity(callIntent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
