package cc.ethon.coldspot.frontend;

import java.util.List;

import cc.ethon.coldspot.frontend.ast.DoWhileLoopStatementNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;

public class ControlFlowDecompiler {

	private final List<BasicBlock> basicBlocks;
	private final LabelManager labels;

	private boolean isLeftByUpJump(BasicBlock block) {
		return block.getLastInstructionIndex() > labels.getInstructionIndexForLabel(block.getJumpTarget());
	}

	@SuppressWarnings("unused")
	private boolean isLeftByDownJump(BasicBlock block) {
		return block.getLastInstructionIndex() < labels.getInstructionIndexForLabel(block.getJumpTarget());
	}

	private boolean isLeftByJumpToSameBlockStart(BasicBlock block) {
		return block.getFirstInstructionIndex() == labels.getInstructionIndexForLabel(block.getJumpTarget());
	}

	private StatementBlock merge(StatementBlock block1, StatementBlock block2) {
		block1.getStatements().addAll(block2.getStatements());
		return block1;
	}

	private StatementBlock merge(StatementNode statement, StatementBlock block) {
		final StatementBlock newBlock = new StatementBlock(statement.getSmallestInstructionIndexWithChildren());
		newBlock.getStatements().add(statement);
		newBlock.getStatements().addAll(block.getStatements());
		return newBlock;
	}

	private StatementBlock decompileLeftByReturn(int first, int last) {
		return basicBlocks.get(first).getStatements();
	}

	private StatementBlock decompileLeftByJump(int first, int last) {
		throw new UnsupportedOperationException();
	}

	private StatementBlock decompileLeftByConditionalJump(int first, int last) {
		final BasicBlock firstBlock = basicBlocks.get(first);
		if (isLeftByUpJump(firstBlock)) {
			// A conditional upjump to the begin of the block must be a
			// do-while-loop
			if (isLeftByJumpToSameBlockStart(firstBlock)) {
				final DoWhileLoopStatementNode doWhileLoopStatementNode = new DoWhileLoopStatementNode(firstBlock.getFirstInstructionIndex(),
						firstBlock.getJumpCondition(), firstBlock.getStatements());
				return merge(doWhileLoopStatementNode, decompile(first + 1, last));
			} else {
				throw new IllegalStateException();
			}
		}

		// If the target block does not jump up again, this is probably an if
		return null;
	}

	private StatementBlock decompile(int first, int last) {
		switch (basicBlocks.get(first).getLeftBy()) {
		case RETURN:
			return decompileLeftByReturn(first, last);
		case JUMP:
			return decompileLeftByJump(first, last);
		case CONDITONAL_JUMP:
			return decompileLeftByConditionalJump(first, last);
		case JUMP_TARGET:
			return merge(basicBlocks.get(first).getStatements(), decompile(first + 1, last));
		default:
			throw new UnsupportedOperationException();
		}
	}

	public ControlFlowDecompiler(BasicBlockBuilder basicBlocks, LabelManager labels) {
		super();
		this.basicBlocks = basicBlocks.getBasicBlocks();
		this.labels = labels;

		if (this.basicBlocks.isEmpty()) {
			throw new IllegalArgumentException("No blocks to decompile");
		}
	}

	public StatementBlock decompile() {
		return decompile(0, basicBlocks.size() - 1);
	}
}
