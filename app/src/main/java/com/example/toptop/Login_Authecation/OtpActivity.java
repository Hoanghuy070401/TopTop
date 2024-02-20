package com.example.toptop.Login_Authecation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;
import com.example.toptop.HomeActivity;
import com.example.toptop.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OtpActivity extends AppCompatActivity {
    private TextView alertOTp,numberText;
    private PinView pinView;
    private Button btnVerify;
    private ProgressBar progressBarOTP;

    FirebaseAuth mAuth= FirebaseAuth.getInstance();;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        progressBarOTP = findViewById(R.id.prosesOTP);
        alertOTp = findViewById(R.id.alertOTP);
        pinView = findViewById(R.id.pinView);
        btnVerify= findViewById(R.id.buttonOTP);
        numberText= findViewById(R.id.textView6);



        progressBarOTP.setVisibility(View.GONE);
        alertOTp.setVisibility(View.GONE);
        String verificationId= getIntent().getStringExtra("verificationId");
        String number= getIntent().getStringExtra("number");
        numberText.setText("We sent the otp code to your given"+number );
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, pinView.getText().toString());
                signInWithPhoneAuthCredential(credential);
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            if (user!=null){
                                SendToHomeActivity();
                            }else {
                                Intent intent= new Intent(OtpActivity.this,SigupActivity.class);
                                startActivity(intent);
                            }
                            // Update UI

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
    private void SendToHomeActivity() {
        Intent intent = new Intent(OtpActivity.this, HomeActivity.class);
        startActivity(intent);
    }
}