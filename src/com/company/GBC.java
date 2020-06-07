package com.company;

import java.awt.*;

public class GBC extends GridBagConstraints {

    public GBC(int gridx, int gridy, int gridheight, int gridwidth){
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridheight = gridheight;
        this.gridwidth = gridwidth;
        this.weightx = 1;
        this.weighty = 1;
    }

    public GBC(int gridx, int gridy){
        this.gridx = gridx;
        this.gridy = gridy;
        this.gridheight = 1;
        this.gridwidth = 1;
        this.weightx = 1;
        this.weighty = 1;
    }

}
