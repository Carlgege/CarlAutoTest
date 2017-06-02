package com.carl.table;

//EO: Entitity Object

public class ScriptEO {
	
	private Long ID;
	private String name;
	private String filepath;
	private String repeat;
	public static int defaultRepeat = 1;
	
	public ScriptEO() {}
	public ScriptEO(Long id, String name, String filepath, String repeat) {
		
		this.ID = id;
		this.name = name;
		this.filepath = filepath;
		this.repeat = repeat;
	}
	
	public Long getID() {
		return ID;
	}
	public void setID(Long iD) {
		ID = iD;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFilepath() {
		return filepath;
	}
	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}
	public String getRepeat() {
		return repeat;
	}
	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}
	
	
}
