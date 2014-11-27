package org.leebli.parser.bytecode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.leebli.parser.bytecode.model.ClassData;
import org.leebli.parser.bytecode.utils.AntPatternMatcher;
import org.objectweb.asm.ClassReader;

/**
 * This abstract class provide basic behavior for the BCChecker to extract
 * ClassData out of a container e.g: a directory, a zip file, a jar file... It
 * must be extended to implement such functionalities.
 * 
 */
public abstract class AbstractClassReader {
	private Map<String, List<ClassData>> classes = new Hashtable<String, List<ClassData>>();

	/**
	 * This method should implement the extraction of the classes out of its
	 * container.
	 * 
	 * @throws IOException
	 *             is thrown in case of reading error.
	 */
	abstract void read() throws IOException;

	/**
	 * 
	 * @param visitor
	 * @param name
	 * @param data
	 * @throws ReadClassException
	 */
	protected void readClass(ClassDumper visitor, String name, byte[] data)
			throws ReadClassException {
		try {
			ClassReader cr = new ClassReader(data);
			cr.accept(visitor, 0);
			this.put(name, visitor.getClasses());
		} catch (RuntimeException exc) {
			throw new ReadClassException("Error occurred while loading class "
					+ name + ": " + exc.toString(), exc);
		}
	}

	/**
	 * Get all the discovered classes.
	 * 
	 * @return a list of all the discovered classes.
	 */
	public List<ClassData> getClasses() {
		return getClasses(Collections.<AntPatternMatcher> emptyList(),
				Collections.<AntPatternMatcher> emptyList());
	}

	/**
	 * Get a filtered list of classes. The patterns are filesystem based.
	 * 
	 * @param includes
	 *            the include patterns.
	 * @param excludes
	 *            the exclude patterns.
	 * @return a filtered list.
	 */
	public List<ClassData> getClasses(List<AntPatternMatcher> includes,
			List<AntPatternMatcher> excludes) {
		List<ClassData> result = new ArrayList<ClassData>();
		for (Entry<String, List<ClassData>> entry : this.classes.entrySet()) {
			if (shouldInclude(entry.getKey(), includes, excludes)) {
				result.addAll(entry.getValue());
			}
		}
		return result;
	}

	/**
	 * Empty this class data cache.
	 */
	protected void clear() {
		classes.clear();
	}

	/**
	 * Add a new class definition to the cache.
	 * 
	 * @param name
	 *            the filename
	 * @param classes
	 *            the classes related to this filename.
	 */
	protected void put(String name, List<ClassData> classes) {
		if (this.classes.containsKey(name)) {
			this.classes.get(name).addAll(classes);
		} else {
			this.classes.put(name, new ArrayList<ClassData>(classes));
		}
	}

	/**
	 * Should a path be included based on the patterns.
	 * 
	 * @param subpath
	 *            the path to check
	 * @param includes
	 *            the include patterns
	 * @param excludes
	 *            the exclude patterns.
	 * @return Returns true if the if should be included into the list, false
	 *         otherwise.
	 */
	protected boolean shouldInclude(String subpath,
			List<AntPatternMatcher> includes, List<AntPatternMatcher> excludes) {
		boolean included = includes.size() == 0 ? true : false;
		for (AntPatternMatcher inc : includes) {
			if (inc.matches(subpath)) {
				included = true;
				break;
			}
		}
		for (AntPatternMatcher exc : excludes) {
			if (exc.matches(subpath)) {
				return false;
			}
		}
		return included;
	}
}
