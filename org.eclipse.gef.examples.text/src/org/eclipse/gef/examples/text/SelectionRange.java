/*******************************************************************************
 * Copyright (c) 2004, 2010 IBM Corporation and others.
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

package org.eclipse.gef.examples.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.tools.ToolUtilities;

import org.eclipse.gef.examples.text.edit.TextEditPart;

public class SelectionRange {

	public final TextLocation begin;

	public final TextLocation end;

	public final boolean isForward;
	public final boolean trailing;

	private List<EditPart> selectedParts;
	private List<EditPart> leafParts;

	/**
	 * Constructs a selection range which starts and ends at the given location. The
	 * direction of the range is forward.
	 *
	 * @since 3.1
	 * @param location
	 */
	public SelectionRange(TextLocation location) {
		this(location, location, true);
	}

	/**
	 * Constructs a selection range which starts and ends at the given locations.
	 * The direction of the range is forward.
	 *
	 * @since 3.1
	 * @param begin
	 * @param end
	 */
	public SelectionRange(TextLocation begin, TextLocation end) {
		this(begin, end, true);
	}

	/**
	 * Constructs a selection range which starts and ends at the given locations
	 * with the given direction. If a range is forward, the caret will be placed at
	 * the end of the range. Otherwise, it is placed at the beginning.
	 *
	 * @since 3.1
	 * @param begin
	 * @param end
	 * @param forward
	 */
	public SelectionRange(TextLocation begin, TextLocation end, boolean forward) {
		this(begin, end, forward, true);
	}

	public SelectionRange(TextLocation begin, TextLocation end, boolean forward, boolean trailing) {
		Assert.isNotNull(begin);
		Assert.isNotNull(end);
		this.begin = begin;
		this.end = end;
		this.isForward = forward;
		this.trailing = trailing;
	}

	public SelectionRange(TextEditPart part, int offset) {
		this(new TextLocation(part, offset));
	}

	public SelectionRange(TextEditPart begin, int bo, TextEditPart end, int eo) {
		this(new TextLocation(begin, bo), new TextLocation(end, eo));
	}

	private static void depthFirstTraversal(EditPart part, List<EditPart> result) {
		if (part.getChildren().isEmpty()) {
			result.add(part);
		} else {
			for (EditPart element : part.getChildren()) {
				depthFirstTraversal(element, result);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SelectionRange other) {
			return other.begin == begin && other.end == end && other.isForward == isForward;
		}
		return false;
	}

	private static List<EditPart> findLeavesBetweenInclusive(EditPart left, EditPart right) {
		if (left == right) {
			return Collections.singletonList(left);
		}
		EditPart commonAncestor = ToolUtilities.findCommonAncestor(left, right);

		EditPart nextLeft = left.getParent();
		List<? extends EditPart> children;

		List<EditPart> result = new ArrayList<>();

		if (nextLeft == commonAncestor) {
			result.add(left);
		}
		while (nextLeft != commonAncestor) {
			children = nextLeft.getChildren();
			for (int i = children.indexOf(left); i < children.size(); i++) {
				depthFirstTraversal(children.get(i), result);
			}

			left = nextLeft;
			nextLeft = nextLeft.getParent();
		}

		List<EditPart> rightSide = new ArrayList<>();
		EditPart nextRight = right.getParent();
		if (nextRight == commonAncestor) {
			rightSide.add(right);
		}
		while (nextRight != commonAncestor) {
			children = nextRight.getChildren();
			for (int i = 0; i <= children.indexOf(right); i++) {
				depthFirstTraversal(children.get(i), rightSide);
			}

			right = nextRight;
			nextRight = nextRight.getParent();
		}

		children = commonAncestor.getChildren();
		int start = children.indexOf(left) + 1;
		for (int i = start; i < children.indexOf(right); i++) {
			depthFirstTraversal(children.get(i), result);
		}

		result.addAll(rightSide);
		return result;
	}

	private static List<EditPart> findNodesBetweenInclusive(EditPart left, EditPart right) {
		if (left == right) {
			return Collections.singletonList(left);
		}
		EditPart commonAncestor = ToolUtilities.findCommonAncestor(left, right);

		EditPart nextLeft = left.getParent();
		List<? extends EditPart> children;

		List<EditPart> result = new ArrayList<>();

		result.add(left);
		while (nextLeft != commonAncestor) {
			children = nextLeft.getChildren();
			for (int i = children.indexOf(left) + 1; i < children.size(); i++) {
				result.add(children.get(i));
			}

			left = nextLeft;
			nextLeft = nextLeft.getParent();
		}

		List<EditPart> rightSide = new ArrayList<>();
		EditPart nextRight = right.getParent();
		rightSide.add(right);
		while (nextRight != commonAncestor) {
			children = nextRight.getChildren();
			int end = children.indexOf(right);
			for (int i = 0; i < end; i++) {
				rightSide.add(children.get(i));
			}

			right = nextRight;
			nextRight = nextRight.getParent();
		}

		children = commonAncestor.getChildren();
		int start = children.indexOf(left) + 1;
		int end = children.indexOf(right);
		if (end > start) {
			result.addAll(children.subList(start, end));
		}
		result.addAll(rightSide);
		return result;
	}

	public List<EditPart> getLeafParts() {
		if (leafParts == null) {
			List<EditPart> list = findLeavesBetweenInclusive(begin.part, end.part);
			leafParts = Collections.unmodifiableList(list);
		}
		return leafParts;
	}

	/**
	 * @return the list of selected EditParts. There is no guarantee as to the order
	 *         of EditParts.
	 */
	public List<EditPart> getSelectedParts() {
		if (selectedParts == null) {
			List<EditPart> list = findNodesBetweenInclusive(begin.part, end.part);
			selectedParts = Collections.unmodifiableList(list);
		}
		return selectedParts;
	}

	public boolean isEmpty() {
		return begin.equals(end);
	}

}