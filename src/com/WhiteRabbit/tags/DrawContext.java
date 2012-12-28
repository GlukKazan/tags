package com.WhiteRabbit.tags;

import android.graphics.Bitmap;
import android.graphics.Canvas; 
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;

public class DrawContext implements DrawContextInterface {
	
	private MainView view;
	private Canvas canvas;
	private Model model;
	private Matrix matrix = new Matrix();
	private Paint p;
	private Path pt;
	private int x;
	private int y;
	private int dx = 0;
	private int dy = 0;
	private int direction = 0;
	
	public DrawContext(MainView view, Model model, Canvas canvas) {
		this.view = view;
		this.model  = model;
		this.canvas = canvas;
		this.p      = new Paint();
		p.setStyle(Paint.Style.FILL_AND_STROKE);
		p.setAntiAlias(true);
	}
	
	public Bitmap getBitmap(Long id) {
		return view.getBitmap(id);
	}
	
	public void paint(Bitmap bmp, float x, float y) {
		matrix.setTranslate(x, y);
		canvas.drawBitmap(bmp, matrix, p);
	}
	
	public Paint getPaint() {
		return p;
	}
	
	public Canvas getCanvas() {
		return canvas;
	}

	public void point(int x, int y) {
		p.setARGB(255, 220, 50, 50);
		this.x = (x - 1) * model.getTagSize() + model.getMarginX() + model.getBorderSize();
		this.y = (y - 1) * model.getTagSize() + model.getMarginY() + model.getBorderSize();
		this.dx = 0;
		this.dy = 0;
		pt = new Path();
		pt.moveTo(this.x, this.y);
	}
	
	public void line(int dx, int dy, int direction) {
		if ((this.dx != 0)||(this.dy != 0)) {
			this.dx *= model.getTagSize();
			this.dy *= model.getTagSize();
			if ((direction == RIGHT)&&(this.direction != LEFT)) {
				this.dx -= 2 * model.getBorderSize() * sign(this.dx);
				this.dy -= 2 * model.getBorderSize() * sign(this.dy);
			}
			this.direction = direction;
			draw(this.dx, this.dy);
		}
		this.dx = dx;
		this.dy = dy;
	}
	
	public void paint(int x, int y, int tag) {
		dx *= model.getTagSize();
		dy *= model.getTagSize();
		dx -= 2 * model.getBorderSize() * sign(dx);
		dy -= 2 * model.getBorderSize() * sign(dy);
		draw(dx, dy);
		this.x = (x - 1) * model.getTagSize() + model.getMarginX() + model.getBorderSize() * 2;
		this.y = (y - 1) * model.getTagSize() + model.getMarginY() + model.getBorderSize() * 2;
		pt.close();
		canvas.drawPath(pt, p);
		// DEBUG:
/*		x -= 1; x *= model.getTagSize(); x += model.getMarginX() + model.getBorderSize() * 2;
		y -= 1; y *= model.getTagSize(); y += model.getMarginY() + model.getBorderSize() * 2;
		p.setColor(Color.BLACK);
		canvas.drawText(String.format("%d", tag), 2 + x, 10 + y, p);*/
	}
	
	private int sign(int x) {
		if (x < 0) {
			return -1;
		} else if (x > 0) {
			return 1;
		} else {
			return 0;
		}
	}
	
	private void draw(int dx, int dy) {
		pt.lineTo(x + dx, y + dy);
		x += dx; y += dy;
	}
	
}
