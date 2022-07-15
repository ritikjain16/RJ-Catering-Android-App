package com.ritik.rjcatering.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ritik.rjcatering.R;
import com.ritik.rjcatering.model.ReviewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private List<ReviewModel> reviewModelList;

    public ReviewAdapter(Context context, List<ReviewModel> reviewModelList) {
        this.context = context;
        this.reviewModelList = reviewModelList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {

        holder.review_name.setText(reviewModelList.get(position).getReview_name());
        holder.review_date.setText(reviewModelList.get(position).getReview_date());
        holder.review.setText(reviewModelList.get(position).getReview());

    }

    @Override
    public int getItemCount() {
        return reviewModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView review_name;
        private TextView review_date;
        private TextView review;


        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            review_name=itemView.findViewById(R.id.re_name);
            review_date=itemView.findViewById(R.id.re_review_date);
            review=itemView.findViewById(R.id.re_review);


        }
    }
}
