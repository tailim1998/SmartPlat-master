package my.edu.tarc.user.smartplat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
        textViewPrice.setText("Price: RM " + String.format("%.2f",bundle.getDouble("price")));
        textViewLocation.setText("Location: " + bundle.getString("location"));
        image.setImageResource(bundle.getInt("image"));




        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int)(height*.8));

    }
}
