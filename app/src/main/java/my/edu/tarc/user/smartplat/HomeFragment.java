package my.edu.tarc.user.smartplat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ViewFlipper;

public class HomeFragment extends Fragment {
    private View view;
    private ViewFlipper viewFlipper;
    private ImageView btnimg1, btnimg2, btnimg3, btnimg4;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_fragment, container, false);

        int images[] = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3};
        viewFlipper = view.findViewById(R.id.viewflipper);
        btnimg1 = view.findViewById(R.id.btnimg1);
        btnimg2 = view.findViewById(R.id.btnimg2);
        btnimg3 = view.findViewById(R.id.btnimg3);
        btnimg4 = view.findViewById(R.id.btnimg4);

        for (int image : images) {
            flipperImages(image);
        }

        btnimg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    BusinessFragment businessFragment = new BusinessFragment();
            //    FragmentManager fragmentManager = getFragmentManager();
            //    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //    fragmentTransaction.replace(R.id.fragment_container, businessFragment);
            //    fragmentTransaction.commit();

                CommentFragment commentFragment = new CommentFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, commentFragment);
                fragmentTransaction.commit();
            }
        });

        btnimg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceFragment serviceFragment = new ServiceFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, serviceFragment);
                fragmentTransaction.commit();
            }
        });

        btnimg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventFragment eventFragment = new EventFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, eventFragment);
                fragmentTransaction.commit();
            }
        });

        btnimg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductFragment productFragment = new ProductFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, productFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    public void flipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(4000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }
}
