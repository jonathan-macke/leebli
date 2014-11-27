package org.leebli.parser.bytecode.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.leebli.parser.bytecode.ClassDataLoader;

public class MethodData extends JavaItem {
	private String signature;
	private String descriptor;
	private List<String> exceptions = new ArrayList<String>();
	private int line;
	private Map<Integer, List<AnnotationData>> parameterAnnotations = new HashMap<Integer, List<AnnotationData>>();

	public MethodData(ClassDataLoader loader, ClassData owner, int access,
			String name, String descriptor, String signature,
			String[] exceptions) {
		super(loader, owner, access, name);
		this.setSignature(signature);
		this.setDescriptor(descriptor);
		if (exceptions != null) {
			Collections.addAll(this.exceptions, exceptions);
		}
	}

	public boolean isSame(MethodData method) {
		if (method == null) {
			return false;
		}
		return this.getName().equals(method.getName())
				&& this.getDescriptor().equals(method.getDescriptor());
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	protected void setSignature(String signature) {
		this.signature = signature;
	}

	@Override
	public String getType() {
		return "method";
	}

	/**
	 * @param descriptor
	 *            the descriptor to set
	 */
	protected void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	/**
	 * @return the descriptor
	 */
	public String getDescriptor() {
		return descriptor;
	}

	/**
	 * @param exceptions
	 *            the exceptions to set
	 */
	protected void setExceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * @return the exceptions
	 */
	public List<String> getExceptions() {
		return exceptions;
	}

	public void setLineNumber(int line) {
		this.line = line;
	}

	public int getLineNumber() {
		return line;
	}

	public void addParameterAnnotation(int index, AnnotationData annotation) {
		List<AnnotationData> annotations = parameterAnnotations.get(index);
		if (annotations == null) {
			annotations = new ArrayList<AnnotationData>();
		}

		annotations.add(annotation);
		parameterAnnotations.put(index, annotations);
	}

	public Map<Integer, List<AnnotationData>> getParameterAnnotations() {
		return parameterAnnotations;
	}

	public void setParameterAnnotations(
			Map<Integer, List<AnnotationData>> parameterAnnotations) {
		this.parameterAnnotations = parameterAnnotations;
	}

	/**
	 * Dump method as a string, so it can be used by rules to easily display it.
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return getType() + " " + getName() + "(" + getDescriptor() + ")";
	}
}
