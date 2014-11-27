package org.leebli.parser.bytecode;

import java.util.logging.Logger;

import org.leebli.parser.bytecode.model.AnnotationData;
import org.leebli.parser.bytecode.model.MethodData;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodDumper extends MethodVisitor {
	private Logger logger = Logger.getLogger(MethodDumper.class.getName());
	private final MethodData method;

	public MethodDumper(MethodData method) {
		super(Opcodes.ASM4);
		this.method = method;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return new AnnotationDumper(method, desc, visible);
	}

	@Override
	public AnnotationVisitor visitAnnotationDefault() {
		return null;
	}

	@Override
	public void visitLineNumber(int line, Label start) {
		logger.fine("       @" + line);
		method.setLineNumber(line);
	}

	@Override
	public AnnotationVisitor visitParameterAnnotation(int parameter,
			String desc, boolean visible) {
		AnnotationData annotation = new AnnotationData(desc, visible);
		method.addParameterAnnotation(parameter, annotation);
		return new AnnotationDumper(annotation);
	}
}
