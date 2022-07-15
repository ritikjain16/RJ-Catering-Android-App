package com.ritik.rjcatering.adapter;

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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ritik.rjcatering.AddressActivity;
import com.ritik.rjcatering.FoodDetailsActivity;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.OrderDetailsActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.fragments.CartFragment;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context context;
    private List<FoodItemModel> cartItemList;

    public CartAdapter(Context context, List<FoodItemModel> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull  CartAdapter.ViewHolder holder, int position) {


        holder.setFoodData(position);
        holder.setIsRecyclable(false);

    }


    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {



        private ImageView food_image;
        private TextView food_name,food_price;
        private String food_ID;

//        private TextView decrease_qty;
//        private TextView qty_text;
//        private TextView increase_qty;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            food_image=itemView.findViewById(R.id.food_image3);
            food_name=itemView.findViewById(R.id.food_name3);
            food_price=itemView.findViewById(R.id.food_price3);
//            decrease_qty=itemView.findViewById(R.id.decrease_qty);
//            qty_text=itemView.findViewById(R.id.qty_text);
//            increase_qty=itemView.findViewById(R.id.increase_qty);
        }




        private void setFoodData(int pos){
            Glide.with(context).load(cartItemList.get(pos).getFood_image()).into(food_image);
            food_name.setText(cartItemList.get(pos).getFood_name());
            food_price.setText("Rs."+cartItemList.get(pos).getFood_price()+"/-");
            food_ID = cartItemList.get(pos).getFood_ID();


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


//                    -------------admob------------

                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(itemView.getContext(), "ca-app-pub-3260061336719153/5928233778", adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            MainActivity.interstitialAd3 = interstitialAd;
                            MainActivity.interstitialAd3.show((MainActivity) itemView.getContext());
                            Log.i("--admob", "onAdLoaded");

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i("--admob", loadAdError.getMessage());
                            MainActivity.interstitialAd3 = null;

                        }
                    });


//                    -------------admob------------




                    itemView.getContext().startActivity(new Intent(
                            context, FoodDetailsActivity.class
                    ).putExtra("food_ID",food_ID));

                }
            });


//            decrease_qty.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    FirebaseFirestore.getInstance().collection("USERS")
//                            .document(FirebaseAuth.getInstance().getUid())
//                            .collection("USER_DATA").document("MY_CART")
//
//                }
//            });

        }

    }
}
