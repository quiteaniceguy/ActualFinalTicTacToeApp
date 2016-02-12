package com.example.octopoco.actualfinaltictactoeapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;



/**
 * Created by octopoco on 2/18/2015.
 */
public class MainFragment extends Fragment{

    private static final String DIALOG_DATE="this is whatever i want it to be";
    private String[][] board={{"-","-","-"},{"-","-","-"},{"-","-","-"}};
    private final String tag="cat";
    TableLayout tableLayout;
    ///must be reset each run
    Integer[][] placeValues=new Integer[3][3];

    private int currentPlayer=0;



    private int[] playerMask={0,0};
    private static final String EMPTY_CHAR="-";
    public static final String[] PLAYER_CHAR={"X","O"};
    public enum Win_P{
        ROW(7),COL(73),DIAG1(273),DIAG2(84),FULL_BOARD(511);

        private final int value;
        private Win_P(int value){
            this.value=value;
        }
        public int getValue(){
            return value;
        }
    }





    ///implement onCreateMethod
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Tic Tac Toe");
    }
    ///implement onCreateView method
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        int counter=0;
        View v=inflater.inflate(R.layout.mainfragmentlayout, parent, false);

        tableLayout=(TableLayout)v.findViewById(R.id.tablelayout1);
        ///button listener
        View.OnClickListener listener=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id=v.getId();
                if(id==0){
                    System.out.println("its zero, it juist doesn't want to say");
                    return;
                }
                changeBoard(id);
                System.out.println(String.valueOf(v.getId()));
                aiTurn();
            }
        };
        ///set button listener for each button
        for(int i=0;i<tableLayout.getChildCount();i++){
            TableRow row=(TableRow)tableLayout.getChildAt(i);
            for(int j=0;j<row.getChildCount();j++){
                Button button=(Button)row.getChildAt(j);
                button.setText(board[i][j]);
                button.setOnClickListener(listener);
                button.setId(counter);
                counter++;

            }
        }

        return v;
    }

    public void startDialogFragment(String player){
        FragmentManager fm=getActivity().getSupportFragmentManager();
        WinDialogFragment dialog= WinDialogFragment.newInstance(player, this);
        dialog.show(fm, DIALOG_DATE);
    }

    public boolean checkIfWinning(){
        // checks horizontal three in a row
        for(int x=0;x < 3;x++){
            int rowMask = (Win_P.ROW.getValue() << (x*3));
            if((rowMask & playerMask[currentPlayer]) == rowMask){
                return true;
            }
        }
        // checks vertical three in a row
        for(int x=0;x < 3;x++){
            int colMask = (Win_P.COL.getValue() << x);
            if((colMask & playerMask[currentPlayer]) == colMask){
                return true;
            }
        }
        // check diagonals
        if((Win_P.DIAG1.getValue() & playerMask[currentPlayer]) == Win_P.DIAG1.getValue()){
            return true;
        }
        if((Win_P.DIAG2.getValue() & playerMask[currentPlayer]) == Win_P.DIAG2.getValue()){
            return true;
        }
        return false;
    }
    ///changes board and checks winning player
    private void changeBoard(int id){
        playerMask[currentPlayer] |= (1<<id); // Current player takes id
        printBoard();
        ///checks for winning player
        if(checkIfWinning()){
            getActivity().setTitle("PLAYER "+PLAYER_CHAR[currentPlayer]+" WON");
            resetGame();

        }
        ///checks for ties
        if(!checkIfFilled()){
            getActivity().setTitle("DRAW");
            resetGame();
        }
        changeCurrentPlayer();
    }

    ///makes display equal to board
    /// makes display equal to board
    private void printBoard(){
        for(int i=0,id=0;i<tableLayout.getChildCount();i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button button = (Button) row.getChildAt(j);
                // Determine text
                button.setText(getCurrentDisplayCharForId(id++));
            }
        }
    }

    private String getCurrentDisplayCharForId(int id){
        if((playerMask[0]&(1<<id)) > 0){
            return PLAYER_CHAR[0];
        }else if((playerMask[1]&(1<<id)) > 0){
            return PLAYER_CHAR[1];
        }
        return EMPTY_CHAR;
    }
    private void changeCurrentPlayer(){
        if(++currentPlayer==2){
            currentPlayer=0;
        }
    }
    public void resetGame() {
        /// simply resest board by setting values to 0
        playerMask[0]=playerMask[1]=0;
        printBoard();
    }
    private boolean checkIfFilled(){
        for(String[] x:board){
            for(String y:x){
                if(y=="-"){
                    return true;
                }
            }
        }
        return false;
    }
    private static final Random random = new Random();
    private void aiTurn() {
        int bestId = -1;
        int outcome = -2;
        int res;
        for(int id=0;id<9;id++){
            if((playerMask[0]&(1<<id)) > 0) continue; // already set by player 0
            if((playerMask[1]&(1<<id)) > 0) continue; // already set by player 1
            playerMask[currentPlayer] |= (1<<id);
            if(checkIfWinning()){
                res = 1;
            }else{
                changeCurrentPlayer();
                res = evaluatePath();
                changeCurrentPlayer();
            }
            if(outcome < res || (outcome == res && random.nextBoolean())){
                outcome = res;
                bestId = id;
            }
            playerMask[currentPlayer] -= (1<<id);
        }
        changeBoard(bestId);
    }
    // -1 => lose
// 0 => draw
// 1 => win
    //for testing purpose only. may delete later
///return value of path for each available move
    private int evaluatePath() {
        if(!checkIfFilled()){
            return 0;
        }
        int best = -2;
        for(int id=0;id<9;id++){
            if((playerMask[0]&(1<<id)) > 0) continue; // already set by player 0
            if((playerMask[1]&(1<<id)) > 0) continue; // already set by player 1
            playerMask[currentPlayer] |= (1<<id);
            if(checkIfWinning()){
                return 1;
            }
            changeCurrentPlayer();
            best = Math.max(best, evaluatePath()*-1);
            changeCurrentPlayer();
            playerMask[currentPlayer] -= (1<<id);
        }
        return best;
    }

}