package add.on.dave.sumrun;

import java.util.ArrayList;
import java.util.Random;

public class GreatestPath {

    private Tile[][] tiles;
    private boolean pattern5A = false;
    private boolean pattern5B = false;

    public GreatestPath(){

        tiles = new Tile[5][5];

        for(int i = 0; i < tiles.length; i++){

            for(int j = 0; j < tiles[i].length; j++){

                Random r = new Random();
                int rand = r.nextInt(2+GameView.global)+1;
                Coordinates c = new Coordinates(j,i,rand);
                tiles[i][j] = new Tile(c,5);
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
            coordinates.get(i).value = tiles[coordinates.get(i).y][coordinates.get(i).x].c.value;
            coordinates.get(i).left = tiles[coordinates.get(i).y][coordinates.get(i).x].c.left;
            coordinates.get(i).right = tiles[coordinates.get(i).y][coordinates.get(i).x].c.right;
            coordinates.get(i).top = tiles[coordinates.get(i).y][coordinates.get(i).x].c.top;
            coordinates.get(i).bottom = tiles[coordinates.get(i).y][coordinates.get(i).x].c.bottom;
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
        int p4 = 0;
        int p5 = 0;
        int p6 = 0;

        //test different starting conditions to determine patterns
        if((Math.abs(c2.x-c1.x) == 2) && (Math.abs(c2.y-c1.y) == 2) ){
            p1 = pattern1X(c1,c2);
            p2 = pattern1Y(c1,c2);
            p3 = pattern2Y(c1,c2);
            p4 = pattern2X(c1,c2);
            p5 = patternException1(c1,c2);
            p6 = patternException2(c1,c2);
        }else if((Math.abs(c2.x-c1.x) > 1) && (Math.abs(c2.y-c1.y) == 1)){//changed >= to >
            p1 = pattern1X(c1,c2);
            p2 = pattern1Y(c1,c2);
            p3 = pattern2Y(c1,c2);
            p4 = pattern2Y(c2,c1);
        }else if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) > 1)){
            p1 = pattern1X(c1,c2);
            p2 = pattern1Y(c1,c2);
            p3 = pattern2X(c1,c2);
            p4 = pattern2X(c2,c1);
        }else if((Math.abs(c2.x-c1.x) == 0) && (Math.abs(c2.y-c1.y) == 2)){
            p1 = pattern3XLeft(c1,c2);
            p2 = pattern3XRight(c1,c2);
            p3 = pattern4XLeft(c1,c2);
            p4 = pattern4XRight(c1,c2);
            p5 = pattern4XLeft(c2,c1);
            p6 = pattern4XRight(c2,c1);
        }else if((Math.abs(c2.y-c1.y) == 0) && (Math.abs(c2.x-c1.x) == 2)){
            p1 = pattern3YTop(c1,c2);
            p2 = pattern3YBottom(c1,c2);
            p3 = pattern4XTop(c1,c2);
            p4 = pattern4XBottom(c1,c2);
            p5 = pattern4XTop(c2,c1);
            p6 = pattern4XBottom(c2,c1);
        }else if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 1)){
            p1 = pattern5A(c1,c2);
            pattern5A = false;
            p2 = pattern5B(c1,c2);
            pattern5B = false;
            p3 = pattern5A(c2,c1);
            pattern5A = false;
            p4 = pattern5B(c2,c1);
            pattern5B = false;
        }else{
            p1 = pattern1X(c1,c2);
            p2 = pattern1Y(c1,c2);
        }

        ArrayList<Integer> arr = new ArrayList<Integer>();
        arr.add(p1);
        arr.add(p2);
        arr.add(p3);
        arr.add(p4);
        arr.add(p5);
        arr.add(p6);

        for(int i: arr){
            if(i > ret){
                ret = i;
            }
        }

        return ret;

    }

    public int pattern5B(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //moves left twice
        if((c1.x > 1)&&(c1.x - c2.x == 1) && !(pattern5B)){
            pattern5B = true;
            return c1.value + c1.left.value + pattern5B(c1.left.left,c2);
        }
        //moves right twice
        if((c1.x < 3)&&(c1.x - c2.x == -1) && (!pattern5B)){
            pattern5B = true;
            return c1.value + c1.right.value + pattern5B(c1.right.right,c2);
        }

        //align y
        if((c2.y - c1.y > 0) && (Math.abs(c2.x - c1.x) == 1)){
            return c1.value + pattern5B(c1.bottom,c2);
        }
        if((c2.y - c1.y < 0) && (Math.abs(c2.x - c1.x) == 1)){
            return c1.value + pattern5B(c1.top,c2);
        }
        //connect
        if((c1.y == c2.y) && (c2.x - c1.x == 1)){
            return c1.value + pattern5B(c1.right,c2);
        }
        if((c1.y == c2.y) && (c2.x - c1.x == -1)){
            return c1.value + pattern5B(c1.left,c2);
        }

        //System.out.println("PATTERN 5B NO PATH EXISTS");
        return 0;
    }

    public int pattern5A(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //moves up twice
        if((c1.y > 1)&&(c1.y - c2.y == 1) && !(pattern5A)){
            pattern5A = true;
            return c1.value + c1.top.value + pattern5A(c1.top.top,c2);
        }
        //moves down twice
        if((c1.y < 3)&&(c1.y - c2.y == -1) && (!pattern5A)){
            pattern5A = true;
            return c1.value + c1.bottom.value + pattern5A(c1.bottom.bottom,c2);
        }

        //align x
        if((c2.x - c1.x > 0) && (Math.abs(c2.y - c1.y) == 1)){
            return c1.value + pattern5A(c1.right,c2);
        }
        if((c2.x - c1.x < 0) && (Math.abs(c2.y - c1.y) == 1)){
            return c1.value + pattern5A(c1.left,c2);
        }
        //connect
        if((c1.x == c2.x) && (c2.y - c1.y == 1)){
            return c1.value + pattern5A(c1.bottom,c2);
        }
        if((c1.x == c2.x) && (c2.y - c1.y == -1)){
            return c1.value + pattern5A(c1.top,c2);
        }

        //System.out.println("PATTERN 5A NO PATH EXISTS");
        return 0;
    }

    public int pattern4XBottom(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move 1
        if((c1.y < 4) && (c1.y == c2.y) && (Math.abs(c2.x-c1.x) == 2)){
            return c1.value + pattern4XBottom(c1.bottom,c2);
        }

        //move 2
        if((c1.x-c2.x == 2)&&(c1.y - c2.y > 0) ){
            return c1.value + pattern4XBottom(c1.left,c2);
        }
        if((c1.x-c2.x == -2)&&(c1.y - c2.y > 0) ){
            return c1.value + pattern4XBottom(c1.right,c2);
        }

        //move 3
        if((Math.abs(c2.y-c1.y) == 1)&&(Math.abs(c2.x-c1.x) == 1)){
            return c1.value + pattern4XBottom(c1.top,c2);
        }

        //move 4
        if((c1.y == c2.y)&&(c1.x-c2.x == 1)){
            return c1.value + pattern4XBottom(c1.left,c2);
        }
        if((c1.y == c2.y)&&(c1.x-c2.x == -1)){
            return c1.value + pattern4XBottom(c1.right,c2);
        }

        //System.out.println("NO BOTTOM PATH EXISTS");

        return 0;
    }

    public int pattern4XTop(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move 1
        if((c1.y > 0) && (c1.y == c2.y) && (Math.abs(c2.x-c1.x) == 2)){
            return c1.value + pattern4XTop(c1.top,c2);
        }

        //move 2
        if((c1.x-c2.x == 2)&&(c1.y - c2.y < 0) ){
            return c1.value + pattern4XTop(c1.left,c2);
        }
        if((c1.x-c2.x == -2)&&(c1.y - c2.y < 0) ){
            return c1.value + pattern4XTop(c1.right,c2);
        }

        //move 3
        if((Math.abs(c2.y-c1.y) == 1)&&(Math.abs(c2.x-c1.x) == 1)){
            return c1.value + pattern4XTop(c1.bottom,c2);
        }

        //move 4
        if((c1.y == c2.y)&&(c1.x-c2.x == 1)){
            return c1.value + pattern4XTop(c1.left,c2);
        }
        if((c1.y == c2.y)&&(c1.x-c2.x == -1)){
            return c1.value + pattern4XTop(c1.right,c2);
        }

        //System.out.println("NO TOP PATH EXISTS");

        return 0;
    }

    public int pattern4XRight(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move 1
        if((c1.x < 4) && (c1.x == c2.x) && (Math.abs(c2.y-c1.y) == 2)){
            return c1.value + pattern4XRight(c1.right,c2);
        }

        //move 2
        if((c1.y-c2.y == 2)&&(c1.x - c2.x > 0) ){
            return c1.value + pattern4XRight(c1.top,c2);
        }
        if((c1.y-c2.y == -2)&&(c1.x - c2.x > 0) ){
            return c1.value + pattern4XRight(c1.bottom,c2);
        }

        //move 3
        if((Math.abs(c2.y-c1.y) == 1)&&(Math.abs(c2.x-c1.x) == 1)){
            return c1.value + pattern4XRight(c1.left,c2);
        }

        //move 4
        if((c1.x == c2.x)&&(c1.y-c2.y == 1)){
            return c1.value + pattern4XRight(c1.top,c2);
        }
        if((c1.x == c2.x)&&(c1.y-c2.y == -1)){
            return c1.value + pattern4XRight(c1.bottom,c2);
        }

        //System.out.println("NO RIGHT PATH EXISTS");

        return 0;
    }

    public int pattern4XLeft(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move 1
        if((c1.x > 0) && (c1.x == c2.x) && (Math.abs(c2.y-c1.y) == 2)){
            return c1.value + pattern4XLeft(c1.left,c2);
        }

        //move 2
        if((c1.y-c2.y == 2)&&(c1.x - c2.x < 0) ){
            return c1.value + pattern4XLeft(c1.top,c2);
        }
        if((c1.y-c2.y == -2)&&(c1.x - c2.x < 0) ){
            return c1.value + pattern4XLeft(c1.bottom,c2);
        }

        //move 3
        if((Math.abs(c2.y-c1.y) == 1)&&(Math.abs(c2.x-c1.x) == 1)){
            return c1.value + pattern4XLeft(c1.right,c2);
        }

        //move 4
        if((c1.x == c2.x)&&(c1.y-c2.y == 1)){
            return c1.value + pattern4XLeft(c1.top,c2);
        }
        if((c1.x == c2.x)&&(c1.y-c2.y == -1)){
            return c1.value + pattern4XLeft(c1.bottom,c2);
        }

        //System.out.println("NO LEFT PATH EXISTS");

        return 0;
    }

    //y is aligned
    public int pattern3YBottom(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //disalign y (top)
        if((c1.y < 4) && (c1.y == c2.y) && (Math.abs(c2.x-c1.x) == 2)){
            return c1.value + pattern3YBottom(c1.bottom,c2);
        }

        //align x
        if((Math.abs(c1.y-c2.y) == 1) && (c1.x- c2.x > 0)){
            return c1.value + pattern3YBottom(c1.left,c2);
        }
        if((Math.abs(c1.y-c2.y) == 1) && (c1.x- c2.x < 0)){
            return c1.value + pattern3YBottom(c1.right,c2);
        }

        //align y
        if((c1.x == c2.x) && (c2.y-c1.y == -1)){
            return c1.value + pattern3YBottom(c1.top,c2);
        }

        //System.out.println("NO BOTTOM PATH EXISTS");

        return 0;
    }
    //y is aligned
    public int pattern3YTop(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //disalign y (top)
        if((c1.y > 0) && (c1.y == c2.y) && (Math.abs(c2.x-c1.x) == 2)){
            return c1.value + pattern3YTop(c1.top,c2);
        }

        //align x
        if((Math.abs(c1.y-c2.y) == 1) && (c1.x- c2.x > 0)){
            return c1.value + pattern3YTop(c1.left,c2);
        }
        if((Math.abs(c1.y-c2.y) == 1) && (c1.x- c2.x < 0)){
            return c1.value + pattern3YTop(c1.right,c2);
        }

        //align y
        if((c1.x == c2.x) && (c2.y-c1.y == 1)){
            return c1.value + pattern3YTop(c1.bottom,c2);
        }

        //System.out.println("NO TOP PATH EXISTS");

        return 0;
    }

    //x is aligned
    public int pattern3XLeft(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //disalign x (left)
        if((c1.x > 0) && (c1.x == c2.x) && (Math.abs(c2.y-c1.y) == 2)){
            return c1.value + pattern3XLeft(c1.left,c2);
        }
        //align y
        if((Math.abs(c1.x-c2.x) == 1) && (c1.y- c2.y > 0)){
            return c1.value + pattern3XLeft(c1.top,c2);
        }
        if((Math.abs(c1.x-c2.x) == 1) && (c1.y- c2.y < 0)){
            return c1.value + pattern3XLeft(c1.bottom,c2);
        }

        //align x
        if((c1.y == c2.y) && (c2.x-c1.x == 1)){
            return c1.value + pattern3XLeft(c1.right,c2);
        }

        //System.out.println("NO LEFT PATH EXISTS");

        return 0;
    }
    //x is aligned
    public int pattern3XRight(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //disalign x (right)
        if((c1.x < 4) && (c1.x == c2.x) && (Math.abs(c2.y-c1.y) == 2)){
            return c1.value + pattern3XRight(c1.right,c2);
        }
        //align y
        if((Math.abs(c1.x-c2.x) == 1) && (c1.y- c2.y > 0)){
            return c1.value + pattern3XRight(c1.top,c2);
        }
        if((Math.abs(c1.x-c2.x) == 1) && (c1.y- c2.y < 0)){
            return c1.value + pattern3XRight(c1.bottom,c2);
        }

        //align x
        if((c1.y == c2.y) && (c2.x-c1.x == -1)){
            return c1.value + pattern3XRight(c1.left,c2);
        }

        //System.out.println("NO RIGHT PATH EXISTS");

        return 0;
    }



    //alternate between x and y alignment
    public int pattern2Y(Coordinates c1, Coordinates c2){
        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //if x or y is aligned then finish path directly
        if(c1.x == c2.x || c1.y == c2.y){
            return finishDirectIfAligned(c1,c2);
        }

        //move in x direction first
        if(c2.x - c1.x >= 1 ){
            return c1.value + pattern2X(c1.right,c2);
        }
        if(c2.x - c1.x <= -1 ){
            return c1.value + pattern2X(c1.left,c2);
        }

        System.out.println("SOMETHING WENT WRONG (pattern2Y)");

        return 0;
    }

    //alternate between x and y alignment
    public int pattern2X(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //if x or y is aligned then finish path directly
        if(c1.x == c2.x || c1.y == c2.y){
            return finishDirectIfAligned(c1,c2);
        }

        //move in y direction first
        if(c2.y - c1.y >= 1){
            return c1.value + pattern2Y(c1.bottom,c2);
        }
        if(c2.y - c1.y <= -1){
            return c1.value + pattern2Y(c1.top,c2);
        }

        System.out.println("SOMETHING WENT WRONG (pattern2X)");

        return 0;
    }




    //align x, then align y
    public int pattern1X(Coordinates c1, Coordinates c2){

        //c1 changes, c2 is fixed

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move in horizontal direction of c2
        if(c1.x < c2.x){
            return c1.value + pattern1X(c1.right,c2);
        }
        if(c1.x > c2.x){
            return c1.value + pattern1X(c1.left,c2);
        }
        //move in vertical direction of c2
        if(c1.y < c2.y){
            return c1.value + pattern1X(c1.bottom,c2);
        }
        if(c1.y > c2.y){
            return c1.value + pattern1X(c1.top,c2);
        }
        System.out.println("SOMETHING WENT WRONG");

        return 0;
    }

    //align y, then align x
    public int pattern1Y(Coordinates c1, Coordinates c2){
        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //move in vertical direction of c2
        if(c1.y < c2.y){
            return c1.value + pattern1Y(c1.bottom,c2);
        }
        if(c1.y > c2.y){
            return c1.value + pattern1Y(c1.top,c2);
        }

        //move in horizontal direction of c2
        if(c1.x < c2.x){
            return c1.value + pattern1Y(c1.right,c2);
        }
        if(c1.x > c2.x){
            return c1.value + pattern1Y(c1.left,c2);
        }

        System.out.println("SOMETHING WENT WRONG");

        return 0;
    }

    public int patternException1(Coordinates c1, Coordinates c2){

        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        if( (c2.y - c1.y == 2) && (Math.abs(c1.x - c2.x) == 2) ){
            return c1.value + patternException1(c1.bottom,c2);
        }
        if((c1.y - c2.y == 2) && (Math.abs(c1.x - c2.x) == 2)){
            return c1.value + patternException1(c1.top,c2);
        }
        if((c2.x - c1.x > 0) && (Math.abs(c1.y-c2.y) == 1)){
            return c1.value + patternException1(c1.right,c2);
        }
        if((c2.x - c1.x < 0) && (Math.abs(c1.y-c2.y) == 1)){
            return c1.value + patternException1(c1.left,c2);
        }
        if((c2.y - c1.y == 1)&&(c1.x == c2.x)){
            return c1.value + patternException1(c1.bottom,c2);
        }
        if((c2.y - c1.y == -1)&&(c1.x == c2.x)){
            return c1.value + patternException1(c1.top,c2);
        }

        System.out.println("SOMETHING WENT WRONG IN PATTERN_EXCEPTION_1");

        return 0;
    }

    public int patternException2(Coordinates c1, Coordinates c2){

        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        if( Math.abs(c2.y - c1.y) == 2 && (c1.x - c2.x == 2) ){
            return c1.value + patternException2(c1.left,c2);
        }
        if( Math.abs(c2.y - c1.y) == 2 && (c2.x - c1.x == 2) ){
            return c1.value + patternException2(c1.right,c2);
        }
        if((c2.y - c1.y > 0) && (Math.abs(c1.x-c2.x) == 1)){
            return c1.value + patternException2(c1.bottom,c2);
        }
        if((c2.y - c1.y < 0) && (Math.abs(c1.x-c2.x) == 1)){
            return c1.value + patternException2(c1.top,c2);
        }
        if((c2.x - c1.x == 1)&&(c1.y == c2.y)){
            return c1.value + patternException2(c1.right,c2);
        }
        if((c2.x - c1.x == -1)&&(c1.y == c2.y)){
            return c1.value + patternException2(c1.left,c2);
        }

        System.out.println("SOMETHING WENT WRONG IN PATTERN_EXCEPTION_2");

        return 0;
    }

    public int finishDirectIfAligned(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        if(c1.y == c2.y && c2.x < c1.x){
            return c1.value+ pattern1X(c1.left,c2);
        }
        if(c1.y == c2.y && c2.x > c1.x){
            return c1.value+ pattern1X(c1.right,c2);
        }
        if(c1.x == c2.x && c2.y > c1.y){
            return c1.value+ pattern1Y(c1.bottom,c2);
        }
        if(c1.x == c2.x && c2.y < c1.y){
            return c1.value+ pattern1Y(c1.top,c2);
        }

        System.out.println("SOMETHING WENT WRONG IN FINISH_ALIGN");

        return 0;
    }


    public void printTiles(){
        for(int i = 0; i < tiles.length; i++){

            for(int j = 0; j < tiles[i].length; j++){
                System.out.print(tiles[i][j].c.value+" ");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
    }


    public Tile[][] getTiles(){
        return tiles;
    }

    public void setPointers(){

        //row 1
        tiles[0][0].c.right = tiles[0][1].c;
        tiles[0][0].c.bottom = tiles[1][0].c;

        tiles[0][1].c.right = tiles[0][2].c;
        tiles[0][1].c.bottom = tiles[1][1].c;
        tiles[0][1].c.left = tiles[0][0].c;

        tiles[0][2].c.right = tiles[0][3].c;
        tiles[0][2].c.bottom = tiles[1][2].c;
        tiles[0][2].c.left = tiles[0][1].c;

        tiles[0][3].c.right = tiles[0][4].c;
        tiles[0][3].c.bottom = tiles[1][3].c;
        tiles[0][3].c.left = tiles[0][2].c;

        tiles[0][4].c.bottom = tiles[1][4].c;
        tiles[0][4].c.left = tiles[0][3].c;

        //row 2
        tiles[1][0].c.right = tiles[1][1].c;
        tiles[1][0].c.bottom = tiles[2][0].c;
        tiles[1][0].c.top = tiles[0][0].c;

        tiles[1][1].c.right = tiles[1][2].c;
        tiles[1][1].c.left = tiles[1][0].c;
        tiles[1][1].c.bottom = tiles[2][1].c;
        tiles[1][1].c.top = tiles[0][1].c;

        tiles[1][2].c.right = tiles[1][3].c;
        tiles[1][2].c.left = tiles[1][1].c;
        tiles[1][2].c.bottom = tiles[2][2].c;
        tiles[1][2].c.top = tiles[0][2].c;

        tiles[1][3].c.right = tiles[1][4].c;
        tiles[1][3].c.left = tiles[1][2].c;
        tiles[1][3].c.bottom = tiles[2][3].c;
        tiles[1][3].c.top = tiles[0][3].c;

        tiles[1][4].c.left = tiles[1][3].c;
        tiles[1][4].c.bottom = tiles[2][4].c;
        tiles[1][4].c.top = tiles[0][4].c;

        //row 3
        tiles[2][0].c.right = tiles[2][1].c;
        tiles[2][0].c.bottom = tiles[3][0].c;
        tiles[2][0].c.top = tiles[1][0].c;

        tiles[2][1].c.right = tiles[2][2].c;
        tiles[2][1].c.left = tiles[2][0].c;
        tiles[2][1].c.bottom = tiles[3][1].c;
        tiles[2][1].c.top = tiles[1][1].c;

        tiles[2][2].c.right = tiles[2][3].c;
        tiles[2][2].c.left = tiles[2][1].c;
        tiles[2][2].c.bottom = tiles[3][2].c;
        tiles[2][2].c.top = tiles[1][2].c;

        tiles[2][3].c.right = tiles[2][4].c;
        tiles[2][3].c.left = tiles[2][2].c;
        tiles[2][3].c.bottom = tiles[3][3].c;
        tiles[2][3].c.top = tiles[1][3].c;

        tiles[2][4].c.left = tiles[2][3].c;
        tiles[2][4].c.bottom = tiles[3][4].c;
        tiles[2][4].c.top = tiles[1][4].c;

        //row 4
        tiles[3][0].c.right = tiles[3][1].c;
        tiles[3][0].c.bottom = tiles[4][0].c;
        tiles[3][0].c.top = tiles[2][0].c;

        tiles[3][1].c.right = tiles[3][2].c;
        tiles[3][1].c.left = tiles[3][0].c;
        tiles[3][1].c.bottom = tiles[4][1].c;
        tiles[3][1].c.top = tiles[2][1].c;

        tiles[3][2].c.right = tiles[3][3].c;
        tiles[3][2].c.left = tiles[3][1].c;
        tiles[3][2].c.bottom = tiles[4][2].c;
        tiles[3][2].c.top = tiles[2][2].c;

        tiles[3][3].c.right = tiles[3][4].c;
        tiles[3][3].c.left = tiles[3][2].c;
        tiles[3][3].c.bottom = tiles[4][3].c;
        tiles[3][3].c.top = tiles[2][3].c;

        tiles[3][4].c.left = tiles[3][3].c;
        tiles[3][4].c.bottom = tiles[4][4].c;
        tiles[3][4].c.top = tiles[2][4].c;

        //row 5
        tiles[4][0].c.right = tiles[4][1].c;
        tiles[4][0].c.top = tiles[3][0].c;

        tiles[4][1].c.right = tiles[4][2].c;
        tiles[4][1].c.top = tiles[3][1].c;
        tiles[4][1].c.left = tiles[4][0].c;

        tiles[4][2].c.right = tiles[4][3].c;
        tiles[4][2].c.top = tiles[3][2].c;
        tiles[4][2].c.left = tiles[4][1].c;

        tiles[4][3].c.right = tiles[4][4].c;
        tiles[4][3].c.top = tiles[3][3].c;
        tiles[4][3].c.left = tiles[4][2].c;

        tiles[4][4].c.top = tiles[3][4].c;
        tiles[4][4].c.left = tiles[4][3].c;

    }

}
