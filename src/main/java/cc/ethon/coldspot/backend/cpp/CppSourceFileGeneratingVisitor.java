package cc.ethon.coldspot.backend.cpp;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import cc.ethon.coldspot.backend.IndentingWriter;
import cc.ethon.coldspot.common.ClassName;
import cc.ethon.coldspot.common.MethodSignature;
import cc.ethon.coldspot.common.type.Type;
import cc.ethon.coldspot.frontend.ast.AssignmentStatementNode;
import cc.ethon.coldspot.frontend.ast.AstVisitor;
import cc.ethon.coldspot.frontend.ast.BinaryExpressionNode;
import cc.ethon.coldspot.frontend.ast.ClassNode;
import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.LiteralExpressionNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.ast.ReturnStatementNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableExpressionNode;

class CppSourceFileGeneratingVisitor implements AstVisitor<String> {

	private final IndentingWriter writer;
	@SuppressWarnings("unused")
	private final GenerationSettings settings;
	private final CppTypeVisitor typeVisitor;

	private String getTypeName(Type type) {
		return type.accept(typeVisitor);
	}

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
		final String asString = method.getArguments().stream().filter(arg -> !arg.getName().equals("this"))
				.map(arg -> getTypeName(arg.getType()) + " " + arg.getName()).collect(Collectors.joining(", "));
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
			writer.print(getTypeName(signature.getReturnType()));
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

	public CppSourceFileGeneratingVisitor(IndentingWriter writer, GenerationSettings settings) {
		this.writer = writer;
		this.settings = settings;
		this.typeVisitor = new CppTypeVisitor(settings);
	}

	@Override
	public String visit(ClassNode classNode) {
		writeIncludes(classNode.getName());
		writeMembers(classNode.getPrivateMethods());
		writeMembers(classNode.getProtectedMethods());
		writeMembers(classNode.getPublicMethods());
		return null;
	}

	@Override
	public String visit(MethodNode methodNode) {
		writeMethodStart(methodNode);
		methodNode.getBody().accept(this);
		writeMethodEnd();
		return null;
	}

	@Override
	public String accept(VariableExpressionNode variableExpressionNode) {
		return variableExpressionNode.getName();
	}

	@Override
	public String accept(StatementBlock statementBlock) {
		for (final StatementNode statementNode : statementBlock.getStatements()) {
			statementNode.accept(this);
		}
		return null;
	}

	@Override
	public String accept(ReturnStatementNode returnStatementNode) {
		final Optional<ExpressionNode> result = returnStatementNode.getResult();
		if (result.isPresent()) {
			writer.println("return " + result.get().accept(this) + ";");
		} else {
			writer.println("return;");
		}

		return null;
	}

	@Override
	public String accept(BinaryExpressionNode binaryExpressionNode) {
		final String left = binaryExpressionNode.getLeft().accept(this);
		final String right = binaryExpressionNode.getRight().accept(this);
		return String.format("(%s %s %s)", left, binaryExpressionNode.getOperator(), right);
	}

	@Override
	public String accept(VariableDeclarationStatementNode variableDeclarationNode) {
		writer.printf("%s %s;%n", getTypeName(variableDeclarationNode.getType()), variableDeclarationNode.getName());
		return null;
	}

	@Override
	public String accept(LiteralExpressionNode literalExpressionNode) {
		return literalExpressionNode.getLiteral();
	}

	@Override
	public String accept(AssignmentStatementNode assignmentStatementNode) {
		final String left = assignmentStatementNode.getLeft().accept(this);
		final String right = assignmentStatementNode.getRight().accept(this);
		writer.printf("%s = %s;%n", left, right);
		return null;
	}

}
