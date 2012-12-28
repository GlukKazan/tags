package com.WhiteRabbit.tags;

//import java.util.Collection;

public interface TagsActivityCallbackInterface {
	String getLocalizedString(long id);
	void getEndPositions(ModelCallbackInterface callback, int id);
	void getParams(ModelCallbackInterface callback, int id);
	void getItems(ModelCallbackInterface callback, int id);
	void getPosition(ModelCallbackInterface callback, long id);
	void putTag(long positionId, int tagId, int x, int y);
//	void clearRedo();
//	void saveRedo(long step, Collection<Long> tags, int dx, int dy);
	void onSucceed();
}
