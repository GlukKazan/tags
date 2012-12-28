package com.WhiteRabbit.tags;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.WhiteRabbit.tags.R;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TagsActivity extends Activity implements TagsActivityCallbackInterface {
	
	private static final int BASE_MENU = 100;
	private static final int BASE_NEW_MENU = 200;
	private static final int BASE_LOCALE_MENU = 300;
	
	private static final int RECORDS_REQUEST = 1;
	private static final int PUZZLE_REQUEST = 3;
	
	private static final int MENU_NEW_ITEM = 8;
	private static final int MENU_PROFILE_ITEM = 10;
	public  static final int STR_STEPS = 13; 

	private MainView view;
	private Menu mainMenu = null;
	private Map<Long, String> stringCache = new HashMap<Long, String>();
	
	private int currentLocale = TagsDb.Locales.LOCALE_EN;
	private int currentPuzzle;
	private int currentProfile;
	private long currentPosition;
	private long sessionId = -1;
	private boolean isSessionCreated = false;
	
    private static final String[] GROUP_PROJECTION =
            new String[] {
    			TagsDb.Groups._ID,
    			TagsDb.StringValues.COLUMN_NAME_VALUE
        };

/*  private static final String[] REDO_PROJECTION =
            new String[] {
    			TagsDb.SolutionSteps._ID,
    			TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM,
                TagsDb.StepTag.COLUMN_NAME_TAG_ID,
                TagsDb.SolutionSteps.COLUMN_NAME_DX,
                TagsDb.SolutionSteps.COLUMN_NAME_DY
        };*/
    
    private static final String[] ENDPOS_PROJECTION =
            new String[] {
                TagsDb.EndPositions._ID,
                TagsDb.EndPositions.COLUMN_NAME_POSITION_ID
        };
    
    private static final String[] STAT_PROJECTION =
            new String[] {
                TagsDb.Stats._ID,
                TagsDb.Stats.COLUMN_NAME_STAT_TYPE_ID,
                TagsDb.Stats.COLUMN_NAME_VALUE
        };
    
    private static final String[] PROFILE_PROJECTION =
            new String[] {
                TagsDb.Profiles._ID,
                TagsDb.Profiles.COLUMN_NAME_LOCALE_ID,
                TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID,
                TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID
        };

/*  private static final String[] POSITION_PROJECTION =
            new String[] {
                TagsDb.Puzzles._ID,
                TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID
        };*/
    
    private static final String[] SESSION_PROJECTION =
            new String[] {
                TagsDb.Sessions._ID,
                TagsDb.Sessions.COLUMN_NAME_PROFILE_ID,
                TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID,
                TagsDb.Sessions.COLUMN_NAME_POSITION_ID,
                TagsDb.Sessions.COLUMN_NAME_IS_CLOSED,
                TagsDb.Profiles.COLUMN_NAME_LOCALE_ID
        };
    
/*  private static final String[] MENU_PROJECTION =
            new String[] {
                TagsDb.Puzzles._ID,
                TagsDb.StringValues.COLUMN_NAME_VALUE
        };*/
    
    private static final String[] LOCALE_PROJECTION =
            new String[] {
                TagsDb.Locales._ID,
                TagsDb.StringValues.COLUMN_NAME_VALUE
        };
    
    private static final String[] STRINGS_PROJECTION =
            new String[] {
                TagsDb.StringValues._ID,
                TagsDb.StringValues.COLUMN_NAME_VALUE
        };
    
    private static final String[] PARAMS_PROJECTION =
            new String[] {
                TagsDb.Params._ID,
                TagsDb.Params.COLUMN_NAME_PARAM_TYPE_ID,
                TagsDb.Params.COLUMN_NAME_VALUE
        };

    private static final String[] PUZZLE_PROJECTION =
            new String[] {
                TagsDb.Items._ID,
                TagsDb.Tags.COLUMN_NAME_IX,
                TagsDb.Items.COLUMN_NAME_TAG_ID,
                TagsDb.Items.COLUMN_NAME_IMG_ID,
                TagsDb.Items.COLUMN_NAME_ITEM_X,
                TagsDb.Items.COLUMN_NAME_ITEM_Y,
                TagsDb.Items.COLUMN_NAME_X,
                TagsDb.Items.COLUMN_NAME_Y
        };
    
    public void loadStat() {
    	view.setCurrStep(0);
		Uri uri = ContentUris.withAppendedId(TagsDb.Stats.CONTENT_URI, TagsDb.StatTypes.Steps);
		Cursor cursor = managedQuery(
				uri,
	            STAT_PROJECTION,
	            TagsDb.Stats.COLUMN_NAME_SESSION_ID + " = ?",      
	            new String [] {Long.toString(sessionId)},
	            null 
	        );
		if (cursor.moveToFirst()) {
			int valueColumn = cursor.getColumnIndex(TagsDb.Stats.COLUMN_NAME_VALUE);
			long currStep = cursor.getLong(valueColumn);
	    	view.setCurrStep(currStep);
		}
//		loadRedo(sessionId);
    }
    
/*	public void clearRedo() {
    	if (sessionId < 0) return;
		Uri uri = TagsDb.SolutionSteps.CONTENT_URI;
		getContentResolver().delete(uri, 
			TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + " = ?", 
			new String [] {Long.toString(sessionId)});
	}
	
	public void saveRedo(long step, Collection<Long> tags, int dx, int dy) {
    	if (sessionId < 0) return;
		Uri uri = TagsDb.SolutionSteps.CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID, sessionId);
		values.put(TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM, step);
		values.put(TagsDb.SolutionSteps.COLUMN_NAME_DX, dx);
		values.put(TagsDb.SolutionSteps.COLUMN_NAME_DY, dy);
		String redoId = getContentResolver().insert(uri, values).getPathSegments().get(1);
		for (Long tagId: tags) {
			uri = TagsDb.StepTag.CONTENT_URI;
			values.clear();
			values.put(TagsDb.StepTag.COLUMN_NAME_SESSION_ID, sessionId);
			values.put(TagsDb.StepTag.COLUMN_NAME_REDO_ID, redoId);
			values.put(TagsDb.StepTag.COLUMN_NAME_TAG_ID, tagId);
			getContentResolver().insert(uri, values);
		}
	}*/
    
    public void putStat() {
    	if (sessionId < 0) return;
		Uri uri = TagsDb.Stats.CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(TagsDb.Stats.COLUMN_NAME_STAT_TYPE_ID, TagsDb.StatTypes.Steps);
		values.put(TagsDb.Stats.COLUMN_NAME_SESSION_ID, sessionId);
		values.put(TagsDb.Stats.COLUMN_NAME_VALUE, view.getCurrStep());
		getContentResolver().insert(uri, values);
    }
    
    public void saveStat() {
    	if (sessionId < 0) return;
		Uri uri = ContentUris.withAppendedId(TagsDb.Stats.CONTENT_URI, TagsDb.StatTypes.Steps);
		ContentValues values = new ContentValues();
		values.put(TagsDb.Stats.COLUMN_NAME_VALUE, view.getCurrStep());
		if (getContentResolver().update(uri, values, TagsDb.Stats.COLUMN_NAME_SESSION_ID + " = " + Long.toString(sessionId), null) == 0) {
			putStat();
		}
    }
    
/*  public void loadRedo(long sessionId) {
		Uri uri = ContentUris.withAppendedId(TagsDb.SolutionSteps.CONTENT_URI, sessionId);
		Cursor cursor = managedQuery(
				uri,
	            REDO_PROJECTION,
	            null,      
	            null,
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int ordNumColumn = cursor.getColumnIndex(TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM); 
			int tagIdColumn = cursor.getColumnIndex(TagsDb.StepTag.COLUMN_NAME_TAG_ID); 
			int dxColumn = cursor.getColumnIndex(TagsDb.SolutionSteps.COLUMN_NAME_DX); 
			int dyColumn = cursor.getColumnIndex(TagsDb.SolutionSteps.COLUMN_NAME_DY);
			long ordNum = cursor.getLong(ordNumColumn);
			long tagId = cursor.getLong(tagIdColumn);
			int dx = cursor.getInt(dxColumn);
			int dy = cursor.getInt(dyColumn);
			view.addRedo(ordNum, tagId, dx, dy);
		}
    }*/
    
	public void getMenu(Menu menu) {
		Uri uri = TagsDb.Groups.CONTENT_URI;
		Cursor cursor = managedQuery(
				uri,
	            GROUP_PROJECTION,
	            "b." + TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " = ?",      
	            new String [] {Integer.toString(currentLocale)},
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int idColumn = cursor.getColumnIndex(TagsDb.Groups._ID); 
			int nameColumn = cursor.getColumnIndex(TagsDb.StringValues.COLUMN_NAME_VALUE); 
			int id = cursor.getInt(idColumn);
			String name = cursor.getString(nameColumn);
        	menu.add(0, id + BASE_NEW_MENU, id, name);
		}
	}
	
	public String getLocalizedString(long id) {
		String r = stringCache.get(id);
		if (r != null) {
			return r;
		}
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
		stringCache.put(id, r);
		return r;
	}
	
	public void getParams(ModelCallbackInterface callback, int id) {
		Uri uri = ContentUris.withAppendedId(TagsDb.Params.CONTENT_URI, id);
		Cursor cursor = managedQuery(
				uri,
	            PARAMS_PROJECTION,
	            null,      
	            null,
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int paramIdColumn = cursor.getColumnIndex(TagsDb.Params.COLUMN_NAME_PARAM_TYPE_ID); 
			int paramValueColumn = cursor.getColumnIndex(TagsDb.Params.COLUMN_NAME_VALUE);
			int paramId = cursor.getInt(paramIdColumn);
			int paramValue = cursor.getInt(paramValueColumn);
			callback.setParam(paramId, paramValue);
		}
	}
	
	public void getItems(ModelCallbackInterface callback, int id) {
		callback.clearTags();
		Uri uri = ContentUris.withAppendedId(TagsDb.Puzzles.CONTENT_URI, id);
		Cursor cursor = managedQuery(
				uri,
				PUZZLE_PROJECTION,
	            null,      
	            null,
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int tagIdColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_TAG_ID);
			int tagIxColumn = cursor.getColumnIndex(TagsDb.Tags.COLUMN_NAME_IX);
			int xColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_X); 
			int yColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_Y);
			int imgColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_IMG_ID);
			int itemXColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_ITEM_X);
			int itemYColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_ITEM_Y);
			int tagId = cursor.getInt(tagIdColumn);
			int tagIx = cursor.getInt(tagIxColumn);
			int x = cursor.getInt(xColumn);
			int y = cursor.getInt(yColumn);
			int itemX = cursor.getInt(itemXColumn);
			int itemY = cursor.getInt(itemYColumn);
			long img = cursor.getLong(imgColumn);
			callback.addItem(tagId, x, y, (itemX == 0)&&(itemY == 0), tagIx, img);
		}
	}
  
	public void getPosition(ModelCallbackInterface callback, long id) {
		callback.clearTags();
		Uri uri = ContentUris.withAppendedId(TagsDb.Positions.CONTENT_URI, id);
		Cursor cursor = managedQuery(
				uri,
				PUZZLE_PROJECTION,
				"b." + TagsDb.Tags.COLUMN_NAME_PUZZLE_ID + " = ?",      
	            new String[] {Long.toString(currentPuzzle)},
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int tagIdColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_TAG_ID); 
			int tagIxColumn = cursor.getColumnIndex(TagsDb.Tags.COLUMN_NAME_IX);
			int xColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_X); 
			int yColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_Y);
			int imgColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_IMG_ID);
			int itemXColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_ITEM_X);
			int itemYColumn = cursor.getColumnIndex(TagsDb.Items.COLUMN_NAME_ITEM_Y);
			int tagId = cursor.getInt(tagIdColumn);
			int tagIx = cursor.getInt(tagIxColumn);
			int x = cursor.getInt(xColumn);
			int y = cursor.getInt(yColumn);
			int itemX = cursor.getInt(itemXColumn);
			int itemY = cursor.getInt(itemYColumn);
			long img = cursor.getLong(imgColumn);
			callback.addItem(tagId, x, y, (itemX == 0)&&(itemY == 0), tagIx, img);
		}
	}
	
	public void getEndPositions(ModelCallbackInterface callback, int id) {
		Uri uri = ContentUris.withAppendedId(TagsDb.EndPositions.CONTENT_URI, id);
		Cursor cursor = managedQuery(
				uri,
				ENDPOS_PROJECTION,
				null,      
	            null,
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int positionIdColumn = cursor.getColumnIndex(TagsDb.EndPositions.COLUMN_NAME_POSITION_ID);
			long positionId = cursor.getLong(positionIdColumn);
			getPosition(callback, positionId);
		}		
	}
	
	public void getLocales(Menu menu) {
		Uri uri = TagsDb.Locales.CONTENT_URI;
		Cursor cursor = managedQuery(
				uri,
	            LOCALE_PROJECTION,
	            "b." + TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " = ?",      
	            new String [] {Integer.toString(currentLocale)},
	            null 
	        );
		for (cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()) {
			int idColumn = cursor.getColumnIndex(TagsDb.Puzzles._ID); 
			int nameColumn = cursor.getColumnIndex(TagsDb.StringValues.COLUMN_NAME_VALUE); 
			int id = cursor.getInt(idColumn);
			String name = cursor.getString(nameColumn);
        	menu.add(0, id + BASE_LOCALE_MENU, id, name);
		}
	}
	
	private void getCurrentLocale() {
		currentLocale = 1;
		Configuration conf = getResources().getConfiguration();
		String localeName = conf.locale.toString();
		Uri uri = TagsDb.Locales.CONTENT_URI;
		Cursor cursor = managedQuery(
				uri,
	            LOCALE_PROJECTION,
	            "a." + TagsDb.Locales.COLUMN_NAME_NAME + " = ?",      
	            new String [] {localeName},
	            null 
	        );
		if (cursor.moveToFirst()) {
			int idColumn = cursor.getColumnIndex(TagsDb.Puzzles._ID); 
			currentLocale = cursor.getInt(idColumn);
		} else {
			cursor = managedQuery(
					uri,
		            LOCALE_PROJECTION,
		            "a." + TagsDb.Locales.COLUMN_NAME_IS_DEFAULT + " = 1",      
		            null,
		            null 
		        );
			if (cursor.moveToFirst()) {
				int idColumn = cursor.getColumnIndex(TagsDb.Puzzles._ID); 
				currentLocale = cursor.getInt(idColumn);
			}
		}
	}
	
	private int saveProfile() {
		Uri uri = TagsDb.Profiles.CONTENT_URI;
		ContentValues values = new ContentValues();
		values.put(TagsDb.Profiles.COLUMN_NAME_LOCALE_ID, Integer.toString(currentLocale));
		values.put(TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID, Integer.toString(currentPuzzle));
		return getContentResolver().update(uri, values, null, null);
	}
	
	public void createPosition() {
		ContentValues values = new ContentValues();
		Uri uri = getContentResolver().insert(TagsDb.Positions.CONTENT_URI, values);
		if (uri != null) {
			currentPosition = Long.parseLong(uri.getPathSegments().get(1));
		} else {
			currentPosition = -1;
		}
	}
	
	public void saveSession() {
		Uri uri = ContentUris.withAppendedId(TagsDb.Sessions.CONTENT_URI, sessionId);
		ContentValues values = new ContentValues();
		values.put(TagsDb.Sessions.COLUMN_NAME_POSITION_ID, currentPosition);
		getContentResolver().update(uri, values, null, null);
	}
	
	public void createSession() {
		if (currentPosition < 0) return;
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");        
		ContentValues values = new ContentValues();
		values.put(TagsDb.Sessions.COLUMN_NAME_PROFILE_ID, currentProfile);
		values.put(TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID, currentPuzzle);
		values.put(TagsDb.Sessions.COLUMN_NAME_POSITION_ID, currentPosition);
		values.put(TagsDb.Sessions.COLUMN_NAME_IS_CLOSED, 0);
		values.put(TagsDb.Sessions.COLUMN_NAME_IS_CURRENT, 1);
		values.put(TagsDb.Sessions.COLUMN_NAME_START_DATE, formatter.format(now));
		Uri uri = getContentResolver().insert(TagsDb.Sessions.CONTENT_URI, values);
		if (uri != null) {
			sessionId = Long.parseLong(uri.getPathSegments().get(1));
		} else {
			sessionId = -1;
		}
	}
	
	private void closeSession() {
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");        
		Uri uri = ContentUris.withAppendedId(TagsDb.Sessions.CONTENT_URI, sessionId);
		ContentValues values = new ContentValues();
		values.put(TagsDb.Sessions.COLUMN_NAME_IS_CLOSED, 1);
		values.put(TagsDb.Sessions.COLUMN_NAME_END_DATE, formatter.format(now));
		getContentResolver().update(uri, values, null, null);
	}
	
	public void putTag(long positionId, int tagId, int x, int y) {
		ContentValues values = new ContentValues();
		values.put(TagsDb.TagPositions.COLUMN_NAME_POSITION_ID, positionId);
		values.put(TagsDb.TagPositions.COLUMN_NAME_TAG_ID, tagId);
		values.put(TagsDb.TagPositions.COLUMN_NAME_X, x);
		values.put(TagsDb.TagPositions.COLUMN_NAME_Y, y);
		getContentResolver().insert(TagsDb.TagPositions.CONTENT_URI, values);
	}
	
	public void deletePosition(long positionId) {
		Uri uri = ContentUris.withAppendedId(TagsDb.Positions.CONTENT_URI, positionId);
		getContentResolver().delete(uri, null, null);
	}
	
	public void deleteSession() {
		if (!isSessionCreated) return;
		if (sessionId < 0) return;
		Uri uri = ContentUris.withAppendedId(TagsDb.Sessions.CONTENT_URI, sessionId);
		getContentResolver().delete(uri, TagsDb.Sessions.COLUMN_NAME_IS_CLOSED + " = 0", null);
		isSessionCreated = false;
		sessionId = -1;
	}
	
	private boolean loadSession() {
		Uri uri = TagsDb.Sessions.CONTENT_URI;
		Cursor cursor = managedQuery(
				uri,
	            SESSION_PROJECTION,
	            TagsDb.Sessions.COLUMN_NAME_IS_CLOSED + " = 0 ",      
	            new String [] {Integer.toString(currentLocale)},
	            null 
	        );
		if (cursor.moveToFirst()) {
			int idColumn = cursor.getColumnIndex(TagsDb.Sessions._ID);
			int profileIdColumn = cursor.getColumnIndex(TagsDb.Sessions.COLUMN_NAME_PROFILE_ID); 
			int puzzleIdColumn = cursor.getColumnIndex(TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID); 
			int positionIdColumn = cursor.getColumnIndex(TagsDb.Sessions.COLUMN_NAME_POSITION_ID); 
			int localeIdColumn = cursor.getColumnIndex(TagsDb.Profiles.COLUMN_NAME_LOCALE_ID);
			sessionId = cursor.getInt(idColumn);
			currentProfile = cursor.getInt(profileIdColumn);
			currentPuzzle = cursor.getInt(puzzleIdColumn); 
			currentPosition = cursor.getInt(positionIdColumn); 
			currentLocale = cursor.getInt(localeIdColumn);
			isSessionCreated = true;
			return true;
		}
		return false;
	}
	
	public long getEndPositionId(int puzzleId) {
		Uri uri = ContentUris.withAppendedId(TagsDb.EndPositions.CONTENT_URI, puzzleId);
		Cursor cursor = managedQuery(
				uri,
				ENDPOS_PROJECTION,
				null,      
	            null,
	            null 
	        );
		if (cursor.moveToFirst()) {
			int positionIdColumn = cursor.getColumnIndex(TagsDb.EndPositions.COLUMN_NAME_POSITION_ID);
			return cursor.getLong(positionIdColumn);
		}
		return -1;
	}
	
	private void loadProfile() {
		Uri uri = TagsDb.Profiles.CONTENT_URI;
		Cursor cursor = managedQuery(
				uri,
	            PROFILE_PROJECTION,
	            null,      
	            null,
	            null 
	        );
		if (cursor.moveToFirst()) {
			int profileIdColumn = cursor.getColumnIndex(TagsDb.Profiles._ID); 
			int puzzleIdColumn = cursor.getColumnIndex(TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID); 
			int localeIdColumn = cursor.getColumnIndex(TagsDb.Profiles.COLUMN_NAME_LOCALE_ID);
			int positionIdColumn = cursor.getColumnIndex(TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID); 
			currentProfile = cursor.getInt(profileIdColumn);
			currentPuzzle = cursor.getInt(puzzleIdColumn); 
			currentPosition = cursor.getInt(positionIdColumn); 
			currentLocale = cursor.getInt(localeIdColumn); 
		}		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		mainMenu = menu;
		super.onCreateOptionsMenu(menu);
		SubMenu newMenu = menu.addSubMenu(BASE_MENU, BASE_MENU + MENU_NEW_ITEM, Menu.NONE, getLocalizedString(MENU_NEW_ITEM));
        getMenu(newMenu);
        newMenu = menu.addSubMenu(BASE_MENU, BASE_MENU + MENU_PROFILE_ITEM, Menu.NONE, getLocalizedString(MENU_PROFILE_ITEM));
        getLocales(newMenu);
		return true;
	}
	
	private void help() {
		long endPosId = getEndPositionId(currentPuzzle);
		if (endPosId > 0) {
			LayoutInflater inflater = getLayoutInflater();
			LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.help,
		                               	(ViewGroup) findViewById(R.id.help_page));
			MainView vw = new MainView(this, this);
			int sizeX = view.getPhysX() / 2;
			int sizeY = view.getPhysY() / 2;
			vw.setMaxSizes(sizeX, sizeY);
			vw.clearShowTitle();
			vw.loadPuzzle(currentPuzzle, endPosId);
			layout.addView(vw);
			
			Toast toast = new Toast(getApplicationContext());
			toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
			toast.setDuration(Toast.LENGTH_LONG);
			toast.setView(layout);
			toast.show();
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId >= BASE_LOCALE_MENU) {
			currentLocale = itemId - BASE_LOCALE_MENU;
			stringCache.clear();
			view.invalidate();
			if (mainMenu != null) {
				mainMenu.clear();
				onCreateOptionsMenu(mainMenu);
			}
			saveProfile();
		} else 
		if (itemId >= BASE_NEW_MENU) {
			Intent grid = new Intent(Intent.ACTION_VIEW);
			grid.setData(ContentUris.withAppendedId(TagsDb.Groups.CONTENT_URI, itemId - BASE_NEW_MENU));
			grid.putExtra(TagsDb.StringValues.COLUMN_NAME_LOCALE_ID, currentLocale);
			startActivityForResult(grid, PUZZLE_REQUEST);
		} 
		return true;
	}
	
	public void reloadPuzzle() {
		saveProfile();
    	loadProfile();
        view.loadPuzzle(currentPuzzle, currentPosition);
		view.setCurrStep(0);
		isSessionCreated = false;
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if ((requestCode == PUZZLE_REQUEST)&&(resultCode == RESULT_OK)) {
			Uri uri = data.getData();
			if (isSessionCreated) {
    			deletePosition(currentPosition);
    			deleteSession();
			}
			currentPuzzle = Integer.parseInt(uri.getPathSegments().get(1));
			reloadPuzzle();
			help();
			return;
		}
		switch (requestCode) {
			case RECORDS_REQUEST:
		    	loadProfile();
		    	createSession();
		        view.loadPuzzle(currentPuzzle, currentPosition);
		    	view.setCurrStep(0);
				break;
		}
	}

	public void onSucceed() {
		view.invalidate();
		savePosition();
		closeSession();
		Intent records = new Intent(Intent.ACTION_VIEW);
		records.setData(TagsDb.Sessions.CONTENT_URI);
		records.putExtra(TagsDb.Sessions.ALIAS_NAME_SESSION_ID, sessionId);
		records.putExtra(TagsDb.StringValues.COLUMN_NAME_LOCALE_ID, currentLocale);
		startActivityForResult(records, RECORDS_REQUEST);
	}
	
	private void savePosition() {
    	if (isSessionCreated) {
    		long oldPositionId = currentPosition;
    		createPosition();
    		if (currentPosition > 0) {
    			view.savePosition(currentPosition);
    		}
    		saveSession();
    		if (oldPositionId > 0) {
    			deletePosition(oldPositionId);
    		}
    		saveStat();
    	} else {
//    		if (view.getCurrStep() > 0) {
    			createPosition();
    			createSession();
    			if (currentPosition > 0) {
    				view.savePosition(currentPosition);
    			}
    			putStat();
            	isSessionCreated = true;
//    		}
    	}
/*    	if (sessionId > 0) {
    		view.saveRedo();
    	}*/
	}
	
    @Override
    protected void onPause() {
		savePosition();
    	super.onPause();
    }

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        LinearLayout.LayoutParams containerParams = 
        		new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
        				ViewGroup.LayoutParams.WRAP_CONTENT, 0.0F);
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.LTGRAY);
        root.setLayoutParams(containerParams);
        view = new MainView(this, this);
        if (loadSession()) {
        	loadStat();
        } else {
        	loadProfile();
        	if (currentLocale == 0) {
        		getCurrentLocale();
        		saveProfile();
        	}
        }
        view.loadPuzzle(currentPuzzle, currentPosition);
        root.addView(view);
        setContentView(root);
    }

}