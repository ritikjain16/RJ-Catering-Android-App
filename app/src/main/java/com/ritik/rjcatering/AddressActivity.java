package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;
import com.ritik.rjcatering.fragments.CartFragment;

import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {


    private ImageView arrow_back;

    private EditText name, flat, street, pincode, state, phone;
    private Button saveBtn;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);


        arrow_back = findViewById(R.id.arrow_back);
        name = findViewById(R.id.address_name);
        flat = findViewById(R.id.address_flat_no);
        street = findViewById(R.id.address_street);
        pincode = findViewById(R.id.address_pincode);
        state = findViewById(R.id.address_state);
        phone = findViewById(R.id.address_phone_no);
        saveBtn = findViewById(R.id.save_address);

        loadingDialog = new Dialog(AddressActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Saving, just a moment...");


        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        Intent intent = getIntent();

        String totalAmount = intent.getStringExtra("totalPrice");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAddress(totalAmount);
            }
        });


        loadingDialog.show();
        FirebaseFirestore.getInstance().collection("USERS")
                .document(FirebaseAuth.getInstance().getUid())
                .collection("USER_DATA").document("MY_ADDRESSES")
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    long list_size = task.getResult().getLong("list_size");
                    if (list_size == 1) {

                        name.setText(task.getResult().getString("name"));
                        flat.setText(task.getResult().getString("flat"));
                        street.setText(task.getResult().getString("street"));
                        pincode.setText(task.getResult().getString("pincode"));
                        state.setText(task.getResult().getString("state"));
                        phone.setText(task.getResult().getString("phone").substring(3, 13));

                        saveBtn.setText("Update Address");

                        loadingDialog.dismiss();
                    } else {
                        saveBtn.setText("Save Address");
                        loadingDialog.dismiss();
                    }

                } else {
                    Toast.makeText(AddressActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }


    private void saveAddress(String amount) {

        loadingDialog.show();
        if (!TextUtils.isEmpty(name.getText().toString())) {
            if (!TextUtils.isEmpty(flat.getText().toString())) {
                if (!TextUtils.isEmpty(street.getText().toString())) {
                    if (!TextUtils.isEmpty(pincode.getText().toString()) && pincode.getText().toString().length() == 6) {
                        if (!TextUtils.isEmpty(state.getText().toString())) {
                            if (!TextUtils.isEmpty(phone.getText().toString()) && phone.getText().toString().length() == 10) {

                                Map<String, Object> addressMap = new HashMap<>();
                                addressMap.put("list_size", (long) 1);
                                addressMap.put("name", name.getText().toString());
                                addressMap.put("flat", flat.getText().toString());
                                addressMap.put("street", street.getText().toString());
                                addressMap.put("phone", "+91" + phone.getText().toString());
                                addressMap.put("pincode", pincode.getText().toString());
                                addressMap.put("state", state.getText().toString());

                                FirebaseFirestore.getInstance().collection("USERS")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .collection("USER_DATA").document("MY_ADDRESSES")
                                        .set(addressMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            loadingDialog.dismiss();
                                            Intent intent = new Intent(AddressActivity.this, OrderDetailsActivity.class);

                                            intent.putExtra("name", name.getText().toString());
                                            intent.putExtra("flat", flat.getText().toString());
                                            intent.putExtra("street", street.getText().toString());
                                            intent.putExtra("phone", phone.getText().toString());
                                            intent.putExtra("pincode", pincode.getText().toString());
                                            intent.putExtra("state", state.getText().toString());
                                            intent.putExtra("totalAmount", amount);

                                            startActivity(intent);
                                            finish();

                                        } else {
                                            loadingDialog.dismiss();
                                            Toast.makeText(AddressActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


                            } else {
                                loadingDialog.dismiss();

                                Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                                DynamicToast.Config.getInstance()
                                        .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                        .setDefaultTintColor(getResources().getColor(R.color.white))
                                        .setToastBackground(toastBG)
                                        .apply();

                                DynamicToast.make(AddressActivity.this, "Phone number cannot be empty and length should be equal to 10", 1000).show();


//                                Toast.makeText(this, "Phone number cannot be empty and length should be equal to 10", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            loadingDialog.dismiss();
                            Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                            DynamicToast.Config.getInstance()
                                    .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                    .setDefaultTintColor(getResources().getColor(R.color.white))
                                    .setToastBackground(toastBG)
                                    .apply();

                            DynamicToast.make(AddressActivity.this, "State cannot be empty", 1000).show();


//                            Toast.makeText(this, "State cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        loadingDialog.dismiss();

                        Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                        DynamicToast.Config.getInstance()
                                .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                .setDefaultTintColor(getResources().getColor(R.color.white))
                                .setToastBackground(toastBG)
                                .apply();

                        DynamicToast.make(AddressActivity.this, "Pincode cannot be empty and length should be equal to 6", 1000).show();


//                        Toast.makeText(this, "Pincode cannot be empty and length should be equal to 6", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    loadingDialog.dismiss();

                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                    DynamicToast.Config.getInstance()
                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                            .setDefaultTintColor(getResources().getColor(R.color.white))
                            .setToastBackground(toastBG)
                            .apply();

                    DynamicToast.make(AddressActivity.this, "Street cannot be empty", 1000).show();


//                    Toast.makeText(this, "Street cannot be empty", Toast.LENGTH_SHORT).show();
                }
            } else {
                loadingDialog.dismiss();

                Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                DynamicToast.Config.getInstance()
                        .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                        .setDefaultTintColor(getResources().getColor(R.color.white))
                        .setToastBackground(toastBG)
                        .apply();

                DynamicToast.make(AddressActivity.this, "Flat No. / Building cannot be empty", 1000).show();


//                Toast.makeText(this, "Flat No. / Building cannot be empty", Toast.LENGTH_SHORT).show();
            }
        } else {
            loadingDialog.dismiss();

            Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

            DynamicToast.Config.getInstance()
                    .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                    .setDefaultTintColor(getResources().getColor(R.color.white))
                    .setToastBackground(toastBG)
                    .apply();

            DynamicToast.make(AddressActivity.this, "Name cannot be empty", 1000).show();


//            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show();
        }

    }
}