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
	me.a = prng.nextDouble() * me.AMAX;
	me.vt += Math.PI * nextSignedDouble() * dt;
    }
}
