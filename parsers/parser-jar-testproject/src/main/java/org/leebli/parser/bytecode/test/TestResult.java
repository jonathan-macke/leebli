package org.leebli.parser.bytecode.test;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


@XmlType
public class TestResult implements Serializable {
	
	private static final long serialVersionUID = -4462758553386179240L;
	
	private String key;
	
	@XmlElement(required = true)
	private String label;

	
	@XmlElement(required = true)
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
