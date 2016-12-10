package cc.ethon.coldspot.frontend.ast;

public abstract class ByteCodeNode implements AstNode {

	private final int instructionIndex;

	public ByteCodeNode(int instructionIndex) {
		super();
		this.instructionIndex = instructionIndex;
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}

}
