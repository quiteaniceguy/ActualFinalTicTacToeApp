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

    private static final String[] PLAYER_CHAR={"X","O"};
    private static final String EMPTY_CHAR="-";
    private int currentPlayer=0;





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
                switch(v.getId()){
                    case 0:
                        if(board[0][0]=="-") {
                            changeBoard(0, 0);
                            changePlayerCounter();
                            aiTurn();
                            Log.d(tag, "00");
                        }
                        break;

                    case 1:
                        if(board[0][1]=="-") {
                            changeBoard(0, 1);
                            changePlayerCounter();
                            aiTurn();
                        }
                        break;

                    case 2:
                        if(board[0][2]=="-") {
                            changeBoard(0, 2);
                            changePlayerCounter();
                            aiTurn();
                        }
                        break;

                    case 3:
                        if(board[1][0]=="-") {
                            changeBoard(1, 0);
                            changePlayerCounter();
                            aiTurn();
                        }
                        break;

                    case 4:
                        if(board[1][1]=="-") {
                            changeBoard(1, 1);
                            changePlayerCounter();
                            aiTurn();
                            Log.d(tag, "11");
                        }
                        break;

                    case 5:
                        if(board[1][2]=="-") {
                            changeBoard(1, 2);
                            changePlayerCounter();
                            aiTurn();
                        }
                        break;

                    case 6:
                        if(board[2][0]=="-") {
                            changeBoard(2, 0);
                            changePlayerCounter();
                            aiTurn();
                        }
                        break;

                    case 7:
                        if(board[2][1]=="-") {
                            changeBoard(2, 1);
                            changePlayerCounter();
                            aiTurn();
                        }
                        break;

                    case 8:
                        if(board[2][2]=="-") {
                            changeBoard(2, 2);
                            changePlayerCounter();
                            aiTurn();
                            Log.d(tag, "22");
                        }
                        break;

                }

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

    public boolean checkIfWinning(String[][] board,String player){
        ///checks horizontal three in a row
        String winningmessage="you won player "+player;
        for(int x=0;x<3;x++){
            int counter=0;
            for(int y=0;y<3;y++){

                if(board[x][y]==player){

                    counter++;
                }
            }
            if(counter==3){

                return true;
            }
        }
        for(int x=0;x<3;x++){
            int counter=0;
            for(int y=0;y<3;y++){

                if(board[y][x]==player){
                    counter++;
                }
            }
            if(counter==3){

                return true;
            }
        }
        int counter2=0;
        int counter3=0;
        for(int xy=0;xy<3;xy++){
            if(board[xy][xy]==player){
                counter2++;
            }
            if(board[xy][2-xy]==player){
                counter3++;
            }
        }
        if(counter2==3 || counter3==3){

            return true;
        }


        return false;

    }
    ///changes board and checks winning player
    private void changeBoard(int x, int y){
        board[x][y]=PLAYER_CHAR[currentPlayer];
        printBoard();
        ///checks for winning player
        if(checkIfWinning(board,PLAYER_CHAR[currentPlayer])){
            getActivity().setTitle("PLAYER "+PLAYER_CHAR[currentPlayer]+" WON");
            resetGame();

        }
        ///checks for ties
        if(true!=checkIfFilled()){
            getActivity().setTitle("DRAW");
            resetGame();
        }

    }

    ///makes display equal to board
    private void printBoard(){
        for(int i=0;i<tableLayout.getChildCount();i++) {
            TableRow row = (TableRow) tableLayout.getChildAt(i);
            for (int j = 0; j < row.getChildCount(); j++) {
                Button button = (Button) row.getChildAt(j);
                button.setText(board[i][j]);
            }
        }
    }
    private void changePlayerCounter(){
        if(++currentPlayer==2){
            currentPlayer=0;
        }
    }
    public void resetGame(){
        ///resest board
        for(int x=0;x<3;x++){
            for(int y=0;y<3;y++){
                board[x][y]="-";
            }
        }

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
    private void aiTurn() {
        //coordiantes for final move
        String player;
        int x=0;
        int y=0;
        if (PLAYER_CHAR[currentPlayer] == "O") {
            player = "X";
        } else {
            player = "O";
        }

        ArrayList<Integer> bestxMoves=new ArrayList<Integer>();
        ArrayList<Integer> bestyMoves=new ArrayList<Integer>();


        String[][] fakeboard = board;
        String[][] fakeboard2;
        String debugMessage="";



        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int pathValue2=5;
                if (fakeboard[i][j] == "-") {
                    fakeboard[i][j] ="O";
                    if (checkIfWinning(fakeboard, "O")) {
                        pathValue2 = 10;





                    }
                    else if (pathValue2 != 10) {

                        pathValue2=evaluatePath("X",fakeboard);


                    }

                    placeValues[i][j]=pathValue2;
                    fakeboard[i][j] = "-";
                }
            }
        }

        int highestValueMove=0;
        ///finds move with the highest value;
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]=="-") {
                    if (placeValues[i][j] >= highestValueMove) {
                        highestValueMove = placeValues[i][j];
                    }
                }
            }
        }
        int counter=0;
        ////makes list of highest value move
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                if(board[i][j]=="-") {
                    if (placeValues[i][j] == highestValueMove) {
                        bestxMoves.add(i);
                        bestyMoves.add(j);
                        counter++;
                    }
                }
            }
        }
        Random random=new Random();
        int randomBestMove;
        if(bestxMoves.size()>=1) {
            randomBestMove = random.nextInt(bestxMoves.size());
            x=bestxMoves.get(randomBestMove);
            y=bestyMoves.get(randomBestMove);
        }else{
            x=1;
            y=1;
            getActivity().setTitle(String.valueOf(counter));
        }





        changeBoard(x, y);
        changePlayerCounter();
        String bestxs="";
        for(int i:bestxMoves){
            bestxs+=String.valueOf(i);
        }
        String bestys="";
        for(int i: bestyMoves){
            bestys+=String.valueOf(i);
        }
        for(int i=0;i<3;i++){
            for(int j=0;j<3;j++){
                placeValues[i][j]=5;
            }
        }
        Log.d(tag, bestxs+"    "+bestys+"    "+highestValueMove+"     "+debugMessage);
    }
    //for testing purpose only. may delete later
