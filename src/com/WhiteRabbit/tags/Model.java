package com.WhiteRabbit.tags;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;

public class Model implements ModelCallbackInterface {
	 
	public final static int DIRECTION_ERROR = -1;
	public final static int DIRECTION_NOTHING = 0;
	public final static int DIRECTION_UP = 1;
	public final static int DIRECTION_RIGHT = 2;
	public final static int DIRECTION_DOWN = 3;
	public final static int DIRECTION_LEFT = 4;
	
	private TagsActivityCallbackInterface callback;
	private int borderSize = 1;
	private int sizeX = 4;
	private int sizeY = 5;
	private int physX;
	private int physY;
	private int marginX = 0;
	private int marginY = 0;
	private int marginSize = 0;
	private int tagSize;
	private Map<Long, Tag> tags = new HashMap<Long, Tag>();
	private Map<Long, Tag> currTagSet = null;
	private Set<Map<Long, Tag>> endpositions = new HashSet<Map<Long, Tag>>();
	private boolean isShowTitle = true;
	private boolean isProtected = false;
	
	private Map<Long, RedoItem> redo = new HashMap<Long, RedoItem>(); 
	private Long currStep = 0L;
	private Long redoHwm = 0L;
	
	public Model(TagsActivityCallbackInterface callback) {
		this.callback = callback;
	}
	
	public void setMarginSize() {
		marginSize = 3;
	}
	
	public void setProtected() {
		isProtected = true;
	}
	
	public boolean getProtected() {
		return isProtected;
	}
	
	public void clearShowTitle() {
		isShowTitle = false;
	}
	
	public long getCurrStep() {
		if (isProtected) return 0;
		return currStep;
	}
	
	public void setCurrStep(long currStep) {
		this.currStep = currStep;
		if (isProtected) return;
		if (currStep == 0) {
			redo.clear();
		}
	}
	
/*	public void saveRedo() {
		callback.clearRedo();
		for (Long step: redo.keySet()) {
			RedoItem it = redo.get(step);
			callback.saveRedo(step, it.getTags(), it.getDx(), it.getDy());
		}
	}*/
	
	public int getStepCount(float sz) {
		int r = 0;
		while (sz > 0) {
			sz -= tagSize;
			r++;
		}
		return r;
	}
	
	private boolean checkEndPosition(Map<Long, Tag> pos) {
		boolean r = false;
		for (Tag tag: pos.values()) {
			for (Tag t: tags.values()) {
				if (tag.getId() == t.getId()) {
					if (tag.getX() != t.getX()) return false;
					if (tag.getY() != t.getY()) return false;
					r = true;
				}
			}
		}
		return r;
	}
	
	public boolean checkEndPositions() {
		if (isProtected) return false;
		for (Map<Long, Tag> pos: endpositions) {
			if (checkEndPosition(pos)) {
				return true;
			}
		}
		return false;
	}
	
	public int getDirection(Tag t, float dx, float dy) {
		int r = DIRECTION_NOTHING;
		if (check(t, 0, -1)) {
			if (r != DIRECTION_NOTHING) {
				r = DIRECTION_ERROR;
			} else {
				r = DIRECTION_UP;
			}
		}
		if (check(t, 1, 0)) {
			if (r != DIRECTION_NOTHING) {
				r = DIRECTION_ERROR;
			} else {
				r = DIRECTION_RIGHT;
			}
		}
		if (check(t, 0, 1)) {
			if (r != DIRECTION_NOTHING) {
				r = DIRECTION_ERROR;
			} else {
				r = DIRECTION_DOWN;
			}
		}
		if (check(t, -1, 0)) {
			if (r != DIRECTION_NOTHING) {
				r = DIRECTION_ERROR;
			} else {
				r = DIRECTION_LEFT;
			}
		}
		if (r != DIRECTION_ERROR) return r;
		int x = sign(dx); int y = sign(dy);
		r = DIRECTION_NOTHING;
		if (check(t, 0, -1)&&(x != 0)&&(y != -1)) {
			if (r != DIRECTION_NOTHING) {
				return DIRECTION_NOTHING;
			} else {
				r = DIRECTION_UP;
			}
		}
		if (check(t, 1, 0)&&(x != 1)&&(y != 0)) {
			if (r != DIRECTION_NOTHING) {
				return DIRECTION_NOTHING;
			} else {
				r = DIRECTION_RIGHT;
			}
		}
		if (check(t, 0, 1)&&(x != 0)&&(y != 1)) {
			if (r != DIRECTION_NOTHING) {
				return DIRECTION_NOTHING;
			} else {
				r = DIRECTION_DOWN;
			}
		}
		if (check(t, -1, 0)&&(x != -1)&&(y != 0)) {
			if (r != DIRECTION_NOTHING) {
				return DIRECTION_NOTHING;
			} else {
				r = DIRECTION_LEFT;
			}
		}
		return r;
	}
	
