package com.example.toptop.Login_Authecation;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.toptop.HomeActivity;
import com.example.toptop.LoginActivity;
import com.example.toptop.Models.User;
import com.example.toptop.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GoogleActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 101;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private DatabaseReference db ;
    private FirebaseUser mUser;
    private ProgressDialog mloadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mloadingBar = new ProgressDialog(this);
        mloadingBar.setMessage("Đăng nhập bằng Google...");
        mloadingBar.show();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        db= FirebaseDatabase.getInstance().getReference("User");


        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        Intent signInGGIt = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInGGIt,RC_SIGN_IN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount ac = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(ac.getIdToken());
            } catch (ApiException e) {
                mloadingBar.dismiss();
                Toast.makeText(this, "lỗi "+e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            mloadingBar.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            updateUI(user);
                                //nguoi dung da dang nhap
                        }else{
                            Toast.makeText(GoogleActivity.this, "Đăng Nhập Thất Bại"+task.getException(), Toast.LENGTH_SHORT).show();
                            mloadingBar.dismiss();
                            finish();
                            Intent itent = new Intent(GoogleActivity.this, LoginActivity.class);
                            itent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(itent);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        db.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                     db.child(user.getUid()).child("user_name").setValue(user.getDisplayName());
                     db.child(user.getUid()).child("photoUrl").setValue(user.getPhotoUrl().toString());
                 }else {
                     User user1= new User( user.getUid(),user.getDisplayName(),user.getPhotoUrl().toString());
                     db.child(user.getUid()).setValue(user1);
                 }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Intent intent = new Intent(GoogleActivity.this, HomeActivity.class);
        startActivity(intent);
//        Intent itent = new Intent(GoogleActivity.this, SigupActivity.class);
//        itent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(itent);
    }

}