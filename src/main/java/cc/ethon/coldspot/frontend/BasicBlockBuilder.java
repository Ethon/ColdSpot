package cc.ethon.coldspot.frontend;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

	public void processJumps(LabelManager labels) {
		for (int i = 0; i < basicBlocks.size(); i++) {
			final BasicBlock basicBlock = basicBlocks.get(i);
			if (basicBlock.getLeftBy() == LeftBy.JUMP || basicBlock.getLeftBy() == LeftBy.CONDITONAL_JUMP) {
				final int targetInstructionIndex = labels.getInstructionIndexForLabel(basicBlock.getJumpTarget());
				final int targetBlockIndex = getBlockIndexByInstructionIndex(targetInstructionIndex);
				final BasicBlock targetBlock = basicBlocks.get(targetBlockIndex);
				final Optional<BasicBlock[]> splitted = targetBlock.splitAt(targetInstructionIndex);
				if (splitted.isPresent()) {
					basicBlocks.remove(targetBlockIndex);
					basicBlocks.add(targetBlockIndex, splitted.get()[0]);
					basicBlocks.add(targetBlockIndex + 1, splitted.get()[1]);
					if (i <= targetBlockIndex) {
						++i;
					}
				}
			}
		}
	}

	public List<BasicBlock> getBasicBlocks() {
		return basicBlocks;
	}

	public int getBlockIndexByInstructionIndex(int instructionIndex) {
		for (int i = 0; i < basicBlocks.size(); ++i) {
			final BasicBlock basicBlock = basicBlocks.get(i);
			if (basicBlock.containsInstructionIndex(instructionIndex)) {
				return i;
			}
		}
		throw new IllegalArgumentException();
	}

	public BasicBlock getBlockByInstructionIndex(int instructionIndex) {
		return basicBlocks.get(getBlockIndexByInstructionIndex(instructionIndex));
	}

	public void addStatement(StatementNode statement) {
		if (isBasicBlockFinished || basicBlocks.isEmpty()) {
			basicBlocks.add(new BasicBlock(new StatementBlock(statement.getInstructionIndex())));
		}
		last().getStatements().getStatements().add(statement);
	}

	public void injectStatementBefore(StatementNode statement) {
		getBlockByInstructionIndex(statement.getInstructionIndex()).injectStatementBefore(statement);
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

}
