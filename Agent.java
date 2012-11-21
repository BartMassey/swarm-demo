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

/** Subclass this to produce an agent. See the {@link .Bug}
 *  class for an example implementation. */
abstract public class Agent extends Motile {
    /** Current angle "theta". */
    double t;
    /** Current angular velocity. */
    double vt;
    /** Current forward velocity. */
    double v;
    /** Current acceleration. */
    double a;
    /** Simulation timestep in seconds, taken from
     *  {@link .Playfield.DT} for convenience. */
    double dt = Playfield.DT;
    /** The {@link .AI} controlling this agent. */
    AI ai;
    /** Minimum acceleration. */
    double AMIN;
    /** Maximum acceleration. */
    double AMAX;
    /** Minimum angular velocity. */
    double VTMIN;
    /** Maximum angular velocity. */
    double VTMAX;
    /** Coefficient of drag. */
    double CDRAG;

    /** Make an {@link .Agent} on the given {@link .Playfield}. */
    public Agent(Playfield playfield) {
	super(playfield);
    }

    /** Method called when the agent collides with something.
     *  This can be used to set the agent's collision behavior. */
    abstract public void thump();

    /** Move the agent's state forward by a timestep.
     *  One would not normally override this. */
    public void step() {
	double x0 = x, y0 = y, t0 = t;
	x += v * Math.cos(t) * dt;
	y += - v * Math.sin(t) * dt;
	t += vt * dt;
	while (t < - Math.PI)
	    t += 2 * Math.PI;
	while (t >= Math.PI)
	    t -= 2 * Math.PI;
       	if (playfield.collision(this)) {
	    x = x0; y = y0; t = t0;
	    return;
	}
	v += a * dt;
	double drag = r * r * CDRAG;
	if (drag >= 0.95) {
	    System.err.println("internal error: CDRAG too big");
	    System.exit(1);
	}
	v -= v * drag;
    }

    /** Convenience method for clamping a value to a range.
     *
     *  @param v   Value to be clamped.
     *  @param lo   Minimum value.
     *  @param hi   Maximum value.
     *  @return    The clamped value: the value to be clamped,
     *             but limited to be no lower than the minimum
     *             and no higher than the maximum.
     */
    public static double clamp(double v, double lo, double hi) {
	if (v < lo)
	    return lo;
	if (v > hi)
	    return hi;
	return v;
    }

    /** Invoke the {@link AI.#control} method of each
        agent in the simulation with the appropriate
        view. This would not normally be overridden. */
    public void control(Agent[] agents, Thing[] things) {
	MeView meV = new MeView();
	meV.t = t;
	meV.vt = vt;
	meV.x = x;
	meV.y = y;
	meV.r = r;
	meV.a = a;
	meV.AMIN = AMIN;
	meV.AMAX = AMAX;
	meV.VTMIN = VTMIN;
	meV.VTMAX = VTMAX;
	meV.CDRAG = CDRAG;
	AgentView[] aVs = new AgentView[agents.length];
	for (int i = 0; i < aVs.length; i++) {
	    aVs[i] = new AgentView();
	    aVs[i].c = agents[i].getClass();
	    aVs[i].r = agents[i].r;
	    aVs[i].t = agents[i].t;
	    aVs[i].v = agents[i].v;
	    aVs[i].x = agents[i].x;
	    aVs[i].y = agents[i].y;
	}
	if (things != null)
	    throw new Error("internal error: no things yet");
	ai.control(meV, aVs, null);
	a = clamp (meV.a, AMIN, AMAX);
	vt = clamp (meV.vt, VTMIN, VTMAX);
    }
}
