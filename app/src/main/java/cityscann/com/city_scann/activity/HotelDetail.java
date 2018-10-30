package cityscann.com.city_scann.activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.model.HotelItem;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class HotelDetail extends AppCompatActivity implements View.OnClickListener {

    boolean isInternetPresent = false;
    private ConnectionDetector cd;
    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageView hotelDetailImage;
    TextView hotelDetailTital, hotelDetail;
    Button call,bookatable,showmenu,bar,bartable,inroomdinning,resturenttable,laundry,Keeping,Resturent,Bill;
    String titel, detail, hotelItemImage,phone2;
    ImageButton img1, img2, img3,img4;
    String time;
    int position;
    LinearLayout laybar;
    ImageView search;
    PreferencesClass preferencesClass=new PreferencesClass(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_detail);

        if( preferencesClass.getUserToken() != null ){

        }else {
            Log.d("Session",""+preferencesClass.getUserToken());
            Intent i = new Intent(HotelDetail.this,Licence_agreement.class);
            startActivity(i);
        }

        final List<HotelItem> hotelItemList = (ArrayList<HotelItem>) getIntent().getSerializableExtra("item");
        position = getIntent().getExtras().getInt("Positions");
        titel = hotelItemList.get(position).getHotelItemName();
        detail = hotelItemList.get(position).getHotelItemDescription();
        hotelItemImage = hotelItemList.get(position).getHotelItemImage2();
        phone2=hotelItemList.get(position).getHotelItemphone2();

        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent)
        {
            defineviewforitem();
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
                /*preferencesClass.setStateName(null);
                preferencesClass.setHid(null);*/
                Intent i = new Intent(HotelDetail.this, selection_hotel.class);
                startActivity(i);
                finish();
            }
        });
        toolbar=(android.support.v7.widget.Toolbar)findViewById(R.id.toolbar);
        toolbartitle=(TextView)findViewById(R.id.toolbar_title);
        toolbartitle.setText(titel);
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    onBackPressed();
            }
        });

        hotelDetailImage = (ImageView)findViewById(R.id.hotel_detail_image);
        hotelDetailTital = (TextView)findViewById(R.id.hotel_detail_tital);
        hotelDetail = (TextView)findViewById(R.id.hotel_item_detail);
        call=(Button)findViewById(R.id.call);
        bookatable=(Button)findViewById(R.id.bookatable);
        showmenu=(Button)findViewById(R.id.showmenu);
        laundry=(Button)findViewById(R.id.laundry);
        Keeping=(Button)findViewById(R.id.Keeping);
        Resturent=(Button)findViewById(R.id.Resturent);
        inroomdinning=(Button)findViewById(R.id.inroomdinning);
        resturenttable=(Button)findViewById(R.id.resturenttable);
        laybar=(LinearLayout)findViewById(R.id.laybar);
        bartable=(Button)findViewById(R.id.bartable);
        Bill=(Button)findViewById(R.id.Bill);
        bar=(Button)findViewById(R.id.bar);
        String checkOrigin = phone2;
        Log.d("checkOrigin","checkOrigin"+checkOrigin);


        if(phone2.equals("NO")){
            call.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            laundry.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            /*Breakfast.setVisibility(View.GONE);*/
            laybar.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
        }
        else if(phone2.equals("table"))
        {
            laundry.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            /*Breakfast.setVisibility(View.GONE);
            Lunch.setVisibility(View.GONE);*/
            inroomdinning.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            bookatable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   Intent intent =new Intent(HotelDetail.this,BookingResturent.class);
                    startActivity(intent);

                }
            });

            showmenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent =new Intent(HotelDetail.this,ShowMenu.class);
