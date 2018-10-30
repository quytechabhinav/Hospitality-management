package cityscann.com.city_scann.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import cityscann.com.city_scann.R;
import cityscann.com.city_scann.activity.E_Resturent_category_item;
import cityscann.com.city_scann.activity.Menu_category_item;

import static cityscann.com.city_scann.R.id.img;

/**
 * Created by Praveen on 04-04-17.
 */

public class E_Resturent_category_Adopter extends RecyclerView.Adapter<E_Resturent_category_Adopter.ViewHolder> {

    private Context context;
    private ArrayList<String> names;
    private ArrayList<String> id;
    private ArrayList<String> image;
    private ArrayList<String> details;
    public E_Resturent_category_Adopter(Context context,ArrayList<String> names,ArrayList<String> categoryid,ArrayList<String>image,ArrayList<String>details) {
        this.context = context;
        this.names = names;
        this.id=categoryid;
        this.image=image;
        this.details=details;

    }

    @Override

    public E_Resturent_category_Adopter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.e__resturent_category__adopter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final E_Resturent_category_Adopter.ViewHolder viewHolder, int i) {

        viewHolder.textView.setText(names.get(i));
        Picasso.with(context).load("http://cityscann.in/media/"+image.get(i)).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                viewHolder.img.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                Log.d("Error","Image");
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView textView;
        public LinearLayout img;
        public ViewHolder(View view) {
            super(view);
            textView = (TextView)view.findViewById(R.id.textView);
            img = (LinearLayout)view.findViewById(R.id.Catimg);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, E_Resturent_category_item.class);
            intent.putExtra("URTEXT",id.get(getAdapterPosition()));
            intent.putExtra("DETAILS",details.get(getAdapterPosition()));
            intent.putExtra("BANNER",image.get(getAdapterPosition()));
            intent.putExtra("CATNAME",names.get(getAdapterPosition()));
            Log.d("id_abhinav",""+id.get(getAdapterPosition()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
    }
}
