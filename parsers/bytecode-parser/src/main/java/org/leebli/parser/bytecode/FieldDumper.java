package org.leebli.parser.bytecode;

import org.leebli.parser.bytecode.model.FieldData;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Opcodes;

public class FieldDumper extends FieldVisitor {

	private FieldData field;

	public FieldDumper(FieldData field) {
		super(Opcodes.ASM4);
		this.field = field;
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return new AnnotationDumper(field, desc, visible);
	}

}
