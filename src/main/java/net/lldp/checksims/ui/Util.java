/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 *
 * Copyright (c) 2014-2016 Ted Meyer, Nicholas DeMarinis, Matthew Heon, and Dolan Murvihill
 */
package net.lldp.checksims.ui;

import java.awt.Graphics;

/**
 * Some generic static method utilities for UI convenience.
 * @author ted
 *
 */
public class Util
{
    /**
     * Get the length of a string in pixels for a given graphics context
     * @param s the string
     * @param g the graphics context
     * @return a length in pixels
     */
    public static int getWidth(String s, Graphics g)
    {
        return g.getFontMetrics().stringWidth(s);
    }
}
