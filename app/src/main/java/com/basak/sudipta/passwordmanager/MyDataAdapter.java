package com.basak.sudipta.passwordmanager;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MyDataAdapter extends ArrayAdapter<myData> {

    private Context mContext;
    private List<myData> myDataList = new ArrayList<>();

    public MyDataAdapter(@NonNull Context context, ArrayList<myData> list){
        super(context, 0 , list);
        mContext = context;
        myDataList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View listItem = convertView;
        if (listItem == null){
            listItem = LayoutInflater.from(mContext).inflate(R.layout.contents,parent,false);
        }

        myData myData = myDataList.get(position);

        TextView idView = listItem.findViewById(R.id.id);
        idView.setText(myData.getId());

        TextView titleView = listItem.findViewById(R.id.dataTitle);
        titleView.setText(myData.getTitle());

        TextView usernameView = listItem.findViewById(R.id.dataUsername);
        usernameView.setText(myData.getUsername());

        TextView passwordView = listItem.findViewById(R.id.dataPassword);
        passwordView.setText(myData.getPassword());

        return listItem;
    }
}
