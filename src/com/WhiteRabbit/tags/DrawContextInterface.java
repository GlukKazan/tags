package com.WhiteRabbit.tags;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface DrawContextInterface {
	public static int NOTHING = 0;
	public static int LEFT = 1;
	public static int RIGHT = 2;
	
	void point(int x, int y);
	void line(int dx, int dy, int direction);
	void paint(int x, int y, int tag);
	
	Paint getPaint();
	Canvas getCanvas();
}
