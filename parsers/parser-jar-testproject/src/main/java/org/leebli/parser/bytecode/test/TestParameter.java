package org.leebli.parser.bytecode.test;

import java.io.Serializable;
import java.util.Collection;

import javax.xml.bind.annotation.XmlElement;


public class TestParameter implements Serializable {
	
	private static final long serialVersionUID = -4462758553386179240L;
	
	
	@XmlElement(required = true)
	private String key;

	private String label;

	private Collection<TestAttribute> attributes;

	public Collection<TestAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<TestAttribute> attributes) {
		this.attributes = attributes;
	}

	public String getKey() {
		return this.key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	
	public String getLabel() {
		return this.label;
	}

	
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "(" + key + " : " + label + ")";
	}

}
