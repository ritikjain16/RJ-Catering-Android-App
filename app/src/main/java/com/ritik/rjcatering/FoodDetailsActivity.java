package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.ritik.rjcatering.adapter.FoodAdapter;
import com.ritik.rjcatering.model.FoodItemModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodDetailsActivity extends AppCompatActivity {

    private ImageView arrow_back, food_item_image, cart_food_details;
    private TextView food_item_name, food_item_price, total_this_item, food_desc;
    private Button addToCartBtn;
    private Dialog loadingDialog;
    private FloatingActionButton add_to_fav;
    private RecyclerView related_items_recyclerView;

    public static boolean FOOD_DETAILS_CART;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_details);

        arrow_back = findViewById(R.id.arrow_back);
        food_item_image = findViewById(R.id.food_item_image);
        food_item_name = findViewById(R.id.food_item_name);
        food_item_price = findViewById(R.id.food_item_price);
        addToCartBtn = findViewById(R.id.add_to_cart);
        cart_food_details = findViewById(R.id.cart_food_details);
        add_to_fav = findViewById(R.id.add_to_fav);
        total_this_item = findViewById(R.id.total_this_item);
        related_items_recyclerView = findViewById(R.id.related_items_recyclerView);
        food_desc = findViewById(R.id.food_desc);

        loadingDialog = new Dialog(FoodDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
//        loading_text.setText("Loading...");

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();

        String food_ID = intent.getStringExtra("food_ID");


        setRelatedItems(related_items_recyclerView, food_ID);

        setTotalThisItem(total_this_item, food_ID);

        alreadyAddOrNotToFavs(food_ID);

        setFoodDetails(food_ID, "ITEMS1");


//        if (addToCartBtn.getText().toString().equals("add to cart")) {
            addToCartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent1 = getIntent();
                    String food_ID1 = intent1.getStringExtra("food_ID");
                    addToCart(food_ID1);
                }
            });

