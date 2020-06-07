package com.company;

public class Display {
    private int width;
    private int height;
    private int tileSize;
    private int[] pixels;


    public Display(int tilesX, int tilesY, int tileSize){
        this.tileSize = tileSize;
        this.width = tilesX*tileSize;
        this.height = tilesY*tileSize;
        this.pixels =new int[width*height];
    }

    public void render(Piece[][] p){
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                pixels[x + (y * width)] = p[y/tileSize][x/tileSize] != null? p[y/tileSize][x/tileSize].getColour():0;
            }
        }
    }

    public int getPixelAt(int i){
        return pixels[i];
    }

    public int[] getPixels(){
        return pixels;
    }
}
