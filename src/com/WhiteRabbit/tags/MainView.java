package com.WhiteRabbit.tags;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.view.View;

public class MainView extends View {
	
	private Model model;
	private TagSet tagSet;
	private TagsActivityCallbackInterface callback;
	private int maxX = 10000;
	private int maxY = 10000;
	private boolean isVector = true;
	
	private Map<Long, Bitmap> bmps = new HashMap<Long, Bitmap>();

	public MainView(Context context, TagsActivityCallbackInterface callback) {
		super(context);
		setMinimumWidth(200);
		setMinimumHeight(300);
		model = new Model(callback);
		tagSet = new TagSet(model);
		this.callback = callback;
/*		loadBitmap(0L,    R.drawable.p0000); loadBitmap(100L,  R.drawable.p0100);
		loadBitmap(200L,  R.drawable.p0200); loadBitmap(300L,  R.drawable.p0300);
		loadBitmap(308L,  R.drawable.p0308); loadBitmap(400L,  R.drawable.p0400);
		loadBitmap(500L,  R.drawable.p0500); loadBitmap(600L,  R.drawable.p0600);
		loadBitmap(601L,  R.drawable.p0601); loadBitmap(700L,  R.drawable.p0700);
		loadBitmap(800L,  R.drawable.p0800); loadBitmap(900L,  R.drawable.p0900);
		loadBitmap(904L,  R.drawable.p0904); loadBitmap(1000L, R.drawable.p1000);
		loadBitmap(1100L, R.drawable.p1100); loadBitmap(1200L, R.drawable.p1200);
		loadBitmap(1202L, R.drawable.p1202); loadBitmap(1300L, R.drawable.p1300);
		loadBitmap(1400L, R.drawable.p1400); loadBitmap(1500L, R.drawable.p1500);
		loadBitmap(112L,  R.drawable.p0112); loadBitmap(209L,  R.drawable.p0209);
		loadBitmap(403L,  R.drawable.p0403); loadBitmap(806L,  R.drawable.p0806);
		loadBitmap(201L,  R.drawable.p0201); loadBitmap(208L,  R.drawable.p0208);
		loadBitmap(108L,  R.drawable.p0108); loadBitmap(104L,  R.drawable.p0104);
		loadBitmap(402L,  R.drawable.p0402); loadBitmap(401L,  R.drawable.p0401);
		loadBitmap(804L,  R.drawable.p0804); loadBitmap(802L,  R.drawable.p0802);
		loadBitmap(1L,    R.drawable.p0001); loadBitmap(2L,    R.drawable.p0002);
		loadBitmap(3L,    R.drawable.p0003); loadBitmap(4L,    R.drawable.p0004);
		loadBitmap(5L,    R.drawable.p0005); loadBitmap(6L,    R.drawable.p0006);
		loadBitmap(7L,    R.drawable.p0007); loadBitmap(8L,    R.drawable.p0008);
		loadBitmap(9L,    R.drawable.p0009); loadBitmap(10L,   R.drawable.p0010);
		loadBitmap(11L,   R.drawable.p0011); loadBitmap(12L,   R.drawable.p0012);
		loadBitmap(13L,   R.drawable.p0013); loadBitmap(14L,   R.drawable.p0014);
		loadBitmap(15L,   R.drawable.p0015);*/
	}

/*	private void loadBitmap(Long id, int name) {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), name);
		bmps.put(id, bmp);
	}*/
	
	public Bitmap getBitmap(Long id) {
		Bitmap r = bmps.get(id);
		return r;
	}
	
	public void setMaxSizes(int x, int y) {
		maxX = x;
		maxY = y;
		isVector = true;
		model.setMarginSize();
	}
	
	public void setProtected() {
		model.setProtected();
	}
	
	public boolean getProtected() {
		return model.getProtected();
	}
	
	public int getPhysX() {
		return model.getPhysX();
	}
	
	public int getPhysY() {
		return model.getPhysY();
	}
	
	public void clearShowTitle() {
		model.clearShowTitle();
	}

	public long getCurrStep() {
		return model.getCurrStep();
	}
	
	public void addRedo(long ordNum, long tagId, int dx, int dy) {
		model.addRedo(ordNum, tagId, dx, dy);
	}
	
/*	public void saveRedo() {
		model.saveRedo();
	}*/

	public void undo() {
		if (model.undo()) {
			invalidate();
		}
	}
	
	public void redo() {
		if (model.redo()) {
			invalidate();
		}
	}
	
	public void setCurrStep(long currStep) {
		model.setCurrStep(currStep);
	}
	
	public void loadPuzzle(int puzzleId) {
		model.loadPuzzle(puzzleId, -1);
		invalidate();
	}
	
	public void loadPuzzle(int puzzleId, long positionId) {
		model.loadPuzzle(puzzleId, positionId);
		invalidate();
	}
	
	public void savePosition(long positionId) {
		model.savePosition(positionId);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if ((MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.UNSPECIFIED) ||
			(MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED)) {
			setMeasuredDimension(maxX, maxY);
		} else {
			int szX = MeasureSpec.getSize(widthMeasureSpec);
			int szY = MeasureSpec.getSize(heightMeasureSpec);
			if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
				if (szX > maxX) {
					szX = maxX;
				}
			}
			if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
				if (szY > maxY) {
					szY = maxY;
				}
			}
			setMeasuredDimension(szX, szY);
			model.setSizes(szX, szY);
		}
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		canvas.drawColor(Color.BLACK);
		DrawContext dc = new DrawContext(this, model, canvas); 
		model.draw(dc, isVector);
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				tagSet.setStart(event.getX(), event.getY());
				break;
			case MotionEvent.ACTION_MOVE: 	
				if (tagSet.setDelta(event.getX(), event.getY())) {
					invalidate();
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				tagSet.setDelta(event.getX(), event.getY());
				if (tagSet.close()) {
					invalidate();
					if (model.checkEndPositions()) {
						callback.onSucceed();
					}
				}
				break;
		}
		return true;
	}

}
