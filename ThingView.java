/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

/** Things that other agents can see. */
public class ThingView {
    /** What class this thing belongs to. */
    Class c;
    /** The name of this thing. Used for debugging output - or maybe other
     * things in the future. */
    String name;
    /** X coordinate. */
    double x;
    /** Y coordinate. */
    double y;
    /** Radius of thing. (Things are currently presumed to be circular.) */
    double r;
}
