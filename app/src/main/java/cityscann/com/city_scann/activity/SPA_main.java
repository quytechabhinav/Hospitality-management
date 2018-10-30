package cityscann.com.city_scann.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;

public class SPA_main extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager mLayoutManager;
    private Context context;
    public static final String TAG_NAME = "treatment_name";
    public static final String DATA_URL = "http://cityscann.in/app/request_spa_treatment_cat.php";
    public static final String IMAGE = "treatment_image1";
    LinearLayout Opencart;
    ArrayList prgmName;
    private RelativeLayout mRelativeLayout;
    private ArrayList<String> names;
    private ArrayList<String> categoryid;
    private ArrayList<String> image;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2, img3;
    String ID,SPANAME,DESCRIPTION,BANNERIMAGE,LOGO;
    Button showmenu;
    ImageView bannerImage,search;
    TextView spaName,about;
    PreferencesClass preferencesClass=new PreferencesClass(this);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spa_main);
        defineviewforitem();

        Bundle bundle = getIntent().getExtras();
        ID = bundle.getString("id");
        SPANAME = bundle.getString("SpaName");
        DESCRIPTION = bundle.getString("description");
        BANNERIMAGE = bundle.getString("bannerImage");
        bannerImage = (ImageView)findViewById(R.id.bannerImage);
        spaName = (TextView)findViewById(R.id.spaName);
        about = (TextView)findViewById(R.id.about);
        showmenu=(Button)findViewById(R.id.showmenu);

        spaName.setText(SPANAME);
        about.setText(DESCRIPTION);
        Picasso.with(this)
                .load("http://cityscann.in/media/"+BANNERIMAGE)
                .into(bannerImage);


         names = new ArrayList<String>();
        categoryid= new ArrayList<String>();
        image= new ArrayList<String>();

        mRelativeLayout = (RelativeLayout) findViewById(R.id.e_menu_category);
        layoutManager = new LinearLayoutManager(SPA_main.this);

        mLayoutManager = new LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
        );

        showmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SPA_main.this,Spa_category.class);
                intent.putExtra("Cid",ID);
                Log.d("JsonUrl",ID);
                startActivity(intent);

            }
        });

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
                Intent i = new Intent(SPA_main.this, Licence_agreement.class);
                startActivity(i);
                finish();
            }
        });

        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText(SPANAME);
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);*/

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent home = new Intent(this, Home.class);
                startActivity(home);
                break;

            case R.id.img2:
                Intent category = new Intent(this, CategoryList.class);
                startActivity(category);
                break;

           /* case R.id.img3:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                break;*/
        }
    }

}

