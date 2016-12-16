package cc.ethon.coldspot.core;

import cc.ethon.coldspot.frontend.ast.AstNode;

public class Beautifier {

	public AstNode beautify(AstNode node) {
		node.accept(new BeautifyingVisitor());
		return node;
	}

}
