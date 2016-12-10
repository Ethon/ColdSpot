package cc.ethon.coldspot.frontend;

import java.awt.Label;

import cc.ethon.coldspot.frontend.ast.StatementBlock;

class BasicBlock {

	public enum LeftBy {
		RETURN, JUMP
	}

	private final StatementBlock statements;
	private LeftBy leftBy;
	private Label jumpTarget;

	public BasicBlock(StatementBlock statements) {
		super();
		this.statements = statements;
	}

	public StatementBlock getStatements() {
		return statements;
	}

	public LeftBy getLeftBy() {
		return leftBy;
	}

	public void setLeftBy(LeftBy leftBy) {
		this.leftBy = leftBy;
	}

	public Label getJumpTarget() {
		return jumpTarget;
	}

	public void setJumpTarget(Label jumpTarget) {
		this.jumpTarget = jumpTarget;
	}

}
