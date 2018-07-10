package com.basak.sudipta.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class CreatePasswordActivity extends AppCompatActivity {

    private EditText pass,confirmPass;
    private String passStr,confirmPassStr,myAppEncryptionKeyStr;
    private static final String combinations = "ABCDEFGHIJKLMNOPQRSTUVWXYZ[{(!@#$%&-0123456789+?=*,./)}]abcdefghijklmnopqrstuvwxyz";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_create_password);

        pass = findViewById(R.id.pass);
        confirmPass = findViewById(R.id.confirmPass);
        Button create = findViewById(R.id.create);

        SecureRandom secureRandom = new SecureRandom();
        StringBuilder sb = new StringBuilder(20);
        for (int i=0; i < 20; i++){
            sb.append(combinations.charAt(secureRandom.nextInt(combinations.length())));
        }
        myAppEncryptionKeyStr = sb.toString();

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passStr = pass.getText().toString().trim();
                confirmPassStr = confirmPass.getText().toString().trim();
                if (passStr.isEmpty() || confirmPassStr.isEmpty()){
                    if (passStr.isEmpty()) pass.setError("Empty Field");
                    if (confirmPassStr.isEmpty()) confirmPass.setError("Empty Field");
                }
                else if (passStr.length() < 6 || passStr.length() > 15){
                    Toast.makeText(getApplicationContext(),"Minimum Length : 6\n Maximum Length : 15",Toast.LENGTH_LONG).show();
                }
                else if (ChangePasswordActivity.isNotPasswordValid(passStr)){
                    Toast.makeText(getApplicationContext(),"Passwords Should contain at least one uppercase, lowercase, numbers and symbols",Toast.LENGTH_LONG).show();
                }
                else {
                    if (passStr.equals(confirmPassStr)){
                        SharedPreferences settings = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        EncryptionX.encryptionKey = myAppEncryptionKeyStr;
                        EncryptionX.passwordKey = passStr;
                        try {
                            passStr = (new EncryptionX()).encryptX(passStr,passStr);
                            myAppEncryptionKeyStr = (new EncryptionX()).encryptX(myAppEncryptionKeyStr,passStr);
                        } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                            e.printStackTrace();
                        }
                        editor.putString("Key",myAppEncryptionKeyStr);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(CreatePasswordActivity.this,"Passwords Doesn't Match",Toast.LENGTH_SHORT).show();
                    }
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
