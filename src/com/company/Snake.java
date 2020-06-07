package com.company;

import java.util.ArrayList;

public class Snake {
    private ArrayList<SnakePart> snakeParts = new ArrayList<>();

    public Snake(){
        for(int i = 0; i < 4; i++) {
            snakeParts.add(new SnakePart(10 + i, 8));
        }
        snakeParts.get(0).setColour(0xcc0000);
    }

    public ArrayList<SnakePart> getSnakeParts() {
        return snakeParts;
    }

    public SnakePart getHead(){
        return snakeParts.get(0);
    }

    public SnakePart getTail(){
        return snakeParts.get(snakeParts.size() - 1);
    }

    public void move(Direction d){
        SnakePart p;
        for(int i = snakeParts.size() -1; i > 0; i--){
            SnakePart previous = snakeParts.get(i - 1);
            p = snakeParts.get(i);
            p.setCoordinates(previous.getX(), previous.getY());
        }
        p = snakeParts.get(0);
        p.setCoordinates(p.getX() +d.getX(),p.getY() + d.getY());
    }

    public void addPart(Direction d){
        SnakePart tail = new SnakePart(snakeParts.get(snakeParts.size() - 1).getX(),snakeParts.get(snakeParts.size() - 1).getY());
        move(d);
        snakeParts.add(tail);
    }

    public String opis(){
        String opis = "";
        for(SnakePart part:snakeParts){
            opis+= "X: " + part.getX() + " Y: " +part.getY() + " || ";
        }
        return opis;
    }
}
