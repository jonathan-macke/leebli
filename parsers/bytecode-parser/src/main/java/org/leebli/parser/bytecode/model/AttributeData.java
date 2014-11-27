package org.leebli.parser.bytecode.model;

import org.leebli.parser.bytecode.ClassDataLoader;

public class AttributeData extends JavaItem {

	public AttributeData(ClassDataLoader loader, ClassData owner, int access,
			String name) {
		super(loader, owner, access, name);
	}

	@Override
	public String getType() {
		return "attribute";
	}
}
