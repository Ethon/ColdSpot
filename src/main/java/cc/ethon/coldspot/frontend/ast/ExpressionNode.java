package cc.ethon.coldspot.frontend.ast;

public abstract class ExpressionNode extends ByteCodeNode {

	public ExpressionNode(int instructionIndex) {
		super(instructionIndex);
	}

	public abstract int getPrecedence();

}
