package com.example.toptop;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toptop.Login_Authecation.AuthecationActivity;
import com.example.toptop.Login_Authecation.FacebookActivity;
import com.example.toptop.Login_Authecation.GoogleActivity;

public class LoginActivity extends AppCompatActivity {
    private TextView btnFacebook ;
    private TextView btnGoogle ;
    private TextView btnPhone ;
    private ProgressBar progress ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inti();
    }

    private void inti() {
        btnFacebook = findViewById(R.id.loginfb);
        btnGoogle = findViewById(R.id.loginGg);
        btnPhone = findViewById(R.id.loginPhone);
        btnFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    //nguoi dung chua dang nhap
                    Intent intent = new Intent(LoginActivity.this, FacebookActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
            }
        });
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, GoogleActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, AuthecationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);

            }
        });
    }
}