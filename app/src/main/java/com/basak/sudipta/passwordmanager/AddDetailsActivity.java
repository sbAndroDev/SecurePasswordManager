package com.basak.sudipta.passwordmanager;

import android.content.Intent;
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

public class AddDetailsActivity extends AppCompatActivity {
    Button cancel,save;
    EditText title,username,password;
    private DBManager dbManager;

    private String titleStrEncrypted,usernameStrEncrypted,passwordStrEncrypted;
    private String myAppEncryptionKeyStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_add_details);

        myAppEncryptionKeyStr = EncryptionX.encryptionKey;

        dbManager = new DBManager(this);
        dbManager.open();

        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);
        title = findViewById(R.id.title);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleStr,usernameStr,passwordStr;
                titleStr = title.getText().toString().trim();
                usernameStr = username.getText().toString().trim();
                passwordStr = password.getText().toString().trim();

                EncryptionX encryptionX = new EncryptionX();
                try {
                    titleStrEncrypted = encryptionX.encryptX(titleStr,myAppEncryptionKeyStr);
                    usernameStrEncrypted = encryptionX.encryptX(usernameStr,myAppEncryptionKeyStr);
                    passwordStrEncrypted = encryptionX.encryptX(passwordStr,myAppEncryptionKeyStr);
                } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | NoSuchPaddingException | InvalidKeyException | NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                if (titleStr.isEmpty() || usernameStr.isEmpty() || passwordStr.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill all the boxes",Toast.LENGTH_SHORT).show();
                }

                else {
                    dbManager.insert(titleStrEncrypted,usernameStrEncrypted,passwordStrEncrypted);
                    Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
