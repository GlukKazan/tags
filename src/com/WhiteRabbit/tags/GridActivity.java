package com.WhiteRabbit.tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GridActivity extends Activity {
	
    public static final long PUZZLE_1_IMG  = 200001L;
    public static final long PUZZLE_2_IMG  = 200002L;
    public static final long PUZZLE_3_IMG  = 200003L;
    public static final long PUZZLE_5_IMG  = 200005L;
    public static final long PUZZLE_6_IMG  = 200006L;
    public static final long PUZZLE_7_IMG  = 200007L;
    public static final long PUZZLE_8_IMG  = 200008L;
    public static final long PUZZLE_9_IMG  = 200009L;
    public static final long PUZZLE_10_IMG = 200010L;
    public static final long PUZZLE_11_IMG = 200011L;
    public static final long PUZZLE_12_IMG = 200012L;
    public static final long PUZZLE_13_IMG = 200013L;
    public static final long PUZZLE_14_IMG = 200014L;
    public static final long PUZZLE_15_IMG = 200015L;
    public static final long PUZZLE_17_IMG = 200017L;
    public static final long PUZZLE_18_IMG = 200018L;
    public static final long PUZZLE_19_IMG = 200019L;
    public static final long PUZZLE_20_IMG = 200020L;
	   
	private String groupId;
	private int currentLocale;
	TextView helpString;
	GridView icons;

	private Map<Long, Bitmap> bmps = new HashMap<Long, Bitmap>();
	
    private static final String[] GROUP_PROJECTION =
            new String[] {
    			TagsDb.Groups._ID,
    			TagsDb.StringValues.COLUMN_NAME_VALUE
        };
    
    private static final String[] MENU_PROJECTION =
            new String[] {
                TagsDb.Puzzles._ID,
                TagsDb.Puzzles.COLUMN_NAME_IMG_ID,
                TagsDb.StringValues.COLUMN_NAME_VALUE
        };
    
    public class AppsAdapter extends BaseAdapter {
    	
    	private Context c;
    	private GridActivity parent;
    	private List<Long> ids = new ArrayList<Long>();
    	private Map<Long, Long> img = new HashMap<Long, Long>();
    	
    	public AppsAdapter(Context c, GridActivity parent, String id) {
    		this.c = c;
    		this.parent = parent;
    	}
    	
    	public void addId(long id, long img_id) {
    		ids.add(id);
    		img.put(id, img_id);
    	}

		@Override
		public int getCount() {
			return ids.size();
		}

		@Override
		public Object getItem(int position) {
			long img_id = img.get(getItemId(position));
			return parent.getBitmap(img_id);
		}

		@Override
		public long getItemId(int position) {
			return ids.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
            ImageView i;
            if (convertView == null) {
                i = new ImageView(c);
                i.setScaleType(ImageView.ScaleType.FIT_CENTER);
                i.setLayoutParams(new GridView.LayoutParams(50, 50));
            } else {
                i = (ImageView) convertView;
            }
            Bitmap bmp = (Bitmap)getItem(position);
            if (bmp != null) {
            	i.setImageBitmap(bmp);
            }
			return i;
		}
    	
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
        
		loadBitmap(PUZZLE_1_IMG,   R.drawable.p200001); loadBitmap(PUZZLE_2_IMG,   R.drawable.p200002);
		loadBitmap(PUZZLE_3_IMG,   R.drawable.p200003); loadBitmap(PUZZLE_5_IMG,   R.drawable.p200005);
		loadBitmap(PUZZLE_6_IMG,   R.drawable.p200006); loadBitmap(PUZZLE_7_IMG,   R.drawable.p200007);
		loadBitmap(PUZZLE_8_IMG,   R.drawable.p200008); loadBitmap(PUZZLE_9_IMG,   R.drawable.p200009);
		loadBitmap(PUZZLE_10_IMG,  R.drawable.p200010); loadBitmap(PUZZLE_11_IMG,  R.drawable.p200011);
		loadBitmap(PUZZLE_12_IMG,  R.drawable.p200012); loadBitmap(PUZZLE_13_IMG,  R.drawable.p200013);
		loadBitmap(PUZZLE_14_IMG,  R.drawable.p200014); loadBitmap(PUZZLE_15_IMG,  R.drawable.p200015);
		loadBitmap(PUZZLE_17_IMG,  R.drawable.p200017); loadBitmap(PUZZLE_18_IMG,  R.drawable.p200018);
		loadBitmap(PUZZLE_19_IMG,  R.drawable.p200019); loadBitmap(PUZZLE_20_IMG,  R.drawable.p200020);
		
        helpString = (TextView)findViewById(R.id.help_string);
        icons = (GridView)findViewById(R.id.puzzle_list);
        
        Bundle b = getIntent().getExtras();
        currentLocale = b.getInt(TagsDb.StringValues.COLUMN_NAME_LOCALE_ID);
        Uri uri = getIntent().getData();
        
		groupId = uri.getPathSegments().get(1);
		Cursor cursor = managedQuery(
				uri,
				GROUP_PROJECTION,
	            "and b." + TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " = ? " +
           		"and a." + TagsDb.Groups._ID + " = ?",      
	            new String [] {Integer.toString(currentLocale), groupId},
	            null 
	        );
		if (cursor.moveToFirst()) {
			int helpColumn = cursor.getColumnIndex(TagsDb.StringValues.COLUMN_NAME_VALUE);
			helpString.setText(cursor.getString(helpColumn));
		}
		AppsAdapter adapter = new AppsAdapter(this, this, groupId);
		
		uri = TagsDb.Puzzles.CONTENT_URI;
		cursor = managedQuery(
				uri,
	            MENU_PROJECTION,
	            TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " = ? and a." + TagsDb.Puzzles.COLUMN_NAME_GROUP_ID + " = ?",      
	            new String [] {Integer.toString(currentLocale), groupId},
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int idColumn = cursor.getColumnIndex(TagsDb.Puzzles._ID);
			int imgIdColumn = cursor.getColumnIndex(TagsDb.Puzzles.COLUMN_NAME_IMG_ID);
			long id = cursor.getLong(idColumn);
			long imgId = cursor.getLong(imgIdColumn);
			adapter.addId(id, imgId);
		}
		icons.setAdapter(adapter);
		
		icons.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView perent, View v, int position, long id) {
				Uri uri = ContentUris.withAppendedId(TagsDb.Puzzles.CONTENT_URI, id);
				setResult(RESULT_OK, new Intent().setData(uri));
				finish();
			}
		});
	}
	
	private void loadBitmap(Long id, int name) {
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), name);
		bmps.put(id, bmp);
	}
	
	public Bitmap getBitmap(Long id) {
		Bitmap r = bmps.get(id);
		return r;
	}
}
