package com.codegreed_devs.driveit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;


public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        int i=0;
        while (i<3000) {

            i++;
        }
        Intent intent=new Intent(this,LoginActivity.class);
        startActivity(intent);

        //initialize toasty


    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser!=null){
            Intent intent= new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        }else{
            Intent intent= new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();



        }


    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }
}
