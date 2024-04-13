/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.gef;

import java.util.List;

import org.eclipse.draw2d.geometry.Point;

/**
 * This class provides keyboard accessibility support for <i>Anchors</i>.
 * Anchors are simply locations (relative to the Viewer's Control) on the
 * GraphicalEditPart's figure indicated by
 * {@link org.eclipse.draw2d.geometry.Point Points}. Anchors are used when
 * creating or manipulating connections graphically. <I>Accessible</I> Anchors
 * are the locations that should be used during keyboard connection
 * manipulation.
 * <P>
 * Keyboard connection manipulation moves the Mouse cursor programmatically to
 * the AccessibleAnchor's location. Targeting is still performed as if the User
 * were using the Mouse and not the keyboard. Therefore, the GraphicalEditPart
 * should provide locations that will result in its being targeted by the
 * current tool.
 * <P>
 * Connection operations involves either the source or target end of a
 * connection. The <code>AccessibleAnchorProvider</code> has the option of
 * returning different locations depending on the context of the operation.
 */

public interface AccessibleAnchorProvider {

	/**
	 * Returns a list of Points in <B>absolute</B> coordinates where source anchors
	 * are located. Tools that work with connections should use these locations when
	 * operating in accessible keyboard modes.
	 *
	 * @return A list of absolute locations (Points relative to the Viewer's
	 *         Control)
	 */
	List<Point> getSourceAnchorLocations();

	/**
	 * Returns a list of Points in <B>absolute</B> coordinates where target anchors
	 * are located. Tools that work with connections should use these locations when
	 * operating in accessible keyboard modes.
	 *
	 * @return A list of absolute locations (Points relative to the Viewer's
	 *         Control)
	 */
	List<Point> getTargetAnchorLocations();

}
