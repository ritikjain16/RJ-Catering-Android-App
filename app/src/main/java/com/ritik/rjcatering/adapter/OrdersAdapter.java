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
import com.ritik.rjcatering.FinalOrderDetailsactivity;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.model.OrdersModel;

import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<OrdersModel> ordersModelList;

    public OrdersAdapter(Context context, List<OrdersModel> ordersModelList) {
        this.context = context;
        this.ordersModelList = ordersModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {



        holder.setOrderData(position);

    }

    @Override
    public int getItemCount() {
        return ordersModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView order_image;
        private TextView order_name;
        private TextView order_status;
        private String order_ID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_image = itemView.findViewById(R.id.food_image5);
            order_name = itemView.findViewById(R.id.food_name5);
            order_status = itemView.findViewById(R.id.food_status5);

        }

        private void setOrderData(int pos) {

            Glide.with(context).load(ordersModelList.get(pos).getOrder_Image()).into(order_image);
//            Glide.with(context).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQNPfZedHlzrzDZ9DnRo5s5DAdNgxVWfqdjNg&usqp=CAU").into(order_image);
            order_name.setText(ordersModelList.get(pos).getOrder_name());
            order_status.setText(ordersModelList.get(pos).getOrder_status());

            order_ID = ordersModelList.get(pos).getOrder_ID();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //                    -------------admob------------

                    AdRequest adRequest = new AdRequest.Builder().build();
                    InterstitialAd.load(itemView.getContext(), "ca-app-pub-3260061336719153/8788053602", adRequest, new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                            // The mInterstitialAd reference will be null until
                            // an ad is loaded.
                            MainActivity.interstitialAd2 = interstitialAd;
                            MainActivity.interstitialAd2.show((MainActivity) itemView.getContext());
                            Log.i("--admob", "onAdLoaded");

                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error
                            Log.i("--admob", loadAdError.getMessage());
                            MainActivity.interstitialAd2 = null;

                        }
                    });


//                    -------------admob------------



                    itemView.getContext().startActivity(new Intent(context, FinalOrderDetailsactivity.class)
                    .putExtra("order_ID",order_ID));

                }
            });


        }


    }
}
