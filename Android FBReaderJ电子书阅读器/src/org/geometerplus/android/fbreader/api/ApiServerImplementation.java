/*
 * Copyright (C) 2009-2012 Geometer Plus <contact@geometerplus.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

package org.geometerplus.android.fbreader.api;

import java.util.*;

import android.content.ContextWrapper;
import android.content.Intent;

import org.geometerplus.zlibrary.core.library.ZLibrary;
import org.geometerplus.zlibrary.core.config.ZLConfig;

import org.geometerplus.zlibrary.text.view.*;

import org.geometerplus.fbreader.fbreader.*;

public class ApiServerImplementation extends ApiInterface.Stub implements Api, ApiMethods {
	public static void sendEvent(ContextWrapper context, String eventType) {
		context.sendBroadcast(
			new Intent(ApiClientImplementation.ACTION_API_CALLBACK)
				.putExtra(ApiClientImplementation.EVENT_TYPE, eventType)
		);
	}

	private final FBReaderApp myReader = (FBReaderApp)FBReaderApp.Instance();

	private ApiObject.Error unsupportedMethodError(int method) {
		return new ApiObject.Error("Unsupported method code: " + method);
	}

	private ApiObject.Error exceptionInMethodError(int method, Throwable e) {
		return new ApiObject.Error("Exception in method " + method + ": " + e);
	}

	public ApiObject request(int method, ApiObject[] parameters) {
		try {
			switch (method) {
				case GET_FBREADER_VERSION:
					return ApiObject.envelope(getFBReaderVersion());
				case GET_OPTION_VALUE:
					return ApiObject.envelope(getOptionValue(
						((ApiObject.String)parameters[0]).Value,
						((ApiObject.String)parameters[1]).Value
					));
				case SET_OPTION_VALUE:
					setOptionValue(
						((ApiObject.String)parameters[0]).Value,
						((ApiObject.String)parameters[1]).Value,
						((ApiObject.String)parameters[2]).Value
					);
					return ApiObject.Void.Instance;
				case GET_BOOK_LANGUAGE:
					if (parameters.length == 0) {
						return ApiObject.envelope(getBookLanguage());
					} else {
						return ApiObject.envelope(getBookLanguage(((ApiObject.Long)parameters[0]).Value));
					}
				case GET_BOOK_TITLE:
					if (parameters.length == 0) {
						return ApiObject.envelope(getBookTitle());
					} else {
						return ApiObject.envelope(getBookTitle(((ApiObject.Long)parameters[0]).Value));
					}
				case GET_BOOK_FILE_PATH:
					if (parameters.length == 0) {
						return ApiObject.envelope(getBookFilePath());
					} else {
						return ApiObject.envelope(getBookFilePath(((ApiObject.Long)parameters[0]).Value));
					}
				case GET_BOOK_HASH:
					if (parameters.length == 0) {
						return ApiObject.envelope(getBookHash());
					} else {
						return ApiObject.envelope(getBookHash(((ApiObject.Long)parameters[0]).Value));
					}
				case GET_BOOK_UNIQUE_ID:
					if (parameters.length == 0) {
						return ApiObject.envelope(getBookUniqueId());
					} else {
						return ApiObject.envelope(getBookUniqueId(((ApiObject.Long)parameters[0]).Value));
					}
				case GET_BOOK_LAST_TURNING_TIME:
					if (parameters.length == 0) {
						return ApiObject.envelope(getBookLastTurningTime());
					} else {
						return ApiObject.envelope(getBookLastTurningTime(((ApiObject.Long)parameters[0]).Value));
					}
				case GET_PARAGRAPHS_NUMBER:
					return ApiObject.envelope(getParagraphsNumber());
				case GET_ELEMENTS_NUMBER:
					return ApiObject.envelope(getElementsNumber(
						((ApiObject.Integer)parameters[0]).Value
					));
				case GET_PARAGRAPH_TEXT:
					return ApiObject.envelope(getParagraphText(
						((ApiObject.Integer)parameters[0]).Value
					));
				case GET_PAGE_START:
					return getPageStart();
				case GET_PAGE_END:
					return getPageEnd();
				case IS_PAGE_END_OF_SECTION:
					return ApiObject.envelope(isPageEndOfSection());
				case IS_PAGE_END_OF_TEXT:
					return ApiObject.envelope(isPageEndOfText());
				case SET_PAGE_START:
					setPageStart((TextPosition)parameters[0]);
					return ApiObject.Void.Instance;
				case HIGHLIGHT_AREA:
				{
					highlightArea((TextPosition)parameters[0], (TextPosition)parameters[1]);
					return ApiObject.Void.Instance;
				}
				case CLEAR_HIGHLIGHTING:
					clearHighlighting();
					return ApiObject.Void.Instance;
				case GET_KEY_ACTION:
					return ApiObject.envelope(getKeyAction(
						((ApiObject.Integer)parameters[0]).Value,
						((ApiObject.Boolean)parameters[1]).Value
					));
				case SET_KEY_ACTION:
					setKeyAction(
						((ApiObject.Integer)parameters[0]).Value,
						((ApiObject.Boolean)parameters[1]).Value,
						((ApiObject.String)parameters[2]).Value
					);
					return ApiObject.Void.Instance;
				case GET_ZONEMAP:
				    return ApiObject.envelope(getZoneMap());
				case SET_ZONEMAP:
				    setZoneMap(((ApiObject.String)parameters[0]).Value);
					return ApiObject.Void.Instance;
				case GET_ZONEMAP_HEIGHT:
					return ApiObject.envelope(getZoneMapHeight(((ApiObject.String)parameters[0]).Value));
				case GET_ZONEMAP_WIDTH:
					return ApiObject.envelope(getZoneMapWidth(((ApiObject.String)parameters[0]).Value));
				case GET_TAPZONE_ACTION:
					return ApiObject.envelope(getTapZoneAction(
						((ApiObject.String)parameters[0]).Value,
						((ApiObject.Integer)parameters[1]).Value,
						((ApiObject.Integer)parameters[2]).Value,
						((ApiObject.Boolean)parameters[3]).Value
					));
				case SET_TAPZONE_ACTION:
					setTapZoneAction(
						((ApiObject.String)parameters[0]).Value,
						((ApiObject.Integer)parameters[1]).Value,
						((ApiObject.Integer)parameters[2]).Value,
						((ApiObject.Boolean)parameters[3]).Value,
						((ApiObject.String)parameters[4]).Value
					);
					return ApiObject.Void.Instance;
				case CREATE_ZONEMAP:
					createZoneMap(
						((ApiObject.String)parameters[0]).Value,
						((ApiObject.Integer)parameters[1]).Value,
						((ApiObject.Integer)parameters[2]).Value
					);
					return ApiObject.Void.Instance;
				case IS_ZONEMAP_CUSTOM:
					return ApiObject.envelope(isZoneMapCustom(
						((ApiObject.String)parameters[0]).Value
					));
				case DELETE_ZONEMAP:
					deleteZoneMap(((ApiObject.String)parameters[0]).Value);
					return ApiObject.Void.Instance;
				default:
					return unsupportedMethodError(method);
			}
		} catch (Throwable e) {
			return new ApiObject.Error("Exception in method " + method + ": " + e);
		}
	}

	public List<ApiObject> requestList(int method, ApiObject[] parameters) {
		try {
			switch (method) {
				case LIST_OPTION_GROUPS:
					return ApiObject.envelope(getOptionGroups());
				case LIST_OPTION_NAMES:
					return ApiObject.envelope(getOptionNames(
						((ApiObject.String)parameters[0]).Value
					));
				case LIST_BOOK_TAGS:
					return ApiObject.envelope(getBookTags());
				case LIST_ACTIONS:
					return ApiObject.envelope(listActions());
				case LIST_ACTION_NAMES:
				{
					final ArrayList<String> actions = new ArrayList<String>(parameters.length);
					for (ApiObject o : parameters) {
					  	actions.add(((ApiObject.String)o).Value);
					}
					return ApiObject.envelope(listActionNames(actions));
				}
				case LIST_ZONEMAPS:
					return ApiObject.envelope(listZoneMaps());
				default:
					return Collections.<ApiObject>singletonList(unsupportedMethodError(method));
			}
		} catch (Throwable e) {
			return Collections.<ApiObject>singletonList(exceptionInMethodError(method, e));
		}
	}

	private Map<ApiObject,ApiObject> errorMap(ApiObject.Error error) {
		return Collections.<ApiObject,ApiObject>singletonMap(error, error);
	}

	public Map<ApiObject,ApiObject> requestMap(int method, ApiObject[] parameters) {
		try {
			switch (method) {
				default:
					return errorMap(unsupportedMethodError(method));
			}
		} catch (Throwable e) {
			return errorMap(exceptionInMethodError(method, e));
		}
	}

	// information about fbreader
	public String getFBReaderVersion() {
		return ZLibrary.Instance().getVersionName();
	}

	// preferences information
	public List<String> getOptionGroups() {
		return ZLConfig.Instance().listGroups();
	}

	public List<String> getOptionNames(String group) {
		return ZLConfig.Instance().listNames(group);
	}

	public String getOptionValue(String group, String name) {
		return ZLConfig.Instance().getValue(group, name, null);
	}

	public void setOptionValue(String group, String name, String value) {
		// TODO: implement
	}

	public String getBookLanguage() {
		return myReader.Model.Book.getLanguage();
	}

	public String getBookTitle() {
		return myReader.Model.Book.getTitle();
	}

	public List<String> getBookTags() {
		// TODO: implement
		return Collections.emptyList();
	}

	public String getBookFilePath() {
		return myReader.Model.Book.File.getPath();
	}

	public String getBookHash() {
		return myReader.Model.Book.getContentHashCode();
	}

	public String getBookUniqueId() {
		// TODO: implement
		return null;
	}

	public Date getBookLastTurningTime() {
		// TODO: implement
		return null;
	}

	public String getBookLanguage(long id) {
		// TODO: implement
		return null;
	}

	public String getBookTitle(long id) {
		// TODO: implement
		return null;
	}

	public List<String> getBookTags(long id) {
		// TODO: implement
		return Collections.emptyList();
	}

	public String getBookFilePath(long id) {
		// TODO: implement
		return null;
	}

	public String getBookHash(long id) {
		// TODO: implement
		return null;
	}

	public String getBookUniqueId(long id) {
		// TODO: implement
		return null;
	}

	public Date getBookLastTurningTime(long id) {
		// TODO: implement
		return null;
	}

	// page information
	public TextPosition getPageStart() {
		return getTextPosition(myReader.getTextView().getStartCursor());
	}

	public TextPosition getPageEnd() {
		return getTextPosition(myReader.getTextView().getEndCursor());
	}

	public boolean isPageEndOfSection() {
		final ZLTextWordCursor cursor = myReader.getTextView().getEndCursor();
		return cursor.isEndOfParagraph() && cursor.getParagraphCursor().isEndOfSection();
	}

	public boolean isPageEndOfText() {
		final ZLTextWordCursor cursor = myReader.getTextView().getEndCursor();
		return cursor.isEndOfParagraph() && cursor.getParagraphCursor().isLast();
	}

	private TextPosition getTextPosition(ZLTextWordCursor cursor) {
		return new TextPosition(
			cursor.getParagraphIndex(),
			cursor.getElementIndex(),
			cursor.getCharIndex()
		);
	}

	private ZLTextFixedPosition getZLTextPosition(TextPosition position) {
		return new ZLTextFixedPosition(
			position.ParagraphIndex,
			position.ElementIndex,
			position.CharIndex
		);
	}

	// manage view
	public void setPageStart(TextPosition position) {
		myReader.getTextView().gotoPosition(position.ParagraphIndex, position.ElementIndex, position.CharIndex);
		myReader.getViewWidget().repaint();
	}

	public void highlightArea(TextPosition start, TextPosition end) {
		myReader.getTextView().highlight(
			getZLTextPosition(start),
			getZLTextPosition(end)
		);
	}

	public void clearHighlighting() {
		myReader.getTextView().clearHighlighting();
	}

	public int getParagraphsNumber() {
		return myReader.Model.getTextModel().getParagraphsNumber();
	}

	public int getElementsNumber(int paragraphIndex) {
		final ZLTextWordCursor cursor = new ZLTextWordCursor(myReader.getTextView().getStartCursor());
		cursor.moveToParagraph(paragraphIndex);
		cursor.moveToParagraphEnd();
		return cursor.getElementIndex();
	}

	public String getParagraphText(int paragraphIndex) {
		final StringBuffer sb = new StringBuffer();
		final ZLTextWordCursor cursor = new ZLTextWordCursor(myReader.getTextView().getStartCursor());
		cursor.moveToParagraph(paragraphIndex);
		cursor.moveToParagraphStart();
		while (!cursor.isEndOfParagraph()) {
			ZLTextElement element = cursor.getElement();
			if (element instanceof ZLTextWord) {
				sb.append(element.toString() + " ");
			}
			cursor.nextWord();
		}
		return sb.toString();
	}

	// action control
	public List<String> listActions() {
		// TODO: implement
		return Collections.emptyList();
	}

	public List<String> listActionNames(List<String> actions) {
		// TODO: implement
		return Collections.emptyList();
	}

	public String getKeyAction(int key, boolean longPress) {
		// TODO: implement
		return null;
	}

	public void setKeyAction(int key, boolean longPress, String action) {
		// TODO: implement
	}

	public List<String> listZoneMaps() {
		return TapZoneMap.zoneMapNames();
	}

	public String getZoneMap() {
	  	return ScrollingPreferences.Instance().TapZoneMapOption.getValue();
	}

	public void setZoneMap(String name) {
	  	ScrollingPreferences.Instance().TapZoneMapOption.setValue(name);
	}

	public int getZoneMapHeight(String name) {
		return TapZoneMap.zoneMap(name).getHeight();
	}

	public int getZoneMapWidth(String name) {
		return TapZoneMap.zoneMap(name).getWidth();
	}

	public void createZoneMap(String name, int width, int height) {
		TapZoneMap.createZoneMap(name, width, height);
	}

	public boolean isZoneMapCustom(String name) throws ApiException {
		return TapZoneMap.zoneMap(name).isCustom();
	}

	public void deleteZoneMap(String name) throws ApiException {
		TapZoneMap.deleteZoneMap(name);
	}

	public String getTapZoneAction(String name, int h, int v, boolean singleTap) {
		return TapZoneMap.zoneMap(name).getActionByZone(
			h, v, singleTap ? TapZoneMap.Tap.singleNotDoubleTap : TapZoneMap.Tap.doubleTap
		);
	}

	public void setTapZoneAction(String name, int h, int v, boolean singleTap, String action) {
		TapZoneMap.zoneMap(name).setActionForZone(h, v, singleTap, action);
	}
}
