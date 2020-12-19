package com.vaibhavmojidra.doctorsappointment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;

public class SplashScreen extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        SharedPreferences sharedPreferences=this.getSharedPreferences("STORAGE",MODE_PRIVATE);
        if(sharedPreferences.getBoolean("IS_DARKMODE_ENABLED",false)){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        handler=new Handler();
        runnable=new Runnable() {
            @Override
            public void run() {
                if(FirebaseAuth.getInstance().getCurrentUser()!=null && sharedPreferences.getString("USER_TYPE","NON").trim().equals("PATIENT")){
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this,PatientMainActivity.class));
                    SplashScreen.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    SplashScreen.this.finish();
                }else if(FirebaseAuth.getInstance().getCurrentUser()!=null && sharedPreferences.getString("USER_TYPE","NON").trim().equals("DOCTOR")){
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this,DoctorMainActivity.class));
                    SplashScreen.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    SplashScreen.this.finish();
                }else{
                    SplashScreen.this.startActivity(new Intent(SplashScreen.this,AskDoctorPatient.class));
                    SplashScreen.this.overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
                    SplashScreen.this.finish();
                }
            }
        };
        handler.postDelayed(runnable,2000);
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        super.onDestroy();
    }
}