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

/** Sample agent. Little bug with some physics and a cute icon. */
public class Bug extends Agent {

    /** Make a bug on the given playfield. */
    public Bug(Playfield playfield) {
	super(playfield);
	r = 0.02;
	x = prng.nextDouble();
	y = prng.nextDouble();
	t = Math.PI * nextSignedDouble();
	v = 0;
	vt = 0;
	a = 0;
	AMIN = -0.5;
	AMAX = 2.0;
	VTMIN = - 2.0 * Math.PI;
	VTMAX = 2.0 * Math.PI;
	CDRAG = 100.0;
	ai = new BugAI();
    }

    /** Paint the bug on the screen.
     *
     *  @param g   Graphics context for painting.
     *  @param d   Scale parameter.
     */
    public void paintComponent(Graphics g, int d) {
	g.setColor(Color.blue);
	int x0 = (int) Math.floor(x * d + 0.5);
	int y0 = (int) Math.floor(y * d + 0.5);
	int r0 = (int) Math.floor(r * d + 0.5);
	int x1 = x0 - (int) Math.floor(1.3 * r0 * Math.cos(t) + 0.5);
	int y1 = y0 - (int) Math.floor(- 1.3 * r0 * Math.sin(t) + 0.5);
	g.drawOval(x0 - r0, y0 - r0, 2 * r0, 2 * r0);
	g.setColor(Color.red);
	g.drawLine(x0, y0, x1, y1);
    }

    /** When the bug collides, its velocity and acceleration
     *  go to zero. */
    public void thump() {
	v = 0; a = 0;
    }
}
