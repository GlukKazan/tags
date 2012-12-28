package com.WhiteRabbit.tags;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TagSet {
	
	private final static int NONE       = 0;
	private final static int STARTED    = 1;
	private final static int VERTICAL   = 2;
	private final static int HORISONTAL = 3;
	
	private Model   model;
	private int     status = NONE;
	private float   x;
	private float   y;
	private float   eps    = 5; 
	private float   lastDelta;
	private float   delta;
	private int     comitted;
	private boolean isFreezed;
	private int     cnt;
	private boolean isProtected = false;
	private float   prevDx = 0;
	private float   prevDy = 0;
	
	private Set<Tag> tags = new HashSet<Tag>();
	private Set<Tag> newTags = new HashSet<Tag>();
	
	public TagSet(Model model) {
		this.model = model;
	}
	
	public TagSet(Model model, int dx, int dy) {
		this.model = model;
		if (dx != 0) {
			status = HORISONTAL;
			delta = dx * model.getTagSize();
		} else {
			status = VERTICAL;
			delta = dy * model.getTagSize();
		}
		comitted = 0;
		isProtected = true;
	}
	
	private void clear() {
		for (Tag t: tags) {
			t.setTagSet(null);
		}
		tags.clear();
		status = NONE;
		cnt = 0;
	}
	
	public Collection<Tag> getTags() {
		return tags;
	}
	
	public boolean isModified() {
		if (newTags.isEmpty()) return false;
		for (Tag t: newTags) {
			tags.add(t);
		}
		newTags.clear();
		return true;
	}
	
	public boolean addTag(Tag t) {
		if (t == null) return true;
		if (tags.contains(t)) return true;
		if (isFreezed) return false;
		newTags.add(t);
		t.setTagSet(this);
		return true;
	}
	
	public void setStart(float x, float y) {
		clear();
		this.x = x;
		this.y = y;
		Tag t = model.findTag(x, y);
		if (t != null) {
			comitted = 0;
			status = STARTED;
			tags.add(t);
			t.setTagSet(this);
			lastDelta = 0;
			isFreezed = false;
		}
	}
	
	private float abs(float x) {
		if (x >= 0) {
			return x;
		} else {
			return -x;
		}
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
	
	public int getDx() {
		switch (status) {
			case HORISONTAL:
				return sign(delta);
		}
		return 0;
	}
	
	public int getDy() {
		switch (status) {
			case VERTICAL:
				return sign(delta);
		}
		return 0;
	}
	
	public float getDeltaX() {
		if (comitted > abs(delta)) return 0;
		if (status != HORISONTAL) return 0;
		return (abs(delta) - comitted)*sign(delta);
	}
	
	public float getDeltaY() {
		if (comitted > abs(delta)) return 0;
		if (status != VERTICAL) return 0;
		return (abs(delta) - comitted)*sign(delta);
	}
	
	public boolean setDelta(float x, float y) {
		switch (status) {
			case NONE:
				return false;
			case VERTICAL:
				delta = y - this.y;
				break;
			case HORISONTAL:
				delta = x - this.x;
				break;
			case STARTED:
				float dx = x - this.x; 
				float dy = y - this.y;
				if ((abs(dx) <= eps)&&(abs(dy) <= eps)) return false;
				if (abs(dx) > abs(dy)) {
					status = HORISONTAL;
					delta = dx;
				} 
				if (abs(dy) > abs(dx)) {
					status = VERTICAL;
					delta = dy;
				} 
				if (status == STARTED) return false;
				break;
		}
		if (comitted > abs(delta)) {
			return false;
		}
		boolean r = false;
		while (abs(delta) - comitted > model.getTagSize()) {
			if (!model.moveTag(this)) break;
			comitted += model.getTagSize();
			r = true;
			cnt++;
		}
		if (abs(delta) - lastDelta < eps) {
			return false;
		}
		lastDelta = abs(delta);
		if (!model.check(this)) {
			delta = comitted * sign(delta);
			return r;
		}
		isFreezed = true;
		return true;
	}
	
	public boolean close() {
		isFreezed = true;
		if (status == STARTED) {
			for (Tag t: tags) {
				int direct = model.getDirection(t, -prevDx, -prevDy);
				if (direct == Model.DIRECTION_NOTHING) {
					if (abs(prevDx) > 0) {
						status = HORISONTAL;
						delta = prevDx;
					} else {
						status = VERTICAL;
						delta = prevDy;
					}
				} else {
					prevDx = model.getDx(direct);
					prevDy = model.getDy(direct);
				}
				switch (direct) {
					case Model.DIRECTION_LEFT:
					case Model.DIRECTION_RIGHT:
						status = HORISONTAL;
						delta = prevDx;
						break;
					case Model.DIRECTION_UP:
					case Model.DIRECTION_DOWN:
						status = VERTICAL;
						delta = prevDy;
						break;
				}
				break;
			}
		}
		if (status != STARTED) {
			while (abs(delta) - comitted > 0) {
				if (!model.moveTag(this)) break;
				comitted += model.getTagSize();
				cnt++;
			}
			if (!isProtected &&(cnt > 0)) {
				model.saveRedo(tags, getDx(), getDy(), cnt);
			}
		}
		clear();
		return true;
	}

}
