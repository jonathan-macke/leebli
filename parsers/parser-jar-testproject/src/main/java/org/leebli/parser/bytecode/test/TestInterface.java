package org.leebli.parser.bytecode.test;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;


@WebService(targetNamespace = "urn:contract")
public interface TestInterface {
	
	
	@WebMethod
	@WebResult(name = "output", targetNamespace = "urn:contract")
	void testMethodWithObjectInParameter(
			@WebParam(name = "testParameter", targetNamespace = "urn:contract") TestParameter testParameter);


	@WebMethod
	@WebResult(name = "output", targetNamespace = "urn:contract")
	String testMethodWithSeveralParameters(
			@WebParam(name = "locale", targetNamespace = "urn:contract") String locale,
			@WebParam(name = "key", targetNamespace = "urn:contract") String key);


	@WebMethod
	@WebResult(name = "output", targetNamespace = "urn:contract")
	TestResult testMethodWithObjectInResult(
			@WebParam(name = "key", targetNamespace = "urn:contract") String key);

}