	public float getDx(int direct) {
		switch (direct) {
			case DIRECTION_LEFT:
				return -1;
			case DIRECTION_RIGHT:
				return 1;
		}
		return 0;
	}
	
	public float getDy(int direct) {
		switch (direct) {
			case DIRECTION_UP:
				return -1;
			case DIRECTION_DOWN:
				return 1;
		}
		return 0;
	}
	
	public void loadPuzzle(int puzzleId, long positionId) {
		callback.getParams(this, puzzleId);
		if (positionId > 0) {
			callback.getPosition(this, positionId);
		} else {
			callback.getItems(this, puzzleId);
		}
		currTagSet = new HashMap<Long, Tag>();
		callback.getEndPositions(this, puzzleId);
		if (!currTagSet.isEmpty()) {
			endpositions.add(currTagSet);
		}
		currTagSet = null;
		setSizes(physX, physY);
		isProtected = false;
	}
	
	public void savePosition(long positionId) {
		for (Tag t: tags.values()) {
			for (Item i: t.getItems()) {
				if (i.getIsMain()) {
					callback.putTag(positionId, t.getId(), i.getX(), i.getY());
				}
			}
		}
	}
	
	public void setParam(int paramId, int value) {
		switch (paramId) {
			case TagsDb.ParamTypes.SizeX:
				sizeX = value;
				break;
			case TagsDb.ParamTypes.SizeY:
				sizeY = value;
				break;
		}
	}

	public void clearTags() {
		if (currTagSet == null) {
			tags.clear();
			endpositions.clear();
		} else {
			if (!currTagSet.isEmpty()) { 
				endpositions.add(currTagSet);
				currTagSet = new HashMap<Long, Tag>();
			}
		}
	}

	public void addItem(int tagId, int x, int y, boolean isMain, int tagIx, long type) {
		Map<Long, Tag> tagSet = currTagSet;
		if (tagSet == null) {
			tagSet = tags;
		}
		Long id = new Long(tagId);
		Tag t = tagSet.get(id);
		if (t == null) {
			t = new Tag(tagId, tagIx);
			tagSet.put(id, t);
		}
		t.addItem(x, y, isMain, type);
	}
	
	public void setSizes(int x, int y) {
		physX = x;
		physY = y;
		int szX = (physX - 2 * marginSize) / sizeX;
		int szY = (physY - 2 * marginSize) / sizeY;
		if (szX < szY) {
			tagSize = szX;
			marginY = (physY - tagSize * sizeY) / 2;
			marginX = marginSize; 
		} else {
			tagSize = szY;
			marginY = marginSize;
			marginX = (physX - tagSize * sizeX) / 2; 
		}
	}
	
	public int getPhysX() {
		return sizeX * tagSize;
	}
	
	public int getPhysY() {
		return sizeY * tagSize;
	}
	
	public int getTagSize() {
		return tagSize;
	}
	
	public int getMarginX() {
		return marginX;
	}
	
	public int getMarginY() {
		return marginY;
	}
	
	public int getBorderSize() {
		return borderSize;
	}
	
	public int getX(int x) {
		if ((x < marginX) || (x >= physX - marginX)) return 0;
		x -= marginX;
		int sz = (physX - 2 * marginX) / sizeX;
		return (x / sz) + 1;
	}
	
	public int getY(int y) {
		if ((y < marginY) || (y >= physY - marginY)) return 0;
		y -= marginY;
		int sz = (physY - 2 * marginY) / sizeY;
		return (y / sz) + 1;
	}
	
	public Tag getTag(int x, int y) {
		for (Tag t: tags.values()) {
			for (Item i: t.getItems()) {
				if ((i.getX() == x)&&(i.getY() == y)) {
					return t;
				}
			}
		}
		return null;
	}
	
	public Tag findTag(float x, float y) {
		for (Tag t: tags.values()) {
			for (Item i: t.getItems()) {
				float tagSz = getTagSize();
				float startX = getMarginX();
				float startY = getMarginY();
				if (((i.getX()-1) * tagSz + startX <= x) &&
					((i.getX()-1) * tagSz + startX + tagSz > x) &&
					((i.getY()-1) * tagSz + startY <= y) &&
					((i.getY()-1) * tagSz + startY + tagSz > y)) {
					return t;
				}
			}
		}
		return null;
	}
	
	private int sign(float x) {
		if (x > 0) {
			return 1;
		} else if (x < 0) {
			return -1;
		} else {
			return 0;
		}
	}
	
	public void addRedo(long ordNum, long tagId, int dx, int dy) {
		RedoItem i = redo.get(ordNum);
		if (i == null) {
			i = new RedoItem(dx, dy);
		}
		i.addTag(tagId);
		redo.put(ordNum, i);
		if (ordNum >= redoHwm) {
			redoHwm = ordNum + 1; 
		}
	}
	
