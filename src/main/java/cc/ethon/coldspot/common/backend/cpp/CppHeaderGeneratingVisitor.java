package cc.ethon.coldspot.common.backend.cpp;

import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.backend.IndentingWriter;
import cc.ethon.coldspot.frontend.ast.AstVisitor;
import cc.ethon.coldspot.frontend.ast.ClassNode;

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

	public CppHeaderGeneratingVisitor(IndentingWriter writer) {
		this.writer = writer;
	}

	@Override
	public Void visit(ClassNode classNode) {
		final String guard = writeIncludeGuardStart(classNode.getName());
		// TODO : Includes
		writeNamespaceStart(classNode.getName());
		writeClassStart(classNode);

		writeClassEnd();
		writeNamespaceEnd(classNode.getName());
		writeIncludeGuardEnd(guard);
		return null;
	}

}
