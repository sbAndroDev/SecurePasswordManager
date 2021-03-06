package com.basak.sudipta.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    String password ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        password = settings.getString("Key","");

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (password.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(),CreatePasswordActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Intent intent = new Intent(getApplicationContext(),EnterPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },2000);
    }
}