	public void saveRedo(Collection<Tag> tags, float x, float y, int c) {
		int dx = sign(x) * c; int dy = sign(y) * c;
		for (long i = currStep; i < redoHwm; i++) {
			redo.remove(i);
		}
		RedoItem i = new RedoItem(dx, dy);
		for (Tag t: tags) {
			i.addTag(t.getId());
		}
		redo.put(currStep, i);
		currStep++;
		redoHwm = currStep;
	}
	
	public boolean undo() {
		RedoItem r = redo.get(currStep - 1);
		if (r != null) {
			TagSet s = new TagSet(this, -r.getDx(), -r.getDy());
			for (Long tagId: r.getTags()) {
				Tag t = tags.get(tagId);
				if (t == null) return false;
				s.addTag(t);
			}
			if (!s.isModified()) return false;
			boolean f = isProtected;
			isProtected = false;
			if (!s.close()) {
				isProtected = f;
				return false;
			}
			isProtected = f;
			currStep--;
			return true;
		}
		return false;
	}
	
	public boolean redo() {
		RedoItem r = redo.get(currStep);
		if (r != null) {
			TagSet s = new TagSet(this, r.getDx(), r.getDy());
			for (Long tagId: r.getTags()) {
				Tag t = tags.get(tagId);
				if (t == null) return false;
				s.addTag(t);
			}
			if (!s.isModified()) return false;
			boolean f = isProtected;
			isProtected = false;
			if (!s.close()) {
				isProtected = f;
				return false;
			}
			isProtected = f;
			currStep++;
			return true;
		}
		return false;
	}
	
	public boolean moveTag(Tag t, int dx, int dy) {
		if (isProtected) return false;
		if (t == null) return false;
		if (dx + dy == 0) return false;
		if (check(t, dx, dy)) {
			for (Item i: t.getItems()) {
				i.move(dx, dy);
			}
			return true;
		}
		return false;
	}
	
	public boolean moveTag(Tag t, float dx, float dy) {
		return moveTag(t, sign(dx), sign(dy));
	}
	
	public boolean moveTag(TagSet s) {
		int dx = s.getDx();
		int dy = s.getDy();
		if (!check(s)) return false;
		for (Tag t: s.getTags()) {
			for (Item i: t.getItems()) {
				i.move(dx, dy);
			}
		}
		return true;
	}
	
	public boolean check(TagSet s) {
		int dx = s.getDx();
		int dy = s.getDy();
		if (s.getTags().isEmpty()) return false;
		if ((dx == 0)&&(dy == 0)) return false;
		if ((dx != 0)&&(dy != 0)) return false;
		for (;;) {
		for (Tag t: s.getTags()) {
				for (Item i: t.getItems()) {
					int newX = i.getX() + dx;
					int newY = i.getY() + dy; 
					if (newX < 1) return false;
					if (newX > sizeX) return false;
					if (newY < 1) return false;
					if (newY > sizeY) return false;
					Tag tag = getTag(newX, newY);
					if (!s.addTag(tag)) return false;
				}
			}
			if (!s.isModified()) break;
		}
		return true;
	}
	
	public boolean check(Tag t, int dx, int dy) {
		if (t == null) return false;
		if ((dx == 0)&&(dy == 0)) return false;
		if ((dx != 0)&&(dy != 0)) return false;
		for (Item i: t.getItems()) {
			int newX = i.getX() + dx;
			int newY = i.getY() + dy; 
			if (newX < 1) return false;
			if (newX > sizeX) return false;
			if (newY < 1) return false;
			if (newY > sizeY) return false;
			Tag tag = getTag(newX, newY); 
			if (tag != null) {
				if (tag != t) return false;
			}
		}
		return true;
	}
	
	public void draw(DrawContext dc, boolean isVector) {
		for (Tag t: tags.values()) {
			if (isVector) {
				t.draw(dc);
			} else {
				for (Item i: t.getItems()) {
					Bitmap bmp = dc.getBitmap(i.getType());
					if (bmp != null) {
						Bitmap tmp = Bitmap.createScaledBitmap(bmp, tagSize, tagSize, true);
						float x = (i.getX() - 1) * tagSize + marginX + t.getDeltaX();
						float y = (i.getY() - 1) * tagSize + marginY + t.getDeltaY();
						dc.paint(tmp, x, y);
					}
				}
			}
		}
		if (isProtected) return;
		if (isShowTitle) {
			Paint p = dc.getPaint();
			p.setARGB(150, 100, 255, 200);
			dc.getCanvas().drawText(String.format("%s: %d", callback.getLocalizedString(TagsActivity.STR_STEPS), currStep), 10 + marginX, 20 + marginY, p);
		}
	}

	
}
