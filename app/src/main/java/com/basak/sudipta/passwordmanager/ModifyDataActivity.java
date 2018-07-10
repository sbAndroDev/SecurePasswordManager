package com.basak.sudipta.passwordmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class ModifyDataActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText modTitle, modUsername, modPassword;
    private Long _id;
    private DBManager dbManager ;

    EncryptionX encryptionX = new EncryptionX();

    String encryptedTitle, encryptedUsername, encryptedPassword ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_modify_data);

        dbManager = new DBManager(this);
        dbManager.open();

        modTitle = findViewById(R.id.modTitle);
        modUsername = findViewById(R.id.modUsername);
        modPassword = findViewById(R.id.modPassword);

        Button modModify = findViewById(R.id.modModify);
        Button modDelete = findViewById(R.id.modDelete);
        Button modCancel = findViewById(R.id.modCancel);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String title = intent.getStringExtra("title");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        _id = Long.parseLong(id);

        modTitle.setText(title);
        modUsername.setText(username);
        modPassword.setText(password);

        modModify.setOnClickListener(this);
        modCancel.setOnClickListener(this);
        modDelete.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.modModify:
                String myAppEncryptionKeyStr = EncryptionX.encryptionKey;

                String title = modTitle.getText().toString().trim();
                String username = modUsername.getText().toString().trim();
                String password = modPassword.getText().toString().trim();

                try {
                    encryptedTitle = encryptionX.encryptX(title,myAppEncryptionKeyStr);
                    encryptedUsername = encryptionX.encryptX(username,myAppEncryptionKeyStr);
                    encryptedPassword = encryptionX.encryptX(password,myAppEncryptionKeyStr);
                } catch (UnsupportedEncodingException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException e) {
                    e.printStackTrace();
                }

                if (title.isEmpty() || username.isEmpty() || password.isEmpty()){
                    if(title.isEmpty()) modTitle.setError("Fill the field");
                    if(username.isEmpty()) modUsername.setError("Fill the field");
                    if(password.isEmpty()) modPassword.setError("Fill the field");
                }
                else dbManager.update(_id,encryptedTitle,encryptedUsername,encryptedPassword);
                returnHome();
                break;
            case R.id.modDelete:
                dbManager.delete(_id);
                returnHome();
                break;
            case R.id.modCancel:
                finish();
                break;
        }
    }
    private void returnHome(){
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
