package cc.ethon.coldspot.frontend;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;

import cc.ethon.coldspot.frontend.BasicBlock.LeftBy;
import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;

class BasicBlockBuilder {

	private final List<BasicBlock> basicBlocks;
	private boolean isBasicBlockFinished;

	private BasicBlock last() {
		return basicBlocks.get(basicBlocks.size() - 1);
	}

	private void ensureBasicBlockExists(int instructionIndex) {
		if (basicBlocks.isEmpty()) {
			basicBlocks.add(new BasicBlock(new StatementBlock(instructionIndex)));
		}
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

	public void injectStatementBefore(StatementNode statement) {
		for (final BasicBlock basicBlock : basicBlocks) {
			if (basicBlock.containsInstructionIndex(statement.getInstructionIndex())) {
				basicBlock.injectStatementBefore(statement);
				return;
			}
		}
		throw new IllegalArgumentException();
	}

	public void finishBasicBlockByReturn(int instructionIndex) {
		isBasicBlockFinished = true;
		ensureBasicBlockExists(instructionIndex);
		last().setLeftBy(LeftBy.RETURN);
	}

	public void finishBasicBlockByJump(int instructionIndex, Label target) {
		isBasicBlockFinished = true;
		ensureBasicBlockExists(instructionIndex);
		last().setLeftBy(LeftBy.JUMP);
		last().setJumpTarget(target);
	}

	public void finishBasicBlockByConditionalJump(int instructionIndex, Label target, ExpressionNode condition) {
		isBasicBlockFinished = true;
		ensureBasicBlockExists(instructionIndex);
		last().setLeftBy(LeftBy.CONDITONAL_JUMP);
		last().setJumpTarget(target);
		last().setJumpCondition(condition);
	}

	public StatementBlock compile() {
		return basicBlocks.get(0).getStatements();
	}

}
