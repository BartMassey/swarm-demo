/*
 * Copyright (c) 2011 Bart Massey
 * [This program is licensed under the "MIT License"]
 * Please see the file COPYING in the source
 * distribution of this software for license terms.
 */

import java.lang.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;

/** Simulation playfield class. This is where the mechanics
 *  of the simulation play out, including rendering. See
 *  <a href="http://download.oracle.com/javase/tutorial/uiswing/painting/step1.html">http://download.oracle.com/javase/tutorial/uiswing/painting/step1.html</a>
 *  for the canonical tutorial on painting in Swing.
 */
public class Playfield extends JPanel implements Runnable {
    /** Needed by Java for obscure reasons. */
    static final long serialVersionUID = 0;
    /** Array of agents in the simulation. */
    private Agent[] agents;
    /** Graphics scale factor. */
    private int d;
    /** The simulation thread. */
    private Thread t = null;
    /** Whether the simulation thread is running. */
    private boolean threadSuspended = true;
    /** Enables random simulation behavior. */
    private static Random prng = new Random();
    /** Stroke used to draw things in the simulation.
     *  Thanks to
     *  <a href="http://www.rgagnon.com/javadetails/java-0260.html">http://www.rgagnon.com/javadetails/java-0260.html</a>
     *  for the tip on this.
     */
    public static BasicStroke stroke;
    /** Physics time increment. */
    public static double DT;
    /** Multiple for display time. That is, a new frame
     *  will be drawn every DDT physics time steps.
     */
    public static int DDT;

    /** Shuffles the array of agents for fairness. */
    private static void shuffle(Agent[] agents) {
        for (int i = 0; i < agents.length - 1; i++) {
            int j = prng.nextInt(agents.length - i) + i;
            Agent tmp = agents[j];
            agents[j] = agents[i];
            agents[i] = tmp;
        }
    }

    /** Make a playfield.
     *
     *  @param nagents   Number of agents to put on the field.
     *  @param dt   Simulation timestep.
     *  @param ddt  Display timestep multiplier.
     */
    public Playfield(int nagents, double dt, int ddt) {
        DT = dt;
        DDT = ddt;
        agents = new Agent[nagents];
        for (int i = 0; i < nagents; i++)
            do
                agents[i] = new Bug(this);
            while(collision(agents[i]));
    }

    /** Default size of display. */
    public Dimension getPreferredSize() {
        return new Dimension(250,250);
    }

    /** Returns True iff the given agent
     *  has collided with something: another
     *  agent, a thing, a wall.
     */
    public boolean collision(Agent b) {
        boolean result = false;

        if (b.wallCollision()) {
            result = true;
            b.thump();
        }
        for (int i = 0; i < agents.length; i++) {
            if (agents[i] != null && agents[i].id != b.id &&
                agents[i].thingCollision(b)) {
                result = true;
                b.thump();
                agents[i].thump();
            }
        }
        return result;
    }

    /** Draw the playfield, by drawing each
     *  thing and agent on it. */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Dimension ds = getSize();
        d = Math.min(ds.width, ds.height) - 5;
        float strokeWidth = (float) d / 200.0f;
        stroke = new BasicStroke(strokeWidth);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(stroke);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        if (d <= 0)
            return;
        g.clearRect(0, 0, ds.width, ds.height);
        g.translate((ds.width - d) / 2, (ds.height - d) / 2);
        g.setColor(Color.black);
        g.drawRect(0, 0, d, d);
        for (int i = 0; i < agents.length; i++)
            agents[i].paintComponent(g, d);
    }

    /** Run the animation thread.  Thanks to
     *  <a href="http://profs.etsmtl.ca/mmcguffin/learn/java/06-threads/">http://profs.etsmtl.ca/mmcguffin/learn/java/06-threads/</a>
     *  for the tutorial on animation threads.
     */
    public void run() {
        while(true) {
            long then = System.currentTimeMillis();
            for (int j = 0; j < DDT; j++) {
                for (int i = 0; i < agents.length; i++)
                    agents[i].control(agents, null);
                shuffle(agents);
                for (int i = 0; i < agents.length; i++)
                    agents[i].step();
            }
            repaint();
            long interval = (long) Math.floor(1000 * DT * DDT);
            long now = System.currentTimeMillis();
            interval -= now - then;
            if (interval < 0) {
                System.err.println("time overrun");
            } else {
                try {
                    t.sleep(interval);
                } catch(InterruptedException e) {
                    System.exit(1);
                }
            }
            if (threadSuspended) {
                synchronized(this) {
                    while (threadSuspended) {
                        try {
                            t.wait();
                        } catch(InterruptedException e) {
                            System.exit(1);
                        }
                    }
                }
            }
        }
    }

    /** Start the animation thread. */
    public void start() {
        if (t == null) {
            t = new Thread(this);
            threadSuspended = false;
            t.start();
        } else {
            if (threadSuspended) {
                threadSuspended = false;
                synchronized(this) {
                    t.notify();
                }
            }
        }
    }

    /** Pause the animation thread. */
    public void stop() {
        threadSuspended = true;
    }
}
