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
	double cx = 0.5 - me.x;
	double cy = 0.5 - me.y;
	double ct = Math.atan2(-cy, cx);
	double d = Math.sqrt(cx * cx + cy * cy);
	double dct = angleDiff(ct, me.t);
	if (d > 0.05) {
	    me.vt = me.VTMAX * dct;
	    if (dct > 0.01)
		return;
	    double v0 = d;
	    me.a = v0 - me.v;
	}
    }
}