//                    startActivity(intent);
                    new ShowMenu(HotelDetail.this);

                }
            });

        }

        else if(phone2.equals("call"))
        {


            laundry.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(HotelDetail.this,Call_services.class);
                    startActivity(intent);
                    //new ShowMenu(HotelDetail.this);

                }
            });

        }

        else if(phone2.equals("Bill"))
        {
            Bill.setVisibility(View.VISIBLE);
            laundry.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            Bill.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(HotelDetail.this,PaymentOrderList.class);
                    startActivity(intent);
                    //new ShowMenu(HotelDetail.this);

                }
            });

        }

        else if(phone2.equals("Room3")||phone2.equals(null)||checkOrigin.equals(null)) {
            call.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            laundry.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            Keeping.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HotelDetail.this,house_keeping.class);
                    intent.putExtra("Cid","3");
                    startActivity(intent);
                }
            });


        }

        else if(phone2.equals("bar")||phone2.equals(null)||checkOrigin.equals(null)) {
            call.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            laundry.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            bartable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HotelDetail.this,BookingResturent.class);
                    intent.putExtra("Cid","2");
                    startActivity(intent);
                }
            });

            bar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HotelDetail.this,bar_category.class);
                    startActivity(intent);
                }
            });

        }

        else if(phone2.equals("Room2")||phone2.equals(null)||checkOrigin.equals(null)) {
            call.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            laundry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HotelDetail.this,Laundry_category.class);
                    intent.putExtra("Cid","2");
                    startActivity(intent);
                }
            });

        }

        else if(phone2.equals("resturent")||phone2.equals(null)||checkOrigin.equals(null)) {
            call.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            laundry.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            /*Intent intent=new Intent(HotelDetail.this,E_Resturent_category.class);
            intent.putExtra("Cid","4");
            startActivity(intent);*/

            Resturent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HotelDetail.this,E_Resturent_category.class);
                    intent.putExtra("Cid","4");
                    startActivity(intent);
                }
            });

            resturenttable.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(HotelDetail.this,BookingResturent.class);
                    intent.putExtra("Cid","4");
                    startActivity(intent);
                }
            });




        }

        else if(phone2.equals("room")||phone2.equals(null)||checkOrigin.equals(null))
        {
            laundry.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            Calendar c = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
            time=simpleDateFormat.format(c.getTime());
            Log.d("Currenttime", String.valueOf(time));
           /* Breakfast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(time.equals("10:01:00"))
                    {
                        Breakfast.setEnabled(false);
                        Toast.makeText(HotelDetail.this, "Sorry , Not Breakfast Time !!", Toast.LENGTH_LONG).show();

                    }
                    else{
                        Intent intent=new Intent(HotelDetail.this,E_menu_category.class);
                        intent.putExtra("Cid","1");
                        startActivity(intent);
                    }

                }
            });*/

           /* Lunch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(time.equals("15:01:00"))
                    {
                        Lunch.setEnabled(false);
                        Toast.makeText(HotelDetail.this, "Sorry , Not Lunch Time !!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent intent=new Intent(HotelDetail.this,E_menu_category.class);
                        intent.putExtra("Cid","2");
                        startActivity(intent);
                    }
                }
            });*/
            inroomdinning.setVisibility(View.VISIBLE);
            inroomdinning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(time.equals("23:01:00"))
                    {
                        inroomdinning.setEnabled(false);
                        Toast.makeText(HotelDetail.this, "Sorry , Not inroomdinning Time !!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent intent=new Intent(HotelDetail.this,E_menu_category.class);
                        intent.putExtra("Cid","1");
                        startActivity(intent);

                    }
                }
            });
            /*alldaymenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(time.equals("23:01:00"))
                    {
                        inroomdinning.setEnabled(false);
                        Toast.makeText(HotelDetail.this, "Sorry , Not inroomdinning Time !!", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Intent intent=new Intent(HotelDetail.this,E_menu_category.class);
                        intent.putExtra("Cid","4");
                        startActivity(intent);

                    }
                }
            });*/



        }

        else
        {
            bookatable.setVisibility(View.GONE);
            showmenu.setVisibility(View.GONE);
           /* Breakfast.setVisibility(View.GONE);
            Lunch.setVisibility(View.GONE);*/
            laundry.setVisibility(View.GONE);
            inroomdinning.setVisibility(View.GONE);
            laundry.setVisibility(View.GONE);
            laybar.setVisibility(View.GONE);
            resturenttable.setVisibility(View.GONE);
            Resturent.setVisibility(View.GONE);
            call.setVisibility(View.GONE);
            Keeping.setVisibility(View.GONE);
        }
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
      /*  img3 = (ImageButton) findViewById(R.id.img3);
        img4 = (ImageButton) findViewById(R.id.img4);*/
        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
        /*img3.setOnClickListener(this);
        img4.setOnClickListener(this);
*/
        hotelDetailTital.setText(titel);
        //call.setText(phone2);

       /* call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" +phone2));
                startActivity(callIntent);
            }
        });*/
        hotelDetail.setText(detail);
        Picasso.with(this).load(getResources().getString(R.string.ip_address_images) + hotelItemImage).into(hotelDetailImage);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(HotelDetail.this, selection_hotel.class);
                startActivity(intent);
                break;

            case R.id.img2:
                Intent category = new Intent(this, CategoryList.class);
                startActivity(category);
                break;

          /*  case R.id.img3:
                Intent search = new Intent(this, Search.class);
                startActivity(search);
                break;
            case R.id.img4:
                final Dialog dialog = new Dialog(HotelDetail.this);
                dialog.setContentView(R.layout.logout);
                dialog.show();

                break;*/
        }
    }
}
