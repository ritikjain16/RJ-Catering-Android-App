package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    private ImageView arrow_back;
    private TextView veryBad, bad, good, veryGood, excellent;
    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        arrow_back = findViewById(R.id.arrow_back);
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        veryBad = findViewById(R.id.very_bad);
        bad = findViewById(R.id.bad);
        good = findViewById(R.id.good);
        veryGood = findViewById(R.id.very_good);
        excellent = findViewById(R.id.excellent);

        loadingDialog = new Dialog(ReviewActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Just a moment...");


        Intent intent = getIntent();

        String oid = intent.getStringExtra("order_ID");


        veryBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReview("Very Bad",oid);
            }
        });

        bad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReview("Bad",oid);
            }
        });

        good.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReview("Good",oid);
            }
        });

        veryGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReview("Very Good",oid);
            }
        });

        excellent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReview("Excellent",oid);
            }
        });



    }

    private void setReview(String rText, String oid1) {

        loadingDialog.show();

        Map<String, Object> reviewMap = new HashMap<>();
        reviewMap.put("review_Ratings", rText);
        reviewMap.put("review_btn_visibility", (boolean) false);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_ORDERS")
                .document(oid1).update(reviewMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {

                    FirebaseFirestore.getInstance().collection("ORDERS")
                            .document(oid1).update(reviewMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        loadingDialog.dismiss();

                                        Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                                        DynamicToast.Config.getInstance()
                                                .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                                .setDefaultTintColor(getResources().getColor(R.color.white))
                                                .setToastBackground(toastBG)
                                                .apply();

                                        DynamicToast.make(ReviewActivity.this, "Thanks for Rating Us...", 1000).show();

                                        finish();

                                    } else {
                                        loadingDialog.dismiss();
                                        Toast.makeText(ReviewActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(ReviewActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}