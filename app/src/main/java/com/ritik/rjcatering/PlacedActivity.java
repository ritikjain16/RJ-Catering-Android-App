package com.ritik.rjcatering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ritik.rjcatering.fragments.CartFragment;

public class PlacedActivity extends AppCompatActivity {

    private ImageView arrow_back;
    private Button view_orders;
    private ImageView delivery_image;
    private CardView delivery_card;

    public static boolean ORDER_DETAILS;

    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placed);
        arrow_back = findViewById(R.id.arrow_back);
        view_orders=findViewById(R.id.view_orders);
        delivery_image=findViewById(R.id.delivery_image);
        delivery_card=findViewById(R.id.delivery_card);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up_row);

        delivery_card.setAnimation(animation);


        Glide.with(this).load(R.drawable.g1).into(delivery_image);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        view_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ORDER_DETAILS=true;
                startActivity(new Intent(PlacedActivity.this,MainActivity.class));
                finish();
            }
        });

    }
}