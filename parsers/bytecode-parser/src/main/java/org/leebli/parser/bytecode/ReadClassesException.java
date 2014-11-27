package org.leebli.parser.bytecode;

import java.util.ArrayList;
import java.util.List;

public class ReadClassesException extends ReadClassException {

	private static final long serialVersionUID = 3850123619855985428L;
	private List<ReadClassException> causes = new ArrayList<ReadClassException>();

	public ReadClassesException() {
		super("");
	}

	public String getMessage() {
		StringBuilder result = new StringBuilder();
		for (ReadClassException cause : causes) {
			result.append(cause.getMessage());
			result.append("\n");
		}
		return result.toString();
	}

	public void add(ReadClassException cause) {
		causes.add(cause);
	}

	public void throwIfNeeded() throws ReadClassException {
		if (causes.size() > 0) {
			throw this;
		}
	}

}
