package com.ritik.rjcatering.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.ritik.rjcatering.AllFoodsActivity;
import com.ritik.rjcatering.FoodDetailsActivity;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.fragments.HomeFragment;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private Context context;
    private List<FoodItemModel> foodItemModelList;

    public FoodAdapter(Context context, List<FoodItemModel> foodItemModelList) {
        this.context = context;
        this.foodItemModelList = foodItemModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  FoodAdapter.ViewHolder holder, int position) {

        Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_up_row);
        holder.itemView.startAnimation(animation);

        holder.setFoodData(position);
    }

    @Override
    public int getItemCount() {
        return foodItemModelList.size();
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

//                    -------------admob------------

                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(itemView.getContext(), "ca-app-pub-3260061336719153/7523073393", adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            MainActivity.interstitialAd1 = interstitialAd;
                            MainActivity.interstitialAd1.show((MainActivity) itemView.getContext());
                            Log.i("--admob", "onAdLoaded");

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i("--admob", loadAdError.getMessage());
                            MainActivity.interstitialAd1 = null;

                        }
                    });


//                    -------------admob------------


                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,itemView,"search");
                    Intent intent = new Intent(context, FoodDetailsActivity.class);
                    intent.putExtra("food_ID",food_ID);
                    itemView.getContext().startActivity(intent,activityOptionsCompat.toBundle());


                }
            });

        }
    }
}
