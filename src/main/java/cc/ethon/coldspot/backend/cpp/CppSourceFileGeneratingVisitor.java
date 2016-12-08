package cc.ethon.coldspot.backend.cpp;

import java.util.List;
import java.util.stream.Collectors;

import cc.ethon.coldspot.backend.IndentingWriter;
import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.MethodSignature;
import cc.ethon.coldspot.frontend.ast.AstVisitor;
import cc.ethon.coldspot.frontend.ast.ClassNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;

class CppSourceFileGeneratingVisitor implements AstVisitor<Void> {

	private final IndentingWriter writer;

	private void writeIncludes(ClassName className) {
		writer.println("#include <" + className.getPackageAsPath() + "/" + className.getName() + ".hpp>");
		writer.println();
	}

	private void writeMembers(List<MethodNode> methods) {
		if (!methods.isEmpty()) {
			writer.println();
		}
		for (int i = 0; i < methods.size(); i++) {
			if (i != 0) {
				writer.println();
			}
			final MethodNode methodNode = methods.get(i);
			methodNode.accept(this);
		}
	}

	private void writeArguments(MethodNode method) {
		final String asString = method.getSignature().getArgumentTypes().stream().map(type -> CppTypeVisitor.getTypeName(type))
				.collect(Collectors.joining(", "));
		writer.printUnindented(asString);
	}

	private void writeMethodStart(MethodNode methodNode) {
		final MethodSignature signature = methodNode.getSignature();
		final ClassName owner = methodNode.getOwningClass().getName();
		if (signature.getName().equals("<init>")) {
			writer.print(CppClassNameUtil.makeQualifiedName(owner));
			writer.printUnindented("::" + owner.getName() + "(");
			writeArguments(methodNode);
			writer.printlnUnindented(") {");
		} else {
			writer.print(CppTypeVisitor.getTypeName(signature.getReturnType()));
			writer.printUnindented(" ");
			writer.print(CppClassNameUtil.makeQualifiedName(owner));
			writer.printUnindented("::" + signature.getName() + "(");
			writeArguments(methodNode);
			writer.printlnUnindented(") {");
		}
		writer.increaseIndentation();
	}

	private void writeMethodEnd() {
		writer.decreaseIndentation();
		writer.println("}");
	}

	public CppSourceFileGeneratingVisitor(IndentingWriter writer) {
		super();
		this.writer = writer;
	}

	@Override
	public Void visit(ClassNode classNode) {
		writeIncludes(classNode.getName());
		writeMembers(classNode.getPrivateMethods());
		writeMembers(classNode.getProtectedMethods());
		writeMembers(classNode.getPublicMethods());
		return null;
	}

	@Override
	public Void visit(MethodNode methodNode) {
		writeMethodStart(methodNode);
		writeMethodEnd();
		return null;
	}

}
