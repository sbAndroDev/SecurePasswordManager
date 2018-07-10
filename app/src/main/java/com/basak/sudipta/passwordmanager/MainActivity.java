package com.basak.sudipta.passwordmanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView id, title, username, password;
    private EncryptionX encryptionX;

    protected String encryptedTitle, encryptedUsername, encryptedPassword;
    protected String decryptedTitle, decryptedUsername, decryptedPassword;

    FloatingActionButton addData;

    final int[] to = new int[] {
            R.id.id,
            R.id.dataTitle,
            R.id.dataUsername,
            R.id.dataPassword
    };
    final String[] from = new String[] {
            DatabaseHelper._ID,
            DatabaseHelper.TITLE,
            DatabaseHelper.USERNAME,
            DatabaseHelper.PASSWORD
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_main);

        //SharedPreferences myAppEncryptionKey = getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        String myAppEncryptionKeyStr = EncryptionX.encryptionKey;//myAppEncryptionKey.getString("Key", "");

        encryptionX = new EncryptionX();

        DBManager dbManager = new DBManager(this);
        dbManager.open();
        Cursor cursor = dbManager.fetch();

        ListView dataList = findViewById(R.id.dataList);
        dataList.setEmptyView(findViewById(R.id.emptyData));
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.contents, cursor, from, to, 0);
        adapter.notifyDataSetChanged();
        dataList.setAdapter(adapter);

        MyDataAdapter myDataAdapter ;
        ArrayList<myData> myDataArrayList = new ArrayList<>();
        for (int i = 0; i < dataList.getCount(); i++){
            View v = getViewByPosition(i, dataList);

            id = v.findViewById(R.id.id);
            title = v.findViewById(R.id.dataTitle);
            username = v.findViewById(R.id.dataUsername);
            password = v.findViewById(R.id.dataPassword);

            encryptedTitle = title.getText().toString();
            encryptedUsername = username.getText().toString();
            encryptedPassword = password.getText().toString();

            try {
                decryptedTitle = encryptionX.decryptX(encryptedTitle, myAppEncryptionKeyStr);
                decryptedUsername = encryptionX.decryptX(encryptedUsername, myAppEncryptionKeyStr);
                decryptedPassword = encryptionX.decryptX(encryptedPassword, myAppEncryptionKeyStr);
                myDataArrayList.add(new myData(id.getText().toString(),decryptedTitle,decryptedUsername,decryptedPassword));
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        }
        myDataAdapter = new MyDataAdapter(this,myDataArrayList);
        dataList.setAdapter(myDataAdapter);

        dataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView title = view.findViewById(R.id.dataTitle);
                TextView username = view.findViewById(R.id.dataUsername);
                TextView password = view.findViewById(R.id.dataPassword);
                TextView id = view.findViewById(R.id.id);

                String titleStr = title.getText().toString();
                String usernameStr = username.getText().toString();
                String  passwordStr = password.getText().toString();
                String idStr = id.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(),ModifyDataActivity.class);
                modify_intent.putExtra("id",idStr);
                modify_intent.putExtra("title",titleStr);
                modify_intent.putExtra("username",usernameStr);
                modify_intent.putExtra("password",passwordStr);

                startActivity(modify_intent);
            }
        });

        addData = findViewById(R.id.addData);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.changePassword:
                changePassword();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void changePassword() {
        Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
        startActivity(intent);
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
    public View getViewByPosition(int position, ListView listView){
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        if (position < firstListItemPosition || position > lastListItemPosition){
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        }
        else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }
}
