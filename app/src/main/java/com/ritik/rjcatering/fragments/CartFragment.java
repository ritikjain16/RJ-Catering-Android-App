package com.ritik.rjcatering.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.math.MathUtils;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ritik.rjcatering.AddressActivity;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.OrderDetailsActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.adapter.CartAdapter;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class CartFragment extends Fragment {

    public CartFragment() {
        // Required empty public constructor
    }

    private TextView rj_catering;
    private RecyclerView cart_RecyclerView;
    private LinearLayout cart_total_layout;
    private TextView no_cart_items;
    private ImageView no_cart_image;

    private Button continue_btn;
    private TextView total;

    private ImageView clear_cart_options;
    private CardView clear_cart;

    private TextView clear_cart_text;

    private Animation optionsAnimation;

    private ConstraintLayout cons_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        rj_catering = view.findViewById(R.id.rj_catering);
        cart_RecyclerView = view.findViewById(R.id.cart_recyclerView);
        total = view.findViewById(R.id.total);
        continue_btn = view.findViewById(R.id.continue_btn);
        cart_total_layout = view.findViewById(R.id.cart_total_layout);
        no_cart_items = view.findViewById(R.id.no_cart_items);
        no_cart_image = view.findViewById(R.id.no_cart_image);
        clear_cart_options = view.findViewById(R.id.clear_cart_options);
        clear_cart = view.findViewById(R.id.clear_cart);
        cons_layout = view.findViewById(R.id.cons_layout);
        clear_cart_text=view.findViewById(R.id.clear_cart_text);


        rj_catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        loadCartList(cart_RecyclerView);


        clear_cart_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clear_cart.getVisibility() == View.GONE) {
                    optionsAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(),
                            R.anim.slide_in_row);
                    clear_cart.setAnimation(optionsAnimation);
                    clear_cart.setVisibility(View.VISIBLE);
                } else {
                    optionsAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(),
                            R.anim.slide_out_row);
                    clear_cart.setAnimation(optionsAnimation);
                    clear_cart.setVisibility(View.GONE);
                }
            }
        });

        cons_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clear_cart.getVisibility() == View.VISIBLE) {
                    optionsAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(),
                            R.anim.slide_out_row);
                    clear_cart.setAnimation(optionsAnimation);
                    clear_cart.setVisibility(View.GONE);
                }
            }
        });


        clear_cart_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearCart();
            }
        });


        return view;
    }


    private void continueBtn(String totalPrice) {
        startActivity(new Intent(getContext(), AddressActivity.class)
                .putExtra("totalPrice", totalPrice));
    }


    private void loadCartList(RecyclerView recyclerView) {


        List<FoodItemModel> cartItemList = new ArrayList<>();

        cartItemList.clear();


        CartAdapter cartAdapter = new CartAdapter(getContext(), cartItemList);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    long list_size = task.getResult().getLong("list_size");

                    if (list_size == 0) {
                        cart_RecyclerView.setVisibility(View.GONE);
                        cart_total_layout.setVisibility(View.GONE);
                        no_cart_items.setVisibility(View.VISIBLE);
                        no_cart_image.setVisibility(View.VISIBLE);
                    } else {

                        cart_RecyclerView.setVisibility(View.VISIBLE);
                        cart_total_layout.setVisibility(View.VISIBLE);
                        no_cart_items.setVisibility(View.GONE);
                        no_cart_image.setVisibility(View.GONE);

                        for (long i = 1; i <= list_size; i++) {
                            cartItemList.add(new FoodItemModel(
                                    task.getResult().getString("food_ID_" + i),
                                    task.getResult().getString("food_image_" + i),
                                    task.getResult().getString("food_name_" + i),
                                    task.getResult().getString("food_price_" + i)
                            ));
                        }


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(cartAdapter);
                        cartAdapter.notifyDataSetChanged();

                        long total1 = 0;
                        for (long i = 1; i <= list_size; i++) {
                            total1 = total1 + Long.parseLong(task.getResult().getString("food_price_" + i));
                        }

                        total.setText(String.valueOf(total1));

                        continue_btn.setEnabled(true);


                        long finalTotal = total1;
                        continue_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                continueBtn(String.valueOf(finalTotal));
                            }
                        });

                    }
                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void clearCart() {

        Map<String, Object> cartClearMap = new HashMap<>();
        cartClearMap.put("list_size", (long) 0);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_CART")
                .set(cartClearMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                optionsAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(),
                        R.anim.slide_out_row);
                clear_cart.setAnimation(optionsAnimation);
                clear_cart.setVisibility(View.GONE);

                loadCartList(cart_RecyclerView);
            }
        });


    }
}