///return value of path for each available move
    private int evaluatePath(String Player, String[][] fakeboard) {

        switch (Player) {
            case "X":
                int returnValue = 10;
                boolean checkIfLoopRan=false;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int pathValue = 5;
                        if (fakeboard[i][j] == "-") {
                            checkIfLoopRan=true;
                            fakeboard[i][j] = "X";
                            if (checkIfWinning(fakeboard, "X")) {
                                pathValue = 0;


                            } else if (pathValue != 0) {

                                ///what does pathValue even do!?!?!
                                pathValue = evaluatePath("O",fakeboard);



                            }
                            if (pathValue < returnValue) {
                                returnValue = pathValue;
                            }
                            fakeboard[i][j] = "-";
                        }







                    }
                }
                if(!checkIfLoopRan){
                    returnValue=5;
                }
                return returnValue;



            case "O":
                int returnValue2 = 0;
                boolean checkIfLoopRan2=false;
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        int pathValue = 5;
                        if (fakeboard[i][j] == "-") {
                            checkIfLoopRan2=true;
                            fakeboard[i][j] = "O";
                            if (checkIfWinning(fakeboard, "O")) {


                                pathValue = 10;


                            } else if (pathValue != 10) {



                                pathValue=evaluatePath("X",fakeboard);


                            }
                            if (pathValue > returnValue2) {
                                returnValue2 = pathValue;
                            }

                            fakeboard[i][j] = "-";
                        }



                    }
                }
                if(!checkIfLoopRan2){
                    returnValue2=5;
                }
                return returnValue2;

        }
        return 5;


    }

}