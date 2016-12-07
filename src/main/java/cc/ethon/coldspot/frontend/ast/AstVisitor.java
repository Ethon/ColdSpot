package cc.ethon.coldspot.frontend.ast;

public interface AstVisitor<T> {

	public T visit(ClassNode classNode);

}
