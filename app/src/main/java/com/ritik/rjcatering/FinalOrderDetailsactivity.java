package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ritik.rjcatering.adapter.FoodAdapter;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

public class FinalOrderDetailsactivity extends AppCompatActivity {

    private ImageView arrow_back;

    private TextView final_order_id;
    private TextView final_order_status;
    private TextView final_order_date;
    private TextView final_order_price;
    private TextView final_order_discount_price;
    private TextView final_order_delivery_price;
    private TextView final_order_total_price;
    private TextView final_order_name;
    private TextView final_order_address;
    private TextView final_order_phone;
    private TextView final_order_payment_ID;
    private TextView final_order_payment_method;

    private RecyclerView final_recyclerView;

    private Dialog loadingDialog;
    private Button review_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_order_detailsactivity);

        arrow_back = findViewById(R.id.arrow_back);
        final_order_id = findViewById(R.id.final_order_id);
        final_order_status = findViewById(R.id.final_order_status);
        final_order_date = findViewById(R.id.final_order_date);
        final_order_price = findViewById(R.id.final_price);
        final_order_discount_price = findViewById(R.id.final_discount_price);
        final_order_delivery_price = findViewById(R.id.final_delivery_price);
        final_order_total_price = findViewById(R.id.final_total_price);
        final_order_name = findViewById(R.id.final_name);
        final_order_address = findViewById(R.id.final_address);
        final_order_phone = findViewById(R.id.final_phone);
        final_recyclerView = findViewById(R.id.final_recyclerView);
        final_order_payment_ID = findViewById(R.id.final_order_payment_ID);
        final_order_payment_method = findViewById(R.id.final_order_payment_method);
        review_btn = findViewById(R.id.review_btn);


        loadingDialog = new Dialog(FinalOrderDetailsactivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Just a moment...");


        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();

        String order_id = intent.getStringExtra("order_ID");

        List<FoodItemModel> foodItemModelList = new ArrayList<>();
        FoodAdapter foodAdapter = new FoodAdapter(FinalOrderDetailsactivity.this, foodItemModelList);


        loadingDialog.show();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_ORDERS")
                .document(order_id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            final_order_id.setText(task.getResult().getString("order_id"));
                            final_order_status.setText(task.getResult().getString("order_status"));
                            final_order_date.setText(task.getResult().getString("order_date"));
                            final_order_price.setText(task.getResult().getString("order_original_price"));
                            final_order_discount_price.setText(task.getResult().getString("order_discount_price"));
                            final_order_delivery_price.setText(task.getResult().getString("order_delivery_price"));
                            final_order_total_price.setText(task.getResult().getString("order_final_price"));
                            final_order_name.setText(task.getResult().getString("user_name"));
                            final_order_address.setText(task.getResult().getString("user_address"));
                            final_order_phone.setText(task.getResult().getString("user_phone"));

                            final_order_payment_method.setText(task.getResult().getString("payment_method"));
                            if (task.getResult().getString("payment_method").equals("COD")) {
                                final_order_payment_ID.setVisibility(View.GONE);
                            } else {
                                final_order_payment_ID.setVisibility(View.VISIBLE);
                                final_order_payment_ID.setText(task.getResult().getString("payment_ID"));
                            }

                            if (task.getResult().getString("order_status").equals("Delivered") ||
                                    task.getResult().getString("order_status").equals("delivered")) {

                                if (task.getResult().getBoolean("review_btn_visibility")) {
                                    review_btn.setVisibility(View.VISIBLE);
                                    review_btn.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            startActivity(new Intent(FinalOrderDetailsactivity.this, ReviewActivity.class).putExtra("order_ID", order_id));
//                                            startActivity(new Intent(FinalOrderDetailsactivity.this, EditReviewActivity.class).putExtra("order_ID", order_id));
                                            finish();
                                        }
                                    });
                                } else {
                                    review_btn.setVisibility(View.GONE);
                                }
                            }

                            long size = task.getResult().getLong("order_item_list_size");

                            foodItemModelList.clear();

                            for (long i = 1; i <= size; i++) {
                                foodItemModelList.add(new FoodItemModel(
                                        task.getResult().getString("order_food_item_id_" + i),
                                        task.getResult().getString("order_food_image_" + i),
                                        task.getResult().getString("order_food_name_" + i),
                                        task.getResult().getString("order_food_price_" + i)
                                ));
                            }


                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FinalOrderDetailsactivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            final_recyclerView.setLayoutManager(linearLayoutManager);
                            final_recyclerView.setAdapter(foodAdapter);
                            foodAdapter.notifyDataSetChanged();

                            loadingDialog.dismiss();


                        } else {
                            loadingDialog.dismiss();
                            Toast.makeText(FinalOrderDetailsactivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}