package com.company;

public final class Upgrade extends Piece{

    public Upgrade(int x, int y){
        super(100,0xff00ff, "upgrade");
        setCoordinates(x,y);
    }
}
