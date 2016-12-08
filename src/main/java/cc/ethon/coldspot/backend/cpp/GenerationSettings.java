package cc.ethon.coldspot.backend.cpp;

public class GenerationSettings {

	public enum MemoryManagement {
		SHARED_PTR
	}

	private MemoryManagement memoryManagement;

	public MemoryManagement getMemoryManagement() {
		return memoryManagement;
	}

	public void setMemoryManagement(MemoryManagement memoryManagement) {
		this.memoryManagement = memoryManagement;
	}

}
