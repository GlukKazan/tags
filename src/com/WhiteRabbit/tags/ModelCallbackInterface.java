package com.WhiteRabbit.tags;

public interface ModelCallbackInterface {
	void setParam(int paramId, int value);
	void clearTags();
	void addItem(int tagId, int x, int y, boolean isMain, int tagIx, long type);
}
