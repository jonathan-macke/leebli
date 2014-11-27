package org.leebli.parser.bytecode.model;

import org.leebli.parser.bytecode.ClassDataLoader;

public class FieldData extends JavaItem {
	private String descriptor;
	private String signature;
	private Object value;

	public FieldData(ClassDataLoader loader, ClassData owner, int access,
			String name, String descriptor, String signature, Object value) {
		super(loader, owner, access, name);
		this.setDescriptor(descriptor);
		this.setSignature(signature);
		this.setValue(value);
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * @return the description
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	public void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	public boolean isSame(FieldData reference) {
		return this.getName().equals(reference.getName());
	}

	public boolean hasSameType(FieldData reference) {
		return this.getDescriptor().equals(reference.getDescriptor());
	}

	@Override
	public String getType() {
		return "field";
	}

	/**
	 * Dump field as a string, so it can be used by rules to easily display it.
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getType() + " " + getName() + "(" + getDescriptor() + ")";
	}
}
