package org.leebli.parser.bytecode.test;

import java.io.Serializable;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlType;


@XmlType
public class TestAttribute implements Serializable {
	
	private static final long serialVersionUID = -4462758553386179240L;

	private String key;
	
	private TestParameter parameter;

	public TestParameter getParameter() {
		return parameter;
	}

	public void setParameter(TestParameter parameter) {
		this.parameter = parameter;
	}

	
	public String getKey() {
		return this.key;
	}

	
	public void setKey(String key) {
		this.key = key;
	}

}
