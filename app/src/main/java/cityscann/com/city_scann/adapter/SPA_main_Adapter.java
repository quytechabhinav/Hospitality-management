package cityscann.com.city_scann.adapter;

import android.app.Activity;
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
import cityscann.com.city_scann.R;
import cityscann.com.city_scann.activity.Treatment_package;

/**
 * Created by Praveen on 12-04-17.
 */

public class SPA_main_Adapter extends RecyclerView.Adapter<SPA_main_Adapter.ViewHolder> {

    private Context context;
    private ArrayList<String> TRE_Itemname;
    private ArrayList<String> TRE_IMG;
    private ArrayList<String> TRE_ID;
    public ImageView treimg;


    public SPA_main_Adapter(Context context,ArrayList<String> names,ArrayList<String> categoryid,ArrayList<String> image) {
        this.context = context;
        this.TRE_Itemname = names;
        this.TRE_IMG=image;
        this.TRE_ID=categoryid;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView txtname;

        public ViewHolder(View view) {
            super(view);
            txtname = (TextView) view.findViewById(R.id.tre_name);
            treimg = (ImageView) view.findViewById(R.id.tre_img);
            view.setOnClickListener(this);
            
        }

        @Override
        public void onClick(View v) {
           String ID=TRE_ID.get(getAdapterPosition());
            new Treatment_package((Activity) context,ID);
            //intent.putExtra("ID",);
           /* Intent intent=new Intent(context,Treatment_package.class);
            intent.putExtra("ID",TRE_ID.get(getAdapterPosition()));
            context.startActivity(intent);*/

        }
    }


    @Override
    public SPA_main_Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.spa_treatment_grid, viewGroup, false);
        return new SPA_main_Adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SPA_main_Adapter.ViewHolder holder, int i) {
        holder.txtname.setText(TRE_Itemname.get(i));
        Picasso.with(context)
                .load("http://cityscann.in/media/spa/"+TRE_IMG.get(i))
                .into(treimg);
        //holder.treimg.setText(TRE_IMG.get(i));
        Log.d("ImageURL","http://cityscann.in/media/spa/"+TRE_IMG.get(i));

    }

    @Override
    public int getItemCount() {
        return TRE_Itemname.size();
    }
}
