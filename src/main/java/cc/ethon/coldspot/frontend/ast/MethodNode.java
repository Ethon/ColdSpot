package cc.ethon.coldspot.frontend.ast;

import cc.ethon.coldspot.common.MethodSignature;

public class MethodNode implements AstNode {

	private final ClassNode owningClass;
	private final MethodSignature signature;

	public MethodNode(ClassNode owningClass, MethodSignature signature) {
		super();
		this.owningClass = owningClass;
		this.signature = signature;
	}

	public ClassNode getOwningClass() {
		return owningClass;
	}

	public MethodSignature getSignature() {
		return signature;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

}
