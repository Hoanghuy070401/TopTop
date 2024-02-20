package com.example.toptop.Login_Authecation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toptop.HomeActivity;
import com.example.toptop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class AuthecationActivity extends AppCompatActivity {
    private EditText mobile;
    private TextView alert;

    private Button actionBtn;
    private ProgressBar progress;
    FirebaseAuth mAuth;
    String Fullnumber;
    private CountryCodePicker countrycode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authecation);
        anhxa();
    }

    private void anhxa() {

        alert = findViewById(R.id.alert);
        mobile = findViewById(R.id.editau1);
        actionBtn = findViewById(R.id.buttonoke);
        progress = findViewById(R.id.prosess);
        countrycode = findViewById(R.id.nunberContry);
        progress.setVisibility(View.GONE);
        alert.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();


        actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number  = mobile.getText().toString().trim();
                if(number.length()==0){
                    Toast.makeText(AuthecationActivity.this, "Please Enter Your Number", Toast.LENGTH_SHORT).show();
                }else {
                    String fullName="+"+countrycode.getFullNumber()+number;
                    AttemptAuth(fullName);
                }
            }
        });
    }

    private void AttemptAuth(String fullName) {
        Fullnumber = fullName;
        progress.setVisibility(View.VISIBLE);
        alert.setVisibility(View.VISIBLE);


        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(fullName)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


    }
        private  PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

               SendToHomeActivity();
                Log.d("TAG", "onVerificationCompleted:" + credential);
                Toast.makeText(AuthecationActivity.this, "Xac Thuc thanh cong ", Toast.LENGTH_SHORT).show();

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Log.w("TAG", "onVerificationFailed", e);
                Toast.makeText(AuthecationActivity.this, "So dien thoai khong hop le  ", Toast.LENGTH_SHORT).show();

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(AuthecationActivity.this, "gioi han SMS", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                    Toast.makeText(AuthecationActivity.this, "loi"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                // Show a message and update the UI
                Intent intent = new Intent(AuthecationActivity.this, SigupActivity.class);
                startActivity(intent);
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {


                Intent intent = new Intent(AuthecationActivity.this,OtpActivity.class);
                intent.putExtra("verificationId",verificationId);
                intent.putExtra("number",Fullnumber);
                Log.d("TAG", "onCodeSent:" + verificationId);
                startActivity(intent);

                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
            }
        };

    private void SendToHomeActivity() {
        Intent intent = new Intent(AuthecationActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(AuthecationActivity.this, "thanh cong", Toast.LENGTH_SHORT).show();

                                Log.d("TAG", "signInWithCredential:success");

                                FirebaseUser user = task.getResult().getUser();
                                if (user!=null){
                                    SendToHomeActivity();
                                }else {
                                    Intent intent= new Intent(AuthecationActivity.this,SigupActivity.class);
                                    startActivity(intent);
                                }


                            } else {
                                // Sign in failed, display a message and update the UI
                                Toast.makeText(AuthecationActivity.this, "fail", Toast.LENGTH_SHORT).show();
                                Log.w("TAG", "signInWithCredential:failure", task.getException());
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    // The verification code entered was invalid
                                }
                            }
                        }
                    });
        }

    }

