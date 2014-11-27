package org.leebli.parser.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DirectoryReader extends AbstractClassReader {

	private File path;
	private ClassDataLoader loader;

	public DirectoryReader(File path, ClassDataLoader loader) {
		this.path = path;
		this.loader = loader;
	}

	@Override
	public void read() throws IOException {
		clear();
		ReadClassesException errors = new ReadClassesException();
		scanDir(this.path, null, errors);
		errors.throwIfNeeded();
	}

	private void scanDir(File dir, String path, ReadClassesException errors)
			throws IOException {
		byte buffer[] = new byte[2048];
		if (path == null) {
			path = "";
		}
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				scanDir(file, path + file.getName() + "/", errors);
			} else if (file.getName().endsWith(".class")) {
				ClassDumper dumper = new ClassDumper(loader);
				ByteArrayOutputStream os = new ByteArrayOutputStream();
				InputStream is = null;
				try {
					is = new FileInputStream(file);
					int count = 0;
					while ((count = is.read(buffer)) != -1) {
						os.write(buffer, 0, count);
					}
					try {
						this.readClass(dumper, path + file.getName(),
								os.toByteArray());
					} catch (ReadClassException exc) {
						errors.add(exc);
					} finally {
						os.close();
					}
				} finally {
					if (is != null) {
						is.close();
					}
				}
			}
		}
	}

}
