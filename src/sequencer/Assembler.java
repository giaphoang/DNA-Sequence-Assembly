/*
 * Copyright 2021 Marc Liberatore.
 */

package sequencer;

import java.util.List;
import java.util.ArrayList;

public class Assembler {
	private List<Fragment> fragAssembler;

	/**
	 * Creates a new Assembler containing a list of fragments.
	 * 
	 * The list is copied into this assembler so that the original list will not be
	 * modified by the actions of this assembler.
	 * 
	 * @param fragments
	 */
	public Assembler(List<Fragment> fragments) {
		this.fragAssembler = new ArrayList<>(fragments.size());
		for (Fragment frag : fragments) {
			this.fragAssembler.add(frag);
		}
	}

	/**
	 * Returns the current list of fragments this assembler contains.
	 * 
	 * @return the current list of fragments
	 */
	public List<Fragment> getFragments() {
		return new ArrayList<Fragment>(this.fragAssembler);
	}

	/**
	 * Attempts to perform a single assembly, returning true iff an assembly was
	 * performed.
	 * 
	 * This method chooses the best assembly possible, that is, it merges the two
	 * fragments with the largest overlap, breaking ties between merged fragments by
	 * choosing the shorter merged fragment.
	 * 
	 * Merges must have an overlap of at least 1.
	 * 
	 * After merging two fragments into a new fragment, the new fragment is inserted
	 * into the list of fragments in this assembler, and the two original fragments
	 * are removed from the list.
	 * 
	 * @return true iff an assembly was performed
	 */
	public boolean assembleOnce() {
		int overlap;
		int maxOverlap = Integer.MIN_VALUE;
		Fragment mergedFrag, fragLast1 = null, fragLast2 = null;
		for (int i = 0; i < this.fragAssembler.size(); i++) {
			for (int j = 0; j < this.fragAssembler.size(); j++) {
				if (i != j) {
					Fragment f1 = this.fragAssembler.get(i);
					Fragment f2 = this.fragAssembler.get(j);
					overlap = f1.calculateOverlap(f2);
					if (overlap > maxOverlap) {
						maxOverlap = overlap;
						fragLast1 = f1;
						fragLast2 = f2;
					}
				}
			}
		}
		if (maxOverlap > 0) {
			mergedFrag = fragLast1.mergedWith(fragLast2);
			this.fragAssembler.add(mergedFrag);
			this.fragAssembler.remove(fragLast1);
			this.fragAssembler.remove(fragLast2);
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Repeatedly assembles fragments until no more assembly can occur.
	 */
	public void assembleAll() {
		while (this.assembleOnce()) {
			this.assembleOnce();
		}
	}
}
