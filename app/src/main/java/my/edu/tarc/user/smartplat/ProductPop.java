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

public class ProductPop extends AppCompatActivity {

    ImageView image;
    TextView textViewTitle, textViewDesc, textViewPrice, textViewLocation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_pop);


        image = findViewById(R.id.image);
        textViewTitle = findViewById(R.id.title);
        textViewDesc = findViewById(R.id.desc);
        textViewPrice = findViewById(R.id.price);
        textViewLocation = findViewById(R.id.location);


        Bundle bundle = getIntent().getExtras();
        textViewTitle.setText(bundle.getString("title"));
        textViewDesc.setText(bundle.getString("desc"));
        textViewPrice.setText("Price: " + bundle.getString("price"));
        textViewLocation.setText("Location: " + bundle.getString("location"));
        image.setImageResource(bundle.getInt("image"));



       // Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.id.imageViewBusiness);
        //RoundedBitmapDrawable roundedBitmapDrawable =RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        //roundedBitmapDrawable.setCircular(true);
       // image.setImageDrawable(roundedBitmapDrawable);



        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.8));

        image.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                image.setColorFilter(Color.RED);
                return false;
            }
        });
    }
}
