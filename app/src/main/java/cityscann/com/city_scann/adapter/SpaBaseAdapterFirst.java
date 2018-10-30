package cityscann.com.city_scann.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.activity.SPA_main;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Praveen on 12-04-17.
 */

public class SpaBaseAdapterFirst extends BaseAdapter {

    private Context context;
    private final String[] spaName;
    private final String[] spaImage;
    private final String[] spalogo;
    private final String[] description;
    private final String[]id;
    public SpaBaseAdapterFirst(Activity context, String[] name, String[] image, String[] logo, String[] description,String[] id){
        this.context = context;
        this.spaName = name;
        this.spaImage = image;
        this.spalogo = logo;
        this.description = description;
        this.id=id;


    }


    @Override
    public int getCount() {
        return spaName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.spabasicadapter,null);
        ImageView image =(ImageView)view.findViewById(R.id.ImageCircleView);
        /*ImageView logo = (ImageView)view.findViewById(R.id.logo);*/
        TextView name = (TextView)view.findViewById(R.id.name);

        name.setText(spaName[i]);
        Picasso.with(context)
                .load("http://cityscann.in/media/"+spaImage[i]).into(image);

        /*Picasso.with(context)
                .load("http://cityscann.in/media/hotel/"+spaImage[i]).into(image);*/
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(context,SPA_main.class);
                intent.putExtra("SpaName", spaName[i]);
                intent.putExtra("description", description[i]);
                intent.putExtra("bannerImage", spalogo[i]);
                intent.putExtra("id", id[i]);
                context.startActivity(intent);

            }
        });

        return view;
    }
}
