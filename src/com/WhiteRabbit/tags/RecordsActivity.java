package com.WhiteRabbit.tags;

import java.util.HashSet;
import java.util.Set;

import android.app.Activity;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class RecordsActivity extends Activity {
	
	private static final long PUZZLE_COLUMN_NAME = 21; 
	private static final long DATE_COLUMN_NAME = 22; 
	private static final long SCORE_COLUMN_NAME = 23;
	
	private static final int MAX_RECORDS_IN_GROUP = 5;
	
	TableLayout layout;
	private int currentLocale; 
	
    private static final String[] STRINGS_PROJECTION =
            new String[] {
                TagsDb.StringValues._ID,
                TagsDb.StringValues.COLUMN_NAME_VALUE
        };

    private static final String[] SESSION_PROJECTION =
            new String[] {
                TagsDb.Sessions._ID,
                TagsDb.Puzzles.ALIAS_NAME_PUZZLE_NAME,
                TagsDb.Sessions.COLUMN_NAME_END_DATE,
                TagsDb.Stats.COLUMN_NAME_VALUE
        };
    
	public String getLocalizedString(long id) {
		String r = null;
		Uri uri = ContentUris.withAppendedId(TagsDb.Strings.CONTENT_URI, id);
		Cursor cursor = managedQuery(
				uri,
	            STRINGS_PROJECTION,
	            TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " = ?",      
	            new String [] {Integer.toString(currentLocale)},
	            null 
	        );
		if (cursor.moveToFirst()) {
			int stringsValueColumn = cursor.getColumnIndex(TagsDb.StringValues.COLUMN_NAME_VALUE);
			r = cursor.getString(stringsValueColumn);
		}
		return r;
	}
	
	private void addRow(String name, String date, String score, boolean isChecked) {
		TextView c;
        TableRow r = new TableRow(this);
        c = new TextView(this);
        c.setText("  ");
        r.addView(c);
        c = new TextView(this);
        c.setText(name);
        if (isChecked) {
        	c.setTypeface(null, Typeface.BOLD);
        	c.setTextColor(Color.WHITE);
        }
        r.addView(c);
        c = new TextView(this);
        c.setText(date);
        if (isChecked) {
        	c.setTypeface(null, Typeface.BOLD);
        	c.setTextColor(Color.WHITE);
        }
        r.addView(c);
        c = new TextView(this);
        c.setText(score);
        if (isChecked) {
        	c.setTypeface(null, Typeface.BOLD);
        	c.setTextColor(Color.WHITE);
        }
        c.setGravity(Gravity.RIGHT);
        r.addView(c);
        layout.addView(r);
	}

	private void addRow(String name, String date, int score, boolean isChecked) {
		addRow(name, date, Integer.toString(score), isChecked);
	}
	
	private void addSeparator() {
        TableRow r = new TableRow(this);
        layout.addView(r);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.records);
        layout = (TableLayout)findViewById(R.id.vw);
        
        Bundle b = getIntent().getExtras();
        currentLocale = b.getInt(TagsDb.StringValues.COLUMN_NAME_LOCALE_ID);
        TextView c = (TextView)findViewById(R.id.puzzle_name);
        if (c != null) {
        	c.setText(getLocalizedString(PUZZLE_COLUMN_NAME));
        }
        c = (TextView)findViewById(R.id.date);
        if (c != null) {
        	c.setText(getLocalizedString(DATE_COLUMN_NAME));
        }
        c = (TextView)findViewById(R.id.step_cnt);
        if (c != null) {
        	c.setText(getLocalizedString(SCORE_COLUMN_NAME));
        }
        
		Uri uri = TagsDb.Sessions.CONTENT_URI;
		Cursor cursor = managedQuery(
				uri,
	            SESSION_PROJECTION,
	            TagsDb.Sessions.COLUMN_NAME_IS_CLOSED + " = 1 and not " + TagsDb.Sessions.COLUMN_NAME_END_DATE + " is null",      
	            new String [] {Integer.toString(currentLocale)},
	            null 
	        );
		Set<Long> oldSessions = new HashSet<Long>();
		String prev = null;
		int recordsCnt = 0;
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int sessionIdColumn = cursor.getColumnIndex(TagsDb.Sessions._ID);
			int puzzleNameColumn = cursor.getColumnIndex(TagsDb.Puzzles.ALIAS_NAME_PUZZLE_NAME);
			int endDateColumn = cursor.getColumnIndex(TagsDb.Sessions.COLUMN_NAME_END_DATE);
			int scoreColumn = cursor.getColumnIndex(TagsDb.Stats.COLUMN_NAME_VALUE);
			Long sessionId = cursor.getLong(sessionIdColumn);
			String puzzleName = cursor.getString(puzzleNameColumn);
			String endDate = cursor.getString(endDateColumn);
			int score = cursor.getInt(scoreColumn);
			boolean isChecked = false;
			if (b.getLong(TagsDb.Sessions.ALIAS_NAME_SESSION_ID) == sessionId) {
				isChecked = true;
			}
			if ((prev != null)&& !prev.equals(puzzleName)) {
				addSeparator();
				recordsCnt = 0;
			}
			if ((isChecked ? recordsCnt : recordsCnt + 1) >= MAX_RECORDS_IN_GROUP) {
				oldSessions.add(sessionId);
			} else {
				addRow(puzzleName, endDate, score, isChecked);
				recordsCnt++;
			}
			prev = puzzleName;
		}
		for (Long id: oldSessions) {
			uri = ContentUris.withAppendedId(TagsDb.Sessions.CONTENT_URI, id);
			getContentResolver().delete(uri, TagsDb.Sessions.COLUMN_NAME_IS_CLOSED + " = 1", null);
		}
	}
	
}
