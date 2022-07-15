package com.ritik.rjcatering.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.ritik.rjcatering.FavsActivity;
import com.ritik.rjcatering.FinalOrderDetailsactivity;
import com.ritik.rjcatering.FoodDetailsActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.model.FavsModel;
import com.ritik.rjcatering.model.OrdersModel;

import java.util.List;

public class FavsAdapter extends RecyclerView.Adapter<FavsAdapter.ViewHolder> {

    private Context context;
    private List<FavsModel> favsModelList;

    public FavsAdapter(Context context, List<FavsModel> favsModelList) {
        this.context = context;
        this.favsModelList = favsModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FavsAdapter.ViewHolder holder, int position) {


        holder.setOrderData(position);

    }

    @Override
    public int getItemCount() {
        return favsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView food_image;
        private TextView food_name;
        private String food_ID;
        private TextView food_price;
        private ImageView food_delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            food_image = itemView.findViewById(R.id.food_image6);
            food_name = itemView.findViewById(R.id.food_name6);
            food_price = itemView.findViewById(R.id.food_price6);
            food_delete_btn = itemView.findViewById(R.id.delete_btn);


        }

        private void setOrderData(int pos) {

            Glide.with(context).load(favsModelList.get(pos).getFood_Image()).into(food_image);
            food_name.setText(favsModelList.get(pos).getFood_name());
            food_price.setText(favsModelList.get(pos).getFood_price());

            food_ID = favsModelList.get(pos).getFood_ID();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemView.getContext().startActivity(new Intent(context, FoodDetailsActivity.class)
                            .putExtra("food_ID", food_ID));

                }
            });


            food_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    FirebaseFirestore.getInstance().collection("USERS")
                            .document(FirebaseAuth.getInstance().getUid()).collection("MY_FAVS")
                            .document(food_ID).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        notifyDataSetChanged();

                                        Drawable toastBG = itemView.getContext().getDrawable(R.drawable.background_color_gradient_nav);

                                        DynamicToast.Config.getInstance()
                                                .setDefaultBackgroundColor(context.getResources().getColor(R.color.white))
                                                .setDefaultTintColor(context.getResources().getColor(R.color.white))
                                                .setToastBackground(toastBG)
                                                .apply();

                                        DynamicToast.make(context, favsModelList.get(pos).getFood_name() + " removed from Favs!",1000).show();


                                        itemView.getContext().startActivity(new Intent(context, FavsActivity.class));
                                        ((Activity) context).finish();

//                                        Toast.makeText(context, favsModelList.get(pos).getFood_name()+" removed from Favs!", Toast.LENGTH_SHORT).show();



                                    } else {
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }
            });


        }


    }
}
