package com.ritik.rjcatering.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ritik.rjcatering.FoodDetailsActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.List;

public class FoodAdapter1 extends RecyclerView.Adapter<FoodAdapter1.ViewHolder> {

    private Context context;
    private List<FoodItemModel> foodItemModelList;

    public FoodAdapter1(Context context, List<FoodItemModel> foodItemModelList) {
        this.context = context;
        this.foodItemModelList = foodItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout_1,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  FoodAdapter1.ViewHolder holder, int position) {

        holder.setFoodData(position);
    }

    @Override
    public int getItemCount() {
        return foodItemModelList.size();
    }


    public void setFilter(List<FoodItemModel> filterdNames) {
        this.foodItemModelList = filterdNames;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView food_image;
        private TextView food_name,food_price;
        private String food_ID;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            food_image=itemView.findViewById(R.id.food_image);
            food_name=itemView.findViewById(R.id.food_name);
            food_price=itemView.findViewById(R.id.food_price);
        }

        private void setFoodData(int pos){
            Glide.with(context).load(foodItemModelList.get(pos).getFood_image()).into(food_image);
            food_name.setText(foodItemModelList.get(pos).getFood_name());
            food_price.setText("Rs."+foodItemModelList.get(pos).getFood_price()+"/-");
            food_ID = foodItemModelList.get(pos).getFood_ID();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,itemView,"search");
                    Intent intent = new Intent(context, FoodDetailsActivity.class);
                    intent.putExtra("food_ID",food_ID);
                    itemView.getContext().startActivity(intent,activityOptionsCompat.toBundle());



                }
            });

        }
    }
}
