package com.WhiteRabbit.tags;

import android.net.Uri; 
import android.provider.BaseColumns;

public final class TagsDb {

    private static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.WhiteRabbit.provider.Tags";
    
    private TagsDb() {}
    
    public static final class Locales implements BaseColumns {
    	
        public static final String PATH_LOCALE = "locales";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_LOCALE);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.locales";
        
        private Locales() {}
        
        public static final int LOCALE_EN = 1;
        public static final int LOCALE_RU = 2;
    	
        public static final String TABLE_NAME = "locale";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_STRING_ID = "string_id";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_IS_DEFAULT = "is_default";
        
    }
    
    public static final class Strings implements BaseColumns {
    	
        public static final String PATH_STRING = "strings";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_STRING);
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.strings";
        
        private Strings() {}
    	
        public static final String TABLE_NAME = "string";
        
    }
    
    public static final class StringValues implements BaseColumns {
    	
        private StringValues() {}
    	
        public static final String TABLE_NAME = "string_value";
        public static final String COLUMN_NAME_LOCALE_ID = "locale_id";
        public static final String COLUMN_NAME_STRING_ID = "string_id";
        public static final String COLUMN_NAME_VALUE = "value";
        
    }
    
    public static final class StatTypes implements BaseColumns {
    	
    	public final static int Steps = 1;
    	
        private StatTypes() {}
    	
        public static final String TABLE_NAME = "stat_type";
        public static final String COLUMN_NAME_STRING_ID = "string_id";
        
    }
    
    public static final class Stats implements BaseColumns {
    	
        public static final String PATH_STAT = "stats";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_STAT);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.stats";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.stats";
        
        private Stats() {}
    	
        public static final String TABLE_NAME = "stat";
        public static final String COLUMN_NAME_STAT_TYPE_ID = "stat_type_id";
        public static final String COLUMN_NAME_SESSION_ID = "session_id";
        public static final String COLUMN_NAME_VALUE = "value";
        
    }
    
    public static final class Profiles implements BaseColumns {
    	
        public static final String PATH_PROFILE = "profile";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_PROFILE);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.profile";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.profile";

        private Profiles() {}
    	
        public static final String TABLE_NAME = "profile";
        public static final String COLUMN_NAME_LOGIN = "login";
        public static final String COLUMN_NAME_IS_DEFAULT = "is_default";
        public static final String COLUMN_NAME_LOCALE_ID = "locale_id";
        public static final String COLUMN_NAME_PUZZLE_ID = "puzzle_id";
        
    }
    
    public static final class Positions implements BaseColumns {
    	
        public static final String PATH_POSITION = "positions";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_POSITION);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.position";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.position";
        
        private Positions() {}
    	
        public static final String TABLE_NAME = "position";
        public static final String COLUMN_NAME_IS_PROTECTED = "is_protected";
        
    }
    
    public static final class Groups implements BaseColumns {
    	
        public static final String PATH_GROUP = "group";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_GROUP);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.group";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.group";
        
        private Groups() {}
        
        public static final String TABLE_NAME = "groups";
        public static final String COLUMN_NAME_STRING_ID = "string_id";
        public static final String COLUMN_NAME_HELP_ID = "help_id";
    }
    
    public static final class Puzzles implements BaseColumns {
    	
        public static final String PATH_PUZZLE = "puzzle";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_PUZZLE);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.puzzle";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.puzzle";
        
        private Puzzles() {}
    	
        public static final String TABLE_NAME = "puzzle";
        public static final String COLUMN_NAME_STRING_ID = "string_id";
        public static final String COLUMN_NAME_GROUP_ID = "group_id";
        public static final String COLUMN_NAME_IMG_ID = "img_id";
        public static final String COLUMN_NAME_IX = "ix";
        public static final String COLUMN_NAME_START_POSITION_ID = "start_position_id";
        public static final String ALIAS_NAME_PUZZLE_NAME = "puzzle_name";
        
    }
    
    public static final class ParamTypes implements BaseColumns {
    	
    	public final static int SizeX = 1;
    	public final static int SizeY = 2;
    	
        private ParamTypes() {}
    	
        public static final String TABLE_NAME = "param_type";
        public static final String COLUMN_NAME_STRING_ID = "string_id";
        
    }
    
    public static final class Params implements BaseColumns {
    	
        public static final String PATH_PARAMS = "params";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_PARAMS);
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.params";
        
        private Params() {}
    	
        public static final String TABLE_NAME = "param";
        public static final String COLUMN_NAME_PARAM_TYPE_ID = "param_type_id";
        public static final String COLUMN_NAME_PUZZLE_ID = "puzzle_id";
        public static final String COLUMN_NAME_VALUE = "value";
        
    }
    
    public static final class Sessions implements BaseColumns {
    	
        public static final String PATH_SESSION = "sessions";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_SESSION);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.sessions";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.sessions";
        
        private Sessions() {}
    	
        public static final String TABLE_NAME = "session";
        public static final String COLUMN_NAME_PROFILE_ID = "profile_id";
        public static final String COLUMN_NAME_PUZZLE_ID = "puzzle_id";
        public static final String COLUMN_NAME_POSITION_ID = "position_id";
        public static final String COLUMN_NAME_IS_CLOSED = "is_closed";
        public static final String COLUMN_NAME_IS_CURRENT = "is_current";
        public static final String COLUMN_NAME_START_DATE = "start_date";
        public static final String COLUMN_NAME_END_DATE = "end_date";
        public static final String ALIAS_NAME_SESSION_ID = "session_id";
        
    }
    
    public static final class EndPositions implements BaseColumns {
    	
        public static final String PATH_ENDS = "ends";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_ENDS);
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.ends";

        private EndPositions() {}
    	
        public static final String TABLE_NAME = "end_position";
        public static final String COLUMN_NAME_PUZZLE_ID = "puzzle_id";
        public static final String COLUMN_NAME_POSITION_ID = "position_id";
        
    }
    
    public static final class Tags implements BaseColumns {
    	
        private Tags() {}
    	
        public static final String TABLE_NAME = "tag";
        public static final String COLUMN_NAME_PUZZLE_ID = "puzzle_id";
        public static final String COLUMN_NAME_IX = "ix";
        
    }
    
    public static final class Items implements BaseColumns {
    	
        private Items() {}
    	
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_TAG_ID = "tag_id";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
        public static final String COLUMN_NAME_ITEM_X = "item_x";
        public static final String COLUMN_NAME_ITEM_Y = "item_y";
        public static final String COLUMN_NAME_IMG_ID = "img_id";
    }
    
    public static final class TagPositions implements BaseColumns {
    	
        public static final String PATH_TAG = "tags";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_TAG);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.tags";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.tags";
        
        private TagPositions() {}
    	
        public static final String TABLE_NAME = "tag_position";
        public static final String COLUMN_NAME_TAG_ID = "tag_id";
        public static final String COLUMN_NAME_POSITION_ID = "position_id";
        public static final String COLUMN_NAME_X = "x";
        public static final String COLUMN_NAME_Y = "y";
        
    }
    
    public static final class SolutionSteps implements BaseColumns {
    	
        public static final String PATH_REDO = "redo";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_REDO);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.redo";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.com.WhiteRabbit.tags.redo";
        
        private SolutionSteps() {}
    	
        public static final String TABLE_NAME = "solution_step";
        public static final String COLUMN_NAME_SESSION_ID = "session_id";
        public static final String COLUMN_NAME_ORD_NUM = "ord_num";
        public static final String COLUMN_NAME_DX = "dx";
        public static final String COLUMN_NAME_DY = "dy";
        
    }
    
    public static final class StepTag implements BaseColumns {
    	
        public static final String PATH_REDO_TAG = "redo_tag";
        public static final Uri CONTENT_URI =  Uri.parse(SCHEME + AUTHORITY + "/" + PATH_REDO_TAG);
        public static final String CONTENT_DIR_TYPE = "vnd.android.cursor.dir/vnd.com.WhiteRabbit.tags.redo_tag";
        
        private StepTag() {}
    	
        public static final String TABLE_NAME = "step_tag";
        public static final String COLUMN_NAME_SESSION_ID = "session_id";
        public static final String COLUMN_NAME_REDO_ID = "redo_id";
        public static final String COLUMN_NAME_TAG_ID = "tag_id";
        
    }
}
