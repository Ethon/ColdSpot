package cc.ethon.coldspot.frontend.ast;

import java.util.ArrayList;
import java.util.List;

import cc.ethon.coldspot.common.ClassName;

public class ClassNode implements AstNode {

	private ClassName name;
	private ClassName superClassName;
	private List<ClassName> interfaces;

	private final List<MethodNode> publicMethods;
	private final List<MethodNode> protectedMethods;
	private final List<MethodNode> privateMethods;

	public ClassNode(ClassName name, ClassName superClassName, List<ClassName> interfaces) {
		this.name = name;
		this.superClassName = superClassName;
		this.interfaces = interfaces;

		this.publicMethods = new ArrayList<MethodNode>();
		this.protectedMethods = new ArrayList<MethodNode>();
		this.privateMethods = new ArrayList<MethodNode>();
	}

	public ClassName getName() {
		return name;
	}

	public void setName(ClassName name) {
		this.name = name;
	}

	public ClassName getSuperClassName() {
		return superClassName;
	}

	public void setSuperClassName(ClassName superClassName) {
		this.superClassName = superClassName;
	}

	public List<ClassName> getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(List<ClassName> interfaces) {
		this.interfaces = interfaces;
	}

	public List<MethodNode> getPublicMethods() {
		return publicMethods;
	}

	public List<MethodNode> getProtectedMethods() {
		return protectedMethods;
	}

	public List<MethodNode> getPrivateMethods() {
		return privateMethods;
	}

	@Override
	public <T> T accept(AstVisitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public String toString() {
		return "ClassNode [name=" + name + ", superClassName=" + superClassName + ", interfaces=" + interfaces + "]";
	}

}
