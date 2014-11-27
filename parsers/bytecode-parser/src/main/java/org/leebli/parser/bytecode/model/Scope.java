package org.leebli.parser.bytecode.model;

/**
 * Defines the possible visibility scope of a JavaItem.
 * 
 */
public enum Scope {
	PUBLIC(3), PROTECTED(2), NO_SCOPE(1), PRIVATE(0);
	int scope;

	Scope(int scope) {
		this.scope = scope;
	}

	public int getValue() {
		return scope;
	}

	/**
	 * Check if this scope is more visible than the scope.
	 * 
	 * @param scope
	 *            the scope to compare to
	 * @return Returns true if the scope is more visible than the give scope,
	 *         false otherwise.
	 */
	public boolean isMoreVisibleThan(Scope scope) {
		return this.getValue() > scope.getValue();
	}
}
