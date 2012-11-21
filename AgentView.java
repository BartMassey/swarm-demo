/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

/** Class that controls what properties of an
 *  agent another agent can see, beyond those
 *  provided by {@link .ThingView}.
 */
public class AgentView extends ThingView {
    /** Agent's angle. */
    double t;
    /** Agent's velocity. */
    double v;
}
