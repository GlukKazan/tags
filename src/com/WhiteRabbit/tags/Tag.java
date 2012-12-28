package com.WhiteRabbit.tags;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Tag {
	
	private int id;
	private int ix;
	private Set<Item> items = new HashSet<Item>();
	private int color = 0;
	private TagSet tagSet = null;
	
	public Tag(int id, int ix) {
		this.id = id;
		this.ix = ix;
	}
	
	public void setTagSet(TagSet tagSet) {
		this.tagSet = tagSet;
	}
	
	public int getId() {
		return id;
	}
	
	public int getColor(int x, int y) {
		int r = 0;
		for (Item it: items) {
			if (it.isNeighbor(x, y)) {
				r = it.getColor();
				break;
			}
		}
		if (r != 0) {
			for (Item it: items) {
				if (it.isNeighbor(x, y)) {
					it.setColor(r);
				}				
			}
			return r;
		}
		return ++color;
	}
	
	public Item getItem(int x, int y) {
		for (Item i: items) {
			if ((i.getX() == x)&&(i.getY() == y)) {
				return i;
			}
		}
		return null;
	}
	
	private void setType(Item n, int x, int y, int om, int nm) {
		Item o = getItem(x, y);
		if (o != null) {
			o.changeType(om);
			n.changeType(nm);
		}
	}

	private void setMask(Item n, int x, int y, int om, int nm) {
		Item o = getItem(x, y);
		if (o != null) {
			o.changeMask(om);
			n.changeMask(nm);
		}
	}

	public void addItem(int x, int y, boolean isMain, long type) {
		Item it = new Item(x, y, isMain);
		if (type > 0L) {
			it.setType(type);
		}
		setType(it, x - 1, y, Item.DIRECTION_LEFT, Item.DIRECTION_RIGHT);
		setType(it, x + 1, y, Item.DIRECTION_RIGHT, Item.DIRECTION_LEFT);
		setType(it, x, y - 1, Item.DIRECTION_BOTTOM, Item.DIRECTION_TOP);
		setType(it, x, y + 1, Item.DIRECTION_TOP, Item.DIRECTION_BOTTOM);
		setMask(it, x + 1, y + 1, Item.DIRECTION_RIGHT, Item.DIRECTION_LEFT);
		setMask(it, x - 1, y - 1, Item.DIRECTION_LEFT, Item.DIRECTION_RIGHT);
		setMask(it, x + 1, y - 1, Item.DIRECTION_BOTTOM, Item.DIRECTION_TOP);
		setMask(it, x - 1, y + 1, Item.DIRECTION_TOP, Item.DIRECTION_BOTTOM);
		it.setColor(getColor(x, y));
		items.add(it);
	}
	
	public Collection<Item> getItems() {
		return items;
	}
	
	public int getX() {
		for (Item it: items) {
			if (it.getIsMain()) {
				return it.getX();
			}
		}
		return -1;
	}

	public int getY() {
		for (Item it: items) {
			if (it.getIsMain()) {
				return it.getY();
			}
		}
		return -1;
	}

	public float getDeltaX() {
		if (tagSet == null) return 0;
		return tagSet.getDeltaX();
	}
	
	public float getDeltaY() {
		if (tagSet == null) return 0;
		return tagSet.getDeltaY();
	}
	
	private Item findFirstItem(/*int color*/) {
		int x = 0;
		for (Item i: items) {
//			if (i.getColor() == color) {
				if ((i.getX() < x) || (x == 0)) {
					x = i.getX();
				}
//			}
		}
		int y = 0;
		Item r = null;
		for (Item i: items) {
//			if (i.getColor() == color) {
				if (i.getX() == x) {
					if ((i.getY() < y) || (y == 0)) {
						r = i;
						y = i.getY();
					}
				}
//			}
		}
		return r;
	}
	
	private int getDx(int dx, int dy) {
		if ((dx == 0)&&(dy == -1)) return -1;
		if ((dx == 1)&&(dy == 0))  return 0;
		if ((dx == 0)&&(dy == 1))  return 1;
		if ((dx == -1)&&(dy == 0)) return 0;
		return 0;
	}
	
	private int getDy(int dx, int dy) {
		if ((dx == 0)&&(dy == -1)) return 0;
		if ((dx == 1)&&(dy == 0))  return -1;
		if ((dx == 0)&&(dy == 1))  return 0;
		if ((dx == -1)&&(dy == 0)) return 1;
		return 0;
	}
	
	private boolean find(int x, int y) {
		for (Item i: items) {
			if ((i.getX() == x)&&(i.getY() == y)) {
				return true;
			}
		}
		return false;
	}
	
	public void draw(DrawContextInterface context) {
//		for (int i = 1; i <= color; i++) {
			Item it = findFirstItem(/*i*/);
			if (it != null) {
				context.point(it.getX(), it.getY());
				int x = it.getX(); int y = it.getY();
				int dx = 0;	int dy = -1;
				while (true) {
					int nDx = getDx(dx, dy); 
					int nDy = getDy(dx, dy);
					if (find(x + dx, y + dy)) {
						if (find(x + dx + nDx, y + dy + nDy)) {
							context.line(nDx, nDy, DrawContext.LEFT);
							context.line(dx, dy, DrawContext.RIGHT);
							x += nDx + dx;
							y += nDy + dy;
						} else {
							context.line(dx, dy, DrawContext.NOTHING);
							x += dx; y += dy;
						}
					} else {
						nDx = -getDx(dx, dy); 
						nDy = -getDy(dx, dy);
						if (find(x + nDx + dx, y + nDy + dy)) {
							context.line(nDx, nDy, DrawContext.RIGHT);
							context.line(dx, dy, DrawContext.LEFT);
							x += nDx + dx;
							y += nDy + dy;
						} else {
							context.line(nDx, nDy, DrawContextInterface.RIGHT);
							dx = nDx; dy = nDy;
						}
					}
					if ((it.getX() == x)&&(it.getY() == y)&&(dx == 0)&&(dy == -1)) break;
				}
				context.paint(it.getX(), it.getY(), ix);
			}
//		}
	}
	
}
