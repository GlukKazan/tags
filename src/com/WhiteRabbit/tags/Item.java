package com.WhiteRabbit.tags;

public class Item {
	
	public final static int DIRECTION_LEFT = 1; 
	public final static int DIRECTION_TOP = 2; 
	public final static int DIRECTION_RIGHT = 4; 
	public final static int DIRECTION_BOTTOM = 8; 

	private int x;
	private int y;
	private boolean isMain = false;
	private int color = 0;
	private long type = 1500L;
	private int mask = 0;
	private long strongType = 0;
	
	public Item(int x, int y, boolean isMain) {
		this.x = x;
		this.y = y;
		this.isMain = isMain;
	}

	public void setType(long type) {
		this.strongType = type;
	}
	
	private int abs(int x) {
		if (x < 0) {
			return -x;
		} else {
			return x;
		}
	}
	
	public boolean isNeighbor(int x, int y) {
		if (this.x == x) {
			if (abs(this.y - y) == 1) {
				return true;
			}
		}
		if (this.y == y) {
			if (abs(this.x - x) == 1) {
				return true;
			}
		}
		return false;
	}
	
	public int getColor() {
		return color;
	}
	
	public void setColor(int color) {
		this.color = color;
	}
	
	public boolean getIsMain() {
		return isMain;
	}
	
	public long getType() {
		if (strongType > 0L) {
			return strongType;
		}
		if (mask != 0L) {
			for (long i = 1L; i <= 8L; i *= 2L) {
				long m = mask % 0x10;
				if (m == 3L) {
					type += i;
				}
				mask /= 0x10;
			}
			mask = 0;
		}
		return type;
	}
	
	public void changeType(int direction) {
		switch (direction) {
			case DIRECTION_LEFT:
				mask = mask | 0x0012;
				break;
			case DIRECTION_TOP:
				mask = mask | 0x0120;
				break;
			case DIRECTION_RIGHT:
				mask = mask | 0x1200;
				break;
			case DIRECTION_BOTTOM:
				mask = mask | 0x2001;
				break;
		}
		type -= direction * 100L;
	}
	
	public void changeMask(int direction) {
		switch (direction) {
			case DIRECTION_LEFT:
				mask = mask | 0x0004;
				break;
			case DIRECTION_TOP:
				mask = mask | 0x0040;
				break;
			case DIRECTION_RIGHT:
				mask = mask | 0x0400;
				break;
			case DIRECTION_BOTTOM:
				mask = mask | 0x4000;
				break;
		}
	}	

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public void move(int dx, int dy) {
		x += dx;
		y += dy;
	}

}
