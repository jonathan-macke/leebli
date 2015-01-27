package org.leebli.parser.jar

import org.apache.bcel.classfile.EmptyVisitor
import org.apache.bcel.classfile.JavaClass
import org.apache.bcel.classfile.Method
import org.apache.bcel.generic.ConstantPoolGen
import org.apache.bcel.generic.INVOKEINTERFACE
import org.apache.bcel.generic.INVOKEVIRTUAL
import org.apache.bcel.generic.InvokeInstruction
import org.apache.bcel.generic.MethodGen
import org.leebli.model.jar.ClassMethod

class ClassAnalyser extends EmptyVisitor {
  private var currentClass: JavaClass = _
  private var currentPool: ConstantPoolGen = _
  var calledMethods = Map.empty[String, Set[ClassMethod]].withDefaultValue(Set.empty)
  var parents = List.empty[String]

  override def visitJavaClass(c: JavaClass) {
    currentClass = c
    currentPool = new ConstantPoolGen(c.getConstantPool)
    calledMethods = Map.empty[String, Set[ClassMethod]].withDefaultValue(Set.empty)
    parents = (c.getInterfaceNames ++ Option(c.getSuperclassName)).toList
  }

  override def visitMethod(m: Method) {
    val name = m.getName
    val mg = new MethodGen(m, currentClass.getClassName, currentPool)
    for {
      instructions <- Option(mg.getInstructionList)
      i <- instructions.getInstructions
      if i.isInstanceOf[INVOKEVIRTUAL] || i.isInstanceOf[INVOKEINTERFACE]
      invoke = i.asInstanceOf[InvokeInstruction]
      targetType = invoke.getClassName(currentPool)
      method = invoke.getMethodName(currentPool)
    } {
      calledMethods += name -> (calledMethods(name) + ClassMethod(targetType, method))
    }
  }
}
