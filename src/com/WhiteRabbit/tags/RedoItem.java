package com.WhiteRabbit.tags;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class RedoItem {
	
	private Set<Long> tags = new HashSet<Long>();
	private int dx;
	private int dy;
	
	public RedoItem(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}
	
	public void addTag(long tagId) {
		tags.add(tagId);
	}
	
	public Collection<Long> getTags() {
		return tags;
	}
	
	public int getDx() {
		return dx;
	}
	
	public int getDy() {
		return dy;
	}
	
}
