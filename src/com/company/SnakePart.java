package com.company;

public final class SnakePart extends Piece {

    public SnakePart(int x, int y){
        super(0,0xffff00,"Part",x, y);

        this.isEatable = false;
    }

}
