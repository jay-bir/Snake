package com.company;

public class Piece {
    private int x;
    private int y;
    private int value;
    private int colour;
    private String name;
    public boolean isEatable;


    public Piece(int value, int colour, String name){
        this.value=value;
        this.colour = colour;
        this.name = name;
        this.isEatable = true;
    }

    public String getName(){
        return name;
    }

    public int getValue(){
        return value;
    }

    public int getColour(){
        return colour;
    }


    public void setColour(int c) { this.colour = c;}

    public Piece(int value, int colour, String name, int x, int y){
        this.value=value;
        this.colour = colour;
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public void setCoordinates(int x, int y){
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }

}
