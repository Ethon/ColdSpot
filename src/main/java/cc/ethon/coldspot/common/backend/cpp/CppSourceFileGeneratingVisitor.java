package cc.ethon.coldspot.common.backend.cpp;

import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.backend.IndentingWriter;
import cc.ethon.coldspot.frontend.ast.AstVisitor;
import cc.ethon.coldspot.frontend.ast.ClassNode;

class CppSourceFileGeneratingVisitor implements AstVisitor<Void> {

	private final IndentingWriter writer;

	private void writeIncludes(ClassName className) {
		writer.println("#include <" + className.getPackageAsPath() + "/" + className.getName() + ".hpp>");
		writer.println();
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

	public CppSourceFileGeneratingVisitor(IndentingWriter writer) {
		super();
		this.writer = writer;
	}

	@Override
	public Void visit(ClassNode classNode) {
		writeIncludes(classNode.getName());
		writeNamespaceStart(classNode.getName());

		writeNamespaceEnd(classNode.getName());
		return null;
	}

}
