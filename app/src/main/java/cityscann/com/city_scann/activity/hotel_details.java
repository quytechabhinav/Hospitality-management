package cityscann.com.city_scann.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;

import java.util.HashMap;

import cityscann.com.city_scann.R;
import cityscann.com.city_scann.utils.PreferencesClass;

public class hotel_details extends AppCompatActivity  {
    PreferencesClass preferencesClass=new PreferencesClass(this);
    SliderLayout sliderLayout;
    HashMap<String,String> Hash_file_maps ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hotel_details);
        sliderLayout =  (SliderLayout) findViewById(R.id.sup_img);
        if( preferencesClass.getUserToken() != null ){

        }else {
            Log.d("Session",""+preferencesClass.getUserToken());
            Intent i = new Intent(hotel_details.this,Licence_agreement.class);
            startActivity(i);
        }
       // Hash_file_maps = new HashMap<String, String>();

        HashMap<String, Integer> Hash_file_maps = new HashMap<>();
       // Hash_file_maps.put("My Hotel",R.drawable.myhotelotel);
        Hash_file_maps.put("Pool", Integer.valueOf(R.drawable.pool1));
        Hash_file_maps.put("Room", Integer.valueOf(R.drawable.room));
        Hash_file_maps.put("Restaurant", Integer.valueOf(R.drawable.resturent1));
        Hash_file_maps.put("", Integer.valueOf(R.drawable.night));
        for(String name : Hash_file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(hotel_details.this);
            textSliderView
                    .description(name)
                    .image(Hash_file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit);
            //.setOnSliderClickListener(this);
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);
            sliderLayout.addSlider(textSliderView);
        }
        sliderLayout.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        sliderLayout.setCustomAnimation(new DescriptionAnimation());
        sliderLayout.setDuration(3000);
    }
}
