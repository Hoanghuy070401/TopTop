package com.example.toptop.Login_Authecation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.toptop.HomeActivity;
import com.example.toptop.Models.User;
import com.example.toptop.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class SigupActivity extends AppCompatActivity {

    private EditText user;
    private EditText birthday1;
    private Button btnSend;
    private ProgressBar progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sigup);
        anhxa();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressBar(SigupActivity.this);
                progressDialog.setVisibility(View.VISIBLE);
                uploadData();
                Intent intent = new Intent(SigupActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhxa() {
        user = findViewById(R.id.editTextUserName);
        birthday1 = findViewById(R.id.editTextBithday);
        btnSend =  findViewById(R.id.btnSignUp);

    }
    private void uploadData(){

        String user_id =FirebaseAuth.getInstance().getCurrentUser().getUid();
        String user_name =user.getText().toString().trim();
        String birthday =birthday1.getText().toString().trim();

        User userObject =new User(user_id,user_name,birthday);
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        DatabaseReference node =db.getReference().child("User").child(user_id);
        node.setValue(userObject);

    };
}