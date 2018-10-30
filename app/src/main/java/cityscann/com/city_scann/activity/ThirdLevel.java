package cityscann.com.city_scann.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.ConnectionDetector;
import cityscann.com.city_scann.utils.PreferencesClass;

public class ThirdLevel extends AppCompatActivity {
    boolean isInternetPresent = false;
    PreferencesClass preferencesClass=new PreferencesClass(this);
     ImageButton img1, img2, img3 ;
    private ConnectionDetector cd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thirdlevel);
        img1 = (ImageButton) findViewById(R.id.img1);
        img2 = (ImageButton) findViewById(R.id.img2);
       /* img3 = (ImageButton) findViewById(R.id.img3);*/

    }

    public void onClick(View v) {
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {
            switch (v.getId()){
                case R.id.img2:
                    Intent category = new Intent(ThirdLevel.this, CategoryList.class);
                    startActivity(category);
                    break;
               /* case R.id.img3:
                    Intent search = new Intent(ThirdLevel.this, Search.class);
                    startActivity(search);
                    break;*/
                case R.id.img1:
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    break;
            }
        }
    }

}