//        } else {
//            Toast.makeText(this, "Go to cart", Toast.LENGTH_SHORT).show();
//        }

        cart_food_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FOOD_DETAILS_CART = true;
                startActivity(new Intent(FoodDetailsActivity.this, MainActivity.class));
                finish();
            }
        });

        add_to_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFav(food_ID);
            }
        });


    }

    private void setFoodDetails(String foodID, String collection) {

        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Loading...");
        loadingDialog.show();

        FirebaseFirestore.getInstance().collection(collection).document(foodID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    Glide.with(FoodDetailsActivity.this).load(task.getResult().getString("food_image")).into(food_item_image);
                    food_item_name.setText(task.getResult().getString("food_name"));
                    food_desc.setText(task.getResult().getString("food_desc"));
                    food_item_price.setText("Rs." + task.getResult().getString("food_price") + "/-");
                    loadingDialog.dismiss();
                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void addToCart(String foodID) {
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Adding to Cart...");

        loadingDialog.show();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    long list_size = task.getResult().getLong("list_size");

                    long newSize = list_size + 1;


                    FirebaseFirestore.getInstance().collection("ITEMS1").document(foodID)
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                            if (task.isSuccessful()) {

                                Map<String, Object> cartMap = new HashMap<>();
                                cartMap.put("list_size", newSize);
                                cartMap.put("food_ID_" + newSize, foodID);
                                cartMap.put("food_name_" + newSize, task.getResult().getString("food_name"));
                                cartMap.put("food_price_" + newSize, task.getResult().getString("food_price"));
                                cartMap.put("food_image_" + newSize, task.getResult().getString("food_image"));
                                cartMap.put("food_add_to_cart_time_" + newSize, FieldValue.serverTimestamp());

                                String name = task.getResult().getString("food_name");

                                FirebaseFirestore.getInstance().collection("USERS")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .collection("USER_DATA").document("MY_CART")
                                        .update(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
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

                                            DynamicToast.make(FoodDetailsActivity.this, name + " Added To Cart", 1000).show();


//                                            Toast.makeText(FoodDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
                                        } else {
                                            loadingDialog.dismiss();
                                            Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                });


                                loadingDialog.dismiss();
                            } else {
                                loadingDialog.dismiss();
                                Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } else {
                    loadingDialog.dismiss();
                    Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    private void addToCart(String foodID) {
//        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
//        loading_text.setText("Adding to Cart...");
//
//        loadingDialog.show();
//        FirebaseFirestore.getInstance().collection("USERS")
//                .document(FirebaseAuth.getInstance().getUid())
//                .collection("USER_DATA").document("MY_CART")
//                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                if (task.isSuccessful()) {
//                    long list_size = task.getResult().getLong("list_size");
//
//                    long newSize = list_size + 1;
//
//
//                    FirebaseFirestore.getInstance().collection("ITEMS1").document(foodID)
//                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//
//                            if (task.isSuccessful()) {
//
//                                Map<String, Object> cartMap = new HashMap<>();
//                                cartMap.put("list_size", newSize);
//                                cartMap.put("food_ID_" + newSize, foodID);
//                                cartMap.put("food_name_" + newSize, task.getResult().getString("food_name"));
//                                cartMap.put("food_price_" + newSize, task.getResult().getString("food_price"));
//                                cartMap.put("food_image_" + newSize, task.getResult().getString("food_image"));
//                                cartMap.put("food_qty_" + newSize, 1);
//                                cartMap.put("food_add_to_cart_time_" + newSize, FieldValue.serverTimestamp());
//
//                                String name = task.getResult().getString("food_name");
//
//                                FirebaseFirestore.getInstance().collection("USERS")
//                                        .document(FirebaseAuth.getInstance().getUid())
//                                        .collection("USER_DATA").document("MY_CART")
//                                        .update(cartMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        if (task.isSuccessful()) {
//                                            loadingDialog.dismiss();
//
//                                            addToCartBtn.setText("Go To Cart");
//
//                                            Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);
//
//                                            DynamicToast.Config.getInstance()
//                                                    .setDefaultBackgroundColor(getResources().getColor(R.color.white))
//                                                    .setDefaultTintColor(getResources().getColor(R.color.white))
//                                                    .setToastBackground(toastBG)
//                                                    .apply();
//
//                                            DynamicToast.make(FoodDetailsActivity.this, name + " Added To Cart", 1000).show();
//
//
////                                            Toast.makeText(FoodDetailsActivity.this, "Added To Cart", Toast.LENGTH_SHORT).show();
//                                        } else {
//                                            loadingDialog.dismiss();
//                                            Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    }
//                                });
//
//
//                                loadingDialog.dismiss();
//                            } else {
//                                loadingDialog.dismiss();
//                                Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//
//
//                } else {
//                    loadingDialog.dismiss();
//                    Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


    private void addToFav(String foodID) {

        List<String> wishlistIDs = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("MY_FAVS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            wishlistIDs.clear();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                wishlistIDs.add(documentSnapshot.getId());
                            }


                            if (wishlistIDs.contains(foodID)) {

                                FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                        .collection("MY_FAVS").document(foodID).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    add_to_fav.setImageTintList(ColorStateList.valueOf(Color.parseColor("#A59D9D")));


                                                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                                                    DynamicToast.Config.getInstance()
                                                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                                            .setDefaultTintColor(getResources().getColor(R.color.white))
                                                            .setToastBackground(toastBG)
                                                            .apply();

                                                    DynamicToast.make(FoodDetailsActivity.this, "Removed from Favs!", 1000).show();


//                                                    Toast.makeText(FoodDetailsActivity.this, "Removed from Favs!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });


                            } else {

                                FirebaseFirestore.getInstance().collection("ITEMS1").document(foodID)
                                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {


                                            Map<String, Object> favMap = new HashMap<>();
                                            favMap.put("food_ID", foodID);
                                            favMap.put("food_name", task.getResult().getString("food_name"));
                                            favMap.put("food_price", task.getResult().getString("food_price"));
                                            favMap.put("food_image", task.getResult().getString("food_image"));
                                            favMap.put("food_time", FieldValue.serverTimestamp());

                                            String name = task.getResult().getString("food_name");

                                            FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                                                    .collection("MY_FAVS").document(foodID).set(favMap)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                add_to_fav.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.myColor)));


                                                                Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                                                                DynamicToast.Config.getInstance()
                                                                        .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                                                        .setDefaultTintColor(getResources().getColor(R.color.white))
                                                                        .setToastBackground(toastBG)
                                                                        .apply();

                                                                DynamicToast.make(FoodDetailsActivity.this, name + " Added to Favs!", 1000).show();


//                                                                Toast.makeText(FoodDetailsActivity.this, "Added to Favs!", Toast.LENGTH_SHORT).show();

                                                            } else {
                                                                Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });


                                        } else {

                                            Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            }


                        } else {
                            Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

    }


    private void alreadyAddOrNotToFavs(String foodID) {


        List<String> wishlistIDs = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("USERS").document(FirebaseAuth.getInstance().getUid())
                .collection("MY_FAVS").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {

                            wishlistIDs.clear();
                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                wishlistIDs.add(documentSnapshot.getId());
                            }


                            if (wishlistIDs.contains(foodID)) {
                                add_to_fav.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.myColor)));
                            } else {
                                add_to_fav.setImageTintList(ColorStateList.valueOf(Color.parseColor("#A59D9D")));
                            }


                        } else {
                            Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });


    }


    private void setRelatedItems(RecyclerView recyclerView, String foodID) {

        List<FoodItemModel> foodItemModelList = new ArrayList<>();

        foodItemModelList.clear();

        FoodAdapter foodAdapter = new FoodAdapter(this, foodItemModelList);

        FirebaseFirestore.getInstance().collection("ITEMS1")
                .orderBy("food_time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                String food_ID = documentSnapshot.getString("food_ID");

                                FirebaseFirestore.getInstance().collection("ITEMS1")
                                        .document(foodID).get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    String food_type = task.getResult().getString("food_type");

                                                    if (documentSnapshot.getString("food_type").equals(food_type)
                                                            && !food_ID.equals(foodID)) {

                                                        foodItemModelList.add(new FoodItemModel(
                                                                documentSnapshot.getString("food_ID"),
                                                                documentSnapshot.getString("food_image"),
                                                                documentSnapshot.getString("food_name"),
                                                                documentSnapshot.getString("food_price")
                                                        ));
//
                                                    }


                                                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FoodDetailsActivity.this);
                                                    linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                                    recyclerView.setLayoutManager(linearLayoutManager);
                                                    recyclerView.setAdapter(foodAdapter);
                                                    foodAdapter.notifyDataSetChanged();


                                                } else {
                                                    Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                            }


                        } else {
                            Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    private void setTotalThisItem(TextView textView, String oid) {


        List<String> allOrderItemsList = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("ORDERS")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    allOrderItemsList.clear();

                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                        long orderItemSize = documentSnapshot.getLong("order_item_list_size");

                        for (long i = 1; i <= orderItemSize; i++) {
                            allOrderItemsList.add(documentSnapshot.getString("order_food_item_id_" + i));
                        }

                    }

                    int ofiidno = Collections.frequency(allOrderItemsList, oid);

                    if (ofiidno != 0) {
                        textView.setText("Ordered By (" + String.valueOf(ofiidno) + ")");
                    }


                } else {
                    Toast.makeText(FoodDetailsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


}