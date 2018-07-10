package com.basak.sudipta.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EnterPasswordActivity extends AppCompatActivity {

    EditText thePass;
    Button theGo;
    String thePassStr,password,theEncryptionKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_enter_password);

        thePass = findViewById(R.id.thePass);
        theGo = findViewById(R.id.theGo);

        theGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                thePassStr = thePass.getText().toString().trim();
                SharedPreferences settingsX = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                theEncryptionKey = settingsX.getString("Key","");
                try {
                    String thePassStrX = (new EncryptionX()).encryptX(thePassStr,thePassStr);
                    EncryptionX.encryptionKey = (new EncryptionX()).decryptX(theEncryptionKey,thePassStrX);
                    EncryptionX.passwordKey = thePassStr;
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (GeneralSecurityException | IOException e) {
                    Log.d("Exception",e.toString());
                    thePass.setError("Wrong Password");
                }
            }
        });
    }
    private long mLastPress = 0;
    Toast onBackPressToast;
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        int TOAST_DURATION = 5000;
        if (currentTime - mLastPress > TOAST_DURATION){
            onBackPressToast = Toast.makeText(this,"Press Again to Exit",Toast.LENGTH_LONG);
            onBackPressToast.show();
            mLastPress = currentTime;
        }else {
            if (onBackPressToast != null){
                onBackPressToast.cancel();
                onBackPressToast = null;
            }
            super.onBackPressed();
        }
    }
}
