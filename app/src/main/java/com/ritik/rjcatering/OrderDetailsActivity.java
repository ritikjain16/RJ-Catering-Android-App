package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.ritik.rjcatering.fragments.CartFragment;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OrderDetailsActivity extends AppCompatActivity implements PaymentResultListener {

    private ImageView arrow_back;
    private TextView price, discount, totalPrice, name, address, phone, order_delivery_price;
    private String price1, discount1, totalPrice1, name1, flat1, street1, pincode1, state1, phone1;


    private Calendar c;
    private SimpleDateFormat df;
    private String date, order_id1;
    private Random rand;
    private int min = 10000000, max = 99999999;
    private int randomNum;
    private Dialog loadingDialog;
    private double originalPrice;
    private double discountPrice, deliveryPrice;
    private double totalFinalPrice;
    private String addrees1;

    private TextView cod_btn,online_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        arrow_back = findViewById(R.id.arrow_back);
        price = findViewById(R.id.order_price);
        discount = findViewById(R.id.order_discount_price);
        totalPrice = findViewById(R.id.order_total_price);
        name = findViewById(R.id.order_name);
        address = findViewById(R.id.order_address);
        phone = findViewById(R.id.order_phone);
        order_delivery_price = findViewById(R.id.order_delivery_price);
        cod_btn=findViewById(R.id.cod_btn);
        online_btn=findViewById(R.id.online_btn);


        loadingDialog = new Dialog(OrderDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Placing, just a moment...");


        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();

        name1 = intent.getStringExtra("name");
        flat1 = intent.getStringExtra("flat");
        street1 = intent.getStringExtra("street");
        pincode1 = intent.getStringExtra("pincode");
        phone1 = intent.getStringExtra("phone");
        state1 = intent.getStringExtra("state");

        price1 = intent.getStringExtra("totalAmount");

//        Toast.makeText(this, price1, Toast.LENGTH_SHORT).show();

        name.setText(name1);
        address.setText(flat1 + ", " + street1 + ", " + state1 + ", " + pincode1);
        phone.setText("+91 " + phone1);

        addrees1 = flat1 + ", " + street1 + ", " + state1 + ", " + pincode1;


        originalPrice = Double.parseDouble(price1);

        priceDetails();


        cod_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentDone("","COD");
            }
        });

        online_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                razorPay(String.valueOf(totalFinalPrice));
            }
        });

    }


    private void razorPay(String sAmount) {
        Checkout checkout = new Checkout();
//        checkout.setKeyID("rzp_test_ZJwDV0eD9lLlNR");
        checkout.setKeyID("rzp_live_FbAUlsHyZnkcId");
        checkout.setImage(R.drawable.rj_catering_logo);

        int amount = Math.round(Float.parseFloat(sAmount) * 100);


        try {
            JSONObject options = new JSONObject();

            options.put("name", "Ritik Jain");
            options.put("description", "Reference No. #123456");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("order_id",order_id1);//from response of step 3.
            options.put("theme.color", "#EF3E08");
            options.put("currency", "INR");
//            options.put("amount", 100);//pass amount in currency subunits
            options.put("amount", amount);//pass amount in currency subunits
//            options.put("prefill.email", "ritik9628@gmail.com");
            options.put("prefill.contact", phone1);

            checkout.open(OrderDetailsActivity.this, options);

        } catch (Exception e) {
            Log.e("TAG", "Error in starting Razorpay Checkout", e);
        }

    }

    @Override
    public void onPaymentSuccess(String s) {

        paymentDone(s,"ONLINE");
    }

    @Override
    public void onPaymentError(int i, String s) {

        Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

        DynamicToast.Config.getInstance()
                .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                .setDefaultTintColor(getResources().getColor(R.color.white))
                .setToastBackground(toastBG)
                .apply();

        DynamicToast.make(OrderDetailsActivity.this, s, 1000).show();



//        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();

    }


    private void paymentDone(String paymentID,String paymentMethod) {


        c = Calendar.getInstance();
        df = new SimpleDateFormat("ddMMyyyy");
        date = df.format(c.getTime());
        rand = new Random();
        randomNum = rand.nextInt((max - min) + 1) + min;

        order_id1 = "OD" + date + String.valueOf(randomNum);


        String d1 = date.substring(0, 2) + "/" + date.substring(2, 4) + "/" + date.substring(4, 8);

        Log.d("oedfhvebnjmkcflkwjqh", d1);


        loadingDialog.show();

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    long list_size = task.getResult().getLong("list_size");


                    Map<String, Object> ordersMap = new HashMap<>();
                    ordersMap.put("order_id", order_id1);
                    ordersMap.put("order_final_price", "Rs." + String.valueOf(totalFinalPrice) + "/-");
                    ordersMap.put("order_discount_price", "-Rs." + String.valueOf(discountPrice) + "/-");
                    ordersMap.put("order_delivery_price", "Rs." + String.valueOf(deliveryPrice) + "/-");
                    ordersMap.put("order_original_price", "Rs." + String.valueOf(originalPrice) + "/-");
                    ordersMap.put("user_ID", FirebaseAuth.getInstance().getUid());
                    ordersMap.put("user_name", name1);
                    ordersMap.put("user_address", addrees1);
                    ordersMap.put("user_phone", "+91" + phone1);
                    ordersMap.put("order_date", d1);
                    ordersMap.put("packed_date", "");
                    ordersMap.put("shipped_date", "");
                    ordersMap.put("deliver_date", "");
                    ordersMap.put("order_status", "Ordered");
                    ordersMap.put("order_item_list_size", (long) list_size);
                    ordersMap.put("time", FieldValue.serverTimestamp());
                    for (long i = 1; i <= list_size; i++) {
                        ordersMap.put("order_food_item_id_" + i, task.getResult().getString("food_ID_" + i));
                        ordersMap.put("order_food_name_" + i, task.getResult().getString("food_name_" + i));
                        ordersMap.put("order_food_image_" + i, task.getResult().getString("food_image_" + i));
                        ordersMap.put("order_food_price_" + i, task.getResult().getString("food_price_" + i));
                    }
                    ordersMap.put("payment_ID", paymentID);
                    ordersMap.put("payment_method", paymentMethod);
                    ordersMap.put("review_btn_visibility",(boolean) false);
                    ordersMap.put("review_Ratings","");


                    FirebaseFirestore.getInstance().collection("ORDERS")
                            .document(order_id1)
                            .set(ordersMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {

                                FirebaseFirestore.getInstance().collection("USERS")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .collection("USER_ORDERS")
                                        .document(order_id1).set(ordersMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {

                                                    Map<String, Object> cartEmptyMap = new HashMap<>();
                                                    cartEmptyMap.put("list_size", (long) 0);

                                                    FirebaseFirestore.getInstance().collection("USERS")
                                                            .document(FirebaseAuth.getInstance().getUid())
                                                            .collection("USER_DATA").document("MY_CART")
                                                            .set(cartEmptyMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                loadingDialog.dismiss();

                                                                Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                                                                DynamicToast.Config.getInstance()
                                                                        .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                                                        .setDefaultTintColor(getResources().getColor(R.color.white))
                                                                        .setToastBackground(toastBG)
                                                                        .apply();

                                                                DynamicToast.make(OrderDetailsActivity.this, "Order has been successfully placed.", 1000).show();



//                                                                Toast.makeText(OrderDetailsActivity.this, "Order has been successfully placed.", Toast.LENGTH_SHORT).show();

                                                                startActivity(new Intent(OrderDetailsActivity.this, PlacedActivity.class));

                                                                finish();

                                                            } else {
                                                                loadingDialog.dismiss();
                                                                Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                } else {
                                                    loadingDialog.dismiss();
                                                    Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            }
                                        });


                            } else {
                                loadingDialog.dismiss();
                                Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        }
                    });


                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });


    }


    private void priceDetails(){

        FirebaseFirestore.getInstance().collection("PRICE").document("DETAILS")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    if (originalPrice >= task.getResult().getDouble("above_Delivery_Price")) {
                        discountPrice = (originalPrice * task.getResult().getDouble("discount_on_free_delivery")) / 100;
                        deliveryPrice = 0;
                    } else {
                        discountPrice = (originalPrice * task.getResult().getDouble("discount_on_charge_delivery")) / 100;
                        deliveryPrice = task.getResult().getDouble("delivery_price");
                    }

                    totalFinalPrice = originalPrice - discountPrice + deliveryPrice;

                    price.setText("Rs." + String.valueOf(originalPrice) + "/-");
                    discount.setText("-Rs." + String.valueOf(discountPrice) + "/-");
                    totalPrice.setText("Rs." + String.valueOf(totalFinalPrice) + "/-");
                    order_delivery_price.setText("Rs." + String.valueOf(deliveryPrice) + "/-");




                }else {
                    Toast.makeText(OrderDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


}