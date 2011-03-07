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
}
