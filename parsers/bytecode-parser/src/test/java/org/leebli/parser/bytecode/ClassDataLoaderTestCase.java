package org.leebli.parser.bytecode;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;
import org.leebli.parser.bytecode.AnnotationDumper;
import org.leebli.parser.bytecode.ClassDataLoader;
import org.leebli.parser.bytecode.ClassDumper;
import org.leebli.parser.bytecode.MethodDumper;
import org.leebli.parser.bytecode.model.AnnotationData;
import org.leebli.parser.bytecode.model.ClassData;
import org.leebli.parser.bytecode.model.FieldData;
import org.leebli.parser.bytecode.model.MethodData;

public class ClassDataLoaderTestCase {

	private File reference;
	private Handler handler = new Handler() {

		@Override
		public void close() throws SecurityException {
		}

		@Override
		public void flush() {
		}

		@Override
		public void publish(LogRecord record) {
			System.out.println(record.getMessage());
		}

	};

	@Before
	public void setUp() {
		Logger.getLogger(ClassDumper.class.getName()).setLevel(
				java.util.logging.Level.ALL);
		Logger.getLogger(ClassDumper.class.getName()).addHandler(handler);
		Logger.getLogger(AnnotationDumper.class.getName()).setLevel(
				java.util.logging.Level.ALL);
		Logger.getLogger(AnnotationDumper.class.getName()).addHandler(handler);
		Logger.getLogger(MethodDumper.class.getName()).setLevel(
				java.util.logging.Level.ALL);
		Logger.getLogger(MethodDumper.class.getName()).addHandler(handler);
		System.out.println("==================================");
		for (String file : System.getProperty("java.class.path").split(
				File.pathSeparator)) {
			if (file.contains("parser-jar-testproject")) {
				reference = new File(file);
				System.out.println(reference);
			}
		}
		assertNotNull("The reference library is not found.", reference);
	}

	@Test
	public void testReadJarFile() throws Exception {
		ClassDataLoader loader = new ClassDataLoader();
		loader.read(reference);

		assertNotNull(loader.getClasses());
		assertEquals(4, loader.getClasses().size());
		ClassData interfaceTest = loader
				.fromName("org/leebli/parser/bytecode/test/TestInterface");

		assertNotNull(interfaceTest);
		assertTrue(interfaceTest.isInterface());
		assertNotNull(interfaceTest.getAnnotations());
		assertEquals(1, interfaceTest.getAnnotations().size());

		AnnotationData webServiceAnnotation = interfaceTest
				.getAnnotation("Ljavax/jws/WebService;");
		assertNotNull(webServiceAnnotation);
		assertEquals("urn:contract",
				webServiceAnnotation.get("targetNamespace"));

		assertNotNull(interfaceTest.getMethods());
		assertEquals(3, interfaceTest.getMethods().size());

		MethodData method1 = interfaceTest
				.getMethodByName("testMethodWithObjectInParameter");
		assertNotNull(method1);

		// method annotations
		assertEquals(2, method1.getAnnotations().size());
		AnnotationData webMethodAnnotation = method1
				.getAnnotation("Ljavax/jws/WebMethod;");
		assertNotNull(webMethodAnnotation);
		AnnotationData webResultAnnotation = method1
				.getAnnotation("Ljavax/jws/WebResult;");
		assertNotNull(webResultAnnotation);

		// parameter annotations
		// first argument
		List<AnnotationData> parameterAnnotations = method1
				.getParameterAnnotations().get(0);
		assertNotNull(parameterAnnotations);
		assertEquals(1, parameterAnnotations.size());
		AnnotationData webParamAnnotation = parameterAnnotations.get(0);
		assertNotNull(webParamAnnotation);
		assertEquals("testParameter", webParamAnnotation.get("name"));

		ClassData dtoClass = loader
				.fromName("org/leebli/parser/bytecode/test/TestParameter");
		assertNotNull(dtoClass);

		FieldData keyField = dtoClass.getFieldByName("key");
		assertNotNull(keyField);
		assertNotNull(keyField.getAnnotations());
		assertEquals(1, keyField.getAnnotations().size());

		FieldData collectionField = dtoClass.getFieldByName("attributes");
		assertNotNull(collectionField);
		assertEquals("Ljava/util/Collection;", collectionField.getDescriptor());

	}
}
