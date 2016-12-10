package cc.ethon.coldspot.frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;

import cc.ethon.coldspot.common.type.Type;
import cc.ethon.coldspot.frontend.ast.MethodNode;
import cc.ethon.coldspot.frontend.ast.VariableDeclarationStatementNode;

class LocalVariableManager {

	private final LabelManager labels;
	private final Map<Integer, List<VariableDeclarationStatementNode>> locals;

	public LocalVariableManager(LabelManager labels) {
		this.labels = labels;
		locals = new HashMap<Integer, List<VariableDeclarationStatementNode>>();
	}

	public VariableDeclarationStatementNode getVariableDeclarationForIndex(int localIndex, int instructionIndex) {
		final List<VariableDeclarationStatementNode> variables = locals.get(localIndex);
		if (variables == null || variables.isEmpty()) {
			throw new IllegalArgumentException();
		}
		VariableDeclarationStatementNode bestMatch = null;
		for (final VariableDeclarationStatementNode cur : variables) {
			if (cur.getInstructionIndex() <= instructionIndex) {
				if (bestMatch != null) {
					bestMatch = cur.getInstructionIndex() > bestMatch.getInstructionIndex() ? cur : bestMatch;
				} else {
					bestMatch = cur;
				}
			}
		}
		if (bestMatch == null) {
			throw new IllegalArgumentException();
		}
		return bestMatch;
	}

	public void addLocal(int localIndex, VariableDeclarationStatementNode decl) {
		List<VariableDeclarationStatementNode> forIndex = locals.get(localIndex);
		if (forIndex == null) {
			forIndex = new ArrayList<VariableDeclarationStatementNode>();
			locals.put(localIndex, forIndex);
		}
		forIndex.add(decl);
	}

	public void addMethodArguments(MethodNode methodNode) {
		for (int i = 0; i < methodNode.getArguments().size(); i++) {
			final VariableDeclarationStatementNode variableDeclarationNode = methodNode.getArguments().get(i);
			addLocal(i, variableDeclarationNode);
		}
	}

	public void updateNameAndType(int localIndex, Label start, Label end, String name, Type type) {
		final List<VariableDeclarationStatementNode> decls = locals.get(localIndex);
		if (decls == null || decls.isEmpty()) {
			return;
		}
		for (final VariableDeclarationStatementNode decl : decls) {
			if (labels.isInstructionIndexBetweenLabels(start, end, decl.getInstructionIndex())) {
				decl.setName(name);
				decl.setType(type);
			}
		}
	}

}
