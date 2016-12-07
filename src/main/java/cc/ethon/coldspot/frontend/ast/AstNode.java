package cc.ethon.coldspot.frontend.ast;

public interface AstNode {

	public <T> T accept(AstVisitor<T> visitor);

}
