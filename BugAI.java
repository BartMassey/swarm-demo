/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

public class BugAI extends AI {
    static Random prng = new Random();
    
    static double nextSignedDouble() {
	return 2.0 * prng.nextDouble() - 1.0;
    }

    static double normalizeAngle(double a) {
	while (a < 0)
	    a += 2*Math.PI;
	while (a >= 2*Math.PI)
	    a -= 2*Math.PI;
	return a;
    }

    static double dist2(ThingView t1, ThingView t2) {
	double dx = t1.x - t2.x;
	double dy = t1.y - t2.y;
	return dx * dx + dy * dy;
    }
    
    public void control(MeView me, AgentView[] agents, ThingView[] things) {
	if (things != null)
	    throw new Error("internal error: no things yet");
	me.a = 0;
	me.vt = 0;
	AgentView ta = null;
	for (AgentView av : agents) {
	    double d2 = dist2(me, av);
	    if (d2 < 0.0001)
		continue;
	    double tav = Math.atan2(-(av.y - me.y), av.x - me.x);
	    if (d2 > 0.04)
		continue;
	    if (Math.abs(normalizeAngle(tav)) >= Math.PI/8)
		continue;
	    if (ta == null || dist2(me, ta) > d2)
		ta = av;
	}
	if (ta == null) {
	    double cx = 0.5 - me.x;
	    double cy = 0.5 - me.y;
	    double ct = Math.atan2(-cy, cx);
	    me.a = 0.1;
	    me.vt = me.VTMAX * angleDiff(me.t, ct + Math.PI / 2);
	    return;
	}
	double tat = Math.atan2(-(ta.y - me.y), ta.x - me.x);
	double dtt = angleDiff(me.t, tat);
	me.vt = me.VTMAX * dtt;
	me.a = 0.1;
    }
}
