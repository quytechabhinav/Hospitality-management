package cityscann.com.city_scann.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;


public class Paymentcomment extends Activity {
    ImageView closed;
    PreferencesClass preferencesClass=new PreferencesClass(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.paymentcomment);

        closed=(ImageView)findViewById(R.id.closed);
        closed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
