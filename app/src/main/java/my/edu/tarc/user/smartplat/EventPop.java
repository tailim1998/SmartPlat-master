package my.edu.tarc.user.smartplat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.TooltipCompat;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class EventPop extends AppCompatActivity {
    ImageView image;
    TextView textViewTitle, textViewDesc, textViewDateTime, textViewVenue, textViewFee;
    Button JoinButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_pop);

        image = findViewById(R.id.image);
        textViewTitle = findViewById(R.id.title);
        textViewDesc = findViewById(R.id.desc);
        textViewDateTime = findViewById(R.id.datetime);
        textViewVenue = findViewById(R.id.venue);
        textViewFee = findViewById(R.id.fee);

        JoinButton=findViewById(R.id.button);

        Bundle bundle = getIntent().getExtras();
        textViewTitle.setText(bundle.getString("title"));
        textViewDesc.setText(bundle.getString("desc"));
        textViewDateTime.setText("Date & Time: " + bundle.getString("datetime"));
        textViewVenue.setText("Venue: " + bundle.getString("venue"));
        textViewFee.setText("Price: RM " + String.format("%.2f",bundle.getDouble("fee")));
        image.setImageResource(bundle.getInt("image"));

        JoinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFragment commentFragment = new CommentFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, commentFragment);
                fragmentTransaction.commit();
            }
        });

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
