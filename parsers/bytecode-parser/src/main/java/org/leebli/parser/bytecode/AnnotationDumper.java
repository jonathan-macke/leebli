package org.leebli.parser.bytecode;

import java.util.logging.Logger;

import org.leebli.parser.bytecode.model.AnnotationData;
import org.leebli.parser.bytecode.model.JavaItem;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

class AnnotationDumper extends AnnotationVisitor {
	private Logger logger = Logger.getLogger(AnnotationDumper.class.getName());
	private AnnotationData annotation;

	public AnnotationDumper(JavaItem owner, String desc, boolean visible) {
		super(Opcodes.ASM4);
		logger.fine("    (annotation) " + desc + " " + visible);
		this.annotation = new AnnotationData(desc, visible);
		owner.add(annotation);
	}

	public AnnotationDumper(AnnotationData annotation) {
		super(Opcodes.ASM4);
		logger.fine("    (annotation) " + annotation.getDesc() + " "
				+ annotation.isVisible());
		this.annotation = annotation;
	}

	@Override
	public void visit(String name, Object value) {
		this.annotation.put(name, value);
	}

}