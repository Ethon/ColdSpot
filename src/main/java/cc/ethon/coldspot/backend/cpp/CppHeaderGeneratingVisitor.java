package cc.ethon.coldspot.backend.cpp;

import java.util.List;
import java.util.stream.Collectors;

import cc.ethon.coldspot.backend.IndentingWriter;
import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.MethodSignature;
import cc.ethon.coldspot.frontend.ast.AstVisitor;
import cc.ethon.coldspot.frontend.ast.ClassNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;

class CppHeaderGeneratingVisitor implements AstVisitor<Void> {

	private final IndentingWriter writer;

	private String writeIncludeGuardStart(ClassName className) {
		final String guard = CppClassNameUtil.makeIncludeGuardName(className);
		writer.println("#ifndef " + guard);
		writer.println("#define " + guard);
		writer.println();
		return guard;
	}

	private void writeIncludeGuardEnd(String guard) {
		writer.println("#endif // " + guard);
	}

	private void writeNamespaceStart(ClassName className) {
		for (final String part : className.getPackageParts()) {
			writer.println("namespace " + part + " {");
		}
		writer.println();
	}

	private void writeNamespaceEnd(ClassName className) {
		for (final String part : className.getPackageParts()) {
			writer.println("} // namespace " + part);
		}
		writer.println();
	}

	private void writeClassStart(ClassNode node) {
		writer.print("class " + node.getName().getName());
		if (node.getSuperClassName() != null) {
			writer.printUnindented(" : public " + CppClassNameUtil.makeQualifiedName(node.getSuperClassName()));
			for (final ClassName interfaceName : node.getInterfaces()) {
				writer.printUnindented(", public " + CppClassNameUtil.makeQualifiedName(interfaceName));
			}
		}
		writer.printlnUnindented(" {");
		writer.increaseIndentation();
	}

	private void writeClassEnd() {
		writer.decreaseIndentation();
		writer.println("};");
		writer.println();
	}

	private void writeMembers(String access, List<MethodNode> methods) {
		if (methods.isEmpty()) {
			return;
		}
		writer.decreaseIndentation();
		writer.println(access);
		writer.increaseIndentation();

		for (int i = 0; i < methods.size(); i++) {
			if (i != 0) {
				writer.println();
			}
			final MethodNode methodNode = methods.get(i);
			methodNode.accept(this);
		}
	}

	private void printArguments(MethodNode method) {
		final String asString = method.getSignature().getArgumentTypes().stream().map(type -> CppTypeVisitor.getTypeName(type))
				.collect(Collectors.joining(", "));
		writer.printUnindented(asString);
	}

	public CppHeaderGeneratingVisitor(IndentingWriter writer) {
		this.writer = writer;
	}

	@Override
	public Void visit(ClassNode classNode) {
		final String guard = writeIncludeGuardStart(classNode.getName());
		// TODO : Includes
		writeNamespaceStart(classNode.getName());
		writeClassStart(classNode);

		writeMembers("public:", classNode.getPublicMethods());

		writeClassEnd();
		writeNamespaceEnd(classNode.getName());
		writeIncludeGuardEnd(guard);
		return null;
	}

	@Override
	public Void visit(MethodNode methodNode) {
		final MethodSignature signature = methodNode.getSignature();
		if (signature.getName().equals("<init>")) {
			writer.print("explicit " + methodNode.getOwningClass().getName().getName() + "(");
			printArguments(methodNode);
			writer.printlnUnindented(");");
		} else {
			writer.print(CppTypeVisitor.getTypeName(signature.getReturnType()));
			writer.printUnindented(" " + signature.getName() + "(");
			printArguments(methodNode);
			writer.printlnUnindented(");");
		}
		return null;
	}

}
