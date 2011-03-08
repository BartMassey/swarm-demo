/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

abstract public class AI {
    double dt = Playfield.DT;

    abstract public void
	control(MeView me, AgentView[] agents, ThingView[] things);

    static double angleDiff(double target, double current) {
	if (Math.abs(target - current) > Math.PI)
	    return 2.0 * Math.PI - (current - target);
	return target - current;
    }
}
