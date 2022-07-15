package com.ritik.rjcatering.fragments;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.ritik.rjcatering.FavsActivity;
import com.ritik.rjcatering.LoginActivity;
import com.ritik.rjcatering.MainActivity;
import com.ritik.rjcatering.R;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.to
 * create an instance of this fragment.
 */
public class AccountFragment extends Fragment {


    public AccountFragment() {
        // Required empty public constructor
    }

    private TextView rj_catering,version_text;
    private CircleImageView account_image;
    private TextView account_name;
    private TextView account_phone;
    private TextView account_email;
    private TextView account_dob;
    private TextView account_gender;
    private Button log_out;

    private TextView favourites;

    private TextView shareUs, emailUs, update;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        rj_catering = view.findViewById(R.id.rj_catering);
        account_image = view.findViewById(R.id.account_photo);
        account_name = view.findViewById(R.id.account_name);
        account_phone = view.findViewById(R.id.account_phone);
        account_email = view.findViewById(R.id.account_email);
        account_dob = view.findViewById(R.id.account_dob);
        account_gender = view.findViewById(R.id.account_gender);
        log_out = view.findViewById(R.id.log_out);
        favourites = view.findViewById(R.id.favourites);
        version_text=view.findViewById(R.id.version_text);

        shareUs = view.findViewById(R.id.share_us);
        emailUs = view.findViewById(R.id.email_us);
        update = view.findViewById(R.id.check_for_updates);

        rj_catering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), MainActivity.class));
                getActivity().finish();
            }
        });

        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            account_name.setText(task.getResult().getString("userName"));
                            account_phone.setText(task.getResult().getString("userPhone"));
                            account_email.setText(task.getResult().getString("email"));
                            account_dob.setText(task.getResult().getString("dateOfBirth"));
                            account_gender.setText(task.getResult().getString("gender"));


                            if (task.getResult().getString("gender").toLowerCase().equals("male") ||
                                    task.getResult().getString("gender").toLowerCase().equals("m")) {
                                account_image.setImageResource(R.drawable.user_male);
                            } else if (task.getResult().getString("gender").toLowerCase().equals("female") ||
                                    task.getResult().getString("gender").toLowerCase().equals("f")) {
                                account_image.setImageResource(R.drawable.user_female);
                            } else {
                                account_image.setImageResource(R.drawable.user_others);
                            }


                        } else {
                            Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });


        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();

            }
        });
//        -----------------------------------------

        favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), FavsActivity.class));
            }
        });


//        --------------------------------------------

        checkUpdates();

        shareUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Download RJ Catering App");

                FirebaseFirestore.getInstance().collection("APP_UPDATES")
                        .document("RJ_CATERING")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            String app_url = task.getResult().getString("app_url");
                            shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download RJ Catering App\n\n\n" + app_url);
                            startActivity(Intent.createChooser(shareIntent, "Share via"));

                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

        emailUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String subject = "Query / Complaint";
                String email = "rjcatering1608@gmail.com";
                String mail = "mailto:" + email + "?&subject=" + Uri.encode(subject);

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(mail));

                try {
                    startActivity(Intent.createChooser(emailIntent, "Send Email via"));
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                }


            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    String version = packageInfo.versionName;


                    FirebaseFirestore.getInstance().collection("APP_UPDATES")
                            .document("RJ_CATERING")
                            .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                String originalversion = task.getResult().getString("version_name");
                                if (originalversion.equals(version)) {

                                    Drawable toastBG = getContext().getDrawable(R.drawable.background_color_gradient_nav);

                                    DynamicToast.Config.getInstance()
                                            .setDefaultBackgroundColor(getContext().getResources().getColor(R.color.white))
                                            .setDefaultTintColor(getContext().getResources().getColor(R.color.white))
                                            .setToastBackground(toastBG)
                                            .apply();

                                    DynamicToast.make(getContext(), "Already Updated", 1000).show();

//                                    Toast.makeText(getContext(), "Already Updated", Toast.LENGTH_SHORT).show();


                                    update.setText("Updated");
                                    update.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));

                                } else {
                                    update.setText("Update Available");
                                    update.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.myColor)));

                                    String url = task.getResult().getString("app_url");
                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                    startActivity(Intent.createChooser(intent, "Browse with"));
                                }

                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });


//        --------------------------------------------

        return view;
    }

    private void checkUpdates() {

        try {
            PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            String version = packageInfo.versionName;

            version_text.setText("v "+version);


            FirebaseFirestore.getInstance().collection("APP_UPDATES")
                    .document("RJ_CATERING")
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        String originalversion = task.getResult().getString("version_name");
                        if (originalversion.equals(version)) {
//                            Toast.makeText(getContext(), "Already Updated", Toast.LENGTH_SHORT).show();
                            update.setText("Updated");
                            update.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.textColor)));

                        } else {
                            update.setText("Update Available");
                            update.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.myColor)));

                        }

                    } else {
                        String error = task.getException().getMessage();
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }
                }
            });


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

}