package com.example.octopoco.actualfinaltictactoeapp;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Created by octopoco on 2/18/2015.
 */
public class MainFragment extends Fragment{

    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v=inflater.inflate(R.layout.mainfragmentlayout, parent, false);

        TableLayout tableLayout=(TableLayout)v.findViewById(R.id.tablelayout1);
        ///button listener
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello");
            }
        };
        ///set button listener for each button
        for(int i=0;i<tableLayout.getChildCount();i++){
            TableRow row=(TableRow)tableLayout.getChildAt(i);
            for(int j=0;j<row.getChildCount();j++){
                Button button=(Button)row.getChildAt(j);
                button.setText("X");
                button.setOnClickListener(listener);
            }
        }

        return v;
    }
}
