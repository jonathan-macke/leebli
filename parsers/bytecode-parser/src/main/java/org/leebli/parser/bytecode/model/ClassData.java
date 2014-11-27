package org.leebli.parser.bytecode.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.leebli.parser.bytecode.ClassDataLoader;

public class ClassData extends JavaItem {
	private List<MethodData> methods = new ArrayList<MethodData>();
	private List<FieldData> fields = new ArrayList<FieldData>();
	private List<AttributeData> attributes = new ArrayList<AttributeData>();
	private List<InnerClassData> innerClasses = new ArrayList<InnerClassData>();
	private String signature;
	private String superName;
	private List<String> interfaces = new ArrayList<String>();
	private int version;
	private String source;

	public ClassData(ClassDataLoader loader, ClassData owner, int access,
			String name, String signature, String superName,
			String[] interfaces, int version) {
		super(loader, owner, access, name);
		this.setSignature(signature);
		this.superName = superName;
		Collections.addAll(this.interfaces, interfaces);
		this.version = version;
	}

	public void add(MethodData method) {
		methods.add(method);
	}

	public void add(AttributeData attribute) {
		attributes.add(attribute);
	}

	public void add(FieldData field) {
		fields.add(field);
	}

	public boolean isSame(ClassData newClazz) {
		return this.getName().equals(newClazz.getName());
	}

	/**
	 * @param signature
	 *            the signature to set
	 */
	protected void setSignature(String signature) {
		this.signature = signature;
	}

	/**
	 * @return the signature
	 */
	public String getSignature() {
		return signature;
	}

	@Override
	public String getType() {
		return this.isInterface() ? "interface" : "class";
	}

	/**
	 * @return the superName
	 */
	public String getSuperName() {
		return superName;
	}

	/**
	 * @param superName
	 *            the superName to set
	 */
	protected void setSuperName(String superName) {
		this.superName = superName;
	}

	/**
	 * @return the interfaces
	 */
	public List<String> getInterfaces() {
		return interfaces;
	}

	/**
	 * @param interfaces
	 *            the interfaces to set
	 */
	protected void setInterfaces(List<String> interfaces) {
		this.interfaces = interfaces;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	protected void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the methods
	 */
	public List<MethodData> getMethods() {
		return methods;
	}

	/**
	 * @param methods
	 *            the methods to set
	 */
	protected void setMethods(List<MethodData> methods) {
		this.methods = methods;
	}

	/**
	 * @return the fields
	 */
	public List<FieldData> getFields() {
		return fields;
	}

	/**
	 * @param fields
	 *            the fields to set
	 */
	protected void setFields(List<FieldData> fields) {
		this.fields = fields;
	}

	/**
	 * @return the attributes
	 */
	public List<AttributeData> getAttributes() {
		return attributes;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	protected void setAttributes(List<AttributeData> attributes) {
		this.attributes = attributes;
	}

	public void add(InnerClassData clazz) {
		innerClasses.add(clazz);
	}

	/**
	 * @param source
	 *            the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the source
	 */
	public String getSource() {
		return source;
	}

	public MethodData getMethodByName(String name) {

		MethodData result = null;
		for (MethodData method : methods) {
			if (method.getName().equals(name)) {
				result = method;
				break;
			}
		}
		return result;

	}

	public FieldData getFieldByName(String name) {

		FieldData result = null;
		for (FieldData field : fields) {
			if (field.getName().equals(name)) {
				result = field;
				break;
			}
		}
		return result;

	}

	public String getFilename() {
		if (this.getName().lastIndexOf('/') != -1) {
			return this.getName().substring(0,
					this.getName().lastIndexOf('/') + 1)
					+ getSource();
		}
		return this.getSource();
	}
}
