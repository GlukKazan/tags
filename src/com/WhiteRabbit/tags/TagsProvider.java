package com.WhiteRabbit.tags;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

public class TagsProvider extends ContentProvider {
 
   private static final String DATABASE_NAME = "tags.db";
   private static final int DATABASE_VERSION = 12;
   
   private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
   private static final int PARAMS_URI_INDICATOR = 1;
   private static final int PUZZLE_URI_INDICATOR = 2;
   private static final int PUZZLE_LIST_URI_INDICATOR = 3;
   private static final int STRING_URI_INDICATOR = 4;
   private static final int LOCALE_URI_INDICATOR = 5;
   private static final int PROFILE_URI_INDICATOR = 6;
   private static final int PROFILE_ITEM_URI_INDICATOR = 7;
   private static final int SESSION_URI_INDICATOR = 8;
   private static final int SESSION_ITEM_URI_INDICATOR = 9;
   private static final int POSITION_URI_INDICATOR = 10;
   private static final int POSITION_ITEM_URI_INDICATOR = 11;
   private static final int TAG_URI_INDICATOR = 12;
   private static final int TAG_ITEM_URI_INDICATOR = 13;
   private static final int STAT_URI_INDICATOR = 14;
   private static final int STAT_ITEM_URI_INDICATOR = 15;
/* private static final int REDO_URI_INDICATOR = 16;
   private static final int REDO_ITEM_URI_INDICATOR = 17;
   private static final int REDO_TAG_URI_INDICATOR = 19;*/
   private static final int ENDPOS_ITEM_URI_INDICATOR = 18;
   private static final int GROUP_URI_INDICATOR = 20;
   private static final int GROUP_ITEM_URI_INDICATOR = 21;
   private DatabaseHelper mOpenHelper;
   
