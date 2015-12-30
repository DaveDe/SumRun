package add.on.dave.sumrun;

import java.util.ArrayList;
import java.util.Random;

public class GreatestPath4 {

    private Tile[][] tiles4;

    public GreatestPath4(){

        tiles4 = new Tile[4][4];

        for(int i = 0; i < tiles4.length; i++){

            for(int j = 0; j < tiles4[i].length; j++){

                Random r = new Random();
                int rand = r.nextInt(2+GameView.global)+1;
                Coordinates c = new Coordinates(j,i,rand);
                tiles4[i][j] = new Tile(c,4);
            }

        }

        //instantiate coordinate pointers
        setPointers();

    }

    //find greatest path starting at given tile
    public int findGreatestPath(Tile t){

        int greatest = 0;

        //get coordinates that this tile can reach
        ArrayList<Coordinates> coordinates = t.getReachableTiles();

        //add appropriate value and pointers to each coordinate (otherwise all coordinates in ArrayList have the same value and null pointers)
        for(int i = 0; i < coordinates.size(); i++){
            coordinates.get(i).value = tiles4[coordinates.get(i).y][coordinates.get(i).x].c.value;
            coordinates.get(i).left = tiles4[coordinates.get(i).y][coordinates.get(i).x].c.left;
            coordinates.get(i).right = tiles4[coordinates.get(i).y][coordinates.get(i).x].c.right;
            coordinates.get(i).top = tiles4[coordinates.get(i).y][coordinates.get(i).x].c.top;
            coordinates.get(i).bottom = tiles4[coordinates.get(i).y][coordinates.get(i).x].c.bottom;
        }

        Coordinates focus = t.c;

        //try all paths between each coordinates, return the greatest path
        for(int i = 0; i < coordinates.size(); i++){
            int pathVal = greatestPathBetweenCoordinates(focus,coordinates.get(i));
            if(pathVal > greatest){
                greatest = pathVal;
            }
        }

        return greatest;
    }

