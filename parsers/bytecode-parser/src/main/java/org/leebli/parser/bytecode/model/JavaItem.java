package org.leebli.parser.bytecode.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.leebli.parser.bytecode.ClassDataLoader;
import org.objectweb.asm.Opcodes;

public abstract class JavaItem {
	private Scope visibility = Scope.PROTECTED;
	private ClassData owner;
	private String name;
	private boolean isAbstract;
	private boolean isInterface;
	private boolean isFinal;
	private boolean isStatic;
	private boolean isTransient;
	private boolean isVariableArity;
	private ClassDataLoader classDataLoader;
	private List<AnnotationData> annotations = new ArrayList<AnnotationData>();

	protected JavaItem(ClassDataLoader loader, ClassData owner, int access,
			String name) {
		this.setOwner(owner);
		this.setName(name);
		this.setVisibility(toScope(access));
		this.setAbstract((access & Opcodes.ACC_ABSTRACT) == Opcodes.ACC_ABSTRACT);
		this.setInterface((access & Opcodes.ACC_INTERFACE) == Opcodes.ACC_INTERFACE);
		this.setFinal((access & Opcodes.ACC_FINAL) == Opcodes.ACC_FINAL);
		this.setStatic((access & Opcodes.ACC_STATIC) == Opcodes.ACC_STATIC);
		this.setTransient((access & Opcodes.ACC_TRANSIENT) == Opcodes.ACC_TRANSIENT);
		this.setVariableArity((access & Opcodes.ACC_VARARGS) == Opcodes.ACC_VARARGS);
		this.setClassDataLoader(loader);
	}

	protected void setVisibility(Scope visibility) {
		this.visibility = visibility;
	}

	public Scope getVisibility() {
		return visibility;
	}

	public static Scope toScope(int access) {
		if ((access & Opcodes.ACC_PRIVATE) == Opcodes.ACC_PRIVATE) {
			return Scope.PRIVATE;
		} else if ((access & Opcodes.ACC_PROTECTED) == Opcodes.ACC_PROTECTED) {
			return Scope.PROTECTED;
		}
		if ((access & Opcodes.ACC_PUBLIC) == Opcodes.ACC_PUBLIC) {
			return Scope.PUBLIC;
		} else {
			return Scope.NO_SCOPE;
		}
	}

	/**
	 * @param isAbstract
	 *            the isAbstract to set
	 */
	protected void setAbstract(boolean isAbstract) {
		this.isAbstract = isAbstract;
	}

	/**
	 * @return the isAbstract
	 */
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * @param isInterface
	 *            the isInterface to set
	 */
	protected void setInterface(boolean isInterface) {
		this.isInterface = isInterface;
	}

	/**
	 * @return the isInterface
	 */
	public boolean isInterface() {
		return isInterface;
	}

	/**
	 * @param isFinal
	 *            the isFinal to set
	 */
	protected void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	/**
	 * @return the isFinal
	 */
	public boolean isFinal() {
		return isFinal;
	}

	/**
	 * @param isStatic
	 *            the isStatic to set
	 */
	protected void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	/**
	 * @return the isStatic
	 */
	public boolean isStatic() {
		return isStatic;
	}

	/**
	 * @param isTransient
	 *            the isTransient to set
	 */
	protected void setTransient(boolean isTransient) {
		this.isTransient = isTransient;
	}

	/**
	 * @return the isTransient
	 */
	public boolean isTransient() {
		return isTransient;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	public boolean isVariableArity() {
		return isVariableArity;
	}

	protected void setVariableArity(boolean isVariableArity) {
		this.isVariableArity = isVariableArity;
	}

	/**
	 * @return the owner
	 */
	public ClassData getOwner() {
		return owner;
	}

	/**
	 * @param owner
	 *            the owner to set
	 */
	protected void setOwner(ClassData owner) {
		this.owner = owner;
	}

	public abstract String getType();

	/**
	 * Display the item name. {@inheritDoc}
	 */
	public String toString() {
		return name;
	}

	/**
	 * Get the ClassDataLoader associated with this instance. {@inheritDoc}
	 */
	public ClassDataLoader getClassDataLoader() {
		return classDataLoader;
	}

	/**
	 * Get the ClassDataLoader associated with this instance. {@inheritDoc}
	 */
	protected void setClassDataLoader(ClassDataLoader loader) {
		classDataLoader = loader;
	}

	/**
	 * 
	 * @return
	 */
	public List<AnnotationData> getAnnotations() {
		return Collections.<AnnotationData> unmodifiableList(annotations);
	}

	/**
	 * 
	 * @param annotations
	 */
	public void add(AnnotationData annotation) {
		this.annotations.add(annotation);
	}

	public AnnotationData getAnnotation(String desc) {
		for (AnnotationData annotation : this.annotations) {
			if (desc != null && desc.equals(annotation.getDesc())) {
				return annotation;
			}
		}
		return null;
	}
}
