package org.leebli.parser.bytecode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarReader extends AbstractClassReader {
	private File filename;
	private ClassDataLoader loader;

	public JarReader(File filename, ClassDataLoader loader) {
		this.filename = filename;
		this.loader = loader;
	}

	@Override
	public void read() throws IOException {
		this.clear();
		FileInputStream fis = null;
		ZipInputStream zis = null;
		try {
			ReadClassesException errors = new ReadClassesException();
			fis = new FileInputStream(this.filename);
			zis = new ZipInputStream(new BufferedInputStream(fis));
			ZipEntry entry = null;
			byte buffer[] = new byte[2048];
			int count = 0;
			while ((entry = zis.getNextEntry()) != null) {
				if (entry.getName().endsWith(".class")) {
					ClassDumper dumper = new ClassDumper(loader);

					ByteArrayOutputStream os = new ByteArrayOutputStream();
					while ((count = zis.read(buffer)) != -1) {
						os.write(buffer, 0, count);
					}
					try {
						this.readClass(dumper, entry.getName(),
								os.toByteArray());
					} catch (ReadClassException exc) {
						errors.add(exc);
					} finally {
						os.close();
					}
				}
			}
			errors.throwIfNeeded();
		} finally {
			if (fis != null) {
				fis.close();
			}
			if (zis != null) {
				zis.close();
			}
		}
	}

}
