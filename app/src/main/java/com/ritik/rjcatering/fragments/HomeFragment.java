package com.ritik.rjcatering.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.ritik.rjcatering.AllFoodsActivity;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.R;
import com.ritik.rjcatering.adapter.FoodAdapter;
import com.ritik.rjcatering.model.FoodItemModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    private AdView adview1,adview2;


    private TextView rj_catering;
    private ImageSlider imageSlider1;
    private ImageSlider imageSlider2;
    private ImageSlider imageSlider3;
    private ImageSlider imageSlider4;
    private ImageSlider imageSlider5;
    private ImageSlider imageSlider6;
    private ImageSlider imageSlider7;

    private RecyclerView recyclerView1;
    private RecyclerView recyclerView2;
    private RecyclerView recyclerView3;
    private RecyclerView recyclerView4;
    private RecyclerView recyclerView5;
    private RecyclerView recyclerView6;
    private RecyclerView recyclerView7;
    private RecyclerView recyclerView8;
    private RecyclerView recyclerView9;
    private RecyclerView recyclerView10;
    private RecyclerView recyclerView11;
    private RecyclerView recyclerView12;
    private RecyclerView recyclerView13;
    private RecyclerView recyclerView14;


    private CardView cardView1;
    private CardView cardView2;
    private CardView cardView3;
    private CardView cardView4;
    private CardView cardView5;
    private CardView cardView6;
    private CardView cardView7;
    private CardView cardView8;
    private CardView cardView9;
    private CardView cardView10;
    private CardView cardView11;
    private CardView cardView12;
    private CardView cardView13;
    private CardView cardView14;

    private CardView cardView15;
    private CardView cardView16;
    private CardView cardView17;
    private CardView cardView18;
    private CardView cardView19;
    private CardView cardView20;
    private CardView cardView21;


    private CardView search_food;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        rj_catering = view.findViewById(R.id.rj_catering);

        imageSlider1 = view.findViewById(R.id.image_slider1);
        imageSlider2 = view.findViewById(R.id.image_slider2);
        imageSlider3 = view.findViewById(R.id.image_slider3);
        imageSlider4 = view.findViewById(R.id.image_slider4);
        imageSlider5 = view.findViewById(R.id.image_slider5);
        imageSlider6 = view.findViewById(R.id.image_slider6);
        imageSlider7 = view.findViewById(R.id.image_slider7);

        recyclerView1 = view.findViewById(R.id.recyclerView1);
        recyclerView2 = view.findViewById(R.id.recyclerView2);
        recyclerView3 = view.findViewById(R.id.recyclerView3);
        recyclerView4 = view.findViewById(R.id.recyclerView4);
        recyclerView5 = view.findViewById(R.id.recyclerView5);
        recyclerView6 = view.findViewById(R.id.recyclerView6);
        recyclerView7 = view.findViewById(R.id.recyclerView7);
        recyclerView8 = view.findViewById(R.id.recyclerView8);
        recyclerView9 = view.findViewById(R.id.recyclerView9);
        recyclerView10 = view.findViewById(R.id.recyclerView10);
        recyclerView11 = view.findViewById(R.id.recyclerView11);
        recyclerView12 = view.findViewById(R.id.recyclerView12);
        recyclerView13 = view.findViewById(R.id.recyclerView13);
        recyclerView14 = view.findViewById(R.id.recyclerView14);


        cardView1 = view.findViewById(R.id.r1);
        cardView2 = view.findViewById(R.id.r2);
        cardView3 = view.findViewById(R.id.r10);
        cardView4 = view.findViewById(R.id.r14);
        cardView5 = view.findViewById(R.id.r16);
        cardView6 = view.findViewById(R.id.r17);
        cardView7 = view.findViewById(R.id.r19);
        cardView8 = view.findViewById(R.id.r20);
        cardView9 = view.findViewById(R.id.r22);
        cardView10 = view.findViewById(R.id.r23);
        cardView11 = view.findViewById(R.id.r25);
        cardView12 = view.findViewById(R.id.r26);
        cardView13 = view.findViewById(R.id.r28);
        cardView14 = view.findViewById(R.id.r29);

        cardView15 = view.findViewById(R.id.card1);
        cardView16 = view.findViewById(R.id.card2);
        cardView17 = view.findViewById(R.id.r15);
        cardView18 = view.findViewById(R.id.r18);
        cardView19 = view.findViewById(R.id.r21);
        cardView20 = view.findViewById(R.id.r24);
        cardView21 = view.findViewById(R.id.r27);


        search_food = view.findViewById(R.id.search_food);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adview1 = view.findViewById(R.id.adView1);
        AdRequest adRequest = new AdRequest.Builder().build();
        adview1.loadAd(adRequest);

        adview2 = view.findViewById(R.id.adView2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        adview2.loadAd(adRequest1);





        search_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) getContext(), search_food, "search");
                Intent intent = new Intent(getContext(), AllFoodsActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });


        rj_catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });


        FirebaseFirestore.getInstance().collection("FOOD_TYPE")
                .document("banner_visibility")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    setBannersVisibility(task.getResult().getString("banner_visibility1"),cardView15,imageSlider1,"BANNERS1");
                    setBannersVisibility(task.getResult().getString("banner_visibility2"),cardView16,imageSlider2,"BANNERS2");
                    setBannersVisibility(task.getResult().getString("banner_visibility3"),cardView17,imageSlider3,"BANNERS3");
                    setBannersVisibility(task.getResult().getString("banner_visibility4"),cardView18,imageSlider4,"BANNERS4");
                    setBannersVisibility(task.getResult().getString("banner_visibility5"),cardView19,imageSlider5,"BANNERS5");
                    setBannersVisibility(task.getResult().getString("banner_visibility6"),cardView20,imageSlider6,"BANNERS6");
                    setBannersVisibility(task.getResult().getString("banner_visibility7"),cardView21,imageSlider7,"BANNERS7");


                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


