/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

/** Abstract AI for each bug in the swarm. Subclass this to
 *  get your own AI. */
abstract public class AI {
    /** Simulation timestep in seconds, taken from
     *  {@link Playfield#DT} for convenience. */
    double dt = Playfield.DT;

    /** The controller takes in sensor data from the environment
     *  for processing by the AI.
     *
     *  @param me   View of my own state.
     *  @param agents   Array of views of each other agent's state.
     *  @param things   Array of views of state of each thing
     *                  in the environment.
     */
    abstract public void
	control(MeView me, AgentView[] agents, ThingView[] things);

    /** This is just a convenience routine for subtracting
     *  two angles. It has the desirable property that the
     *  difference will always be an angle &leq; &pi;
     *  radians: this will enable turning toward a target
     *  angle in a sane way.
     * 
     *  @param target   Angle to turn toward.
     *  @param current  Current facing angle.
     *  @return   Angular change needed to achieve desired facing.
     */
    public static double angleDiff(double target, double current) {
	if (Math.abs(target - current) > Math.PI)
	    return 2.0 * Math.PI - (current - target);
	return target - current;
    }
}
