package org.leebli.parser.bytecode.model;

import org.leebli.parser.bytecode.ClassDataLoader;

public class InnerClassData extends JavaItem {

	private String outerName;
	private String innerName;

	public InnerClassData(ClassDataLoader loader, ClassData owner, int access,
			String name, String outerName, String innerName) {
		super(loader, owner, access, name);
		this.setOuterName(outerName);
		this.setInnerName(innerName);
	}

	@Override
	public String getType() {
		return "class";
	}

	/**
	 * @param outerName
	 *            the outerName to set
	 */
	protected void setOuterName(String outerName) {
		this.outerName = outerName;
	}

	/**
	 * @return the outerName
	 */
	public String getOuterName() {
		return outerName;
	}

	/**
	 * @param innerName
	 *            the innerName to set
	 */
	protected void setInnerName(String innerName) {
		this.innerName = innerName;
	}

	/**
	 * @return the innerName
	 */
	public String getInnerName() {
		return innerName;
	}

}