   static {
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Puzzles.PATH_PUZZLE, PUZZLE_LIST_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Puzzles.PATH_PUZZLE + "/#", PUZZLE_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Params.PATH_PARAMS + "/#", PARAMS_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Strings.PATH_STRING + "/#", STRING_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Locales.PATH_LOCALE, LOCALE_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Profiles.PATH_PROFILE, PROFILE_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Profiles.PATH_PROFILE + "/#", PROFILE_ITEM_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Sessions.PATH_SESSION, SESSION_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Sessions.PATH_SESSION + "/#", SESSION_ITEM_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Positions.PATH_POSITION, POSITION_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Positions.PATH_POSITION + "/#", POSITION_ITEM_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.TagPositions.PATH_TAG, TAG_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.TagPositions.PATH_TAG + "/#", TAG_ITEM_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Stats.PATH_STAT, STAT_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Stats.PATH_STAT + "/#", STAT_ITEM_URI_INDICATOR);
//     uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.SolutionSteps.PATH_REDO, REDO_URI_INDICATOR);
//     uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.SolutionSteps.PATH_REDO + "/#", REDO_ITEM_URI_INDICATOR);
//     uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.StepTag.PATH_REDO_TAG, REDO_TAG_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.EndPositions.PATH_ENDS  + "/#", ENDPOS_ITEM_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Groups.PATH_GROUP, GROUP_URI_INDICATOR);
       uriMatcher.addURI(TagsDb.AUTHORITY, TagsDb.Groups.PATH_GROUP  + "/#", GROUP_ITEM_URI_INDICATOR);
   }
   
   @Override
   public boolean onCreate() {
       mOpenHelper = new DatabaseHelper(getContext());
       return true;
   }

   @Override
   public String getType(Uri uri) {
	   switch (uriMatcher.match(uri)) {
  			case STRING_URI_INDICATOR:
   				return TagsDb.Strings.CONTENT_ITEM_TYPE;
    		case PARAMS_URI_INDICATOR:
    			return TagsDb.Params.CONTENT_ITEM_TYPE;
	   		case PUZZLE_LIST_URI_INDICATOR:
	   			return TagsDb.Puzzles.CONTENT_DIR_TYPE;
	   		case PUZZLE_URI_INDICATOR:
	   			return TagsDb.Puzzles.CONTENT_ITEM_TYPE;
	   		case LOCALE_URI_INDICATOR:
	   			return TagsDb.Locales.CONTENT_DIR_TYPE;
	   		case PROFILE_URI_INDICATOR:
	   			return TagsDb.Profiles.CONTENT_DIR_TYPE;
	   		case PROFILE_ITEM_URI_INDICATOR:
	   			return TagsDb.Profiles.CONTENT_ITEM_TYPE;
	   		case SESSION_URI_INDICATOR:
	   			return TagsDb.Sessions.CONTENT_DIR_TYPE;
	   		case SESSION_ITEM_URI_INDICATOR:
	   			return TagsDb.Sessions.CONTENT_ITEM_TYPE;
	   		case POSITION_URI_INDICATOR:
	   			return TagsDb.Positions.CONTENT_DIR_TYPE;
	   		case POSITION_ITEM_URI_INDICATOR:
	   			return TagsDb.Positions.CONTENT_ITEM_TYPE;
	   		case TAG_URI_INDICATOR:
	   			return TagsDb.TagPositions.CONTENT_DIR_TYPE;
	   		case TAG_ITEM_URI_INDICATOR:
	   			return TagsDb.TagPositions.CONTENT_ITEM_TYPE;
	   		case STAT_URI_INDICATOR:
	   			return TagsDb.Stats.CONTENT_DIR_TYPE;
	   		case STAT_ITEM_URI_INDICATOR:
	   			return TagsDb.Stats.CONTENT_ITEM_TYPE;
/*   		case REDO_URI_INDICATOR:
	   			return TagsDb.SolutionSteps.CONTENT_DIR_TYPE;
	   		case REDO_ITEM_URI_INDICATOR:
	   			return TagsDb.SolutionSteps.CONTENT_ITEM_TYPE;
	   		case REDO_TAG_URI_INDICATOR:
	   			return TagsDb.StepTag.CONTENT_DIR_TYPE;*/
	   		case ENDPOS_ITEM_URI_INDICATOR:
	   			return TagsDb.EndPositions.CONTENT_ITEM_TYPE;
	   		case GROUP_URI_INDICATOR:
	   			return TagsDb.Groups.CONTENT_DIR_TYPE;
	   		case GROUP_ITEM_URI_INDICATOR:
	   			return TagsDb.Groups.CONTENT_ITEM_TYPE;
	   }
	   return null;
   }

   @Override
   public Uri insert(Uri uri, ContentValues values) {
       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
       switch (uriMatcher.match(uri)) {
/*     		case REDO_TAG_URI_INDICATOR:
       			long id = db.insert(TagsDb.StepTag.TABLE_NAME, 
       					TagsDb.StepTag._ID, 
       					values);
       			if (id > 0) {
       				return ContentUris.withAppendedId(TagsDb.StepTag.CONTENT_URI, id);
       			}
       			break;
       		case REDO_URI_INDICATOR:
       			long redoId = db.insert(TagsDb.SolutionSteps.TABLE_NAME, 
       					TagsDb.SolutionSteps._ID, 
       					values);
       			if (redoId > 0) {
       				return ContentUris.withAppendedId(TagsDb.SolutionSteps.CONTENT_URI, redoId);
       			}
       			break;*/
        	case STAT_URI_INDICATOR:
       			long statId = db.insert(TagsDb.Stats.TABLE_NAME, 
       					TagsDb.Stats._ID, 
       					values);
       			if (statId > 0) {
       				return ContentUris.withAppendedId(TagsDb.Stats.CONTENT_URI, statId);
       			}
        		break;
       		case POSITION_URI_INDICATOR:
       			values.put(TagsDb.Positions.COLUMN_NAME_IS_PROTECTED, 0);
       			long positionId = db.insert(TagsDb.Positions.TABLE_NAME, 
       					TagsDb.Positions._ID, 
       					values);
       			if (positionId > 0) {
       				return ContentUris.withAppendedId(TagsDb.Positions.CONTENT_URI, positionId);
       			}
       			break;
       		case SESSION_URI_INDICATOR:
       			db.execSQL("update " + TagsDb.Sessions.TABLE_NAME + " set " +
       					TagsDb.Sessions.COLUMN_NAME_IS_CLOSED + " = 1," +
       					TagsDb.Sessions.COLUMN_NAME_IS_CURRENT + " = 0");
       			long sessionId = db.insert(TagsDb.Sessions.TABLE_NAME, 
       					TagsDb.Sessions._ID, 
       					values);
       			if (sessionId > 0) {
       				return ContentUris.withAppendedId(TagsDb.Sessions.CONTENT_URI, sessionId);
       			}
       			break;
       		case TAG_URI_INDICATOR:
       			long tagId = db.insert(TagsDb.TagPositions.TABLE_NAME, 
       					TagsDb.TagPositions._ID, 
       					values);
       			if (tagId > 0) {
       				return ContentUris.withAppendedId(TagsDb.TagPositions.CONTENT_URI, tagId);
       			}
       			break;
	   }    
       return null;
   }

   @Override
   public int update(Uri uri, ContentValues values, String where, String[] whereArgs) {
       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	   switch (uriMatcher.match(uri)) {
	   		case STAT_ITEM_URI_INDICATOR:
	   			return db.update(TagsDb.Stats.TABLE_NAME, 
	   					values, 
	   					where + " and " + TagsDb.Stats.COLUMN_NAME_STAT_TYPE_ID + " = " + uri.getPathSegments().get(1), 
	   					whereArgs);
	   		case PROFILE_URI_INDICATOR:
	   			return db.update(TagsDb.Profiles.TABLE_NAME, 
	   					values, 
	   					TagsDb.Profiles.COLUMN_NAME_IS_DEFAULT + " = 1", 
	   					null);
	   		case SESSION_ITEM_URI_INDICATOR:
	   			return db.update(TagsDb.Sessions.TABLE_NAME, 
	   					values, 
	   					TagsDb.Sessions._ID + " = ?", 
	   					new String[] {uri.getPathSegments().get(1)});
	   }	   
       return 0;
   }
   
   @Override
   public int delete(Uri uri, String selection, String[] selectionArgs) {
	   int r = 0;
       SQLiteDatabase db = mOpenHelper.getWritableDatabase();
	   switch (uriMatcher.match(uri)) {
/*	   		case REDO_URI_INDICATOR:
	   			db.delete(TagsDb.StepTag.TABLE_NAME, selection, selectionArgs);
	   			r = db.delete(TagsDb.SolutionSteps.TABLE_NAME, selection, selectionArgs);
	   			break;
	   		case SESSION_ITEM_URI_INDICATOR:
	   			r = db.delete(TagsDb.Sessions.TABLE_NAME, 
	   					selection + " and " + 
	   			        TagsDb.Sessions._ID + " = ?", 
	   			        new String[] {uri.getPathSegments().get(1)});
	   			if (r != 0) {
	   				db.delete(TagsDb.Stats.TABLE_NAME, 
	   					TagsDb.Stats.COLUMN_NAME_SESSION_ID + " = ?", 
	   					new String[] {uri.getPathSegments().get(1)});
	   				db.delete(TagsDb.SolutionSteps.TABLE_NAME, 
   						TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + " = ?", 
   						new String[] {uri.getPathSegments().get(1)});
	   			}
	   			break;*/
  			case POSITION_ITEM_URI_INDICATOR:
  				r = db.delete(TagsDb.Positions.TABLE_NAME, 
 						TagsDb.Positions._ID + " = ? and " +
						TagsDb.Positions.COLUMN_NAME_IS_PROTECTED + " = 0", 
  						new String[] {uri.getPathSegments().get(1)});
  				if (r > 0) {
  					db.delete(TagsDb.TagPositions.TABLE_NAME, 
  							TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + " = ?", 
  							new String[] {uri.getPathSegments().get(1)});
  				}
  				return r;
	   }
	   return r;
   }

   @Override
   public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
       SQLiteDatabase db = mOpenHelper.getReadableDatabase();
	   switch (uriMatcher.match(uri)) {
	   		case GROUP_ITEM_URI_INDICATOR:
	   			return db.rawQuery("select a." + TagsDb.Groups._ID + " as " + TagsDb.Groups._ID + ", " +
	   					           "b." + TagsDb.StringValues.COLUMN_NAME_VALUE + " as " + TagsDb.StringValues.COLUMN_NAME_VALUE + " " +
	   							   "from " + TagsDb.Groups.TABLE_NAME + " a, " + TagsDb.StringValues.TABLE_NAME + " b " +
	   							   "where b." + TagsDb.StringValues.COLUMN_NAME_STRING_ID + " = a." + TagsDb.Groups.COLUMN_NAME_HELP_ID + " " + selection, selectionArgs);
	   		case GROUP_URI_INDICATOR:
	   			return db.rawQuery("select a." + TagsDb.Groups._ID + " as " + TagsDb.Groups._ID + "," +
	   					           "b." + TagsDb.StringValues.COLUMN_NAME_VALUE + " as " + TagsDb.StringValues.COLUMN_NAME_VALUE + " " +
	   					           "from " + TagsDb.Groups.TABLE_NAME + " a, " + TagsDb.StringValues.TABLE_NAME + " b " +
	   					           "where b." + TagsDb.StringValues.COLUMN_NAME_STRING_ID + " = a." + TagsDb.Groups.COLUMN_NAME_STRING_ID + " and " + selection + " " +
   					           	   "order by a." + TagsDb.Groups._ID, selectionArgs);
/*	   		case REDO_ITEM_URI_INDICATOR:
	   			return db.rawQuery("select a." + TagsDb.SolutionSteps._ID + " as " + TagsDb.SolutionSteps._ID + ", " +
				           "a." + TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM + " as " + TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM + ", " +
				           "b." + TagsDb.StepTag.COLUMN_NAME_TAG_ID + " as " + TagsDb.StepTag.COLUMN_NAME_TAG_ID + ", " +
				           "a." + TagsDb.SolutionSteps.COLUMN_NAME_DX + " as " + TagsDb.SolutionSteps.COLUMN_NAME_DX + ", " +
				           "a." + TagsDb.SolutionSteps.COLUMN_NAME_DY + " as " + TagsDb.SolutionSteps.COLUMN_NAME_DY + " " +
				           "from " + TagsDb.SolutionSteps.TABLE_NAME + " a, " +
				           TagsDb.StepTag.TABLE_NAME + " b " +
				           "where b." + TagsDb.StepTag.COLUMN_NAME_REDO_ID + " = a." + TagsDb.SolutionSteps._ID + " " +
				           "and a." + TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + " = " + uri.getPathSegments().get(1), selectionArgs);*/
	   		case ENDPOS_ITEM_URI_INDICATOR:
	   			return db.rawQuery("select " + TagsDb.EndPositions.COLUMN_NAME_POSITION_ID + " " +
	   					           "from " + TagsDb.EndPositions.TABLE_NAME + " " +
	   					           "where " + TagsDb.EndPositions.COLUMN_NAME_PUZZLE_ID + " = " + uri.getPathSegments().get(1), selectionArgs);
	   		case STAT_ITEM_URI_INDICATOR:
	   			return db.rawQuery("select " + TagsDb.Stats.COLUMN_NAME_VALUE + " " +
	   					           "from " + TagsDb.Stats.TABLE_NAME + " " +
	   					           "where " + selection +
	   					           "and " + TagsDb.Stats.COLUMN_NAME_STAT_TYPE_ID + " = " + uri.getPathSegments().get(1), selectionArgs);
	   		case SESSION_URI_INDICATOR:
	   			return db.rawQuery("select a." + TagsDb.Sessions._ID + " as " + TagsDb.Sessions._ID + ", " +
				           "a." + TagsDb.Sessions.COLUMN_NAME_PROFILE_ID + " as " + TagsDb.Sessions.COLUMN_NAME_PROFILE_ID + ", " +
						   "a." + TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID + " as " + TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID + ", " +
						   "a." + TagsDb.Sessions.COLUMN_NAME_POSITION_ID + " as " + TagsDb.Sessions.COLUMN_NAME_POSITION_ID + ", " +
						   "b." + TagsDb.Profiles.COLUMN_NAME_LOCALE_ID + " as " + TagsDb.Profiles.COLUMN_NAME_LOCALE_ID + ", " +
						   "c." + TagsDb.Stats.COLUMN_NAME_VALUE + " as " + TagsDb.Stats.COLUMN_NAME_VALUE + ", " +
						   "e." + TagsDb.StringValues.COLUMN_NAME_VALUE + " as " + TagsDb.Puzzles.ALIAS_NAME_PUZZLE_NAME + ", " +
						   "a." + TagsDb.Sessions.COLUMN_NAME_END_DATE + " as " + TagsDb.Sessions.COLUMN_NAME_END_DATE + " " +
				           "from " + TagsDb.Sessions.TABLE_NAME + " a, " +
				           TagsDb.Profiles.TABLE_NAME + " b," +
				           TagsDb.Stats.TABLE_NAME + " c," +
				           TagsDb.Puzzles.TABLE_NAME + " d, " +
				           TagsDb.StringValues.TABLE_NAME + " e " +
				           "where b." + TagsDb.Sessions._ID + " = a." + TagsDb.Sessions.COLUMN_NAME_PROFILE_ID + " " +
				           "and c." + TagsDb.Stats.COLUMN_NAME_SESSION_ID + " = a." + TagsDb.Sessions._ID + " " +
			           	   "and c." + TagsDb.Stats.COLUMN_NAME_STAT_TYPE_ID + " = 1 " +
				           "and d." + TagsDb.Puzzles._ID + " = a." + TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID + " " +
			           	   "and e." + TagsDb.StringValues.COLUMN_NAME_STRING_ID + " = d." + TagsDb.Puzzles.COLUMN_NAME_STRING_ID + " " +
			           	   "and e." + TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " = ? " +
			           	   "and " + selection + " order by " +
			           	   TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID + " desc," +
			           	   TagsDb.Stats.COLUMN_NAME_VALUE, selectionArgs);
	   		case PROFILE_URI_INDICATOR:
	   			return db.rawQuery("select a." + TagsDb.Profiles._ID + " as " + TagsDb.Profiles._ID + ", " +
	   					           "a." + TagsDb.Profiles.COLUMN_NAME_LOCALE_ID + " as " + TagsDb.Profiles.COLUMN_NAME_LOCALE_ID + ", " +
	   					           "a." + TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID + " as " + TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID + ", " +
	   					           "b." + TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID + " as " + TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID + " " +
	   							   "from " + TagsDb.Profiles.TABLE_NAME + " a, " +
	   					           TagsDb.Puzzles.TABLE_NAME + " b " + 
	   							   "where a." + TagsDb.Profiles.COLUMN_NAME_IS_DEFAULT + " = 1 " +
  							   	   "and b." + TagsDb.Puzzles._ID + " = a." + TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID, selectionArgs);
	   		case PROFILE_ITEM_URI_INDICATOR:
	   			return db.rawQuery("select " + TagsDb.Puzzles._ID + ", " +
	   					           TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID + " " +
	   					           "from " + TagsDb.Puzzles.TABLE_NAME + " " +
	   					           "where " + TagsDb.Puzzles._ID + " = " + uri.getPathSegments().get(1), selectionArgs);
	   		case LOCALE_URI_INDICATOR:
	   			return db.rawQuery("select a." + TagsDb.Locales._ID + " as " + TagsDb.Locales._ID + ", " +
				           		   "b." + TagsDb.StringValues.COLUMN_NAME_VALUE + " as " + TagsDb.StringValues.COLUMN_NAME_VALUE + " " +
				                   "from " + TagsDb.Locales.TABLE_NAME + " a, " +
				                   TagsDb.StringValues.TABLE_NAME + " b " +
				                   "where b." + TagsDb.StringValues.COLUMN_NAME_STRING_ID + " = " +
				                   "a." + TagsDb.Locales.COLUMN_NAME_STRING_ID + " and " + selection, selectionArgs);
	   		case STRING_URI_INDICATOR:
	   			return db.rawQuery("select " + TagsDb.StringValues._ID + 
	   					           ", " + TagsDb.StringValues.COLUMN_NAME_VALUE +
	   					           " from " + TagsDb.StringValues.TABLE_NAME +
	   					           " where " + TagsDb.StringValues.COLUMN_NAME_STRING_ID +
	   					           " = " + uri.getPathSegments().get(1) +
	   					           " and " + selection, selectionArgs);
	   		case PUZZLE_LIST_URI_INDICATOR:
	   			return db.rawQuery("select " +
	   					           "a." + TagsDb.Puzzles._ID + " as " + TagsDb.Puzzles._ID + ", " +
	   					           "a." + TagsDb.Puzzles.COLUMN_NAME_IMG_ID + " as " + TagsDb.Puzzles.COLUMN_NAME_IMG_ID + ", " +
	   					           "a." + TagsDb.Puzzles.COLUMN_NAME_IX + " as " + TagsDb.Puzzles.COLUMN_NAME_IX + ", " +
  					           	   "b." + TagsDb.StringValues.COLUMN_NAME_VALUE + " as " + TagsDb.StringValues.COLUMN_NAME_VALUE + " from " + 
	   					            TagsDb.Puzzles.TABLE_NAME + " a, " +
	   					            TagsDb.StringValues.TABLE_NAME + " b where " +
	   					           "b." + TagsDb.StringValues.COLUMN_NAME_STRING_ID + " = " +
	   					           "a." + TagsDb.Puzzles.COLUMN_NAME_STRING_ID + " and " +
	   					           "b." + selection + " order by a." + TagsDb.Puzzles.COLUMN_NAME_IX, selectionArgs);
	   			
	   		case PARAMS_URI_INDICATOR:
	   			return db.rawQuery("select " +
	   					TagsDb.Params._ID + ", " +
	   					TagsDb.Params.COLUMN_NAME_PARAM_TYPE_ID + ", " +
	   					TagsDb.Params.COLUMN_NAME_VALUE + " from " +
	   					TagsDb.Params.TABLE_NAME + " where " +
	   					TagsDb.Params.COLUMN_NAME_PUZZLE_ID + " = " +
	   					uri.getPathSegments().get(1), selectionArgs);
	   		case PUZZLE_URI_INDICATOR:
	   			return db.rawQuery("select " +
	   					"b." + TagsDb.Tags.COLUMN_NAME_IX + " as " + TagsDb.Tags.COLUMN_NAME_IX + ", " + 
	   					"d." + TagsDb.Items._ID + " as " + TagsDb.Items._ID + ", " +
	   					"b." + TagsDb.Tags._ID + "  as " + TagsDb.Items.COLUMN_NAME_TAG_ID + ", " +
	   					"c." + TagsDb.TagPositions.COLUMN_NAME_X + " + d." + TagsDb.Items.COLUMN_NAME_X + " as " + TagsDb.Items.COLUMN_NAME_X + ", " +
	   					"c." + TagsDb.TagPositions.COLUMN_NAME_Y + " + d." + TagsDb.Items.COLUMN_NAME_Y + " as " + TagsDb.Items.COLUMN_NAME_Y + ", " +
	   					"d." + TagsDb.Items.COLUMN_NAME_IMG_ID + " as " + TagsDb.Items.COLUMN_NAME_IMG_ID + ", " +
	   					"d." + TagsDb.Items.COLUMN_NAME_X + " as " + TagsDb.Items.COLUMN_NAME_ITEM_X + ", " +
	   					"d." + TagsDb.Items.COLUMN_NAME_Y + " as " + TagsDb.Items.COLUMN_NAME_ITEM_Y + " " +
	   					"from   " + TagsDb.Puzzles.TABLE_NAME + " a, " + TagsDb.Tags.TABLE_NAME + " b, " + TagsDb.TagPositions.TABLE_NAME + " c, " + TagsDb.Items.TABLE_NAME + " d " +
	   					"where  b." + TagsDb.Tags.COLUMN_NAME_PUZZLE_ID + " = a." + TagsDb.Puzzles._ID + " and c." + TagsDb.TagPositions.COLUMN_NAME_TAG_ID + " = b." + TagsDb.Tags._ID + " " +
	   					"and    c." + TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + " = a." + TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID + " and d." + TagsDb.Items.COLUMN_NAME_TAG_ID + " = b." + TagsDb.Tags._ID + " " +
	   					"and    a." + TagsDb.Puzzles._ID + " = " +
	   					uri.getPathSegments().get(1), selectionArgs);
	   		case POSITION_ITEM_URI_INDICATOR:
	   			return db.rawQuery("select " +
	   					"b." + TagsDb.Tags.COLUMN_NAME_IX + " as " + TagsDb.Tags.COLUMN_NAME_IX + ", " + 
	   					"d." + TagsDb.Items._ID + " as " + TagsDb.Items._ID + ", " +
	   					"b." + TagsDb.Tags._ID + "  as " + TagsDb.Items.COLUMN_NAME_TAG_ID + ", " +
	   					"c." + TagsDb.TagPositions.COLUMN_NAME_X + " + d." + TagsDb.Items.COLUMN_NAME_X + " as " + TagsDb.Items.COLUMN_NAME_X + ", " +
	   					"c." + TagsDb.TagPositions.COLUMN_NAME_Y + " + d." + TagsDb.Items.COLUMN_NAME_Y + " as " + TagsDb.Items.COLUMN_NAME_Y + ", " +
	   					"d." + TagsDb.Items.COLUMN_NAME_IMG_ID + " as " + TagsDb.Items.COLUMN_NAME_IMG_ID + ", " +
	   					"d." + TagsDb.Items.COLUMN_NAME_X + " as " + TagsDb.Items.COLUMN_NAME_ITEM_X + ", " +
	   					"d." + TagsDb.Items.COLUMN_NAME_Y + " as " + TagsDb.Items.COLUMN_NAME_ITEM_Y + " " +
	   					"from   " + TagsDb.Tags.TABLE_NAME + " b, " + TagsDb.TagPositions.TABLE_NAME + " c, " + TagsDb.Items.TABLE_NAME + " d " +
	   					"where  c." + TagsDb.TagPositions.COLUMN_NAME_TAG_ID + " = b." + TagsDb.Tags._ID + " " +
	   					"and    d." + TagsDb.Items.COLUMN_NAME_TAG_ID + " = b." + TagsDb.Tags._ID + " " +
//	   					"and " + selection + " " +
	   					"and    c." + TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + " = " +
	   					uri.getPathSegments().get(1), null /*selectionArgs*/);
	   }
	   return null;
   }

   static class DatabaseHelper extends SQLiteOpenHelper {

	   DatabaseHelper(Context context) {
          super(context, DATABASE_NAME, null, DATABASE_VERSION);
       }
	   
	   public void addString(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Strings.TABLE_NAME + "(" +
				   TagsDb.Strings._ID + ") values (" + s + ")");
	   }
	   
	   public void addValue(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.StringValues.TABLE_NAME + "(" +
				   TagsDb.StringValues._ID + "," +
				   TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + "," +
				   TagsDb.StringValues.COLUMN_NAME_STRING_ID + "," +
				   TagsDb.StringValues.COLUMN_NAME_VALUE + ") values " +
				   "(" + s + ")");
	   }
	   
	   public void addLocale(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Locales.TABLE_NAME + "(" +
				   TagsDb.Locales._ID + "," +
				   TagsDb.Locales.COLUMN_NAME_NAME + "," +
				   TagsDb.Locales.COLUMN_NAME_STRING_ID + "," +
				   TagsDb.Locales.COLUMN_NAME_DESCRIPTION + ") values " +
				   "(" + s + ")");
	   }
	   
	   public void addPosition(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Positions.TABLE_NAME + "(" +
				   TagsDb.Positions._ID + "," +
				   TagsDb.Positions.COLUMN_NAME_IS_PROTECTED + ") values " +
  		   		   "(" + s + ")");
	   }
	   
	   public void addPuzzle(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Puzzles.TABLE_NAME + "(" +
				   TagsDb.Puzzles._ID + "," +
				   TagsDb.Puzzles.COLUMN_NAME_STRING_ID + "," +
				   TagsDb.Puzzles.COLUMN_NAME_GROUP_ID + "," +
				   TagsDb.Puzzles.COLUMN_NAME_IX + "," +
				   TagsDb.Puzzles.COLUMN_NAME_IMG_ID + "," +
				   TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID + ") values " +
  		   		   "(" + s + ")");
	   }
	       
	   public void addParam(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Params.TABLE_NAME + "(" +
				   TagsDb.Params._ID + "," +
				   TagsDb.Params.COLUMN_NAME_PARAM_TYPE_ID + "," +
				   TagsDb.Params.COLUMN_NAME_PUZZLE_ID + "," +
				   TagsDb.Params.COLUMN_NAME_VALUE + ") values " +
  		   		   "(" + s + ")");
	   }

	   public void addEndPos(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.EndPositions.TABLE_NAME + "(" +
				   TagsDb.EndPositions._ID + "," +
				   TagsDb.EndPositions.COLUMN_NAME_PUZZLE_ID + "," +
				   TagsDb.EndPositions.COLUMN_NAME_POSITION_ID + ") values " +
  		   		   "(" + s + ")");
	   }
	   
	   public void addTag(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Tags.TABLE_NAME + "(" +
				   TagsDb.Tags._ID + "," +
				   TagsDb.Tags.COLUMN_NAME_PUZZLE_ID + "," +
				   TagsDb.Tags.COLUMN_NAME_IX + ") values " +
  		   		   "(" + s + ")");
	   }

	   public void addItem(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Items.TABLE_NAME + "(" +
				   TagsDb.Items._ID + "," +
				   TagsDb.Items.COLUMN_NAME_TAG_ID + "," +
				   TagsDb.Items.COLUMN_NAME_IMG_ID + "," +
				   TagsDb.Items.COLUMN_NAME_X + "," +
				   TagsDb.Items.COLUMN_NAME_Y + ") values " +
  		   		   "(" + s + ")");
	   }
	   
	   public void addTagPos(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.TagPositions.TABLE_NAME + "(" +
				   TagsDb.TagPositions._ID + "," +
				   TagsDb.TagPositions.COLUMN_NAME_TAG_ID + "," +
				   TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + "," +
				   TagsDb.TagPositions.COLUMN_NAME_X + "," +
				   TagsDb.TagPositions.COLUMN_NAME_Y + ") values " +
  		   		   "(" + s + ")");
	   }

	   public void addGroup(SQLiteDatabase db, String s) {
		   db.execSQL("insert into " + TagsDb.Groups.TABLE_NAME + "(" +
				   TagsDb.Groups._ID + "," +
				   TagsDb.Groups.COLUMN_NAME_STRING_ID + "," +
				   TagsDb.Groups.COLUMN_NAME_HELP_ID + ") values " +
  		   		   "(" + s + ")");
	   }
	   
	   @Override
	   public void onCreate(SQLiteDatabase db) {

		   db.execSQL("CREATE TABLE " + TagsDb.Strings.TABLE_NAME + " (" +
				   TagsDb.Strings._ID + " INTEGER PRIMARY KEY);");
		   addString(db, "1");  addString(db, "2");
		   addString(db, "3");  addString(db, "4");
		   addString(db, "6");  addString(db, "7");  
		   addString(db, "8");  addString(db, "9");  
		   addString(db, "10"); addString(db, "11"); 
		   addString(db, "13"); addString(db, "21");
		   addString(db, "22"); addString(db, "23");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Locales.TABLE_NAME + " (" +
				   TagsDb.Locales._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Locales.COLUMN_NAME_NAME + " TEXT," +
				   TagsDb.Locales.COLUMN_NAME_STRING_ID + " INTEGER," +
				   TagsDb.Locales.COLUMN_NAME_IS_DEFAULT + " INTEGER," +
				   TagsDb.Locales.COLUMN_NAME_DESCRIPTION + " TEXT);");
		   addLocale(db, TagsDb.Locales.LOCALE_EN + ", 'en_US', 6, 1");
		   addLocale(db, TagsDb.Locales.LOCALE_RU + ", 'ru_RU', 7, 0");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.StringValues.TABLE_NAME + " (" +
				   TagsDb.StringValues._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.StringValues.COLUMN_NAME_LOCALE_ID + " INTEGER," +
				   TagsDb.StringValues.COLUMN_NAME_STRING_ID + " INTEGER," +
				   TagsDb.StringValues.COLUMN_NAME_VALUE + " TEXT);");
		   addValue(db, "1,  1, 1, 'Donkey'");
		   addValue(db, "6,  2, 1, 'Рыжий осел'");
		   addValue(db, "2,  1, 2, 'Steps'");
		   addValue(db, "7,  2, 2, 'Ходов'");
		   addValue(db, "3,  1, 3, 'Size X'");
		   addValue(db, "8,  2, 3, 'Размер X'");
		   addValue(db, "4,  1, 4, 'Size Y'");
		   addValue(db, "9,  2, 4, 'Размер Y'");
		   addValue(db, "11, 1, 6, 'English'");
		   addValue(db, "12, 2, 6, 'Английский'");
		   addValue(db, "13, 1, 7, 'Russian'");
		   addValue(db, "14, 2, 7, 'Русский'");
		   addValue(db, "15, 1, 8, 'New'");
		   addValue(db, "16, 2, 8, 'Заново'");
		   addValue(db, "17, 1, 9, 'Score'");
		   addValue(db, "18, 2, 9, 'Рекорды'");
		   addValue(db, "19, 1, 10, 'Language'");
		   addValue(db, "20, 2, 10, 'Язык'");
		   addValue(db, "21, 1, 11, 'Game editor'");
		   addValue(db, "22, 2, 11, 'Редактор'");
		   addValue(db, "25, 1, 13, 'Step'");
		   addValue(db, "26, 2, 13, 'Ход'");
		   addValue(db, "32, 1, 21, 'Puzzle'");
		   addValue(db, "33, 2, 21, 'Игра'");
		   addValue(db, "34, 1, 22, 'Date'");
		   addValue(db, "35, 2, 22, 'Дата'");
		   addValue(db, "36, 1, 23, 'Steps:'");
		   addValue(db, "37, 2, 23, 'Ходов:'");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Profiles.TABLE_NAME + " (" +
				   TagsDb.Profiles._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Profiles.COLUMN_NAME_LOGIN + " TEXT," +
				   TagsDb.Profiles.COLUMN_NAME_LOCALE_ID + " INTEGER," +
				   TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID + " INTEGER," +
				   TagsDb.Profiles.COLUMN_NAME_IS_DEFAULT + " INTEGER);");
		   db.execSQL("insert into " + TagsDb.Profiles.TABLE_NAME + "(" +
				   TagsDb.Profiles._ID + "," +
				   TagsDb.Profiles.COLUMN_NAME_LOGIN + "," + 
				   TagsDb.Profiles.COLUMN_NAME_LOCALE_ID + "," +
				   TagsDb.Profiles.COLUMN_NAME_PUZZLE_ID + "," +
				   TagsDb.Profiles.COLUMN_NAME_IS_DEFAULT + ") values " +
				   "(1, 'default', 0, 16, 1)");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Sessions.TABLE_NAME + " (" +
				   TagsDb.Sessions._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Sessions.COLUMN_NAME_PROFILE_ID + " INTEGER," +
				   TagsDb.Sessions.COLUMN_NAME_PUZZLE_ID + " INTEGER," +
				   TagsDb.Sessions.COLUMN_NAME_POSITION_ID + " INTEGER," +
				   TagsDb.Sessions.COLUMN_NAME_IS_CURRENT + " INTEGER," +
				   TagsDb.Sessions.COLUMN_NAME_START_DATE + " TEXT," +
				   TagsDb.Sessions.COLUMN_NAME_END_DATE + " TEXT," +
				   TagsDb.Sessions.COLUMN_NAME_IS_CLOSED + " INTEGER);");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.StatTypes.TABLE_NAME + " (" +
				   TagsDb.StatTypes._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.StatTypes.COLUMN_NAME_STRING_ID + " INTEGER);");
		   db.execSQL("insert into " + TagsDb.StatTypes.TABLE_NAME + "(" +
				   TagsDb.StatTypes._ID + "," +
				   TagsDb.StatTypes.COLUMN_NAME_STRING_ID + ") values " +
				   "(" + Integer.toString(TagsDb.StatTypes.Steps) + ", 2)");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Positions.TABLE_NAME + " (" +
				   TagsDb.Positions._ID + " INTEGER PRIMARY KEY," +
		   		   TagsDb.Positions.COLUMN_NAME_IS_PROTECTED + " INTEGER);");
		   addPosition(db, "1, 1");
		   addPosition(db, "2, 1");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Puzzles.TABLE_NAME + " (" +
				   TagsDb.Puzzles._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Puzzles.COLUMN_NAME_STRING_ID + " INTEGER," +
				   TagsDb.Puzzles.COLUMN_NAME_GROUP_ID + " INTEGER," +
				   TagsDb.Puzzles.COLUMN_NAME_IMG_ID + " INTEGER," +
				   TagsDb.Puzzles.COLUMN_NAME_IX + " INTEGER," +
				   TagsDb.Puzzles.COLUMN_NAME_START_POSITION_ID + " INTEGER);");
		   addPuzzle(db, "1, 1, 30, 1, " + GridActivity.PUZZLE_1_IMG + ", 1");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.ParamTypes.TABLE_NAME + " (" +
				   TagsDb.ParamTypes._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.ParamTypes.COLUMN_NAME_STRING_ID + " INTEGER);");
		   db.execSQL("insert into " + TagsDb.ParamTypes.TABLE_NAME + "(" +
				   TagsDb.ParamTypes._ID + "," +
				   TagsDb.ParamTypes.COLUMN_NAME_STRING_ID + ") values " +
				   "(" + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 3)");
		   db.execSQL("insert into " + TagsDb.ParamTypes.TABLE_NAME + "(" +
				   TagsDb.ParamTypes._ID + "," +
				   TagsDb.ParamTypes.COLUMN_NAME_STRING_ID + ") values " +
				   "(" + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 4)");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Params.TABLE_NAME + " (" +
				   TagsDb.Params._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Params.COLUMN_NAME_PARAM_TYPE_ID + " INTEGER," +
				   TagsDb.Params.COLUMN_NAME_PUZZLE_ID + " INTEGER," +
				   TagsDb.Params.COLUMN_NAME_VALUE + " TEXT);");
		   addParam(db, "1, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 1, 4");
		   addParam(db, "2, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 1, 5");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Stats.TABLE_NAME + " (" +
				   TagsDb.Stats._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Stats.COLUMN_NAME_STAT_TYPE_ID + " INTEGER," + 
				   TagsDb.Stats.COLUMN_NAME_SESSION_ID + " INTEGER," + 
				   TagsDb.Stats.COLUMN_NAME_VALUE + " INTEGER);");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.EndPositions.TABLE_NAME + " (" +
				   TagsDb.EndPositions._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.EndPositions.COLUMN_NAME_PUZZLE_ID + " INTEGER," +
				   TagsDb.EndPositions.COLUMN_NAME_POSITION_ID + " INTEGER);");
		   addEndPos(db, "1, 1, 2");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Tags.TABLE_NAME + " (" +
				   TagsDb.Tags._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Tags.COLUMN_NAME_PUZZLE_ID + " INTEGER," +
				   TagsDb.Tags.COLUMN_NAME_IX + " INTEGER);");
		   db.execSQL("create index " + TagsDb.Tags.TABLE_NAME + "_" + TagsDb.Tags.COLUMN_NAME_PUZZLE_ID + " on " + 
				   TagsDb.Tags.TABLE_NAME + "(" + TagsDb.Tags.COLUMN_NAME_PUZZLE_ID + ")");
		   addTag(db, "1, 1, 1");
		   addTag(db, "2, 1, 2");
		   addTag(db, "3, 1, 3");
		   addTag(db, "4, 1, 4");
		   addTag(db, "5, 1, 5");
		   addTag(db, "6, 1, 6");
		   addTag(db, "7, 1, 7");
		   addTag(db, "8, 1, 8");
		   addTag(db, "9, 1, 9");
		   addTag(db, "10, 1, 10");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Items.TABLE_NAME + " (" +
				   TagsDb.Items._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Items.COLUMN_NAME_TAG_ID + " INTEGER," +
				   TagsDb.Items.COLUMN_NAME_IMG_ID + " INTEGER," +
				   TagsDb.Items.COLUMN_NAME_X + " INTEGER," +
				   TagsDb.Items.COLUMN_NAME_Y + " INTEGER);");
		   db.execSQL("create index " + TagsDb.Items.TABLE_NAME + "_" + TagsDb.Items.COLUMN_NAME_TAG_ID + " on " + 
				   TagsDb.Items.TABLE_NAME + "(" + TagsDb.Items.COLUMN_NAME_TAG_ID + ")");
		   addItem(db, "1,  1,  0, 0, 0");
		   addItem(db, "2,  1,  0, 0, 1");
		   addItem(db, "3,  2,  0, 0, 0");
		   addItem(db, "4,  2,  0, 1, 0");
		   addItem(db, "5,  2,  0, 0, 1");
		   addItem(db, "6,  2,  0, 1, 1");
		   addItem(db, "7,  3,  0, 0, 0");
		   addItem(db, "8,  3,  0, 0, 1");
		   addItem(db, "9,  4,  0, 0, 0");
		   addItem(db, "10, 4,  0, 0, 1");
		   addItem(db, "11, 5,  0, 0, 0");
		   addItem(db, "13, 5,  0, 1, 0");
		   addItem(db, "14, 6,  0, 0, 0");
		   addItem(db, "15, 6,  0, 0, 1");
		   addItem(db, "16, 7,  0, 0, 0");
		   addItem(db, "17, 8,  0, 0, 0");
		   addItem(db, "18, 9,  0, 0, 0");
		   addItem(db, "19, 10, 0, 0, 0");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.TagPositions.TABLE_NAME + " (" +
				   TagsDb.TagPositions._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.TagPositions.COLUMN_NAME_TAG_ID + " INTEGER," +
				   TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + " INTEGER," +
				   TagsDb.TagPositions.COLUMN_NAME_X + " INTEGER," +
				   TagsDb.TagPositions.COLUMN_NAME_Y + " INTEGER);");
		   db.execSQL("create index " + TagsDb.TagPositions.TABLE_NAME + "_" + TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + " on " + 
				   TagsDb.TagPositions.TABLE_NAME + "(" + TagsDb.TagPositions.COLUMN_NAME_POSITION_ID + ")");
		   db.execSQL("create index " + TagsDb.TagPositions.TABLE_NAME + "_" + TagsDb.TagPositions.COLUMN_NAME_TAG_ID + " on " + 
				   TagsDb.TagPositions.TABLE_NAME + "(" + TagsDb.TagPositions.COLUMN_NAME_TAG_ID + ")");
		   addTagPos(db, "1,  1,  1, 1, 1");
		   addTagPos(db, "2,  2,  1, 2, 1");
		   addTagPos(db, "3,  3,  1, 4, 1");
		   addTagPos(db, "4,  4,  1, 1, 3");
		   addTagPos(db, "5,  5,  1, 2, 3");
		   addTagPos(db, "6,  6,  1, 4, 3");
		   addTagPos(db, "7,  7,  1, 2, 4");
		   addTagPos(db, "8,  8,  1, 3, 4");
		   addTagPos(db, "9,  9,  1, 1, 5");
		   addTagPos(db, "10, 10, 1, 4, 5");
		   addTagPos(db, "11, 2,  2, 2, 4");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.SolutionSteps.TABLE_NAME + " (" +
				   TagsDb.SolutionSteps._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + " INTEGER," +
				   TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM + " INTEGER," +
				   TagsDb.SolutionSteps.COLUMN_NAME_DX + " INTEGER," +
				   TagsDb.SolutionSteps.COLUMN_NAME_DY + " INTEGER);");
		   db.execSQL("create index " + TagsDb.SolutionSteps.TABLE_NAME + "_" + TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + " on " + 
				   TagsDb.SolutionSteps.TABLE_NAME + "(" + TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + ")");
		   
		   db.execSQL("CREATE TABLE " + TagsDb.Groups.TABLE_NAME + " (" +
				   TagsDb.Groups._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.Groups.COLUMN_NAME_HELP_ID + " INTEGER," +
				   TagsDb.Groups.COLUMN_NAME_STRING_ID + " INTEGER);");
		   
		   patch_2(db);
		   patch_3(db);
		   patch_5(db);
		   patch_7(db);
		   patch_8(db);
		   patch_9(db);
		   patch_10(db);
		   patch_11(db);
		   patch_12(db);
	   }

	   private void patch_2(SQLiteDatabase db) {

		   addString(db, "5");

		   addValue(db, "5,  1, 5, 'Mother'");
		   addValue(db, "10, 2, 5, 'Мамина головоломка'");
		   
		   addPosition(db, "3, 1");
		   addPosition(db, "4, 1");

		   addPuzzle(db, "2, 5, 20, 1, " + GridActivity.PUZZLE_2_IMG + ", 3");
		   
		   addParam(db, "3, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 2, 5");
		   addParam(db, "4, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 2, 5");
		   
		   addEndPos(db, "2, 2, 4");
		   
		   addTag(db, "11, 2, 1");
		   addTag(db, "12, 2, 2");
		   addTag(db, "13, 2, 3");
		   addTag(db, "14, 2, 4");
		   addTag(db, "15, 2, 5");
		   addTag(db, "16, 2, 6");
		   addTag(db, "17, 2, 7");
		   addTag(db, "18, 2, 8");
		   addTag(db, "19, 2, 9");
		   
		   addItem(db, "20, 11, 0, 0, 0");
		   addItem(db, "21, 11, 0, 1, 0");
		   addItem(db, "22, 11, 0, 2, 0");
		   addItem(db, "23, 12, 0, 0, 0");
		   addItem(db, "24, 12, 0, 1, 0");
		   addItem(db, "25, 12, 0, 1, 1");
		   addItem(db, "26, 13, 0, 0, 0");
		   addItem(db, "27, 13, 0, 1, 0");
		   addItem(db, "28, 14, 0, 0, 0");
		   addItem(db, "29, 14, 0, 1, 0");
		   addItem(db, "30, 15, 0, 0, 0");
		   addItem(db, "31, 15, 0, 0, 1");
		   addItem(db, "32, 15, 0, 1, 1");
		   addItem(db, "33, 16, 0, 0, 0");
		   addItem(db, "34, 16, 0, 1, 0");
		   addItem(db, "35, 17, 0, 0, 0");
		   addItem(db, "36, 17, 0, 1, 0");
		   addItem(db, "37, 18, 0, 0, 0");
		   addItem(db, "38, 18, 0, 1, 0");
		   addItem(db, "39, 18, 0, 2, 0");
		   addItem(db, "40, 19, 0, 0, 0");
		   
		   addTagPos(db, "12, 11, 3, 1, 1");
		   addTagPos(db, "13, 12, 3, 4, 1");
		   addTagPos(db, "14, 13, 3, 1, 2");
		   addTagPos(db, "15, 14, 3, 3, 2");
		   addTagPos(db, "16, 15, 3, 1, 3");
		   addTagPos(db, "17, 16, 3, 2, 3");
		   addTagPos(db, "18, 17, 3, 4, 3");
		   addTagPos(db, "19, 18, 3, 3, 4");
		   addTagPos(db, "20, 19, 3, 3, 5");
		   addTagPos(db, "21, 12, 4, 4, 1");
		   addTagPos(db, "22, 15, 4, 4, 2");
	   }

	   private void patch_3(SQLiteDatabase db) {
		   
		   addString(db, "12");

		   addValue(db, "23, 1, 12, 'Father'");
		   addValue(db, "24, 2, 12, 'Папина головоломка'");
		   
		   addPosition(db, "5, 1");
		   addPosition(db, "6, 1");
		   
		   addPuzzle(db, "3, 12, 10, 2, " + GridActivity.PUZZLE_3_IMG + ", 5");
		   
		   addParam(db, "5, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 3, 4");
		   addParam(db, "6, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 3, 5");
		   
		   addEndPos(db, "3, 3, 6");
		   
		   addTag(db, "20, 3, 1");
		   addTag(db, "21, 3, 2");
		   addTag(db, "22, 3, 3");
		   addTag(db, "23, 3, 4");
		   addTag(db, "24, 3, 5");
		   addTag(db, "25, 3, 6");
		   addTag(db, "26, 3, 7");
		   addTag(db, "27, 3, 8");
		   addTag(db, "28, 3, 9");
		   
		   addItem(db, "41, 20, 0, 0, 0");
		   addItem(db, "42, 20, 0, 1, 0");
		   addItem(db, "43, 20, 0, 0, 1");
		   addItem(db, "44, 20, 0, 1, 1");
		   addItem(db, "45, 21, 0, 0, 0");
		   addItem(db, "46, 21, 0, 1, 0");
		   addItem(db, "47, 22, 0, 0, 0");
		   addItem(db, "48, 22, 0, 1, 0");
		   addItem(db, "49, 23, 0, 0, 0");
		   addItem(db, "50, 24, 0, 0, 0");
		   addItem(db, "51, 25, 0, 0, 0");
		   addItem(db, "52, 25, 0, 0, 1");
		   addItem(db, "53, 26, 0, 0, 0");
		   addItem(db, "54, 26, 0, 0, 1");
		   addItem(db, "55, 27, 0, 0, 0");
		   addItem(db, "56, 27, 0, 1, 0");
		   addItem(db, "57, 28, 0, 0, 0");
		   addItem(db, "58, 28, 0, 1, 0");
		   
		   addTagPos(db, "23, 20, 5, 1, 1");
		   addTagPos(db, "24, 21, 5, 3, 1");
		   addTagPos(db, "25, 22, 5, 3, 2");
		   addTagPos(db, "26, 23, 5, 1, 3");
		   addTagPos(db, "27, 24, 5, 2, 3");
		   addTagPos(db, "28, 25, 5, 1, 4");
		   addTagPos(db, "29, 26, 5, 2, 4");
		   addTagPos(db, "30, 27, 5, 3, 4");
		   addTagPos(db, "31, 28, 5, 3, 5");
		   addTagPos(db, "32, 20, 6, 1, 4");
	   }	   
	   
	   private void patch_5(SQLiteDatabase db) {
		   db.execSQL("DROP TABLE " + TagsDb.SolutionSteps.TABLE_NAME);
		   db.execSQL("CREATE TABLE " + TagsDb.SolutionSteps.TABLE_NAME + " (" +
				   TagsDb.SolutionSteps._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.SolutionSteps.COLUMN_NAME_SESSION_ID + " INTEGER," +
				   TagsDb.SolutionSteps.COLUMN_NAME_ORD_NUM + " INTEGER," +
				   TagsDb.SolutionSteps.COLUMN_NAME_DX + " INTEGER," +
				   TagsDb.SolutionSteps.COLUMN_NAME_DY + " INTEGER);");
		   db.execSQL("CREATE TABLE " + TagsDb.StepTag.TABLE_NAME + " (" +
				   TagsDb.StepTag._ID + " INTEGER PRIMARY KEY," +
				   TagsDb.StepTag.COLUMN_NAME_SESSION_ID + " INTEGER," +
				   TagsDb.StepTag.COLUMN_NAME_TAG_ID + " INTEGER," +
				   TagsDb.StepTag.COLUMN_NAME_REDO_ID + " INTEGER);");
	   }	   

	   private void patch_7(SQLiteDatabase db) {

		   addString(db, "25");
		   
		   addValue(db, "40, 1, 25, 'Puzzle 1'");
		   addValue(db, "41, 2, 25, 'Головоломка 1'");

		   addPosition(db, "11, 1");
		   addPosition(db, "12, 1");
		   
		   addPuzzle(db, "6, 25, 40, 1, " + GridActivity.PUZZLE_6_IMG + ", 11");
		   
		   addParam(db, "11, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 6, 6");
		   addParam(db, "12, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 6, 6");
		   
		   addEndPos(db, "6, 6, 12");
		   
		   addTag(db, "54, 6, 1");
		   addTag(db, "55, 6, 2");
		   addTag(db, "56, 6, 3");
		   addTag(db, "57, 6, 4");
		   addTag(db, "58, 6, 5");
		   addTag(db, "59, 6, 6");
		   addTag(db, "60, 6, 7");
		   addTag(db, "61, 6, 8");
		   addTag(db, "62, 6, 9");
		   addTag(db, "63, 6, 10");
		   addTag(db, "64, 6, 11");
		   
		   addItem(db, "110, 54, 0, 0, 0");
		   addItem(db, "111, 54, 0, 1, 0");
		   addItem(db, "112, 54, 0, 0, 1");
		   addItem(db, "113, 54, 0, 1, 1");
		   addItem(db, "114, 54, 0, 0, 2");
		   addItem(db, "115, 54, 0, 1, 2");
		   addItem(db, "116, 55, 0, 0, 0");
		   addItem(db, "117, 55, 0, 1, 0");
		   addItem(db, "118, 56, 0, 0, 0");
		   addItem(db, "119, 56, 0, 1, 0");
		   addItem(db, "120, 57, 0, 0, 0");
		   addItem(db, "121, 57, 0, 0, 1");
		   addItem(db, "122, 58, 0, 0, 0");
		   addItem(db, "123, 58, 0, 1, 0");
		   addItem(db, "124, 59, 0, 0, 0");
		   addItem(db, "125, 59, 0, 0, 1");
		   addItem(db, "126, 60, 0, 0, 0");
		   addItem(db, "127, 60, 0, 1, 0");
		   addItem(db, "128, 61, 0, 0, 0");
		   addItem(db, "129, 61, 0, 1, 0");
		   addItem(db, "130, 61, 0, 2, 0");
		   addItem(db, "131, 61, 0, 3, 0");
		   addItem(db, "132, 62, 0, 0, 0");
		   addItem(db, "133, 62, 0, 1, 0");
		   addItem(db, "134, 62, 0, 0, 1");
		   addItem(db, "135, 62, 0, 1, 1");
		   addItem(db, "136, 63, 0, 0, 0");
		   addItem(db, "137, 63, 0, 1, 0");
		   addItem(db, "138, 64, 0, 0, 0");
		   addItem(db, "139, 64, 0, 1, 0");
		   
		   addTagPos(db, "59, 54, 11, 1, 1");
		   addTagPos(db, "60, 55, 11, 3, 1");
		   addTagPos(db, "61, 56, 11, 5, 1");
		   addTagPos(db, "62, 57, 11, 3, 2");
		   addTagPos(db, "63, 58, 11, 4, 2");
		   addTagPos(db, "64, 59, 11, 6, 2");
		   addTagPos(db, "65, 60, 11, 4, 3");
		   addTagPos(db, "66, 61, 11, 1, 4");
		   addTagPos(db, "67, 62, 11, 5, 4");
		   addTagPos(db, "68, 63, 11, 1, 5");
		   addTagPos(db, "69, 64, 11, 3, 5");
		   addTagPos(db, "70, 54, 12, 5, 4");
		   
		   addString(db, "26");
		   
		   addValue(db, "42, 1, 26, 'Puzzle 2'");
		   addValue(db, "43, 2, 26, 'Головоломка 2'");

		   addPosition(db, "13, 1");
		   addPosition(db, "14, 1");
		   
		   addPuzzle(db, "7, 26, 40, 2, " + GridActivity.PUZZLE_7_IMG + ", 13");
		   
		   addParam(db, "13, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 7, 5");
		   addParam(db, "14, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 7, 5");
		   
		   addEndPos(db, "7, 7, 14");
		   
		   addTag(db, "65, 7, 1");
		   addTag(db, "66, 7, 2");
		   addTag(db, "67, 7, 3");
		   addTag(db, "68, 7, 4");
		   addTag(db, "69, 7, 5");
		   addTag(db, "70, 7, 6");
		   addTag(db, "71, 7, 7");
		   addTag(db, "72, 7, 8");
		   addTag(db, "73, 7, 9");
		   addTag(db, "74, 7, 10");
		   
		   addItem(db, "140, 65, 0, 0, 0");
		   addItem(db, "141, 65, 0, 1, 0");
		   addItem(db, "142, 65, 0, 0, 1");
		   addItem(db, "143, 65, 0, 1, 1");
		   addItem(db, "144, 65, 0, 0, 2");
		   addItem(db, "145, 65, 0, 1, 2");
		   addItem(db, "146, 66, 0, 0, 0");
		   addItem(db, "147, 66, 0, 1, 0");
		   addItem(db, "148, 67, 0, 0, 0");
		   addItem(db, "149, 67, 0, 1, 0");
		   addItem(db, "150, 68, 0, 0, 0");
		   addItem(db, "151, 68, 0, 1, 0");
		   addItem(db, "152, 69, 0, 0, 0");
		   addItem(db, "153, 70, 0, 0, 0");
		   addItem(db, "154, 70, 0, 0, 1");
		   addItem(db, "155, 71, 0, 0, 0");
		   addItem(db, "156, 71, 0, 0, 1");
		   addItem(db, "157, 72, 0, 0, 0");
		   addItem(db, "158, 73, 0, 0, 0");
		   addItem(db, "159, 73, 0, 0, 1");
		   addItem(db, "160, 74, 0, 0, 0");
		   addItem(db, "161, 74, 0, 0, 1");
		   
		   addTagPos(db, "71, 65, 13, 1, 1");
		   addTagPos(db, "72, 66, 13, 4, 1");
		   addTagPos(db, "73, 67, 13, 4, 2");
		   addTagPos(db, "74, 68, 13, 4, 3");
		   addTagPos(db, "75, 69, 13, 3, 4");
		   addTagPos(db, "76, 70, 13, 1, 4");
		   addTagPos(db, "77, 71, 13, 2, 4");
		   addTagPos(db, "78, 72, 13, 3, 5");
		   addTagPos(db, "79, 73, 13, 4, 4");
		   addTagPos(db, "80, 74, 13, 5, 4");
		   addTagPos(db, "81, 65, 14, 4, 3");
		   
		   addString(db, "28");
		   
		   addValue(db, "46, 1, 28, 'Puzzle 4'");
		   addValue(db, "47, 2, 28, 'Головоломка 4'");

		   addPosition(db, "17, 1");
		   addPosition(db, "18, 1");
		   
		   addPuzzle(db, "9, 28, 40, 4, " + GridActivity.PUZZLE_9_IMG + ", 17");
		   
		   addParam(db, "17, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 9, 6");
		   addParam(db, "18, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 9, 5");
		   
		   addEndPos(db, "9, 9, 18");
		   
		   addTag(db, "84, 9, 1");
		   addTag(db, "85, 9, 2");
		   addTag(db, "86, 9, 3");
		   addTag(db, "87, 9, 4");
		   addTag(db, "88, 9, 5");
		   addTag(db, "89, 9, 6");
		   addTag(db, "90, 9, 7");
		   addTag(db, "91, 9, 8");
		   addTag(db, "92, 9, 9");
		   addTag(db, "93, 9, 10");
		   addTag(db, "94, 9, 11");
		   
		   addItem(db, "182, 84, 0, 0, 0");
		   addItem(db, "183, 84, 0, 1, 0");
		   addItem(db, "184, 84, 0, 2, 0");
		   addItem(db, "185, 84, 0, 0, 1");
		   addItem(db, "186, 84, 0, 1, 1");
		   addItem(db, "187, 84, 0, 2, 1");
		   addItem(db, "188, 85, 0, 0, 0");
		   addItem(db, "189, 85, 0, 1, 0");
		   addItem(db, "190, 85, 0, 2, 0");
		   addItem(db, "191, 86, 0, 0, 0");
		   addItem(db, "192, 86, 0, 1, 0");
		   addItem(db, "193, 87, 0, 0, 0");
		   addItem(db, "194, 87, 0, 0, 1");
		   addItem(db, "195, 88, 0, 0, 0");
		   addItem(db, "196, 88, 0, 1, 0");
		   addItem(db, "197, 88, 0, 2, 0");
		   addItem(db, "198, 89, 0, 0, 0");
		   addItem(db, "199, 89, 0, 1, 0");
		   addItem(db, "200, 90, 0, 0, 0");
		   addItem(db, "201, 91, 0, 0, 0");
		   addItem(db, "202, 91, 0, 1, 0");
		   addItem(db, "203, 92, 0, 0, 0");
		   addItem(db, "204, 92, 0, 1, 0");
		   addItem(db, "205, 92, 0, 2, 0");
		   addItem(db, "206, 93, 0, 0, 0");
		   addItem(db, "207, 94, 0, 0, 0");
		   
		   addTagPos(db, "92,  84, 17, 1, 1");
		   addTagPos(db, "93,  85, 17, 4, 1");
		   addTagPos(db, "94,  86, 17, 4, 2");
		   addTagPos(db, "95,  87, 17, 6, 2");
		   addTagPos(db, "96,  88, 17, 1, 3");
		   addTagPos(db, "97,  89, 17, 4, 3");
		   addTagPos(db, "98,  90, 17, 1, 4");
		   addTagPos(db, "99,  91, 17, 2, 4");
		   addTagPos(db, "100, 92, 17, 4, 4");
		   addTagPos(db, "101, 93, 17, 3, 5");
		   addTagPos(db, "102, 94, 17, 4, 5");
		   addTagPos(db, "103, 84, 18, 4, 4");

		   addString(db, "29");
		   
		   addValue(db, "48, 1, 29, 'Puzzle 5'");
		   addValue(db, "49, 2, 29, 'Головоломка 5'");

		   addPosition(db, "19, 1");
		   addPosition(db, "20, 1");
		   
		   addPuzzle(db, "10, 29, 40, 5, " + GridActivity.PUZZLE_10_IMG + ", 19");
		   
		   addParam(db, "19, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 10, 6");
		   addParam(db, "20, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 10, 5");
		   
		   addEndPos(db, "10, 10, 20");
		   
		   addTag(db, "95,  10, 1");
		   addTag(db, "96,  10, 2");
		   addTag(db, "97,  10, 3");
		   addTag(db, "98,  10, 4");
		   addTag(db, "99,  10, 5");
		   addTag(db, "100, 10, 6");
		   addTag(db, "101, 10, 7");
		   addTag(db, "102, 10, 8");
		   addTag(db, "103, 10, 9");
		   addTag(db, "104, 10, 10");
		   addTag(db, "105, 10, 11");
		   addTag(db, "106, 10, 12");
		   
		   addItem(db, "208,  95, 0,  0, 0");
		   addItem(db, "209,  95, 0,  1, 0");
		   addItem(db, "210,  95, 0,  0, 1");
		   addItem(db, "211,  95, 0,  1, 1");
		   addItem(db, "212,  96, 0,  0, 0");
		   addItem(db, "213,  96, 0,  1, 0");
		   addItem(db, "214,  96, 0,  2, 0");
		   addItem(db, "215,  97, 0,  0, 0");
		   addItem(db, "216,  98, 0,  0, 0");
		   addItem(db, "217,  98, 0,  1, 0");
		   addItem(db, "218,  98, 0,  0, 1");
		   addItem(db, "219,  98, 0, -1, 1");
		   addItem(db, "220,  99, 0,  0, 0");
		   addItem(db, "221,  99, 0,  1, 0");
		   addItem(db, "222, 100, 0,  0, 0");
		   addItem(db, "223, 101, 0,  0, 0");
		   addItem(db, "224, 101, 0,  1, 0");
		   addItem(db, "225, 101, 0,  2, 0");
		   addItem(db, "226, 102, 0,  0, 0");
		   addItem(db, "227, 102, 0,  1, 0");
		   addItem(db, "228, 103, 0,  0, 0");
		   addItem(db, "229, 103, 0,  1, 0");
		   addItem(db, "230, 104, 0,  0, 0");
		   addItem(db, "231, 104, 0,  1, 0");
		   addItem(db, "232, 105, 0,  0, 0");
		   addItem(db, "233, 106, 0,  0, 0");
		   
		   addTagPos(db, "104,  95, 19, 1, 1");
		   addTagPos(db, "105,  96, 19, 3, 1");
		   addTagPos(db, "106,  97, 19, 6, 1");
		   addTagPos(db, "107,  98, 19, 3, 2");
		   addTagPos(db, "108,  99, 19, 5, 2");
		   addTagPos(db, "109, 100, 19, 1, 3");
		   addTagPos(db, "110, 101, 19, 4, 3");
		   addTagPos(db, "111, 102, 19, 1, 4");
		   addTagPos(db, "112, 103, 19, 3, 4");
		   addTagPos(db, "113, 104, 19, 5, 4");
		   addTagPos(db, "114, 105, 19, 3, 5");
		   addTagPos(db, "115, 106, 19, 4, 5");
		   addTagPos(db, "116,  95, 20, 5, 4");
		   
		   addString(db, "30");
		   
		   addValue(db, "50, 1, 30, 'Puzzle 6'");
		   addValue(db, "51, 2, 30, 'Головоломка 6'");

		   addPosition(db, "21, 1");
		   addPosition(db, "22, 1");
		   
		   addPuzzle(db, "11, 30, 40, 6, " + GridActivity.PUZZLE_11_IMG + ", 21");
		   
		   addParam(db, "21, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 11, 5");
		   addParam(db, "22, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 11, 5");
		   
		   addEndPos(db, "11, 11, 22");
		   
		   addTag(db, "107, 11, 1");
		   addTag(db, "108, 11, 2");
		   addTag(db, "109, 11, 3");
		   addTag(db, "110, 11, 4");
		   addTag(db, "111, 11, 5");
		   addTag(db, "112, 11, 6");
		   addTag(db, "113, 11, 7");
		   addTag(db, "114, 11, 8");
		   addTag(db, "115, 11, 9");
		   
		   addItem(db, "234, 107, 0, 0, 0");
		   addItem(db, "235, 107, 0, 1, 0");
		   addItem(db, "236, 107, 0, 0, 1");
		   addItem(db, "237, 107, 0, 1, 1");
		   addItem(db, "238, 108, 0, 0, 0");
		   addItem(db, "239, 108, 0, 1, 0");
		   addItem(db, "240, 108, 0, 2, 0");
		   addItem(db, "241, 109, 0, 0, 0");
		   addItem(db, "242, 109, 0, 1, 0");
		   addItem(db, "243, 110, 0, 0, 0");
		   addItem(db, "244, 111, 0, 0, 0");
		   addItem(db, "245, 111, 0, 1, 0");
		   addItem(db, "246, 111, 0, 2, 0");
		   addItem(db, "247, 112, 0, 0, 0");
		   addItem(db, "248, 112, 0, 1, 0");
		   addItem(db, "249, 113, 0, 0, 0");
		   addItem(db, "250, 113, 0, 1, 0");
		   addItem(db, "251, 114, 0, 0, 0");
		   addItem(db, "252, 114, 0, 1, 0");
		   addItem(db, "253, 114, 0, 2, 0");
		   addItem(db, "254, 115, 0, 0, 0");
		   
		   addTagPos(db, "117, 107, 21, 1, 1");
		   addTagPos(db, "118, 108, 21, 3, 1");
		   addTagPos(db, "119, 109, 21, 3, 2");
		   addTagPos(db, "120, 110, 21, 5, 2");
		   addTagPos(db, "121, 111, 21, 1, 3");
		   addTagPos(db, "122, 112, 21, 4, 3");
		   addTagPos(db, "123, 113, 21, 1, 4");
		   addTagPos(db, "124, 114, 21, 3, 4");
		   addTagPos(db, "125, 115, 21, 1, 5");
		   addTagPos(db, "126, 107, 22, 4, 4");

		   addString(db, "31");
		   
		   addValue(db, "52, 1, 31, 'Puzzle 7'");
		   addValue(db, "53, 2, 31, 'Головоломка 7'");

		   addPosition(db, "23, 1");
		   addPosition(db, "24, 1");
		   
		   addPuzzle(db, "12, 31, 40, 7, " + GridActivity.PUZZLE_12_IMG + ", 23");
		   
		   addParam(db, "23, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 12, 6");
		   addParam(db, "24, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 12, 5");
		   
		   addEndPos(db, "12, 12, 24");
		   
		   addTag(db, "116, 12, 1");
		   addTag(db, "117, 12, 2");
		   addTag(db, "118, 12, 3");
		   addTag(db, "119, 12, 4");
		   addTag(db, "120, 12, 5");
		   addTag(db, "121, 12, 6");
		   addTag(db, "122, 12, 7");
		   addTag(db, "123, 12, 8");
		   addTag(db, "124, 12, 9");
		   addTag(db, "125, 12, 10");
		   addTag(db, "126, 12, 11");
		   
		   addItem(db, "255, 116, 0, 0, 0");
		   addItem(db, "256, 116, 0, 1, 0");
		   addItem(db, "257, 116, 0, 2, 0");
		   addItem(db, "258, 116, 0, 0, 1");
		   addItem(db, "259, 116, 0, 1, 1");
		   addItem(db, "260, 116, 0, 2, 1");
		   addItem(db, "261, 117, 0, 0, 0");
		   addItem(db, "262, 117, 0, 1, 0");
		   addItem(db, "263, 117, 0, 2, 0");
		   addItem(db, "264, 118, 0, 0, 0");
		   addItem(db, "265, 119, 0, 0, 0");
		   addItem(db, "266, 119, 0, 1, 0");
		   addItem(db, "267, 120, 0, 0, 0");
		   addItem(db, "268, 120, 0, 0, 1");
		   addItem(db, "269, 121, 0, 0, 0");
		   addItem(db, "270, 121, 0, 0, 1");
		   addItem(db, "271, 122, 0, 0, 0");
		   addItem(db, "272, 122, 0, 1, 0");
		   addItem(db, "273, 122, 0, 2, 0");
		   addItem(db, "274, 123, 0, 0, 0");
		   addItem(db, "275, 124, 0, 0, 0");
		   addItem(db, "276, 124, 0, 1, 0");
		   addItem(db, "277, 125, 0, 0, 0");
		   addItem(db, "278, 125, 0, 1, 0");
		   addItem(db, "279, 126, 0, 0, 0");
		   addItem(db, "280, 126, 0, 1, 0");
		   
		   addTagPos(db, "127, 116, 23, 1, 1");
		   addTagPos(db, "128, 117, 23, 4, 1");
		   addTagPos(db, "129, 118, 23, 4, 2");
		   addTagPos(db, "130, 119, 23, 5, 2");
		   addTagPos(db, "131, 120, 23, 1, 3");
		   addTagPos(db, "132, 121, 23, 2, 3");
		   addTagPos(db, "133, 122, 23, 3, 3");
		   addTagPos(db, "134, 123, 23, 6, 3");
		   addTagPos(db, "135, 124, 23, 3, 4");
		   addTagPos(db, "136, 125, 23, 5, 4");
		   addTagPos(db, "137, 126, 23, 1, 5");
		   addTagPos(db, "138, 116, 24, 4, 4");
		   
		   addString(db, "32");
		   
		   addValue(db, "54, 1, 32, 'Puzzle 8'");
		   addValue(db, "55, 2, 32, 'Головоломка 8'");

		   addPosition(db, "25, 1");
		   addPosition(db, "26, 1");
		   
		   addPuzzle(db, "13, 32, 40, 8, " + GridActivity.PUZZLE_13_IMG + ", 25");
		   
		   addParam(db, "25, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 13, 6");
		   addParam(db, "26, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 13, 5");
		   
		   addEndPos(db, "13, 13, 26");
		   
		   addTag(db, "127, 13, 1");
		   addTag(db, "128, 13, 2");
		   addTag(db, "129, 13, 3");
		   addTag(db, "130, 13, 4");
		   addTag(db, "131, 13, 5");
		   addTag(db, "132, 13, 6");
		   addTag(db, "133, 13, 7");
		   addTag(db, "134, 13, 8");
		   addTag(db, "135, 13, 9");
		   addTag(db, "136, 13, 10");
		   
		   addItem(db, "281, 127, 0, 0, 0");
		   addItem(db, "282, 127, 0, 1, 0");
		   addItem(db, "283, 127, 0, 2, 0");
		   addItem(db, "284, 127, 0, 0, 1");
		   addItem(db, "285, 127, 0, 1, 1");
		   addItem(db, "286, 127, 0, 2, 1");
		   addItem(db, "287, 128, 0, 0, 0");
		   addItem(db, "288, 128, 0, 0, 1");
		   addItem(db, "289, 129, 0, 0, 0");
		   addItem(db, "290, 129, 0, 0, 1");
		   addItem(db, "291, 130, 0, 0, 0");
		   addItem(db, "292, 130, 0, 0, 1");
		   addItem(db, "293, 131, 0, 0, 0");
		   addItem(db, "294, 131, 0, 1, 0");
		   addItem(db, "295, 132, 0, 0, 0");
		   addItem(db, "296, 132, 0, 1, 0");
		   addItem(db, "297, 133, 0, 0, 0");
		   addItem(db, "298, 133, 0, 1, 0");
		   addItem(db, "299, 134, 0, 0, 0");
		   addItem(db, "300, 134, 0, 1, 0");
		   addItem(db, "301, 134, 0, 2, 0");
		   addItem(db, "302, 135, 0, 0, 0");
		   addItem(db, "303, 135, 0, 1, 0");
		   addItem(db, "304, 135, 0, 2, 0");
		   addItem(db, "305, 136, 0, 0, 0");
		   addItem(db, "306, 136, 0, 1, 0");
		   
		   addTagPos(db, "139, 127, 25, 1, 1");
		   addTagPos(db, "140, 128, 25, 4, 1");
		   addTagPos(db, "141, 129, 25, 5, 1");
		   addTagPos(db, "142, 130, 25, 6, 1");
		   addTagPos(db, "143, 131, 25, 1, 3");
		   addTagPos(db, "144, 132, 25, 3, 3");
		   addTagPos(db, "145, 133, 25, 5, 3");
		   addTagPos(db, "146, 134, 25, 1, 4");
		   addTagPos(db, "147, 135, 25, 4, 4");
		   addTagPos(db, "148, 136, 25, 1, 5");
		   addTagPos(db, "149, 127, 26, 4, 4");
		   
		   addString(db, "33");
		   
		   addValue(db, "56, 1, 33, 'Puzzle 9'");
		   addValue(db, "57, 2, 33, 'Головоломка 9'");

		   addPosition(db, "27, 1");
		   addPosition(db, "28, 1");
		   
		   addPuzzle(db, "14, 33, 40, 9, " + GridActivity.PUZZLE_14_IMG + ", 27");
		   
		   addParam(db, "27, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 14, 5");
		   addParam(db, "28, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 14, 6");
		   
		   addEndPos(db, "14, 14, 28");
		   
		   addTag(db, "137, 14, 1");
		   addTag(db, "138, 14, 2");
		   addTag(db, "139, 14, 3");
		   addTag(db, "140, 14, 4");
		   addTag(db, "141, 14, 5");
		   addTag(db, "142, 14, 6");
		   addTag(db, "143, 14, 7");
		   addTag(db, "144, 14, 8");
		   addTag(db, "145, 14, 9");
		   addTag(db, "146, 14, 10");
		   addTag(db, "147, 14, 11");
		   
		   addItem(db, "307, 137, 0, 0, 0");
		   addItem(db, "308, 137, 0, 1, 0");
		   addItem(db, "309, 137, 0, 0, 1");
		   addItem(db, "310, 137, 0, 1, 1");
		   addItem(db, "311, 137, 0, 0, 2");
		   addItem(db, "312, 137, 0, 1, 2");
		   addItem(db, "313, 138, 0, 0, 0");
		   addItem(db, "314, 138, 0, 1, 0");
		   addItem(db, "315, 139, 0, 0, 0");
		   addItem(db, "316, 140, 0, 0, 0");
		   addItem(db, "317, 141, 0, 0, 0");
		   addItem(db, "318, 141, 0, 1, 0");
		   addItem(db, "319, 142, 0, 0, 0");
		   addItem(db, "320, 142, 0, 0, 1");
		   addItem(db, "321, 142, 0, 0, 2");
		   addItem(db, "322, 143, 0, 0, 0");
		   addItem(db, "323, 143, 0, 0, 1");
		   addItem(db, "324, 143, 0, 0, 2");
		   addItem(db, "325, 144, 0, 0, 0");
		   addItem(db, "326, 144, 0, 0, 1");
		   addItem(db, "327, 145, 0, 0, 0");
		   addItem(db, "328, 145, 0, 0, 1");
		   addItem(db, "329, 146, 0, 0, 0");
		   addItem(db, "330, 146, 0, 0, 1");
		   addItem(db, "331, 147, 0, 0, 0");
		   addItem(db, "332, 147, 0, 1, 0");
		   addItem(db, "333, 147, 0, 2, 0");
		   
		   addTagPos(db, "150, 137, 27, 1, 1");
		   addTagPos(db, "151, 138, 27, 3, 1");
		   addTagPos(db, "152, 139, 27, 5, 1");
		   addTagPos(db, "153, 140, 27, 3, 2");
		   addTagPos(db, "154, 141, 27, 4, 2");
		   addTagPos(db, "155, 142, 27, 1, 4");
		   addTagPos(db, "156, 143, 27, 2, 4");
		   addTagPos(db, "157, 144, 27, 3, 3");
		   addTagPos(db, "158, 145, 27, 4, 3");
		   addTagPos(db, "159, 146, 27, 5, 3");
		   addTagPos(db, "160, 147, 27, 3, 5");
		   addTagPos(db, "161, 137, 28, 4, 4");
		   
		   addString(db, "34");
		   
		   addValue(db, "58, 1, 34, 'Puzzle 10'");
		   addValue(db, "59, 2, 34, 'Головоломка 10'");

		   addPosition(db, "29, 1");
		   addPosition(db, "30, 1");
		   
		   addPuzzle(db, "15, 34, 40, 10, " + GridActivity.PUZZLE_15_IMG + ", 29");
		   
		   addParam(db, "29, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 15, 5");
		   addParam(db, "30, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 15, 5");
		   
		   addEndPos(db, "15, 15, 30");
		   
		   addTag(db, "148, 15, 1");
		   addTag(db, "149, 15, 2");
		   addTag(db, "150, 15, 3");
		   addTag(db, "151, 15, 4");
		   addTag(db, "152, 15, 5");
		   addTag(db, "153, 15, 6");
		   addTag(db, "154, 15, 7");
		   addTag(db, "155, 15, 8");
		   addTag(db, "156, 15, 9");
		   addTag(db, "157, 15, 10");
		   
		   addItem(db, "334, 148, 0, 0, 0");
		   addItem(db, "335, 148, 0, 1, 0");
		   addItem(db, "336, 148, 0, 2, 0");
		   addItem(db, "337, 149, 0, 0, 0");
		   addItem(db, "338, 149, 0, 1, 0");
		   addItem(db, "339, 149, 0, 0, 1");
		   addItem(db, "340, 149, 0, 1, 1");
		   addItem(db, "341, 150, 0, 0, 0");
		   addItem(db, "342, 150, 0, 0, 1");
		   addItem(db, "343, 151, 0, 0, 0");
		   addItem(db, "344, 151, 0, 0, 1");
		   addItem(db, "345, 152, 0, 0, 0");
		   addItem(db, "346, 153, 0, 0, 0");
		   addItem(db, "347, 153, 0, 1, 0");
		   addItem(db, "348, 153, 0, 2, 0");
		   addItem(db, "349, 154, 0, 0, 0");
		   addItem(db, "350, 154, 0, 1, 0");
		   addItem(db, "351, 155, 0, 0, 0");
		   addItem(db, "352, 155, 0, 1, 0");
		   addItem(db, "353, 156, 0, 0, 0");
		   addItem(db, "354, 157, 0, 0, 0");
		   addItem(db, "355, 157, 0, 1, 0");
		   
		   addTagPos(db, "162, 148, 29, 1, 1");
		   addTagPos(db, "163, 149, 29, 4, 1");
		   addTagPos(db, "164, 150, 29, 1, 2");
		   addTagPos(db, "165, 151, 29, 2, 2");
		   addTagPos(db, "166, 152, 29, 3, 2");
		   addTagPos(db, "167, 153, 29, 3, 3");
		   addTagPos(db, "168, 154, 29, 1, 4");
		   addTagPos(db, "169, 155, 29, 3, 4");
		   addTagPos(db, "170, 156, 29, 5, 4");
		   addTagPos(db, "171, 157, 29, 1, 5");
		   addTagPos(db, "172, 149, 30, 1, 4");
	   }
	   
	   private void patch_8(SQLiteDatabase db) {
		   
		   addPosition(db, "31, 1");
		   addPuzzle(db, "16, 12, 10, 1, " + GridActivity.PUZZLE_3_IMG + ", 5");
		   addParam(db, "31, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 16, 4");
		   addParam(db, "32, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 16, 5");
		   addEndPos(db, "16, 16, 31");
		   addTagPos(db, "173, 20, 31, 3, 4");
		   
		   addPosition(db, "32, 1");
		   addPosition(db, "33, 1");
		   addPuzzle(db, "17, 5, 20, 2, " + GridActivity.PUZZLE_17_IMG + ", 32");
		   addParam(db, "33, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 17, 5");
		   addParam(db, "34, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 17, 5");
		   addEndPos(db, "17, 17, 33");
		   addTag(db, "158, 17, 10");
		   addItem(db, "356, 158, 0, 0, 0");
		   
		   addTagPos(db, "174,  11, 32, 1, 2");
		   addTagPos(db, "175,  12, 32, 4, 1");
		   addTagPos(db, "176,  13, 32, 1, 3");
		   addTagPos(db, "177,  14, 32, 3, 3");
		   addTagPos(db, "178,  15, 32, 1, 4");
		   addTagPos(db, "179,  16, 32, 2, 4");
		   addTagPos(db, "180,  17, 32, 4, 4");
		   addTagPos(db, "181,  18, 32, 3, 5");
		   addTagPos(db, "182,  19, 32, 1, 1");
		   addTagPos(db, "183, 158, 32, 2, 1");
		   addTagPos(db, "184,  12, 33, 4, 1");
		   addTagPos(db, "185,  15, 33, 4, 2");
		   
		   addPosition(db, "34, 1");
		   addPosition(db, "35, 1");
		   
		   addPuzzle(db, "18, 1, 30, 2, " + GridActivity.PUZZLE_18_IMG + ", 34");
		   
		   addParam(db, "35, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 18, 4");
		   addParam(db, "36, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 18, 5");
		   
		   addEndPos(db, "18, 18, 35");
		   
		   addTag(db, "159, 18, 1");
		   addTag(db, "160, 18, 2");
		   addTag(db, "161, 18, 3");
		   addTag(db, "162, 18, 4");
		   addTag(db, "163, 18, 5");
		   addTag(db, "164, 18, 6");
		   addTag(db, "165, 18, 7");
		   addTag(db, "166, 18, 8");
		   addTag(db, "167, 18, 9");
		   addTag(db, "168, 18, 10");
		   
		   addItem(db, "357, 159, 0, 0, 0");
		   addItem(db, "358, 159, 0, 0, 1");
		   addItem(db, "359, 160, 0, 0, 0");
		   addItem(db, "360, 160, 0, 1, 0");
		   addItem(db, "361, 160, 0, 0, 1");
		   addItem(db, "362, 160, 0, 1, 1");
		   addItem(db, "363, 161, 0, 0, 0");
		   addItem(db, "364, 161, 0, 0, 1");
		   addItem(db, "365, 162, 0, 0, 0");
		   addItem(db, "366, 162, 0, 1, 0");
		   addItem(db, "367, 163, 0, 0, 0");
		   addItem(db, "368, 163, 0, 1, 0");
		   addItem(db, "369, 164, 0, 0, 0");
		   addItem(db, "370, 165, 0, 0, 0");
		   addItem(db, "371, 165, 0, 1, 0");
		   addItem(db, "372, 166, 0, 0, 0");
		   addItem(db, "373, 167, 0, 0, 0");
		   addItem(db, "374, 168, 0, 0, 0");
		   
		   addTagPos(db, "186, 159, 34, 1, 1");
		   addTagPos(db, "187, 160, 34, 2, 1");
		   addTagPos(db, "188, 161, 34, 4, 1");
		   addTagPos(db, "189, 162, 34, 1, 3");
		   addTagPos(db, "190, 163, 34, 3, 3");
		   addTagPos(db, "191, 164, 34, 1, 4");
		   addTagPos(db, "192, 165, 34, 2, 4");
		   addTagPos(db, "193, 166, 34, 4, 4");
		   addTagPos(db, "194, 167, 34, 1, 5");
		   addTagPos(db, "195, 168, 34, 4, 5");
		   addTagPos(db, "196, 160, 35, 2, 4");
		   
		   addPosition(db, "36, 1");
		   addPosition(db, "37, 1");
		   
		   addPuzzle(db, "19, 1, 30, 3, " + GridActivity.PUZZLE_19_IMG + ", 36");
		   
		   addParam(db, "37, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 19, 4");
		   addParam(db, "38, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 19, 5");
		   
		   addEndPos(db, "19, 19, 37");
		   
		   addTag(db, "169, 19, 1");
		   addTag(db, "170, 19, 2");
		   addTag(db, "171, 19, 3");
		   addTag(db, "172, 19, 4");
		   addTag(db, "173, 19, 5");
		   addTag(db, "174, 19, 6");
		   addTag(db, "175, 19, 7");
		   addTag(db, "176, 19, 8");
		   addTag(db, "177, 19, 9");
		   addTag(db, "178, 19, 10");
		   addTag(db, "179, 19, 11");
		   addTag(db, "180, 19, 12");
		   
		   addItem(db, "375, 169, 0, 0, 0");
		   addItem(db, "376, 169, 0, 0, 1");
		   addItem(db, "377, 170, 0, 0, 0");
		   addItem(db, "378, 170, 0, 1, 0");
		   addItem(db, "379, 170, 0, 0, 1");
		   addItem(db, "380, 170, 0, 1, 1");
		   addItem(db, "381, 171, 0, 0, 0");
		   addItem(db, "382, 171, 0, 0, 1");
		   addItem(db, "383, 172, 0, 0, 0");
		   addItem(db, "384, 173, 0, 0, 0");
		   addItem(db, "385, 174, 0, 0, 0");
		   addItem(db, "386, 175, 0, 0, 0");
		   addItem(db, "387, 176, 0, 0, 0");
		   addItem(db, "388, 177, 0, 0, 0");
		   addItem(db, "389, 177, 0, 1, 0");
		   addItem(db, "390, 178, 0, 0, 0");
		   addItem(db, "391, 179, 0, 0, 0");
		   addItem(db, "392, 180, 0, 0, 0");
		   
		   addTagPos(db, "197, 169, 36, 1, 1");
		   addTagPos(db, "198, 170, 36, 2, 1");
		   addTagPos(db, "199, 171, 36, 4, 1");
		   addTagPos(db, "200, 172, 36, 1, 3");
		   addTagPos(db, "201, 173, 36, 2, 3");
		   addTagPos(db, "202, 174, 36, 3, 3");
		   addTagPos(db, "203, 175, 36, 4, 3");
		   addTagPos(db, "204, 176, 36, 1, 4");
		   addTagPos(db, "205, 177, 36, 2, 4");
		   addTagPos(db, "206, 178, 36, 4, 4");
		   addTagPos(db, "207, 179, 36, 1, 5");
		   addTagPos(db, "208, 180, 36, 4, 5");
		   addTagPos(db, "209, 170, 37, 2, 4");
	   }
	   
	   private void patch_9(SQLiteDatabase db) {
		   
		   addString(db, "35");    // Шамшин
		   addString(db, "36");    // Еще головоломки
		   addString(db, "37");    // Donkey help
		   addString(db, "38");    // Mother help
		   addString(db, "39");    // Father help
		   addString(db, "40");    // Шамшин help
		   
		   addGroup(db, "10, 12, 39"); // Father
		   addGroup(db, "20, 5,  38"); // Mother
		   addGroup(db, "30, 1,  37"); // Donkey
		   addGroup(db, "40, 35, 40"); // Shamshin
		   
		   addValue(db, "60, 1, 35, 'Shamshin''s puzzles'");
		   addValue(db, "61, 2, 35, 'Головоломки от Шамшина'");
		   
		   addValue(db, "64, 1, 37, 'In France, this puzzle has long been known as ''Red ass''.\n" +
		        "Here are the variants of this puzzle'");
		   addValue(db, "65, 2, 37, 'Во Франции, эта головоломка долгое время была известна под названием ''Рыжий осел''.\n" +
		   		"Здесь представлены варианты этой головоломки.'");

		   addValue(db, "66, 1, 38, 'The puzzle ''Mother and baby'' was invented by Charles L.A. Dayemend in 1927'");
		   addValue(db, "67, 2, 38, 'Головоломка ''Мама и малыш'' была придумана Чарлзом Л.А. Дайемендом в 1927 году.\n" +
		   		"Фигуру в правом верхнем углу назвали ''Мама'', а в левом нижнем - ''Мой мальчик''.\n" +
		   		"Остальные фигуры символизировали жизненные невзгоды между матерью и сыном.'");
		   
		   addValue(db, "68, 1, 39, 'No one knows who invented this puzzle.\n" +
		   		"In 1926, in America, it was sold as ''Father''s puzzle''.'");
		   addValue(db, "69, 2, 39, 'Происхождение этой головоломки неизвестно.\n" +
		   		"В 1926 году, в Америке, она продавалась под названием ''Папина головоломка''.\n" +
		   		"Достаточно легко перевести квадрат из левого верхнего угла, в правый нижний.\n" +
		   		"Несколько сложнее перевести большой квадрат в левый нижний угол.'");
		   
		   addValue(db, "70, 1, 40, 'These puzzle invented by Alexey Shamshin http://www.puzzle-shamshin.narod.ru/fifteen_m.html'");
		   addValue(db, "71, 2, 40, 'Эти головоломки любезно предоставлены Алексеем Шамшиным http://www.puzzle-shamshin.narod.ru/fifteen_m.html'");
	   }
	   
	   private void patch_10(SQLiteDatabase db) {
		   
		   addString(db, "27");
		   
		   addValue(db, "44, 1, 27, 'Puzzle 3'");
		   addValue(db, "45, 2, 27, 'Головоломка 3'");

		   addPosition(db, "15, 1");
		   addPosition(db, "16, 1");
		   
		   addPuzzle(db, "8, 27, 40, 3, " + GridActivity.PUZZLE_8_IMG + ", 15");
		   
		   addParam(db, "15, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 8, 5");
		   addParam(db, "16, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 8, 5");
		   
		   addEndPos(db, "8, 8, 16");
		   
		   addTag(db, "75, 8, 1");
		   addTag(db, "76, 8, 2");
		   addTag(db, "77, 8, 3");
		   addTag(db, "78, 8, 4");
		   addTag(db, "79, 8, 5");
		   addTag(db, "80, 8, 6");
		   addTag(db, "81, 8, 7");
		   addTag(db, "82, 8, 8");
		   addTag(db, "83, 8, 9");
		   
		   addItem(db, "162, 75, 0, 0,  0");
		   addItem(db, "163, 75, 0, 1,  0");
		   addItem(db, "164, 75, 0, 0,  1");
		   addItem(db, "165, 75, 0, 1,  1");
		   addItem(db, "166, 76, 0, 0,  0");
		   addItem(db, "167, 77, 0, 0,  0");
		   addItem(db, "168, 77, 0, 1,  0");
		   addItem(db, "169, 78, 0, 0,  0");
		   addItem(db, "170, 78, 0, 1,  0");
		   addItem(db, "171, 78, 0, 2, -1");
		   addItem(db, "172, 78, 0, 2,  0");
		   addItem(db, "173, 79, 0, 0,  0");
		   addItem(db, "174, 79, 0, 1,  0");
		   addItem(db, "175, 80, 0, 0,  0");
		   addItem(db, "176, 80, 0, 1,  0");
		   addItem(db, "177, 81, 0, 0,  0");
		   addItem(db, "178, 82, 0, 0,  0");
		   addItem(db, "179, 82, 0, 0,  1");
		   addItem(db, "180, 83, 0, 0,  0");
		   addItem(db, "181, 83, 0, 0,  1");
		   
		   addTagPos(db, "82, 75, 15, 1, 1");
		   addTagPos(db, "83, 76, 15, 3, 1");
		   addTagPos(db, "84, 77, 15, 4, 1");
		   addTagPos(db, "85, 78, 15, 1, 3");
		   addTagPos(db, "86, 79, 15, 4, 2");
		   addTagPos(db, "87, 80, 15, 1, 4");
		   addTagPos(db, "88, 81, 15, 3, 4");
		   addTagPos(db, "89, 82, 15, 4, 3");
		   addTagPos(db, "90, 83, 15, 5, 3");
		   addTagPos(db, "91, 75, 16, 4, 4");
	   }

	   private void patch_11(SQLiteDatabase db) {
		   
		   addString(db, "41");        // More help
		   addGroup(db, "50, 36, 41"); // More
		   
		   addValue(db, "62, 1, 36, 'More ...'");
		   addValue(db, "63, 2, 36, 'Ещё головоломки ...'");
		   
		   addString(db, "24");
		   
		   addValue(db, "38, 1, 24, 'Domino'");
		   addValue(db, "39, 2, 24, 'Домино'");

		   addPosition(db, "9,  1");
		   addPosition(db, "10, 1");
		   
		   addPuzzle(db, "5, 24, 50, 1, " + GridActivity.PUZZLE_5_IMG + ", 9");
		   
		   addParam(db, "9, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 5, 7");
		   addParam(db, "10, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 5, 7");
		   
		   addEndPos(db, "5, 5, 10");
		   
		   addTag(db, "30, 5, 1");
		   addTag(db, "31, 5, 2");
		   addTag(db, "32, 5, 3");
		   addTag(db, "33, 5, 4");
		   addTag(db, "34, 5, 5");
		   addTag(db, "35, 5, 6");
		   addTag(db, "36, 5, 7");
		   addTag(db, "37, 5, 8");
		   addTag(db, "38, 5, 9");
		   addTag(db, "39, 5, 10");
		   addTag(db, "40, 5, 11");
		   addTag(db, "41, 5, 12");
		   addTag(db, "42, 5, 13");
		   addTag(db, "43, 5, 14");
		   addTag(db, "44, 5, 15");
		   addTag(db, "45, 5, 16");
		   addTag(db, "46, 5, 17");
		   addTag(db, "47, 5, 18");
		   addTag(db, "48, 5, 19");
		   addTag(db, "49, 5, 20");
		   addTag(db, "50, 5, 21");
		   addTag(db, "51, 5, 22");
		   addTag(db, "52, 5, 23");
		   addTag(db, "53, 5, 24");
		   
		   addItem(db, "63,  30, 0, 0, 0");
		   addItem(db, "64,  30, 0, 0, 1");
		   addItem(db, "65,  31, 0, 0, 0");
		   addItem(db, "66,  31, 0, 0, 1");
		   addItem(db, "67,  32, 0, 0, 0");
		   addItem(db, "68,  32, 0, 1, 0");
		   addItem(db, "69,  33, 0, 0, 0");
		   addItem(db, "70,  33, 0, 0, 1");
		   addItem(db, "71,  34, 0, 0, 0");
		   addItem(db, "72,  34, 0, 1, 0");
		   addItem(db, "73,  35, 0, 0, 0");
		   addItem(db, "74,  35, 0, 0, 1");
		   addItem(db, "75,  36, 0, 0, 0");
		   addItem(db, "76,  36, 0, 0, 1");
		   addItem(db, "77,  37, 0, 0, 0");
		   addItem(db, "78,  37, 0, 1, 0");
		   addItem(db, "79,  38, 0, 0, 0");
		   addItem(db, "80,  38, 0, 0, 1");
		   addItem(db, "81,  39, 0, 0, 0");
		   addItem(db, "82,  39, 0, 0, 1");
		   addItem(db, "83,  40, 0, 0, 0");
		   addItem(db, "84,  40, 0, 0, 1");
		   addItem(db, "85,  41, 0, 0, 0");
		   addItem(db, "86,  41, 0, 0, 1");
		   addItem(db, "87,  42, 0, 0, 0");
		   addItem(db, "88,  42, 0, 0, 1");
		   addItem(db, "89,  43, 0, 0, 0");
		   addItem(db, "90,  43, 0, 1, 0");
		   addItem(db, "91,  44, 0, 0, 0");
		   addItem(db, "92,  44, 0, 0, 1");
		   addItem(db, "93,  45, 0, 0, 0");
		   addItem(db, "94,  45, 0, 1, 0");
		   addItem(db, "95,  46, 0, 0, 0");
		   addItem(db, "96,  46, 0, 1, 0");
		   addItem(db, "97,  47, 0, 0, 0");
		   addItem(db, "98,  47, 0, 1, 0");
		   addItem(db, "99,  48, 0, 0, 0");
		   addItem(db, "100, 48, 0, 1, 0");
		   addItem(db, "101, 49, 0, 0, 0");
		   addItem(db, "102, 49, 0, 1, 0");
		   addItem(db, "103, 50, 0, 0, 0");
		   addItem(db, "104, 50, 0, 1, 0");
		   addItem(db, "105, 51, 0, 0, 0");
		   addItem(db, "106, 51, 0, 1, 0");
		   addItem(db, "107, 52, 0, 0, 0");
		   addItem(db, "108, 52, 0, 1, 0");
		   addItem(db, "109, 53, 0, 0, 0");
		   
		   addTagPos(db, "34, 30, 9, 3, 1");
		   addTagPos(db, "35, 31, 9, 4, 1");
		   addTagPos(db, "36, 32, 9, 5, 1");
		   addTagPos(db, "37, 33, 9, 7, 1");
		   addTagPos(db, "38, 34, 9, 1, 2");
		   addTagPos(db, "39, 35, 9, 5, 2");
		   addTagPos(db, "40, 36, 9, 6, 2");
		   addTagPos(db, "41, 37, 9, 1, 3");
		   addTagPos(db, "42, 38, 9, 3, 3");
		   addTagPos(db, "43, 39, 9, 4, 3");
		   addTagPos(db, "44, 40, 9, 7, 3");
		   addTagPos(db, "45, 41, 9, 1, 4");
		   addTagPos(db, "46, 42, 9, 2, 4");
		   addTagPos(db, "47, 43, 9, 5, 4");
		   addTagPos(db, "48, 44, 9, 3, 5");
		   addTagPos(db, "49, 45, 9, 4, 5");
		   addTagPos(db, "50, 46, 9, 6, 5");
		   addTagPos(db, "51, 47, 9, 1, 6");
		   addTagPos(db, "52, 48, 9, 4, 6");
		   addTagPos(db, "53, 49, 9, 6, 6");
		   addTagPos(db, "54, 50, 9, 1, 7");
		   addTagPos(db, "55, 51, 9, 3, 7");
		   addTagPos(db, "56, 52, 9, 5, 7");
		   addTagPos(db, "57, 53, 9, 7, 7");
		   addTagPos(db, "58, 53, 10, 1, 1");
	   }

	   private void patch_12(SQLiteDatabase db) {
		   addString(db, "1000");
		   
		   addValue(db, "1000, 1, 1000, 'Not easy maneuvering'");
		   addValue(db, "1001, 2, 1000, 'Нелегкие маневры'");

		   addPosition(db, "1000, 1");
		   addPosition(db, "1001, 1");
		   
		   addPuzzle(db, "1000, 1000, 50, 2, " + GridActivity.PUZZLE_20_IMG + ", 1000");
		   
		   addParam(db, "1000, " + Integer.toString(TagsDb.ParamTypes.SizeX) + ", 1000, 6");
		   addParam(db, "1001, " + Integer.toString(TagsDb.ParamTypes.SizeY) + ", 1000, 5");
		   
		   addEndPos(db, "1000, 1000, 1001");
		   
		   addTag(db, "1000, 1000, 1");
		   addTag(db, "1001, 1000, 2");
		   addTag(db, "1002, 1000, 3");
		   addTag(db, "1003, 1000, 4");
		   addTag(db, "1004, 1000, 5");
		   addTag(db, "1005, 1000, 6");
		   addTag(db, "1006, 1000, 7");
		   addTag(db, "1007, 1000, 8");

		   addItem(db, "1000, 1000, 0, 0, 0");
		   addItem(db, "1001, 1000, 0, 1, 0");
		   addItem(db, "1002, 1000, 0, 2, 0");
		   addItem(db, "1003, 1000, 0, 3, 0");
		   addItem(db, "1004, 1001, 0, 0, 0");
		   addItem(db, "1005, 1001, 0, 1, 0");
		   addItem(db, "1006, 1001, 0, 2, 0");
		   addItem(db, "1007, 1001, 0, 3, 0");
		   addItem(db, "1008, 1002, 0, 0, 0");
		   addItem(db, "1009, 1002, 0, 1, 0");
		   addItem(db, "1010, 1003, 0, 0, 0");
		   addItem(db, "1011, 1003, 0, 1, 0");
		   addItem(db, "1012, 1004, 0, 0, 0");
		   addItem(db, "1013, 1004, 0, 1, 0");
		   addItem(db, "1014, 1005, 0, 0, 0");
		   addItem(db, "1015, 1005, 0, 1, 0");
		   addItem(db, "1016, 1005, 0, 0, 1");
		   addItem(db, "1017, 1005, 0, 1, 1");
		   addItem(db, "1018, 1006, 0, 0, 0");
		   addItem(db, "1019, 1006, 0, 1, 0");
		   addItem(db, "1020, 1006, 0, 0, 1");
		   addItem(db, "1021, 1006, 0, 1, 1");
		   addItem(db, "1022, 1007, 0, 0, 0");
		   addItem(db, "1023, 1007, 0, 1, 0");
		   addItem(db, "1024, 1007, 0, 0, 1");
		   addItem(db, "1025, 1007, 0, 1, 1");
	   
		   addTagPos(db, "1000, 1000, 1000, 2, 1");
		   addTagPos(db, "1001, 1001, 1000, 2, 2");
		   addTagPos(db, "1002, 1002, 1000, 1, 3");
		   addTagPos(db, "1003, 1003, 1000, 3, 3");
		   addTagPos(db, "1004, 1004, 1000, 5, 3");
		   addTagPos(db, "1005, 1005, 1000, 1, 4");
		   addTagPos(db, "1006, 1006, 1000, 3, 4");
		   addTagPos(db, "1007, 1007, 1000, 5, 4");
		   addTagPos(db, "1008, 1000, 1001, 2, 4");
		   addTagPos(db, "1009, 1001, 1001, 2, 5");
	   }
	   
	   @Override
	   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		   if (oldVersion < 2)  {patch_2(db);}
		   if (oldVersion < 3)  {patch_3(db);}
		   if (oldVersion < 5)  {patch_5(db);}
		   if (oldVersion < 7)  {patch_7(db);}
		   if (oldVersion < 8)  {patch_8(db);}
		   if (oldVersion < 9)  {patch_9(db);}
		   if (oldVersion < 10) {patch_10(db);}
		   if (oldVersion < 11) {patch_11(db);}
		   if (oldVersion < 12) {patch_12(db);}
	   }
		   
   }
	   
}
