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

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText prevPass,newPass,confirmNewPass;
    Button save;
    String prevPassStr,newPassStr,confirmNewPassStr,password,theEncryptionKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_change_password);

        password = EncryptionX.passwordKey;

        prevPass = findViewById(R.id.prevPass);
        newPass = findViewById(R.id.newPass);
        confirmNewPass = findViewById(R.id.confirmNewPass);
        save = findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prevPassStr = prevPass.getText().toString().trim();
                newPassStr = newPass.getText().toString().trim();
                confirmNewPassStr = confirmNewPass.getText().toString().trim();
                if (prevPassStr.isEmpty() || newPassStr.isEmpty() || confirmNewPassStr.isEmpty()){
                    if (prevPassStr.isEmpty()) prevPass.setError("Empty Field");
                    if (newPassStr.isEmpty()) newPass.setError("Empty Field");
                    if (confirmNewPassStr.isEmpty()) confirmNewPass.setError("Empty Field");
                }
                else if (newPassStr.length() < 6 || newPassStr.length() > 15){
                    Toast.makeText(getApplicationContext(),"Minimum Length : 6 and \n Maximum Length : 15",Toast.LENGTH_LONG).show();
                }
                else if (isNotPasswordValid(newPassStr)){
                    Toast.makeText(getApplicationContext(),"Passwords Should contain at least one\n uppercase, lowercase, numbers and symbols",Toast.LENGTH_LONG).show();
                }
                else if (!newPassStr.equals(confirmNewPassStr)){
                    Toast.makeText(getApplicationContext(),"Passwords Should Be Same",Toast.LENGTH_SHORT).show();
                }
                else if (prevPassStr.equals(password)){
                    EncryptionX.passwordKey = newPassStr;
                    SharedPreferences settingsX = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settingsX.edit();
                    try {
                        String newPassStrX = (new EncryptionX()).encryptX(newPassStr,newPassStr);
                        theEncryptionKey = (new EncryptionX()).encryptX(EncryptionX.encryptionKey,newPassStrX);
                    } catch (UnsupportedEncodingException | BadPaddingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException e) {
                        e.printStackTrace();
                    }
                    editor.putString("Key",theEncryptionKey);
                    editor.apply();

                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    prevPass.setError("Wrong Password");
                }
            }
        });

    }
    public static boolean isNotPasswordValid(String password){
        final char[] capLetters = new char[]{'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};
        final char[] smallLetters = new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
        final char[] numbers = new char[]{'0','1','2','3','4','5','6','7','8','9'};
        final char[] symbols = new char[]{'~','!','@','#','$','%','^','&','*','(',')','_','-','=','+','`','{','}','[',']',':','<','>',',','.',':',';','?','/','"'};
        int x1 = 0, x2 = 0, x3 = 0, x4 = 0;
        for (int j = 0; j < password.length(); j++) {
            for (char capLetter : capLetters) {
                if (password.charAt(j) == capLetter) {
                    x1 = 1;
                    break;
                }
                x1 = 0;
            }
            if (x1 == 1) break;
        }
        for (int j = 0; j < password.length(); j++) {
            for (char smallLetter : smallLetters){
                if (password.charAt(j) == smallLetter) {
                    x2 = 1;
                    break;
                }
                x2 = 0;
            }
            if (x2 == 1) break;
        }
        for (int j = 0; j < password.length(); j++){
            for (int i = 0; i <= 9; i++) {
                if (password.charAt(j) == numbers[i]) {
                    x3 = 1;
                    break;
                }
                x3 = 0;
            }
            if (x3 == 1) break;
        }
        for (int j = 0; j < password.length(); j++) {
            for (char symbol : symbols) {
                if (password.charAt(j) == symbol) {
                    x4 = 1;
                    break;
                }
                x4 = 0;
            }
            if (x4 == 1) break;
        }
        return ((x1 == 0 || x2 == 0 || x3 == 0 || x4 == 0));
    }
}
