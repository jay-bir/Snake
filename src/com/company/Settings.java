package com.company;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Settings implements KeyListener {

    private int vk;

    public int getVK(int k){
        return k;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        vk = e.getKeyCode();
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
