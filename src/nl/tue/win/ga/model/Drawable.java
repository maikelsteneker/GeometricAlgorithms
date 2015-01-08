package nl.tue.win.ga.model;

import java.awt.Graphics;

/**
 * Interface for objects that can be drawn.
 *
 * @author maikel
 */
public interface Drawable {
    public void draw(Graphics g, boolean scale, boolean invertY);
}
