package my.edu.tarc.user.smartplat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class BusinessPop extends AppCompatActivity {
    ImageView image;
    TextView textViewTitle, textViewDesc, textViewOperationTime, textViewVenue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_pop);


        image = findViewById(R.id.image);
        textViewTitle = findViewById(R.id.title);
        textViewDesc = findViewById(R.id.desc);
        textViewOperationTime = findViewById(R.id.operationTime);
        textViewVenue = findViewById(R.id.venue);


        Bundle bundle = getIntent().getExtras();
        textViewTitle.setText(bundle.getString("title"));
        textViewDesc.setText(bundle.getString("desc"));
        textViewOperationTime.setText("Operation Time: " + bundle.getString("operationTime"));
        textViewVenue.setText("Venue: " + bundle.getString("venue"));
        image.setImageResource(bundle.getInt("image"));


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.8));
    }
}
