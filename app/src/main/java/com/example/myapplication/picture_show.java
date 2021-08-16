package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.myapplication.define;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

public class picture_show extends AppCompatActivity {
    ImageButton btnNext, btnPrevious, btnBack;
    PhotoView picture;
    Integer numStep = 0;

    ViewPager mViewPager;

    // images array
//    int[] images = {R.drawable.r_step1, R.drawable.r_step2, R.drawable.r_step3, R.drawable.r_step4, R.drawable.r_step5,
//            R.drawable.r_step6, R.drawable.r_step7, R.drawable.r_step8, R.drawable.r_step9, R.drawable.r_step10,
//            R.drawable.r_step11, R.drawable.r_step12, R.drawable.r_step13, R.drawable.r_step14, R.drawable.r_step15,
//            R.drawable.r_step16, R.drawable.r_step17, R.drawable.r_step18, R.drawable.r_step19, R.drawable.r_step20,
//            R.drawable.r_step21, R.drawable.r_step22, R.drawable.r_step23, R.drawable.r_step24
//            R.drawable.r_step26, R.drawable.r_step27, R.drawable.r_step28, R.drawable.r_step29, R.drawable.r_step30,
//            R.drawable.r_step31, R.drawable.r_step32, R.drawable.r_step33, R.drawable.r_step34, R.drawable.r_step35,
//            R.drawable.r_step36, R.drawable.r_step37, R.drawable.r_step38, R.drawable.r_step39, R.drawable.r_step40,
//            R.drawable.r_step41, R.drawable.r_step42, R.drawable.r_step43, R.drawable.r_step44, R.drawable.r_step45,
//            R.drawable.r_step46, R.drawable.r_step47, R.drawable.r_step48, R.drawable.r_step49, R.drawable.r_step50
//    };

    // Creating Object of ViewPagerAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_show);
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }

        btnPrevious = (ImageButton)findViewById(R.id.btnPdfBack);
        btnNext = (ImageButton)findViewById(R.id.btnPdfNext);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        picture = (PhotoView) findViewById(R.id.pictureView);
//        picture.setImageResource(images[numStep]);
//        picture.setMaxZoom(4f);
//        setContentView(picture);
        //touchImageView img = (ImageView)findViewById(R.id.pictureView);

        // creating object of ViewPager

        // Initializing the ViewPager Object
//        mViewPager = (ViewPager)findViewById(R.id.pictureView);
        // Initializing the ViewPagerAdapter
//        mViewPagerAdapter = new ViewPagerAdapter(picture_show.this, images);
//        // Adding the Adapter to the ViewPager
//        mViewPager.setAdapter(mViewPagerAdapter);

        //touchImageView img = new touchImageView(this);
        //img.setImageResource(R.drawable.ice_age_2);



//        imageShow = new touchImageView(picture_show.this, images);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
//            private String getPackageName;

            @Override
            public void onClick(View v) {
//                numStep--;
//                if (numStep <= 0)
//                    numStep = 0;
//                picture.setImageResource(images[numStep]);
//                imageShow.setMaxZoom(4f);
//                setContentView(imageShow);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                numStep++;
//                if (numStep >= define.MAX_STEP_RANZER)
//                    numStep = define.MAX_STEP_RANZER;
//                picture.setImageResource(images[numStep]);
//                img.setMaxZoom(4f);
//                setContentView(img);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(picture_show.this, Mechanical.class));
            }
        });
    }
}