    //find greatest path between 2 coordinates
    public int greatestPathBetweenCoordinates(Coordinates c1, Coordinates c2){

        //c2 can't reference any pointers

        int ret = 0;

        int p1 = 0;
        int p2 = 0;
        int p3 = 0;


        //test different starting conditions to determine patterns
        if((Math.abs(c2.x-c1.x) == 0) && (Math.abs(c2.y-c1.y) == 1)){
            p1 = patternAboveBelowLeft(c1,c2);
            p2 = patternAboveBelowRight(c1,c2);
        }else if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 2)){
            p1 = patternAlignXThenY(c1,c2);
            p2 = patternAlignYThenX(c1,c2);
            p3 = patternZigZagA(c1,c2);
        }else if((Math.abs(c2.x-c1.x) == 2) && (Math.abs(c2.y-c1.y) == 1)){
            p1 = patternAlignXThenY(c1,c2);
            p2 = patternAlignYThenX(c1,c2);
            p3 = patternZigZagB(c1,c2);
        }else if((Math.abs(c2.x-c1.x) == 0) && (Math.abs(c2.y-c1.y) == 3)){
            p1 = patternAlignY(c1,c2);
        }else if((Math.abs(c2.x-c1.x) == 3) && (Math.abs(c2.y-c1.y) == 0)){
            p1 = patternAlignX(c1,c2);
        }

        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(p1);
        arr.add(p2);
        arr.add(p3);

        for(int i: arr){
            if(i > ret){
                ret = i;
            }
        }

        return ret;

    }

    public int patternAboveBelowLeft(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move left
        if((Math.abs(c2.x-c1.x) == 0) && (Math.abs(c2.y-c1.y) == 1) && (c1.left != null)){
            return c1.value + patternAboveBelowLeft(c1.left,c2);
        }

        //align y
        if((Math.abs(c2.x-c1.x) == 1) && (c2.y > c1.y)){
            return c1.value + patternAboveBelowLeft(c1.bottom,c2);
        }
        if((Math.abs(c2.x-c1.x) == 1) && (c2.y < c1.y)){
            return c1.value + patternAboveBelowLeft(c1.top,c2);
        }

        //move right
        if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 0)){
            return c1.value + patternAboveBelowLeft(c1.right,c2);
        }

        //System.out.println("this pattern goes out of bounds for this tile");

        return 0;
    }

    public int patternAboveBelowRight(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move right
        if((Math.abs(c2.x-c1.x) == 0) && (Math.abs(c2.y-c1.y) == 1) && (c1.right != null)){
            return c1.value + patternAboveBelowRight(c1.right,c2);
        }

        //align y
        if((Math.abs(c2.x-c1.x) == 1) && (c2.y > c1.y)){
            return c1.value + patternAboveBelowRight(c1.bottom,c2);
        }
        if((Math.abs(c2.x-c1.x) == 1) && (c2.y < c1.y)){
            return c1.value + patternAboveBelowRight(c1.top,c2);
        }

        //move left
        if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 0)){
            return c1.value + patternAboveBelowRight(c1.left,c2);
        }

        //System.out.println("this pattern goes out of bounds for this tile");

        return 0;
    }

    public int patternAlignXThenY(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align x
        if(c1.x > c2.x){
            return c1.value + patternAlignXThenY(c1.left,c2);
        }
        if(c1.x < c2.x){
            return c1.value + patternAlignXThenY(c1.right,c2);
        }

        //align y
        if(c1.y > c2.y){
            return c1.value + patternAlignXThenY(c1.top,c2);
        }
        if(c1.y < c2.y){
            return c1.value + patternAlignXThenY(c1.bottom,c2);
        }

        return 0;
    }

    public int patternAlignYThenX(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align y
        if(c1.y > c2.y){
            return c1.value + patternAlignYThenX(c1.top,c2);
        }
        if(c1.y < c2.y){
            return c1.value + patternAlignYThenX(c1.bottom,c2);
        }

        //align x
        if(c1.x > c2.x){
            return c1.value + patternAlignYThenX(c1.left,c2);
        }
        if(c1.x < c2.x){
            return c1.value + patternAlignYThenX(c1.right,c2);
        }

        return 0;
    }

    public int patternZigZagA(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move down
        if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 2) && (c1.bottom != null)){
            return c1.value + patternZigZagA(c1.bottom,c2);
        }

        //align x
        if((Math.abs(c2.y-c1.y) == 1) && (c2.x > c1.x)){
            return c1.value + patternZigZagA(c1.right,c2);
        }
        if((Math.abs(c2.y-c1.y) == 1) && (c2.x < c1.x)){
            return c1.value + patternZigZagA(c1.left,c2);
        }

        //move down
        if((Math.abs(c2.x-c1.x) == 0) && (Math.abs(c2.y-c1.y) == 1)){
            return c1.value + patternZigZagA(c1.bottom,c2);
        }

        return 0;
    }

    public int patternZigZagB(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move right
        if((Math.abs(c2.x-c1.x) == 2) && (Math.abs(c2.y-c1.y) == 1) && (c1.right != null)){
            return c1.value + patternZigZagB(c1.right,c2);
        }

        //align y
        if((Math.abs(c2.x-c1.x) == 1) && (c2.y > c1.y)){
            return c1.value + patternZigZagB(c1.bottom,c2);
        }
        if((Math.abs(c2.x-c1.x) == 1) && (c2.y < c1.y)){
            return c1.value + patternZigZagB(c1.top,c2);
        }

        //move right
        if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 0)){
            return c1.value + patternZigZagB(c1.right,c2);
        }

        return 0;
    }

    public int patternAlignY(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align y
        if(c1.y > c2.y){
            return c1.value + patternAlignY(c1.top,c2);
        }
        if(c1.y < c2.y){
            return c1.value + patternAlignY(c1.bottom,c2);
        }

        return 0;
    }

    public int patternAlignX(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align x
        if(c1.x > c2.x){
            return c1.value + patternAlignX(c1.left,c2);
        }
        if(c1.x < c2.x){
            return c1.value + patternAlignX(c1.right,c2);
        }

        return 0;
    }


    public void printTiles(){
        for(int i = 0; i < tiles4.length; i++){

            for(int j = 0; j < tiles4[i].length; j++){
                System.out.print(tiles4[i][j].c.value+" ");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
    }


    public Tile[][] getTiles(){
        return tiles4;
    }

    public void setPointers(){

        //row 1
        tiles4[0][0].c.right = tiles4[0][1].c;
        tiles4[0][0].c.bottom = tiles4[1][0].c;

        tiles4[0][1].c.right = tiles4[0][2].c;
        tiles4[0][1].c.bottom = tiles4[1][1].c;
        tiles4[0][1].c.left = tiles4[0][0].c;

        tiles4[0][2].c.right = tiles4[0][3].c;
        tiles4[0][2].c.bottom = tiles4[1][2].c;
        tiles4[0][2].c.left = tiles4[0][1].c;

        tiles4[0][3].c.left = tiles4[0][2].c;
        tiles4[0][3].c.bottom = tiles4[1][3].c;

        //row 2
        tiles4[1][0].c.right = tiles4[1][1].c;
        tiles4[1][0].c.bottom = tiles4[2][0].c;
        tiles4[1][0].c.top = tiles4[0][0].c;

        tiles4[1][1].c.right = tiles4[1][2].c;
        tiles4[1][1].c.bottom = tiles4[2][1].c;
        tiles4[1][1].c.left = tiles4[1][0].c;
        tiles4[1][1].c.top = tiles4[0][1].c;

        tiles4[1][2].c.right = tiles4[1][3].c;
        tiles4[1][2].c.bottom = tiles4[2][2].c;
        tiles4[1][2].c.left = tiles4[1][1].c;
        tiles4[1][2].c.top = tiles4[0][2].c;

        tiles4[1][3].c.left = tiles4[1][2].c;
        tiles4[1][3].c.bottom = tiles4[2][3].c;
        tiles4[1][3].c.top = tiles4[0][3].c;

        //row 3
        tiles4[2][0].c.right = tiles4[2][1].c;
        tiles4[2][0].c.bottom = tiles4[3][0].c;
        tiles4[2][0].c.top = tiles4[1][0].c;

        tiles4[2][1].c.right = tiles4[2][2].c;
        tiles4[2][1].c.bottom = tiles4[3][1].c;
        tiles4[2][1].c.left = tiles4[2][0].c;
        tiles4[2][1].c.top = tiles4[1][1].c;

        tiles4[2][2].c.right = tiles4[2][3].c;
        tiles4[2][2].c.bottom = tiles4[3][2].c;
        tiles4[2][2].c.left = tiles4[2][1].c;
        tiles4[2][2].c.top = tiles4[1][2].c;

        tiles4[2][3].c.left = tiles4[2][2].c;
        tiles4[2][3].c.bottom = tiles4[3][3].c;
        tiles4[2][3].c.top = tiles4[1][3].c;

        //row 4
        tiles4[3][0].c.right = tiles4[3][1].c;
        tiles4[3][0].c.top = tiles4[2][0].c;

        tiles4[3][1].c.right = tiles4[3][2].c;
        tiles4[3][1].c.top = tiles4[2][1].c;
        tiles4[3][1].c.left = tiles4[3][0].c;

        tiles4[3][2].c.right = tiles4[3][3].c;
        tiles4[3][2].c.top = tiles4[2][2].c;
        tiles4[3][2].c.left = tiles4[3][1].c;

        tiles4[3][3].c.left = tiles4[3][2].c;
        tiles4[3][3].c.top = tiles4[2][3].c;

    }

}