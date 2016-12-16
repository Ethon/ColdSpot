package cc.ethon.coldspot.backend.cpp;

import java.util.List;
import java.util.stream.Collectors;

import cc.ethon.coldspot.backend.IndentingWriter;
import cc.ethon.coldspot.backend.cpp.GenerationSettings.MemoryManagement;
import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.MethodSignature;
import cc.ethon.coldspot.common.type.Type;
import cc.ethon.coldspot.frontend.ast.AssignmentStatementNode;
import cc.ethon.coldspot.frontend.ast.AstVisitor;
import cc.ethon.coldspot.frontend.ast.BinaryExpressionNode;
import cc.ethon.coldspot.frontend.ast.ClassNode;
import cc.ethon.coldspot.frontend.ast.DoWhileLoopStatementNode;
import cc.ethon.coldspot.frontend.ast.ExpressionStatementNode;
import cc.ethon.coldspot.frontend.ast.IncrementExpressionNode;
import cc.ethon.coldspot.frontend.ast.LiteralExpressionNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.ast.ReturnStatementNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableExpressionNode;

class CppHeaderGeneratingVisitor implements AstVisitor<Void> {

	private final IndentingWriter writer;
	private final GenerationSettings settings;
	private final CppTypeVisitor typeVisitor;

	private String getTypeName(Type type) {
		return type.accept(typeVisitor);
	}

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

	private void writeIncludes() {
		writer.println("// C++ Standard Library:");
		writer.println("#include <cstdint>");
		if (settings.getMemoryManagement() == MemoryManagement.SHARED_PTR) {
			writer.println("#include <memory>");
		}
		// TODO: Write includes for used object types.
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

	private void writeClassStart(ClassNode node) {
		writer.print("class " + node.getName().getName());
		if (node.getSuperClassName() != null) {
			writer.printUnindented(" : public " + CppClassNameUtil.makeQualifiedName(node.getSuperClassName()));
			for (final ClassName interfaceName : node.getInterfaces()) {
				writer.printUnindented(", public " + CppClassNameUtil.makeQualifiedName(interfaceName));
			}
		} else {
			// This has to be java.lang.Object
			if (!node.getName().equals(new ClassName("java/lang/Object"))) {
				throw new IllegalArgumentException();
			}
			if (settings.getMemoryManagement() == MemoryManagement.SHARED_PTR) {
				writer.printUnindented(": public std::shared_from_this<" + CppClassNameUtil.makeQualifiedName(node.getName()) + ">");
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

	private void writeArguments(MethodNode method) {
		final String asString = method.getArguments().stream().filter(arg -> !arg.getName().equals("this"))
				.map(arg -> getTypeName(arg.getType()) + " " + arg.getName()).collect(Collectors.joining(", "));
		writer.printUnindented(asString);
	}

	public CppHeaderGeneratingVisitor(IndentingWriter writer, GenerationSettings settings) {
		this.writer = writer;
		this.settings = settings;
		this.typeVisitor = new CppTypeVisitor(settings);
	}

	@Override
	public Void visit(ClassNode classNode) {
		final String guard = writeIncludeGuardStart(classNode.getName());
		writeIncludes();
		writeNamespaceStart(classNode.getName());
		writeClassStart(classNode);

		writeMembers("private:", classNode.getPrivateMethods());
		writeMembers("protected:", classNode.getProtectedMethods());
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
			writeArguments(methodNode);
			writer.printlnUnindented(");");
		} else {
			writer.print(getTypeName(signature.getReturnType()));
			writer.printUnindented(" " + signature.getName() + "(");
			writeArguments(methodNode);
			writer.printlnUnindented(");");
		}
		return null;
	}

	@Override
	public Void visit(VariableExpressionNode variableExpressionNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(StatementBlock statementBlock) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(ReturnStatementNode returnStatementNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(BinaryExpressionNode binaryExpressionNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(VariableDeclarationStatementNode variableDeclarationNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(LiteralExpressionNode literalExpressionNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(AssignmentStatementNode assignmentStatementNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(IncrementExpressionNode incrementExpressionNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(DoWhileLoopStatementNode doWhileLoopStatementNode) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Void visit(ExpressionStatementNode expressionStatementNode) {
		throw new UnsupportedOperationException();
	}

}
