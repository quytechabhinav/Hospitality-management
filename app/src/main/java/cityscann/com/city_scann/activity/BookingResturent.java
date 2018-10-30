package cityscann.com.city_scann.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;

import static cityscann.com.city_scann.R.id.date;


public class BookingResturent extends AppCompatActivity implements View.OnClickListener {

    android.support.v7.widget.Toolbar toolbar;
    TextView toolbartitle;
    ImageButton img1, img2, img3;
    int position;
    String mPhoneNumber;
    private Spinner typeSpinner,timeSlotSpinner,titleSpinner;
    private TextView textDate;
    String[] type1 = { "Select Time", "6:00 AM", "7:00 AM", "8:00 AM", "9:00 AM", "10:00 AM", "11:00 AM","12:00 PM", "1:00 PM", "2:00 PM","3:00 PM", "4:00 PM", "5:00 PM","6:00 PM", "7:00 PM","8:00 PM", "9:00 PM", "10:00 PM","11:00 PM", "12:00 AM","1:00 AM", "2:00 AM", "3:00 AM","4:00 AM", "5:00 AM",};
    DatePickerDialog datePickerDialog;
    Button btnbook;
    EditText Mobileno,Emailid,msg;
    TextView gsub,gadd,gnum,name,tablenu;
    String DATA_URL="request_hotel_book_a_table.php";
    int gastnum=0;
    ImageView search;
    PreferencesClass preferencesClass=new PreferencesClass(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.booking_resturent);
        defineviewforitem();
        typeSpinner = (Spinner) findViewById(R.id.type);
      /*  timeSlotSpinner = (Spinner)findViewById(R.id.time);*/
        /*titleSpinner = (Spinner)findViewById(R.id.title);*/
        name=(TextView)findViewById(R.id.name);
        textDate = (TextView) findViewById(date);
        btnbook=(Button)findViewById(R.id.btnbook);
        gsub = (TextView) findViewById(R.id.gsub);
        gadd = (TextView) findViewById(R.id.gadd);
        gnum = (TextView) findViewById(R.id.gnum);
        tablenu=(TextView)findViewById(R.id.tablenu);
        Mobileno=(EditText)findViewById(R.id.Mobileno);
        Mobileno.setEnabled(false);
        Emailid=(EditText)findViewById(R.id.Emailid);
        msg=(EditText)findViewById(R.id.msg);
        gnum.setText("0");
        name.setText("Hello "+preferencesClass.getusername()+",");

        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);
        mPhoneNumber = tMgr.getLine1Number();
        if(mPhoneNumber.equals(null) || mPhoneNumber.equals("")|| mPhoneNumber.equals(" ")) {
            Mobileno.setText("999999999");
        }
        else{
            Log.d("mPhoneNumber",mPhoneNumber);
            Mobileno.setText(mPhoneNumber);
        }



        //Title Spinner
       /* ArrayAdapter title = new ArrayAdapter(this,android.R.layout.simple_spinner_item,title1);
        title.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        titleSpinner.setAdapter(title);*/

        //Time SlotS
        ArrayAdapter type = new ArrayAdapter(this,android.R.layout.simple_spinner_item,type1);
        type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(type);



      /*  typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(typeSpinner.getSelectedItem().toString().equals("Select Type")){
                    timeSlotSpinner.setVisibility(View.GONE);
                }
                else  if(typeSpinner.getSelectedItem().toString().equals("Breakfast")){
                    timeSlotSpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter time = new ArrayAdapter(BookingResturent.this,android.R.layout.simple_spinner_item,breakfast);
                    time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    timeSlotSpinner.setAdapter(time);
                }
                else  if(typeSpinner.getSelectedItem().toString().equals("Lunch")){
                    timeSlotSpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter time = new ArrayAdapter(BookingResturent.this,android.R.layout.simple_spinner_item,lunch);
                    time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    timeSlotSpinner.setAdapter(time);
                }else  if(typeSpinner.getSelectedItem().toString().equals("Diner")){
                    timeSlotSpinner.setVisibility(View.VISIBLE);
                    ArrayAdapter time = new ArrayAdapter(BookingResturent.this,android.R.layout.simple_spinner_item,diner);
                    time.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    timeSlotSpinner.setAdapter(time);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        timeSlotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/

       /* titleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(typeSpinner.getSelectedItem().toString().equals("Select Title")){
                    Toast.makeText(getApplicationContext(),"Please Select Title", Toast.LENGTH_LONG).show();
                }
                else {

                    //fetchDistrict(state.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

*/


        textDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(BookingResturent.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                textDate.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();


            }
        });


        //btnbook click open Sucess msg
        btnbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bookatable();

               /* Toast.makeText(BookingResturent.this, "Your Table has be Booked ,Sucessfully !",
                        Toast.LENGTH_LONG).show();
                Intent intent=new Intent(BookingResturent.this,Home.class);
                startActivity(intent);*/
            }
        });


        gadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gastnum=gastnum+1;
                gnum.setText(""+gastnum);

            }
        });

        gsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gastnum=gastnum-1;
                gnum.setText(""+gastnum);
                if(gastnum<=0)
                {
                    gastnum=0;
                    gnum.setText("0");
                }

            }
        });


    }

    private void Bookatable() {
        String phone =Mobileno.getText().toString();
        String email =Emailid.getText().toString();
        String s_request =msg.getText().toString();
        String guest_no =gnum.getText().toString();
        String b_date =textDate.getText().toString();
        String b_time =typeSpinner.getSelectedItem().toString();
        b_time=b_time .replace(" ", "");


        final ProgressDialog loading = ProgressDialog.show(this, "Thank you",", Your selection is awesome..!",false,false);
       Log.d("BookATableurl",getResources().getString(R.string.ip_address)+DATA_URL+"?hid="+preferencesClass.getHid()+"&phone="+preferencesClass.getMobilenu()+"&email="+email+"&s_request="+s_request+"&b_date="+b_date+"&b_time="+b_time+"&guest_no="+guest_no);
        StringRequest stringRequest = new StringRequest(getResources().getString(R.string.ip_address)+DATA_URL+"?hid="+preferencesClass.getHid()+"&phone="+phone+"&email="+email+"&s_request="+s_request+"&b_date="+b_date+"&guest_no="+gastnum+"&b_time="+b_time, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("AddDataINDataBase",String.valueOf(response));
                loading.dismiss();
                if(response.contains("success")) {
                    if (response != null) {
                        Intent intent = new Intent(BookingResturent.this, Home.class);
                        startActivity(intent);
                        Toast.makeText(BookingResturent.this, "Table has been Booked ,Sucessfully !", Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        Toast.makeText(BookingResturent.this, "Try Again !!!",
                                Toast.LENGTH_LONG).show();

                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error","Error in log D");
                Toast.makeText(BookingResturent.this.getApplicationContext(),"failed",Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                               /* params.put("price", orderprice);
                                params.put("quentity", String.valueOf(c));
                                params.put("Tableid", session.getTableid());
                                params.put("Waiterid", session.getWaiterid());*/

                return params;
            }


        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);



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
                Intent i = new Intent(BookingResturent.this, selection_hotel.class);
                startActivity(i);
                finish();
            }
        });


        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbartitle = (TextView) findViewById(R.id.toolbar_title);
        toolbartitle.setText("Book a Table");
        toolbar.setNavigationIcon(R.drawable.leftarrowwhite);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
      /*  img3 = (ImageButton) findViewById(R.id.img3);*/

        img1.setOnClickListener(this);
        img2.setOnClickListener(this);
       /* img3.setOnClickListener(this);*/

    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img1:
                Intent intent = new Intent(BookingResturent.this, selection_hotel.class);
                startActivity(intent);
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
