package com.ritik.rjcatering.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.adapter.OrdersAdapter;
import com.ritik.rjcatering.model.OrdersModel;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class OrdersFragment extends Fragment {


    public OrdersFragment() {
        // Required empty public constructor
    }

    private TextView rj_catering;
    private RecyclerView orders_recyclerView;

    private ImageView no_order_image;
    private TextView no_order_items;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        rj_catering = view.findViewById(R.id.rj_catering);
        orders_recyclerView = view.findViewById(R.id.orders_recyclerView);
        no_order_image = view.findViewById(R.id.no_order_image);
        no_order_items = view.findViewById(R.id.no_order_items);

        rj_catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        setOrders(orders_recyclerView);


        return view;
    }

    private void setOrders(RecyclerView recyclerView) {

        List<OrdersModel> ordersModelList = new ArrayList<>();
        ordersModelList.clear();

        OrdersAdapter ordersAdapter = new OrdersAdapter(getContext(), ordersModelList);

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_ORDERS").orderBy("time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {



                            int count = 0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count++;
                            }

                            if (count == 0) {
                                orders_recyclerView.setVisibility(View.GONE);
                                no_order_image.setVisibility(View.VISIBLE);
                                no_order_items.setVisibility(View.VISIBLE);

                            } else {

                                orders_recyclerView.setVisibility(View.VISIBLE);
                                no_order_image.setVisibility(View.GONE);
                                no_order_items.setVisibility(View.GONE);

                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                    long size = documentSnapshot.getLong("order_item_list_size");


                                    if (size == 1) {
                                        ordersModelList.add(new OrdersModel(
                                                documentSnapshot.getString("order_id"),
                                                documentSnapshot.getString("order_food_image_1"),
                                                documentSnapshot.getString("order_food_name_1"),
                                                documentSnapshot.getString("order_status") + " on " + documentSnapshot.getString("order_date")
                                        ));
                                    } else {
                                        ordersModelList.add(new OrdersModel(
                                                documentSnapshot.getString("order_id"),
                                                documentSnapshot.getString("order_food_image_1"),
                                                documentSnapshot.getString("order_food_name_1") + " + " + String.valueOf(documentSnapshot.getLong("order_item_list_size") - 1) + " more",
                                                documentSnapshot.getString("order_status") + " on " + documentSnapshot.getString("order_date")
                                        ));
                                    }

                                }

                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                                recyclerView.setLayoutManager(linearLayoutManager);
                                recyclerView.setAdapter(ordersAdapter);
                                ordersAdapter.notifyDataSetChanged();

                            }
                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}