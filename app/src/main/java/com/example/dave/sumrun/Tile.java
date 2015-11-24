package com.example.dave.sumrun;

import java.util.ArrayList;

public class Tile {

    Coordinates c;
    ArrayList<Coordinates> reachableTiles;

    public Tile(Coordinates c){

        this.c = new Coordinates(c.x,c.y,c.value);

        reachableTiles = new ArrayList<Coordinates>();

        //for tile centered at 1, matrix should look like
			/*
			    X O X O X
			    O X O X O
			    X O X O O
			    O X O O O
			    X O O O O
			*/

        addReachableTiles(c.x,c.y,c.value);

        //shift right-up
        addReachableTiles(c.x+1,c.y-1,c.value);
        addReachableTiles(c.x+2,c.y-2,c.value);

        //shift down-left
        addReachableTiles(c.x-1,c.y+1,c.value);
        addReachableTiles(c.x-2,c.y+2,c.value);

    }

    public void addReachableTiles(int x, int y, int value){
        int flag = 0;
        for(int i = y-2; i <= y+2; i++){

            for(int j = x-2+flag; j <= x+2; j+=2){
                if(j >=0 && i >= 0 && i < 5 && j < 5){
                    Coordinates c = new Coordinates(j,i,value);
                    if(!inArrayList(c)){
                        if(Math.abs(this.c.x - j) + Math.abs(this.c.y - i) <= 4){//ensure tile isnt too far away
                            reachableTiles.add(c);
                        }
                    }
                }
            }
            //add 1 to x for every other row
            if(flag == 0){
                flag = 1;
            }else{
                flag = 0;
            }

        }
    }

    public boolean inArrayList(Coordinates c){

        for(int i = 0; i < reachableTiles.size(); i++){
            if((reachableTiles.get(i).x == c.x) && (reachableTiles.get(i).y == c.y)){
                return true;
            }
        }

        return false;
    }

    public void printReachableCoordinates(){
        for(int i = 0; i < reachableTiles.size(); i++){
            reachableTiles.get(i).print();
        }
    }

    public ArrayList<Coordinates> getReachableTiles(){
        return reachableTiles;
    }

}
