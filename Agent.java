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

abstract public class Agent extends Motile {
    double t;
    double v, vt;
    double a, at;
    double dt = Playfield.DT;
    AI ai;

    public Agent(Playfield playfield) {
	super(playfield);
    }

    abstract public void thump();

    public void step() {
	double x0 = x, y0 = y, t0 = t;
	x += v * Math.cos(t) * dt;
	y += - v * Math.sin(t) * dt;
	t += vt * dt;
	while (t < 0)
	    t += 2 * Math.PI;
	while (t >= 2 * Math.PI)
	    t -= 2 * Math.PI;
       	if (playfield.collision(this)) {
	    x = x0; y = y0; t = t0;
	    return;
	}
	v += a * dt;
	vt += at * dt;
    }

    public void control(Agent me, Agent[] agents, Thing[] things) {
	MeView meV = new MeView();
	meV.t = me.t;
	meV.vt = me.vt;
	meV.x = me.x;
	meV.y = me.y;
	meV.r = me.r;
	meV.a = me.a;
	meV.at = me.at;
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
	me.at = meV.at;
	me.a = meV.a;
    }
}
