package com.ritik.rjcatering;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhoneNumber, editTextOTP;
    private Button sendOTPBtn, verifyOTPBtn;

    private TextView resend_otp;

    String phonenumber;
    String otpid;
    FirebaseAuth firebaseAuth;


    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;

    private Dialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhoneNumber = findViewById(R.id.phone_number);
        editTextOTP = findViewById(R.id.enter_otp);
        sendOTPBtn = findViewById(R.id.send_otp);
        verifyOTPBtn = findViewById(R.id.verify_btn);
        resend_otp = findViewById(R.id.resend_otp);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();


        loadingDialog = new Dialog(LoginActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView loading_text = loadingDialog.findViewById(R.id.loading_text);
        loading_text.setText("Verifying...");


        sendOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextPhoneNumber.getText().toString())) {

                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                    DynamicToast.Config.getInstance()
                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                            .setDefaultTintColor(getResources().getColor(R.color.white))
                            .setToastBackground(toastBG)
                            .apply();

                    DynamicToast.make(LoginActivity.this, "Please enter a Valid Number!", 1000).show();





//                    Toast.makeText(LoginActivity.this, "Please enter a Valid Number!", Toast.LENGTH_SHORT).show();
                } else if (editTextPhoneNumber.getText().toString().length() != 10) {

                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                    DynamicToast.Config.getInstance()
                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                            .setDefaultTintColor(getResources().getColor(R.color.white))
                            .setToastBackground(toastBG)
                            .apply();

                    DynamicToast.make(LoginActivity.this, "Phone Number Length should be 10", 1000).show();



//                    Toast.makeText(LoginActivity.this, "Phone Number Length should be 10", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        phonenumber = "+91" + editTextPhoneNumber.getText().toString();
                        sendOTP(phonenumber);
                        editTextOTP.setVisibility(View.VISIBLE);
                        verifyOTPBtn.setVisibility(View.VISIBLE);
                        verifyOTPBtn.setEnabled(true);
                        sendOTPBtn.setEnabled(false);
//                        resend_otp.setVisibility(View.VISIBLE);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        resend_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(editTextPhoneNumber.getText().toString())) {
                    Toast.makeText(LoginActivity.this, "Please enter a Valid Number!", Toast.LENGTH_SHORT).show();
                } else if (editTextPhoneNumber.getText().toString().length() != 10) {
                    Toast.makeText(LoginActivity.this, "Phone Number Length should be 10", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        phonenumber = "+91" + editTextPhoneNumber.getText().toString();
                        sendOTP(phonenumber);
                        editTextOTP.setVisibility(View.VISIBLE);
                        verifyOTPBtn.setVisibility(View.VISIBLE);
                        verifyOTPBtn.setEnabled(true);
                        sendOTPBtn.setEnabled(false);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        verifyOTPBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(editTextOTP.getText().toString())) {


                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                    DynamicToast.Config.getInstance()
                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                            .setDefaultTintColor(getResources().getColor(R.color.white))
                            .setToastBackground(toastBG)
                            .apply();

                    DynamicToast.make(LoginActivity.this, "Please enter OTP!", 1000).show();




//                    Toast.makeText(getApplicationContext(), "Please enter OTP!", Toast.LENGTH_LONG).show();
                } else if (editTextOTP.getText().toString().length() != 6) {


                    Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                    DynamicToast.Config.getInstance()
                            .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                            .setDefaultTintColor(getResources().getColor(R.color.white))
                            .setToastBackground(toastBG)
                            .apply();

                    DynamicToast.make(LoginActivity.this, "Invalid OTP!", 1000).show();




//                    Toast.makeText(getApplicationContext(), "Invalid OTP!", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        loadingDialog.setCancelable(false);
                        loadingDialog.show();
                        verifyCode(editTextOTP.getText().toString());
                        verifyOTPBtn.setEnabled(false);
                    } catch (Exception e) {
                        Toast.makeText(LoginActivity.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }


    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(otpid, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void sendOTP(String number) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(firebaseAuth)
                        .setPhoneNumber(number)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                          @Override
                                          public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                              String sms = phoneAuthCredential.getSmsCode();
                                              if (sms != null) {
                                                  verifyCode(sms);
                                                  editTextOTP.setText(sms);
                                              }
                                          }

                                          @Override
                                          public void onVerificationFailed(@NonNull FirebaseException e) {
                                              loadingDialog.dismiss();
                                              Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                                          }

                                          @Override
                                          public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                              super.onCodeSent(s, forceResendingToken);
                                              otpid = s;
                                          }
                                      }
                        )
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            loadingDialog.dismiss();
                            user = firebaseAuth.getCurrentUser();
                            if (user != null) {

//                                -------------------------------


                                List<String> userIDsList = new ArrayList<>();

                                FirebaseFirestore.getInstance().collection("USERS")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {

                                            for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                                userIDsList.add(documentSnapshot.getId());
                                            }

                                            if (userIDsList.contains(FirebaseAuth.getInstance().getUid())) {
                                                startActivity(new Intent(LoginActivity.this, SetUserInfoActivity.class)
                                                        .putExtra("alreadyExist", "Yes"));
                                                finish();
                                            } else {
                                                String userID = user.getUid();
                                                UsersModel usersModel = new UsersModel(
                                                        userID,
                                                        "",
                                                        user.getPhoneNumber(),
                                                        "",
                                                        "",
                                                        ""
                                                );

                                                firebaseFirestore.collection("USERS")
                                                        .document(userID)
                                                        .set(usersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        loadingDialog.show();


                                                        CollectionReference userDataReference = firebaseFirestore.collection("USERS").document(firebaseAuth.getUid()).collection("USER_DATA");



                                                        //////////maps
                                                        Map<String, Object> wishlistmap = new HashMap<>();
                                                        wishlistmap.put("list_size", (long) 0);

                                                        Map<String, Object> ratingsmap = new HashMap<>();
                                                        ratingsmap.put("list_size", (long) 0);

                                                        Map<String, Object> cartmap = new HashMap<>();
                                                        cartmap.put("list_size", (long) 0);

                                                        Map<String, Object> myaddressesmap = new HashMap<>();
                                                        myaddressesmap.put("list_size", (long) 0);

                                                        Map<String, Object> notificationsmap = new HashMap<>();
                                                        notificationsmap.put("list_size", (long) 0);


                                                        //////////maps

                                                        List<String> documentNames = new ArrayList<>();
                                                        documentNames.add("MY_WISHLIST");
                                                        documentNames.add("MY_RATINGS");
                                                        documentNames.add("MY_CART");
                                                        documentNames.add("MY_ADDRESSES");
                                                        documentNames.add("MY_NOTIFICATIONS");

                                                        List<Map<String, Object>> documentFields = new ArrayList<>();
                                                        documentFields.add(wishlistmap);
                                                        documentFields.add(ratingsmap);
                                                        documentFields.add(cartmap);
                                                        documentFields.add(myaddressesmap);
                                                        documentFields.add(notificationsmap);


                                                        for (int x = 0; x < documentNames.size(); x++) {
                                                            int finalX = x;
                                                            userDataReference.document(documentNames.get(x))
                                                                    .set(documentFields.get(x)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        if (finalX == documentNames.size() - 1) {
                                                                            loadingDialog.dismiss();
                                                                            startActivity(new Intent(LoginActivity.this, SetUserInfoActivity.class)
                                                                                    .putExtra("alreadyExist", "No"));
                                                                            finish();
                                                                        }
                                                                    } else {
                                                                        Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }

                                                                }
                                                            });
                                                        }


