/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;

/** Control parameters of an agent that the
 *  agent itself can know.
 */
public class MeView extends AgentView {
    /** Turn rate. */
    double vt;
    /** Acceleration. */
    double a;
    /** Bounding parameter, for convenience. */
    double AMIN, AMAX, VTMIN, VTMAX, CDRAG;
}
