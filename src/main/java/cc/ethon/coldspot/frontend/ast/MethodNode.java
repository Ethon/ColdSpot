package cc.ethon.coldspot.frontend.ast;

import java.util.ArrayList;
import java.util.List;

import cc.ethon.coldspot.common.MethodSignature;
import cc.ethon.coldspot.common.type.ClassType;

public class MethodNode implements AstNode {

	private final ClassNode owningClass;
	private final MethodSignature signature;
	private final List<VariableDeclarationStatementNode> arguments;
	private StatementBlock body;

	private int maxStack;
	private int maxLocals;

	public MethodNode(ClassNode owningClass, MethodSignature signature) {
		super();
		this.owningClass = owningClass;
		this.signature = signature;
		this.arguments = new ArrayList<VariableDeclarationStatementNode>(signature.getArgumentTypes().size());

		final VariableDeclarationStatementNode thisVar = new VariableDeclarationStatementNode(0);
		thisVar.setName("this");
		thisVar.setType(ClassType.typeOf(owningClass.getName()));
		arguments.add(thisVar);

		for (int i = 0; i < signature.getArgumentTypes().size(); ++i) {
			final VariableDeclarationStatementNode argument = new VariableDeclarationStatementNode(0);
			argument.setName("arg" + i);
			argument.setType(this.signature.getArgumentTypes().get(i));
			arguments.add(argument);
		}
	}

	public ClassNode getOwningClass() {
		return owningClass;
	}

	public MethodSignature getSignature() {
		return signature;
	}

	public int getMaxStack() {
		return maxStack;
	}

	public void setMaxStack(int maxStack) {
		this.maxStack = maxStack;
	}

	public int getMaxLocals() {
		return maxLocals;
	}

	public void setMaxLocals(int maxLocals) {
		this.maxLocals = maxLocals;
	}

	public List<VariableDeclarationStatementNode> getArguments() {
		return arguments;
	}

	public StatementBlock getBody() {
		return body;
	}

	public void setBody(StatementBlock body) {
		this.body = body;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
