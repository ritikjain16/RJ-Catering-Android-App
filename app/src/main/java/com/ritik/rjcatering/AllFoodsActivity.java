package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ritik.rjcatering.adapter.FoodAdapter;
import com.ritik.rjcatering.adapter.FoodAdapter1;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

public class AllFoodsActivity extends AppCompatActivity {

    private RecyclerView all_food_recyclerView;
    private EditText all_food_search;
    List<FoodItemModel> foodItemModelList1 = new ArrayList<>();

    private FoodAdapter1 foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_foods);

        all_food_recyclerView = findViewById(R.id.all_food_recyclerView);
        all_food_search = findViewById(R.id.all_food_search);



        setAllFoods(foodItemModelList1, all_food_recyclerView, "ITEMS1");

        this.all_food_search.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void afterTextChanged(Editable editable) {
                AllFoodsActivity.this.filterQuery(editable.toString());
            }
        });

    }

    private void setAllFoods(final List<FoodItemModel> foodItemModelList, RecyclerView recyclerView, String collection) {

        foodItemModelList.clear();

        foodAdapter = new FoodAdapter1(this, foodItemModelList);

        FirebaseFirestore.getInstance().collection(collection)
                .orderBy("food_time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                foodItemModelList.add(new FoodItemModel(
                                        documentSnapshot.getString("food_ID"),
                                        documentSnapshot.getString("food_image"),
                                        documentSnapshot.getString("food_name"),
                                        documentSnapshot.getString("food_price")
                                ));
                            }



                            recyclerView.setLayoutManager(new GridLayoutManager(AllFoodsActivity.this, 2));
                            recyclerView.setAdapter(foodAdapter);
                            foodAdapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(AllFoodsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }


    public void filterQuery(String text) {
        ArrayList<FoodItemModel> filterdNames = new ArrayList<>();
        for (FoodItemModel s : foodItemModelList1) {
            if (s.getFood_name().toLowerCase().contains(text)){
                filterdNames.add(s);
            }
        }
        try {
            foodAdapter.setFilter(filterdNames);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}