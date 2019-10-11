package edu.upc.essi.mongo.exp;

public class Exper {
	public String name;
	public int size;
	public int count;
	public String path;

	/**
	 * @param name
	 * @param size
	 * @param count
	 */
	public Exper(String name, int size, int count) {
		this.name = name;
		this.size = size;
		this.count = count;
	}

	public Exper(String name, int size, int count, String path) {
		this.name = name;
		this.size = size;
		this.count = count;
		this.path = path;
	}

}
