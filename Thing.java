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

/** Abstract class of "things" that can be in the simulation.
 *  Currently only implemented by {@link Motile}. */
abstract public class Thing {
    /** Counter for generating a unique ID for each thing. */
    static int nextId = 0;
    /** Unique ID for this thing. */
    public int id = ++nextId;
    /** X coordinate. */
    double x;
    /** Y coordinate. */
    double y;
    /** Radius of thing. (Things are currently presumed to be circular.) */
    double r;
    /** PRNG for thing generation. */
    static Random prng = new Random();
    /** Playfield on which thing will be placed. */
    Playfield playfield;
    /** Name of individual thing (instance). */
    String name;

    /** Create a thing on the given playfield. */
    public Thing(Playfield playfield, String name) {
        this.playfield = playfield;
        this.name = name;
    }

    /** Return True if this thing has collided
     *  with the given other thing. */
    public boolean thingCollision(Thing b) {
        double dx = b.x - x;
        double dy = b.y - y;
        double dr = b.r + r;
        return (dx * dx + dy * dy <= dr * dr);
    }

    /** Draw this thing.
     *
     *  @param g   Graphics context for drawing.
     *  @param d   Scale factor.
     */
    abstract public void paintComponent(Graphics g, int d);
}
