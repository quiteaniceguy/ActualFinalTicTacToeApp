package com.example.octopoco.actualfinaltictactoeapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by octopoco on 2/18/2015.
 */
public class WinDialogFragment extends android.support.v4.app.DialogFragment {
    private String player=null;
    public Fragment fragment=null;
    public static final String PLAYER="TAG THAT CAN BE WHATEVER I WANT";
    public static WinDialogFragment newInstance(String player,Fragment fragment1){
        Bundle args=new Bundle();
        args.putSerializable(PLAYER, player);
        WinDialogFragment fragment=new WinDialogFragment();
        fragment.setArguments(args);
        fragment.fragment=fragment1;
        return fragment;
    }
    public Dialog onCreateDialog(Bundle savedInstanceState){
        player=(String)getArguments().getSerializable(PLAYER);
        return new AlertDialog.Builder(getActivity()).setTitle("PLAYER "+player+" WON").setNegativeButton("NEW GAME",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int which){
                        fragment.getActivity().finish();
                    }
        }).setPositiveButton("PLAY AGAIN", null).create();
    }
}