//        -------------------------------------
        FirebaseFirestore.getInstance().collection("FOOD_TYPE")
                .document("food_types")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {


                    List<FoodItemModel> foodItemModelList1 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList2 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList3 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList4 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList5 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList6 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList7 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList8 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList9 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList10 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList11 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList12 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList13 = new ArrayList<>();
                    List<FoodItemModel> foodItemModelList14 = new ArrayList<>();


                    itemCardVisibility(cardView1, recyclerView1, task.getResult().getString("visibility_food_type1"), foodItemModelList1, task.getResult().getString("food_type1"));
                    itemCardVisibility(cardView2, recyclerView2, task.getResult().getString("visibility_food_type2"), foodItemModelList2, task.getResult().getString("food_type2"));
                    itemCardVisibility(cardView3, recyclerView3, task.getResult().getString("visibility_food_type3"), foodItemModelList3, task.getResult().getString("food_type3"));
                    itemCardVisibility(cardView4, recyclerView4, task.getResult().getString("visibility_food_type4"), foodItemModelList4, task.getResult().getString("food_type4"));
                    itemCardVisibility(cardView5, recyclerView5, task.getResult().getString("visibility_food_type5"), foodItemModelList5, task.getResult().getString("food_type5"));
                    itemCardVisibility(cardView6, recyclerView6, task.getResult().getString("visibility_food_type6"), foodItemModelList6, task.getResult().getString("food_type6"));
                    itemCardVisibility(cardView7, recyclerView7, task.getResult().getString("visibility_food_type7"), foodItemModelList7, task.getResult().getString("food_type7"));
                    itemCardVisibility(cardView8, recyclerView8, task.getResult().getString("visibility_food_type8"), foodItemModelList8, task.getResult().getString("food_type8"));
                    itemCardVisibility(cardView9, recyclerView9, task.getResult().getString("visibility_food_type9"), foodItemModelList9, task.getResult().getString("food_type9"));
                    itemCardVisibility(cardView10, recyclerView10, task.getResult().getString("visibility_food_type10"), foodItemModelList10, task.getResult().getString("food_type10"));
                    itemCardVisibility(cardView11, recyclerView11, task.getResult().getString("visibility_food_type11"), foodItemModelList11, task.getResult().getString("food_type11"));
                    itemCardVisibility(cardView12, recyclerView12, task.getResult().getString("visibility_food_type12"), foodItemModelList12, task.getResult().getString("food_type12"));
                    itemCardVisibility(cardView13, recyclerView13, task.getResult().getString("visibility_food_type13"), foodItemModelList13, task.getResult().getString("food_type13"));
                    itemCardVisibility(cardView14, recyclerView14, task.getResult().getString("visibility_food_type14"), foodItemModelList14, task.getResult().getString("food_type14"));


                } else {
                    Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


    private void setBanners(ImageSlider imageSlider, String collection) {
        List<SlideModel> imageList = new ArrayList<>();
        imageList.clear();

        FirebaseFirestore.getInstance().collection(collection)
                .orderBy("food_time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                imageList.add(new SlideModel(documentSnapshot.getString("food_banner_image"), ScaleTypes.FIT));
                            }

                            imageSlider.setImageList(imageList, ScaleTypes.FIT);

                            imageSlider.setItemClickListener(new ItemClickListener() {
                                @Override
                                public void onItemSelected(int i) {
//                                    Toast.makeText(getContext(), "Pos: " + String.valueOf(i), Toast.LENGTH_SHORT).show();
                                }
                            });


                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void setFoodItems(final List<FoodItemModel> foodItemModelList, RecyclerView recyclerView, String collection, String foodType) {


        foodItemModelList.clear();

        FoodAdapter foodAdapter = new FoodAdapter(getContext(), foodItemModelList);

        FirebaseFirestore.getInstance().collection(collection)
                .orderBy("food_time", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {

                                if (documentSnapshot.getString("food_type").equals(foodType)) {
                                    foodItemModelList.add(new FoodItemModel(
                                            documentSnapshot.getString("food_ID"),
                                            documentSnapshot.getString("food_image"),
                                            documentSnapshot.getString("food_name"),
                                            documentSnapshot.getString("food_price")
                                    ));
                                }


                            }

                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(foodAdapter);
                            foodAdapter.notifyDataSetChanged();


                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    private void itemCardVisibility(CardView cardView, RecyclerView recyclerView, String visibility, List<FoodItemModel> foodItemModelList, String foodType) {
        if (visibility.equals("t")) {
            cardView.setVisibility(View.VISIBLE);
            setFoodItems(foodItemModelList, recyclerView, "ITEMS1", foodType);
        } else {
            cardView.setVisibility(View.GONE);
        }
    }

    private void setBannersVisibility(String visibility,CardView cardView,ImageSlider imageSlider,String collection){
        if(visibility.equals("t")) {
            cardView.setVisibility(View.VISIBLE);
            setBanners(imageSlider, collection);
        }else {
            cardView.setVisibility(View.GONE);
        }
    }

}