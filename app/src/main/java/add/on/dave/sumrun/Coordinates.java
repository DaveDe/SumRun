package add.on.dave.sumrun;

public class Coordinates {

    int x;
    int y;
    int value;
    Coordinates top;
    Coordinates left;
    Coordinates bottom;
    Coordinates right;

    public Coordinates(int x, int y, int value){

        this.x = x;
        this.y = y;
        this.value = value;
        top = null;
        left = null;
        bottom = null;
        right = null;

    }

    public void print(){
        System.out.println("("+x+","+y+") value: "+value);
    }

}
