package cc.ethon.coldspot.frontend;

import java.util.Optional;
import java.util.Stack;

import cc.ethon.coldspot.frontend.ast.BinaryExpressionNode;
import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.ReturnStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableExpressionNode;

public class InstructionCodeBuilder {

	private int instructionIndex;
	private final BasicBlockBuilder basicBlockBuilder;
	private final LocalVariableManager locals;
	private final Stack<ExpressionNode> expressionStack;

	public InstructionCodeBuilder(BasicBlockBuilder basicBlockBuilder, LocalVariableManager locals) {
		this.instructionIndex = 0;
		this.basicBlockBuilder = basicBlockBuilder;
		this.locals = locals;
		this.expressionStack = new Stack<ExpressionNode>();
	}

	public int getInstructionIndex() {
		return instructionIndex;
	}

	public void handleVariableExpression(int localIndex) {
		final VariableDeclarationStatementNode decl = locals.getVariableDeclarationForIndex(localIndex, instructionIndex);
		final VariableExpressionNode variableExpressionNode = new VariableExpressionNode(instructionIndex, decl);
		expressionStack.push(variableExpressionNode);
		++instructionIndex;
	}

	public void handleBinaryExpression(String operator) {
		final ExpressionNode right = expressionStack.pop();
		final ExpressionNode left = expressionStack.pop();
		expressionStack.push(new BinaryExpressionNode(instructionIndex, left, right, operator));
		++instructionIndex;
	}

	public void handleReturn(boolean withResult) {
		Optional<ExpressionNode> result = Optional.empty();
		if (withResult) {
			result = Optional.of(expressionStack.pop());
		}
		basicBlockBuilder.addStatement(new ReturnStatementNode(instructionIndex, result));
		basicBlockBuilder.finishBasicBlockByReturn();
		++instructionIndex;
	}

}
