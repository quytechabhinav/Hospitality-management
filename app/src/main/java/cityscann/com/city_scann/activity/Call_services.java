package cityscann.com.city_scann.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;

import static cityscann.com.city_scann.R.id;
import static cityscann.com.city_scann.R.layout;

public class Call_services extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout spa1,laundry,restaurant,bar,pool,housekeeping,roomtoroom;
    private String spaPhone="8307109210";
    private String laundryPhone="8750258995";
    private String restaurantPhone="8750258995";
    private String barPhone="8750258995";
    private String poolPhone="8750258995";
    private String housekeepingPhone="8750258995";
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2 ;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.call_services);
        defineviewforitem();
        spa1 = (LinearLayout)findViewById(id.spa);
        laundry = (LinearLayout)findViewById(id.laundry);
        restaurant = (LinearLayout)findViewById(id.restaurant);
        pool = (LinearLayout)findViewById(id.pool);
        housekeeping = (LinearLayout)findViewById(id.housekeeping);
        roomtoroom=(LinearLayout)findViewById(id.roomtoroom);
        spa1.setOnClickListener(this);
        laundry.setOnClickListener(this);
        restaurant.setOnClickListener(this);
        roomtoroom.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.spa:
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" +spaPhone));
                startActivity(callIntent);
                break;
            case R.id.laundry:
                Intent callIntent1 = new Intent(Intent.ACTION_CALL);
                callIntent1.setData(Uri.parse("tel:" +laundryPhone));
                startActivity(callIntent1);
                break;
            case R.id.restaurant:
                Intent callIntent2 = new Intent(Intent.ACTION_CALL);
                callIntent2.setData(Uri.parse("tel:" +restaurantPhone));
                startActivity(callIntent2);
                break;
            case R.id.bar:
                Intent callIntent3 = new Intent(Intent.ACTION_CALL);
                callIntent3.setData(Uri.parse("tel:" +barPhone));
                startActivity(callIntent3);
                break;
            case R.id.pool:
                Intent callIntent4 = new Intent(Intent.ACTION_CALL);
                callIntent4.setData(Uri.parse("tel:" +poolPhone));
                startActivity(callIntent4);
                break;
            case R.id.housekeeping:
                Intent callIntent5 = new Intent(Intent.ACTION_CALL);
                callIntent5.setData(Uri.parse("tel:" +housekeepingPhone));
                startActivity(callIntent5);
                break;
            case R.id.roomtoroom:
                Intent roomtoroom = new Intent(Call_services.this,RoomtoRoom.class);
                startActivity(roomtoroom);
                break;



            //Code of footer click and call activity
            case R.id.img1:
                Intent intent = new Intent(Call_services.this, selection_hotel.class);
                startActivity(intent);
                break;

            case R.id.img2:
                Intent category = new Intent(this, CategoryList.class);
                startActivity(category);
                break;

        }
    }


    private void defineviewforitem() {
        search=(ImageView)findViewById(R.id.logout);
        search.setVisibility(View.VISIBLE);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preferencesClass.setUserToken(null);
                preferencesClass.setusername(null);
                preferencesClass.setphone(null);
                preferencesClass.setuser_email(null);
                Intent i = new Intent(Call_services.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Call Service");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);

    }



}
