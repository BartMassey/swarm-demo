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

    public void control(MeView me, AgentView[] agents, ThingView[] things) {
	if (things != null)
	    throw new Error("internal error: no things yet");
	me.a = 0;
	me.vt = 0;
	double cx = me.x - 0.5;
	double cy = me.y - 0.5;
	double ct = Math.PI / 2;
	if (Math.abs(cx) <= 0.01) {
	    if (Math.abs(cy) <= 0.01)
		return;
	    if (cy > 0)
		cy = -Math.PI / 2;
	} else {
	    ct = Math.atan(-cy/cx);
	}
	double dt = ct - me.t;
	System.err.println(dt);
	if (Math.abs(dt) > 0.01) {
	    if (dt > 0.01)
		me.vt = me.VTMAX;
	    else if (dt < -0.01)
		me.vt = me.VTMIN;
	    return;
	}
	double d = Math.sqrt(cx * cx + cy * cy);
	if (-me.AMIN * me.v * me.v * 0.5 < d)
	    me.a = -me.AMIN;
	else
	    me.a = me.AMIN;
    }
}
