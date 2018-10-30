package cityscann.com.city_scann.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;

/**
 * Created by Praveen on 06-04-17.
 */

public class GoogleReviewAdopter extends RecyclerView.Adapter<GoogleReviewAdopter.ViewHolder>  {

    private Context context;
    private ArrayList<String> Name;
    private ArrayList<String> Text;
    private ArrayList<String> Image;
    public ImageView reviewimage;
    PreferencesClass preferencesClass;
    public GoogleReviewAdopter(Context context,ArrayList<String> author_name,ArrayList<String> text,ArrayList<String> image) {
        this.context = context;
        this.Name = author_name;
        this.Text=text;
        this.Image=image;
        Log.d("helloName",""+author_name);

        preferencesClass=new PreferencesClass(context);
    }

    public GoogleReviewAdopter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.google_review_adopter, viewGroup, false);
        return new GoogleReviewAdopter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final GoogleReviewAdopter.ViewHolder holder, int i) {
        holder.textView.setText(Name.get(i));
        Picasso.with(context).load((Image.get(i)).toString().trim()).into(reviewimage);
        holder.detail.setText(Text.get(i));
    }

    @Override
    public int getItemCount() {

        return Name.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView, revrating, detail, tot, sub, add;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.txtname);
            reviewimage = (ImageView) view.findViewById(R.id.reviewimage);
            //revrating = (TextView) view.findViewById(R.id.revrating);
            detail = (TextView) view.findViewById(R.id.detail);
            /*sub=(TextView)view.findViewById(R.id.sub);
            add=(TextView)view.findViewById(R.id.add);
            view.setOnClickListener(this);*/

        }


    }
}