//                                                        ---------------------------


                                                    }
                                                });

                                            }
//                    }
                                            Log.d("bdhsgpeshgeg", FirebaseAuth.getInstance().getUid());

                                        } else {
                                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });


//                                -------------------------------


                            } else {

                                Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                                DynamicToast.Config.getInstance()
                                        .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                        .setDefaultTintColor(getResources().getColor(R.color.white))
                                        .setToastBackground(toastBG)
                                        .apply();

                                DynamicToast.make(LoginActivity.this, "Something Error", 1000).show();



//                                Toast.makeText(LoginActivity.this, "Something Error", Toast.LENGTH_SHORT).show();
                            }

                        } else {


                            Drawable toastBG = getDrawable(R.drawable.background_color_gradient_nav);

                            DynamicToast.Config.getInstance()
                                    .setDefaultBackgroundColor(getResources().getColor(R.color.white))
                                    .setDefaultTintColor(getResources().getColor(R.color.white))
                                    .setToastBackground(toastBG)
                                    .apply();

                            DynamicToast.make(LoginActivity.this, "Sign In Code Error", 1000).show();



//                            Toast.makeText(getApplicationContext(), "Sign In Code Error", Toast.LENGTH_LONG).show();
                        }
                    }
                }).

                addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingDialog.dismiss();
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}