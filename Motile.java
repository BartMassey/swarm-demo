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

/** Abstract class of things that move. */
abstract public class Motile extends Thing {
    /** Simulation timestep in seconds, taken from
     *  {@link .Playfield.DT} for convenience. */
    double dt = Playfield.DT;
    /** Enables random behavior. */
    static Random prng = new Random();

    /** Convenience routine for getting a random signed
     *  floating-point number. */
    double nextSignedDouble() {
	return 2.0 * prng.nextDouble() - 1.0;
    }

    /** Create a motile on the given playfield. */
    public Motile(Playfield playfield) {
	super(playfield);
    }

    /** Check for collision with a wall.
     *
     *  @return True iff a wall has been collided with.
     */
    public boolean wallCollision() {
	return (x - r <= 0.0 || x + r >= 1.0 ||
		y - r <= 0.0 || y + r >= 1.0);
    }

    /** Called when a motile colides with something. */
    abstract public void thump();

    /** Called to advance a motile's state by a timestep. */
    abstract public void step();
}
