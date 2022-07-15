package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ritik.rjcatering.adapter.FavsAdapter;
import com.ritik.rjcatering.model.FavsModel;

import java.util.ArrayList;
import java.util.List;

public class FavsActivity extends AppCompatActivity {

    private RecyclerView fav_recyclerView;
    private FavsAdapter favsAdapter;
    private ImageView arrow_back,no_fav_image;
    private TextView no_fav_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);

        fav_recyclerView = findViewById(R.id.favs_recyclerView);
        arrow_back=findViewById(R.id.arrow_back);
        no_fav_image = findViewById(R.id.no_fav_image);
        no_fav_text=findViewById(R.id.no_fav_items);

        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        List<FavsModel> favsModelList = new ArrayList<>();

        favsAdapter = new FavsAdapter(this, favsModelList);


        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid()).collection("MY_FAVS")
                .orderBy("food_time", Query.Direction.DESCENDING)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {

                    favsModelList.clear();

                    int count=0;
                    for(QueryDocumentSnapshot documentSnapshot:task.getResult()){
                        count++;
                    }

                    if(count==0){

                        fav_recyclerView.setVisibility(View.GONE);
                        no_fav_image.setVisibility(View.VISIBLE);
                        no_fav_text.setVisibility(View.VISIBLE);

                    }else {

                        no_fav_image.setVisibility(View.GONE);
                        no_fav_text.setVisibility(View.GONE);
                        fav_recyclerView.setVisibility(View.VISIBLE);


                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                            favsModelList.add(new FavsModel(
                                    documentSnapshot.getString("food_ID"),
                                    documentSnapshot.getString("food_image"),
                                    documentSnapshot.getString("food_name"),
                                    "Rs."+documentSnapshot.getString("food_price")+"/-"
                            ));

                        }


                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FavsActivity.this);
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        fav_recyclerView.setLayoutManager(linearLayoutManager);
                        fav_recyclerView.setAdapter(favsAdapter);
                        favsAdapter.notifyDataSetChanged();


                    }


                } else {
                    Toast.makeText(FavsActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });





    }


}