/*******************************************************************************
 * Copyright (c) 2003, 2023 IBM Corporation and others.
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
package org.eclipse.gef.examples.flow.policies;

import org.eclipse.draw2d.Label;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import org.eclipse.gef.examples.flow.model.Activity;
import org.eclipse.gef.examples.flow.model.commands.RenameActivityCommand;

/**
 * EditPolicy for the direct editing of Activity names.
 *
 * @author Daniel Lee
 */
public class ActivityDirectEditPolicy extends DirectEditPolicy {

	/**
	 * @see DirectEditPolicy#getDirectEditCommand(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		RenameActivityCommand cmd = new RenameActivityCommand();
		cmd.setSource((Activity) getHost().getModel());
		cmd.setOldName(((Activity) getHost().getModel()).getName());
		cmd.setName((String) request.getCellEditor().getValue());
		return cmd;
	}

	/**
	 * @see DirectEditPolicy#showCurrentEditValue(org.eclipse.gef.requests.DirectEditRequest)
	 */
	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
		String value = (String) request.getCellEditor().getValue();
		((Label) getHostFigure()).setText(value);
		// hack to prevent async layout from placing the cell editor twice.
		// getHostFigure().getUpdateManager().performUpdate();
	}

}
