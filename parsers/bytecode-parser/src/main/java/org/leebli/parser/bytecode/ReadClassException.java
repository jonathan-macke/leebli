package org.leebli.parser.bytecode;

import java.io.IOException;

public class ReadClassException extends IOException {

	private static final long serialVersionUID = 4735293932288385256L;

	public ReadClassException(String message) {
		super(message);
	}

	public ReadClassException(String message, Throwable cause) {
		super(message, cause);
	}

}
