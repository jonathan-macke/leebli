package org.leebli.parser.bytecode;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.leebli.parser.bytecode.model.ClassData;
import org.leebli.parser.bytecode.utils.AntPatternMatcher;

/**
 * Basic implementation of the ClassDataLoader. It populates itself thanks to
 * the read method.
 */
public class ClassDataLoader {
	private Map<URI, AbstractClassReader> readers = new Hashtable<URI, AbstractClassReader>();

	/**
	 * Read a set of classes via this ClassDataLoader. The idea is similar to
	 * the regular Java ClassLoader but for ClassData type of objects.
	 * 
	 * @param filename
	 *            the archive file or directory to read class file from.
	 * @throws IOException
	 *             thrown in case of error while extracting the class data.
	 */
	protected void read(File filename) throws IOException {
		AbstractClassReader reader;
		if (filename.isDirectory()) {
			reader = new DirectoryReader(filename, this);
		} else {
			reader = new JarReader(filename, this);
		}
		reader.read();
		readers.put(filename.toURI(), reader);
	}

	/**
	 * {@inheritDoc}
	 */
	public ClassData fromName(String name) {
		for (AbstractClassReader reader : readers.values()) {
			for (ClassData clazz : reader.getClasses()) {
				if (clazz.getName().equals(name)) {
					return clazz;
				}
			}
		}
		return null;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ClassData> getClasses(URI uri) {
		List<ClassData> result = new ArrayList<ClassData>();
		if (readers.containsKey(uri)) {
			result.addAll(readers.get(uri).getClasses());
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ClassData> getClasses() {
		List<ClassData> result = new ArrayList<ClassData>();
		for (AbstractClassReader reader : readers.values()) {
			result.addAll(reader.getClasses());
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	public List<ClassData> getClasses(URI uri,
			List<AntPatternMatcher> includes, List<AntPatternMatcher> excludes) {
		List<ClassData> result = new ArrayList<ClassData>();
		if (readers.containsKey(uri)) {
			result.addAll(readers.get(uri).getClasses(includes, excludes));
		}
		return result;
	}

	public List<ClassData> getClassesWithAnnotation(String annotation) {

		List<ClassData> classes = new ArrayList<ClassData>();

		for (ClassData clazz : getClasses()) {
			if (clazz.getAnnotation("L" + annotation + ";") != null) {
				classes.add(clazz);
			}
		}
		return classes;
	}

	/**
	 * {@inheritDoc}
	 */
	public void read(URI uri) throws IOException {
		if ("file".equals(uri.getScheme())) {
			read(new File(uri.getPath()));
		} else {
			throw new IOException("Unsupported scheme: " + uri.getScheme());
		}
	}
}
