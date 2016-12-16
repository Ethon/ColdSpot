package cc.ethon.coldspot.core;

import java.util.List;
import java.util.Optional;

import cc.ethon.coldspot.frontend.ast.AbstractAstVisitor;
import cc.ethon.coldspot.frontend.ast.AssignmentStatementNode;
import cc.ethon.coldspot.frontend.ast.ExpressionNode;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.ast.ReturnStatementNode;
import cc.ethon.coldspot.frontend.ast.StatementBlock;
import cc.ethon.coldspot.frontend.ast.StatementNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;
import cc.ethon.coldspot.frontend.ast.VariableExpressionNode;

class BeautifyingVisitor extends AbstractAstVisitor<Void> {

	@Override
	public Void visit(MethodNode methodNode) {
		final StatementBlock body = methodNode.getBody();
		final List<StatementNode> statements = body.getStatements();
		if (!statements.isEmpty()) {

			// Remove trailing return void at end of body.
			final StatementNode last = statements.get(statements.size() - 1);
			if (last instanceof ReturnStatementNode) {
				final ReturnStatementNode asReturn = (ReturnStatementNode) last;
				if (!asReturn.getResult().isPresent()) {
					statements.remove(statements.size() - 1);
				}
			}

		}
		return super.visit(methodNode);
	}

	@Override
	public Void visit(StatementBlock statementBlock) {
		final int size = statementBlock.getStatements().size();
		for (int i = 0; i < statementBlock.getStatements().size(); i++) {
			final StatementNode statement = statementBlock.getStatements().get(i);

			// Convert variable declarations with an immediately following
			// assignment to a combined form.
			if (statement instanceof VariableDeclarationStatementNode) {
				final VariableDeclarationStatementNode decl = (VariableDeclarationStatementNode) statement;
				if (i < size - 1) {
					final StatementNode nextStatement = statementBlock.getStatements().get(i + 1);
					if (nextStatement instanceof AssignmentStatementNode) {
						final AssignmentStatementNode asAssignment = (AssignmentStatementNode) nextStatement;
						final ExpressionNode left = asAssignment.getLeft();
						if (left instanceof VariableExpressionNode) {
							final VariableExpressionNode target = (VariableExpressionNode) left;
							if (target.getDeclaration() == decl) {
								statementBlock.getStatements().remove(i + 1);
								decl.setValue(Optional.of(asAssignment.getRight()));
							}
						}
					}
				}
			}
		}
		return null;
	}

}
