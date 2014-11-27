package org.leebli.parser.bytecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.leebli.parser.bytecode.model.ClassData;
import org.leebli.parser.bytecode.model.FieldData;
import org.leebli.parser.bytecode.model.InnerClassData;
import org.leebli.parser.bytecode.model.MethodData;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

class ClassDumper extends ClassVisitor {
	private ClassDataLoader loader;
	private Logger logger = Logger.getLogger(ClassDumper.class.getName());
	private ClassData clazz; // current main class being parsed.
	private Map<String, ClassData> classes = new HashMap<String, ClassData>();

	/**
	 * Create a new visitor instance.
	 * 
	 * @param loader
	 *            the ClassDataLoader to which the model are associated.
	 */
	public ClassDumper(ClassDataLoader loader) {
		super(Opcodes.ASM4);
		this.loader = loader;
	}

	/**
	 * {@inheritDoc}
	 */
	public void visit(int version, int access, String name, String signature,
			String superName, String[] interfaces) {
		logger.fine("class " + name + " extends " + superName + " {");
		clazz = new ClassData(loader, null, access, name, signature, superName,
				interfaces, version);
		classes.put(name, clazz);
	}

	/**
	 * {@inheritDoc}
	 */
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return new AnnotationDumper(clazz, desc, visible);
	}

	public void visitAttribute(Attribute attribute) {
	}

	public void visitEnd() {
		logger.fine("}");
		clazz = null;
	}

	public FieldVisitor visitField(int access, String name, String desc,
			String signature, Object value) {
		logger.fine("    -(field) " + name + " " + signature + " " + desc);
		FieldData field = new FieldData(loader, clazz, access, name, desc,
				signature, value);
		clazz.add(field);
		return new FieldDumper(field);
	}

	public void visitInnerClass(String name, String outerName,
			String innerName, int access) {
		logger.fine("    +(ic) " + name + " " + outerName + " " + innerName
				+ " " + access);
		// clazz = new ClassData(access, name, innerName);
		clazz.add(new InnerClassData(loader, clazz, access, name, outerName,
				innerName));
	}

	public MethodVisitor visitMethod(int access, String name,
			String descriptor, String signature, String[] exceptions) {
		logger.fine("    +(m) " + name + " " + descriptor + " " + signature
				+ " " + Arrays.toString(exceptions));
		MethodData method = new MethodData(loader, clazz, access, name,
				descriptor, signature, exceptions);
		clazz.add(method);
		return new MethodDumper(method);
	}

	public void visitOuterClass(String owner, String name, String desc) {
		logger.fine("    *(oc) " + name + " " + desc);
	}

	public void visitSource(String source, String debug) {
		logger.fine(" - source: " + source);
		logger.fine(" - debug: " + debug);
		clazz.setSource(source);
	}

	public List<ClassData> getClasses() {
		return new ArrayList<ClassData>(classes.values());
	}
}
