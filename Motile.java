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

abstract public class Motile extends Thing {
    double dt = Playfield.DT;
    static Random prng = new Random();

    double nextSignedDouble() {
	return 2.0 * prng.nextDouble() - 1.0;
    }

    public Motile(Playfield playfield) {
	super(playfield);
    }

    public boolean wallCollision() {
	return (x - r <= 0.0 || x + r >= 1.0 ||
		y - r <= 0.0 || y + r >= 1.0);
    }

    abstract public void thump();

    abstract public void step();
}
