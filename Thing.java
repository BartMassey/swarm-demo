/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

abstract public class Thing {
    static int nextId = 0;
    public int id = ++nextId;
    double x, y, r;
    static Random prng = new Random();
    Playfield playfield;

    public Thing(Playfield playfield) {
	this.playfield = playfield;
    }

    public boolean thingCollision(Thing b) {
	double dx = b.x - x;
	double dy = b.y - y;
	double dr = b.r + r;
	return (dx * dx + dy * dy <= dr * dr);
    }

    abstract public void paintComponent(Graphics g, int d);
}

