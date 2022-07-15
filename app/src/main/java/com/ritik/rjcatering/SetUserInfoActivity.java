package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SetUserInfoActivity extends AppCompatActivity {

    private EditText name,email,dob,gender;
    private Button saveBtn;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_user_info);
        name=findViewById(R.id.name);
        email=findViewById(R.id.enter_email);
        dob=findViewById(R.id.enter_DOB);
        gender=findViewById(R.id.enter_Gender);
        saveBtn = findViewById(R.id.save_btn);


        loadingDialog = new Dialog(SetUserInfoActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Saving...");


        Intent intent = getIntent();
        String alreadyExist = intent.getStringExtra("alreadyExist");



        if (alreadyExist.equals("Yes")) {


            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                FirebaseFirestore.getInstance().collection("USERS")
                        .document(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            name.setText(task.getResult().getString("userName"));
                            email.setText(task.getResult().getString("email"));
                            dob.setText(task.getResult().getString("dateOfBirth"));
                            gender.setText(task.getResult().getString("gender"));

                        } else {
                            Toast.makeText(SetUserInfoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        } else if (alreadyExist.equals("No")) {

        }


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doUpdate();
            }
        });

    }

    private void doUpdate() {


        loadingDialog.setCancelable(false);
        loadingDialog.show();

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {

            Map<String, Object> usersModel = new HashMap<>();
            usersModel.put("userName", name.getText().toString());
            usersModel.put("dateOfBirth", dob.getText().toString());
            usersModel.put("email", email.getText().toString());
            usersModel.put("gender", gender.getText().toString());

            FirebaseFirestore.getInstance().collection("USERS")
                    .document(firebaseUser.getUid()).update(usersModel)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            loadingDialog.dismiss();



                            Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                            DynamicToast.Config.getInstance()
                                    .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                    .setDefaultTintColor(getResources().getColor(R.color.white))
                                    .setToastBackground(toastBG)
                                    .apply();

                            DynamicToast.make(SetUserInfoActivity.this, "Updated Successfully", 1000).show();



//                            Toast.makeText(SetUserInfoActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SetUserInfoActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {


                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                    DynamicToast.Config.getInstance()
                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                            .setDefaultTintColor(getResources().getColor(R.color.white))
                            .setToastBackground(toastBG)
                            .apply();

                    DynamicToast.make(SetUserInfoActivity.this, "Update Failed..", 1000).show();



//                    Toast.makeText(SetUserInfoActivity.this, "Update Failed..", Toast.LENGTH_SHORT).show();

                }
            });


        } else {


            Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

            DynamicToast.Config.getInstance()
                    .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                    .setDefaultTintColor(getResources().getColor(R.color.white))
                    .setToastBackground(toastBG)
                    .apply();

            DynamicToast.make(SetUserInfoActivity.this, "Please Login First...", 1000).show();



//            Toast.makeText(SetUserInfoActivity.this, "Please Login First...", Toast.LENGTH_SHORT).show();
            loadingDialog.dismiss();
        }

    }


}