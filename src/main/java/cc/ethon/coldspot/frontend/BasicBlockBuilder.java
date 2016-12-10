package cc.ethon.coldspot.frontend;

import java.util.ArrayList;
import java.util.List;

import cc.ethon.coldspot.frontend.BasicBlock.LeftBy;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;

class BasicBlockBuilder {

	private final List<BasicBlock> basicBlocks;
	private boolean isBasicBlockFinished;

	private BasicBlock last() {
		return basicBlocks.get(basicBlocks.size() - 1);
	}

	public BasicBlockBuilder() {
		basicBlocks = new ArrayList<BasicBlock>();
	}

	public void addStatement(StatementNode statement) {
		if (isBasicBlockFinished || basicBlocks.isEmpty()) {
			basicBlocks.add(new BasicBlock(new StatementBlock(statement.getInstructionIndex())));
		}
		last().getStatements().getStatements().add(statement);
	}

	public void finishBasicBlock() {
		isBasicBlockFinished = true;
	}

	public void finishBasicBlockByReturn() {
		finishBasicBlock();
		last().setLeftBy(LeftBy.RETURN);
	}

	public StatementBlock compile() {
		return basicBlocks.get(0).getStatements();
	}

}
