package cc.ethon.coldspot.frontend.ast;

public abstract class ByteCodeNode implements AstNode {

	protected int instructionIndex;

	public ByteCodeNode(int instructionIndex) {
		super();
		this.instructionIndex = instructionIndex;
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}

	public int getSmallestInstructionIndexWithChildren() {
		return instructionIndex;
	}

}
