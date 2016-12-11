package cc.ethon.coldspot.frontend;

import java.util.Optional;
import java.util.Stack;

import org.objectweb.asm.Label;

import cc.ethon.coldspot.frontend.ast.AssignmentStatementNode;
import cc.ethon.coldspot.frontend.ast.BinaryExpressionNode;
import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.IncrementExpressionNode;
import cc.ethon.coldspot.frontend.ast.LiteralExpressionNode;
import cc.ethon.coldspot.frontend.ast.ReturnStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableExpressionNode;
import cc.ethon.coldspot.frontend.errors.InvalidLocalVariableException;

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

	public void handleLiteralExpression(String literal) {
		expressionStack.push(new LiteralExpressionNode(instructionIndex, literal));
		++instructionIndex;
	}

	public void handleIntLiteralExpression(int literal) {
		handleLiteralExpression(String.valueOf(literal));
	}

	public void handleLocalVariableStore(int localIndex) {
		final Optional<VariableDeclarationStatementNode> decl = locals.getVariableDeclarationForIndex(localIndex, instructionIndex);
		final ExpressionNode value = expressionStack.pop();
		VariableDeclarationStatementNode target = null;
		if (!decl.isPresent()) {
			// We need to pass the next instruction index as the
			// label indicating variable lifetime comes after the store.
			target = locals.addUnresolvedLocal(instructionIndex + 1, localIndex);
		} else {
			target = decl.get();
		}
		final VariableExpressionNode targetPseudoNode = new VariableExpressionNode(instructionIndex, target);
		basicBlockBuilder.addStatement(new AssignmentStatementNode(localIndex, targetPseudoNode, value));
		++instructionIndex;
	}

	public void handleLocalVariableLoad(int localIndex) throws InvalidLocalVariableException {
		final Optional<VariableDeclarationStatementNode> decl = locals.getVariableDeclarationForIndex(localIndex, instructionIndex);
		if (!decl.isPresent()) {
			throw new InvalidLocalVariableException("Attempt to load local variable " + localIndex + ", which was not declared");
		}
		final VariableExpressionNode variableExpressionNode = new VariableExpressionNode(instructionIndex, decl.get());
		expressionStack.push(variableExpressionNode);
		++instructionIndex;
	}

	public void handleConditionalBinaryJump(String operator, Label target) {
		final ExpressionNode right = expressionStack.pop();
		final ExpressionNode left = expressionStack.pop();
		final ExpressionNode condition = new BinaryExpressionNode(instructionIndex, left, right, operator);
		basicBlockBuilder.finishBasicBlockByConditionalJump(instructionIndex, target, condition);
		++instructionIndex;
	}

	public void handleIncrement(int localIndex, int increment) throws InvalidLocalVariableException {
		final Optional<VariableDeclarationStatementNode> decl = locals.getVariableDeclarationForIndex(localIndex, instructionIndex);
		if (!decl.isPresent()) {
			throw new InvalidLocalVariableException("Attempt to increment local variable " + localIndex + ", which was not declared");
		}
		expressionStack.push(new IncrementExpressionNode(instructionIndex, decl.get(), increment));
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
		basicBlockBuilder.finishBasicBlockByReturn(instructionIndex);
		++instructionIndex;
	}

}
