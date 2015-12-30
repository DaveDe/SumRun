package add.on.dave.sumrun;

import java.util.ArrayList;
import java.util.Random;


public class GreatestPath3 {

    private Tile[][] tiles3;

    public GreatestPath3(){

        tiles3 = new Tile[3][3];

        for(int i = 0; i < tiles3.length; i++){

            for(int j = 0; j < tiles3[i].length; j++){

                Random r = new Random();
                int rand = r.nextInt(2+GameView.global)+1;
                Coordinates c = new Coordinates(j,i,rand);
                tiles3[i][j] = new Tile(c,3);
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
            coordinates.get(i).value = tiles3[coordinates.get(i).y][coordinates.get(i).x].c.value;
            coordinates.get(i).left = tiles3[coordinates.get(i).y][coordinates.get(i).x].c.left;
            coordinates.get(i).right = tiles3[coordinates.get(i).y][coordinates.get(i).x].c.right;
            coordinates.get(i).top = tiles3[coordinates.get(i).y][coordinates.get(i).x].c.top;
            coordinates.get(i).bottom = tiles3[coordinates.get(i).y][coordinates.get(i).x].c.bottom;
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

        //test different starting conditions to determine patterns
        if((Math.abs(c2.x-c1.x) == 1) && (Math.abs(c2.y-c1.y) == 1)){
            ret = patternCorner(c1,c2);
        }else if((Math.abs(c2.x-c1.x) == 2) && (Math.abs(c2.y-c1.y) == 0)){
            ret = patternX(c1,c2);
        }else if((Math.abs(c2.y-c1.y) == 2) && (Math.abs(c2.x-c1.x) == 0)){
            ret = patternY(c1,c2);
        }

        return ret;

    }

    public int patternCorner(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align x
        if(c1.x > c2.x){
            return c1.value + patternCorner(c1.left,c2);
        }
        if(c1.x < c2.x){
            return c1.value + patternCorner(c1.right,c2);
        }

        //align y
        if(c1.y > c2.y){
            return c1.value + patternCorner(c1.top,c2);
        }
        if(c1.y < c2.y){
            return c1.value + patternCorner(c1.bottom,c2);
        }

        return 0;
    }

    public int patternX(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align x
        if(c1.x > c2.x){
            return c1.value + patternX(c1.left,c2);
        }
        if(c1.x < c2.x){
            return c1.value + patternX(c1.right,c2);
        }

        return 0;
    }

    public int patternY(Coordinates c1, Coordinates c2){

        //base case
        if(c1.x == c2.x && c1.y == c2.y){
            return c2.value;
        }

        //align y
        if(c1.y > c2.y){
            return c1.value + patternY(c1.top,c2);
        }
        if(c1.y < c2.y){
            return c1.value + patternY(c1.bottom,c2);
        }

        return 0;
    }

    public void printTiles(){
        for(int i = 0; i < tiles3.length; i++){

            for(int j = 0; j < tiles3[i].length; j++){
                System.out.print(tiles3[i][j].c.value+" ");
            }
            System.out.println();

        }
        System.out.println();
        System.out.println();
    }


    public Tile[][] getTiles(){
        return tiles3;
    }

    public void setPointers(){

        //row 1
        tiles3[0][0].c.right = tiles3[0][1].c;
        tiles3[0][0].c.bottom = tiles3[1][0].c;

        tiles3[0][1].c.right = tiles3[0][2].c;
        tiles3[0][1].c.bottom = tiles3[1][1].c;
        tiles3[0][1].c.left = tiles3[0][0].c;

        tiles3[0][2].c.left = tiles3[0][1].c;
        tiles3[0][2].c.bottom = tiles3[1][2].c;

        //row 2
        tiles3[1][0].c.right = tiles3[1][1].c;
        tiles3[1][0].c.bottom = tiles3[2][0].c;
        tiles3[1][0].c.top = tiles3[0][0].c;

        tiles3[1][1].c.right = tiles3[1][2].c;
        tiles3[1][1].c.bottom = tiles3[2][1].c;
        tiles3[1][1].c.left = tiles3[1][0].c;
        tiles3[1][1].c.top = tiles3[0][1].c;

        tiles3[1][2].c.left = tiles3[1][1].c;
        tiles3[1][2].c.bottom = tiles3[2][2].c;
        tiles3[1][2].c.top = tiles3[0][2].c;

        //row 3
        tiles3[2][0].c.right = tiles3[2][1].c;
        tiles3[2][0].c.top = tiles3[1][0].c;

        tiles3[2][1].c.right = tiles3[2][2].c;
        tiles3[2][1].c.top = tiles3[1][1].c;
        tiles3[2][1].c.left = tiles3[2][0].c;

        tiles3[2][2].c.left = tiles3[2][1].c;
        tiles3[2][2].c.top = tiles3[1][2].c;

    }

}
