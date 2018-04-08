/*
 * Copyright (C) 2007-2011 Geometer Plus <contact@geometerplus.com>
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

package org.geometerplus.fbreader.fbreader;

import java.util.ArrayList;
import java.util.List;

import org.geometerplus.fbreader.bookmodel.BookModel;
import org.geometerplus.fbreader.library.Author;
import org.geometerplus.fbreader.library.Book;
import org.geometerplus.fbreader.library.Bookmark;
import org.geometerplus.fbreader.library.Library;
import org.geometerplus.zlibrary.core.application.ZLApplication;
import org.geometerplus.zlibrary.core.application.ZLKeyBindings;
import org.geometerplus.zlibrary.core.filesystem.ZLFile;
import org.geometerplus.zlibrary.core.options.ZLBooleanOption;
import org.geometerplus.zlibrary.core.options.ZLColorOption;
import org.geometerplus.zlibrary.core.options.ZLEnumOption;
import org.geometerplus.zlibrary.core.options.ZLIntegerRangeOption;
import org.geometerplus.zlibrary.core.options.ZLStringOption;
import org.geometerplus.zlibrary.core.resources.ZLResource;
import org.geometerplus.zlibrary.core.util.ZLColor;
import org.geometerplus.zlibrary.text.hyphenation.ZLTextHyphenator;
import org.geometerplus.zlibrary.text.view.ZLTextWordCursor;

import com.nil.util.EpubUtil;
import com.nil.util.L;

public final class FBReaderApp extends ZLApplication {
	public final ZLBooleanOption AllowScreenBrightnessAdjustmentOption = new ZLBooleanOption(
			"LookNFeel", "AllowScreenBrightnessAdjustment", true);
	public final ZLStringOption TextSearchPatternOption = new ZLStringOption(
			"TextSearch", "Pattern", "");

	public final ZLBooleanOption UseSeparateBindingsOption = new ZLBooleanOption(
			"KeysOptions", "UseSeparateBindings", false);

	public final ZLBooleanOption EnableDoubleTapOption = new ZLBooleanOption(
			"Options", "EnableDoubleTap", false);
	public final ZLBooleanOption NavigateAllWordsOption = new ZLBooleanOption(
			"Options", "NavigateAllWords", false);

	public static enum WordTappingAction {
		doNothing, selectSingleWord, startSelecting, openDictionary
	}

	public final ZLEnumOption<WordTappingAction> WordTappingActionOption = new ZLEnumOption<WordTappingAction>(
			"Options", "WordTappingAction", WordTappingAction.startSelecting);

	public final ZLColorOption ImageViewBackgroundOption = new ZLColorOption(
			"Colors", "ImageViewBackground", new ZLColor(127, 127, 127));

	public static enum ImageTappingAction {
		doNothing, selectImage, openImageView
	}

	public final ZLEnumOption<ImageTappingAction> ImageTappingActionOption = new ZLEnumOption<ImageTappingAction>(
			"Options", "ImageTappingAction", ImageTappingAction.openImageView);

	public final ZLIntegerRangeOption LeftMarginOption = new ZLIntegerRangeOption(
			"Options", "LeftMargin", 0, 30, 4);
	public final ZLIntegerRangeOption RightMarginOption = new ZLIntegerRangeOption(
			"Options", "RightMargin", 0, 30, 4);
	public final ZLIntegerRangeOption TopMarginOption = new ZLIntegerRangeOption(
			"Options", "TopMargin", 0, 30, 0);
	public final ZLIntegerRangeOption BottomMarginOption = new ZLIntegerRangeOption(
			"Options", "BottomMargin", 0, 30, 4);

	public final ZLIntegerRangeOption ScrollbarTypeOption = new ZLIntegerRangeOption(
			"Options", "ScrollbarType", 0, 3, FBView.SCROLLBAR_SHOW_AS_FOOTER);
	public final ZLIntegerRangeOption FooterHeightOption = new ZLIntegerRangeOption(
			"Options", "FooterHeight", 8, 20, 9);
	public final ZLBooleanOption FooterShowTOCMarksOption = new ZLBooleanOption(
			"Options", "FooterShowTOCMarks", true);
	public final ZLBooleanOption FooterShowClockOption = new ZLBooleanOption(
			"Options", "ShowClockInFooter", true);
	public final ZLBooleanOption FooterShowBatteryOption = new ZLBooleanOption(
			"Options", "ShowBatteryInFooter", true);
	public final ZLBooleanOption FooterShowProgressOption = new ZLBooleanOption(
			"Options", "ShowProgressInFooter", true);
	public final ZLStringOption FooterFontOption = new ZLStringOption(
			"Options", "FooterFont", "Droid Sans");

	final ZLStringOption ColorProfileOption = new ZLStringOption("Options",
			"ColorProfile", ColorProfile.DAY);

	public final ZLBooleanOption ShowPreviousBookInCancelMenuOption = new ZLBooleanOption(
			"CancelMenu", "previousBook", false);
	public final ZLBooleanOption ShowPositionsInCancelMenuOption = new ZLBooleanOption(
			"CancelMenu", "positions", true);

	private final ZLKeyBindings myBindings = new ZLKeyBindings("Keys");

	public final FBView BookTextView;
	public final FBView FootnoteView;

	public BookModel Model;

	private final String myArg0;

	public FBReaderApp(String arg) {
		myArg0 = arg;

		addAction(ActionCode.INCREASE_FONT, new ChangeFontSizeAction(this, +2));// 字体加大
		addAction(ActionCode.DECREASE_FONT, new ChangeFontSizeAction(this, -2));// 字体缩小
		addAction(ActionCode.ROTATE, new RotateAction(this));// 翻转屏幕

		addAction(ActionCode.FIND_NEXT, new FindNextAction(this));// 查找下一个
		addAction(ActionCode.FIND_PREVIOUS, new FindPreviousAction(this));// 查找前一个
		addAction(ActionCode.CLEAR_FIND_RESULTS, new ClearFindResultsAction(
				this));// 清除查找结果

		addAction(ActionCode.SELECTION_CLEAR, new SelectionClearAction(this));

		addAction(ActionCode.TURN_PAGE_FORWARD, new TurnPageAction(this, true));
		addAction(ActionCode.TURN_PAGE_BACK, new TurnPageAction(this, false));

		addAction(ActionCode.VOLUME_KEY_SCROLL_FORWARD,
				new VolumeKeyTurnPageAction(this, true));
		addAction(ActionCode.VOLUME_KEY_SCROLL_BACK,
				new VolumeKeyTurnPageAction(this, false));

		addAction(ActionCode.SWITCH_TO_DAY_PROFILE, new SwitchProfileAction(
				this, ColorProfile.DAY));// 黑底白字
		addAction(ActionCode.SWITCH_TO_NIGHT_PROFILE, new SwitchProfileAction(
				this, ColorProfile.NIGHT));// 白底黑字

		addAction(ActionCode.EXIT, new ExitAction(this));

		BookTextView = new FBView(this);
		FootnoteView = new FBView(this);

		setView(BookTextView);
	}

	@Override
	public void initWindow() {
		super.initWindow();
		// L.startTime("openBook");
		wait("loadingBook", new Runnable() {
			public void run() {
				Book book = createBookForFile(ZLFile.createFileByPath(myArg0));
				if (book == null) {
					book = Library.getRecentBook();
				}
				if ((book == null) || !book.File.exists()) {
					book = Book.getByFile(Library.getHelpFile());
				}
				openBookInternal(book, null);// 打开电子书
			}
		});
		// L.endTime("openBook");
	}

	public void reloadBook() {
		if (Model != null && Model.Book != null) {
			Model.Book.reloadInfoFromDatabase();
			wait("loadingBook", new Runnable() {
				public void run() {
					openBookInternal(Model.Book, null);
				}
			});
		}
	}

	public void reloadEpubBook(final int fromKind) {
		wait("loadingBook", new Runnable() {
			public void run() {
				openEpubBookInternal(Model.Book, null,fromKind);
			}
		});
	}

	public void openBook(final Book book, final Bookmark bookmark) {
		if (book == null) {
			return;
		}
		if (Model != null) {
			if (bookmark == null
					& book.File.getPath().equals(Model.Book.File.getPath())) {
				return;
			}
		}

		// 在这里添加检测 epub的代码

		if (book.File.getExtension().equalsIgnoreCase("epub")) {
			L.l("=========77777777777777777777777==========");
			wait("loadingBook", new Runnable() {
				public void run() {
					openBookInternal(book, bookmark);
				}
			});

			// L.l("=========999999999999999999==========");
			// wait("loadingBook", new Runnable() {
			// public void run() {
			// L.setEpubFlag(true);
			// openBookInternal(book, bookmark);
			// L.setEpubFlag(false);
			// }
			// });

		} else {
			wait("loadingBook", new Runnable() {
				public void run() {
					openBookInternal(book, bookmark);
				}
			});
		}
	}

	private ColorProfile myColorProfile;

	public ColorProfile getColorProfile() {
		if (myColorProfile == null) {
			myColorProfile = ColorProfile.get(getColorProfileName());
		}
		return myColorProfile;
	}

	public String getColorProfileName() {
		return ColorProfileOption.getValue();
	}

	public void setColorProfileName(String name) {
		ColorProfileOption.setValue(name);
		myColorProfile = null;
	}

	public ZLKeyBindings keyBindings() {
		return myBindings;
	}

	public FBView getTextView() {
		return (FBView) getCurrentView();
	}

	public void tryOpenFootnote(String id) {
		if (Model != null) {
			BookModel.Label label = Model.getLabel(id);
			if (label != null) {
				addInvisibleBookmark();
				if (label.ModelId == null) {
					BookTextView.gotoPosition(label.ParagraphIndex, 0, 0);
				} else {
					FootnoteView
							.setModel(Model.getFootnoteModel(label.ModelId));
					setView(FootnoteView);
					FootnoteView.gotoPosition(label.ParagraphIndex, 0, 0);
				}
				getViewWidget().repaint();
			}
		}
	}

	public void clearTextCaches() {
		BookTextView.clearCaches();
		FootnoteView.clearCaches();
	}

	void openEpubBookInternal(Book book, Bookmark bookmark, int fromKind) {
		if (book.File.getExtension().equalsIgnoreCase("epub")) {
			L.setEpubFlag(true);

			int dirction = 0;
			boolean isBookFirst = false;
			// 只是一打开的时候，fromKind=0 ;否则就是从章节中过来的 ,fromKind=1
			if (fromKind == 0) {
				if (BookTextView.getStartCursor().getParagraphIndex() == 0) {

					int current_number = EpubUtil
							.getEpubUtil_PARAGRAPH_NUMBER();
					L.l("=========current_number:" + current_number);
					if (EpubUtil.getEpubUtil_PARAGRAPH_NUMBER() != 0) {
						dirction = 1;
						EpubUtil.setEpubUtil_PARAGRAPH_NUMBER(current_number - 1);
					} else if (EpubUtil.getEpubUtil_PARAGRAPH_NUMBER() == 0) {
						dirction = 1;
						isBookFirst = true;
						EpubUtil.setEpubUtil_PARAGRAPH_NUMBER(current_number + 1);
					} else {
						dirction = 1;
						EpubUtil.setEpubUtil_PARAGRAPH_NUMBER(current_number + 1);
					}

				} else {
					dirction = 2;
					int current_number = EpubUtil
							.getEpubUtil_PARAGRAPH_NUMBER();
					EpubUtil.setEpubUtil_PARAGRAPH_NUMBER(current_number + 1);
				}

				Model = BookModel.createModel(book);

				if (Model != null) {
					ZLTextHyphenator.Instance().load(book.getLanguage());
					BookTextView.setModel(Model.getTextModel());

					// 如果向右走，章节直接跑到开始，否则跑到此MODEL的末尾
					L.l("============d:	" + dirction);
					if (dirction == 2) {
						BookTextView.gotoPosition(null);
					} else if (dirction == 1) {
						if (isBookFirst) {
							BookTextView.gotoPosition(null);
						} else {
							BookTextView.gotoPage(Model.getTextModel()
									.getParagraphsNumber());
						}
					}
				}
			} else if (fromKind == 1)// 从章节过来的
			{
				Model = BookModel.createModel(book);
				if (Model != null) {
					ZLTextHyphenator.Instance().load(book.getLanguage());
					BookTextView.setModel(Model.getTextModel());
					BookTextView.gotoPosition(null);
				}
				
			}
			
			
			//书签部分
			if (bookmark == null) {
				setView(BookTextView);
			} else {
				gotoBookmark(bookmark);
			}
			Library.addBookToRecentList(book);
			final StringBuilder title = new StringBuilder(
					book.getTitle());
			if (!book.authors().isEmpty()) {
				boolean first = true;
				for (Author a : book.authors()) {
					title.append(first ? " (" : ", ");
					title.append(a.DisplayName);
					first = false;
				}
				title.append(")");
			}
			setTitle(title.toString());

			L.setEpubFlag(false);
		}
		getViewWidget().repaint();
	}

	/**
	 * 打开电子书
	 * 
	 * @param book
	 * @param bookmark
	 */
	void openBookInternal(final Book book, final Bookmark bookmark) {
		L.startTime("wenru");
		if (book != null) {
			onViewChanged();

			if (Model != null) {
				Model.Book.storePosition(BookTextView.getStartCursor());
			}
			BookTextView.setModel(null);
			FootnoteView.setModel(null);
			clearTextCaches();

			Model = null;

			// ///////////这里看成tempModel/////////////
			System.gc();
			System.gc();

			EpubUtil.setEpubUtil_CURRENT_BOOK(book);

			Model = BookModel.createModel(book);
			L.l("============2.0====:" + book.getLanguage() + "==== coding:"
					+ book.getEncoding());
			if (Model != null) {
				ZLTextHyphenator.Instance().load(book.getLanguage());
				BookTextView.setModel(Model.getTextModel());
				BookTextView.gotoPosition(book.getStoredPosition());
				if (bookmark == null) {
					setView(BookTextView);
				} else {
					gotoBookmark(bookmark);
				}
				Library.addBookToRecentList(book);
				final StringBuilder title = new StringBuilder(book.getTitle());
				if (!book.authors().isEmpty()) {
					boolean first = true;
					for (Author a : book.authors()) {
						title.append(first ? " (" : ", ");
						title.append(a.DisplayName);
						first = false;
					}
					title.append(")");
				}
				setTitle(title.toString());
			}
		}
		getViewWidget().repaint();
		L.endTime("wenru");

		// 如果是epub的话，则重新加载
		// if (book.File.getExtension().equalsIgnoreCase("epub")) {
		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// openEpubBookInternal(book,bookmark);
		// }
		// }).start();
		//
		// }

	}

	public void gotoBookmark(Bookmark bookmark) {
		addInvisibleBookmark();
		final String modelId = bookmark.ModelId;
		if (modelId == null) {
			BookTextView.gotoPosition(bookmark);
			setView(BookTextView);
		} else {
			FootnoteView.setModel(Model.getFootnoteModel(modelId));
			FootnoteView.gotoPosition(bookmark);
			setView(FootnoteView);
		}
		getViewWidget().repaint();
	}

	public void showBookTextView() {
		setView(BookTextView);
	}

	private Book createBookForFile(ZLFile file) {
		if (file == null) {
			return null;
		}
		Book book = Book.getByFile(file);
		if (book != null) {
			book.insertIntoBookList();
			return book;
		}
		if (file.isArchive()) {
			for (ZLFile child : file.children()) {
				book = Book.getByFile(child);
				if (book != null) {
					book.insertIntoBookList();
					return book;
				}
			}
		}
		return null;
	}

	@Override
	public void openFile(ZLFile file) {
		final Book book = createBookForFile(file);
		if (book != null) {
			openBook(book, null);
		}
	}

	public void onWindowClosing() {
		if (Model != null && BookTextView != null) {
			Model.Book.storePosition(BookTextView.getStartCursor());
		}
	}

	static enum CancelActionType {
		previousBook, returnTo, close
	}

	public static class CancelActionDescription {
		final CancelActionType Type;
		public final String Title;
		public final String Summary;

		CancelActionDescription(CancelActionType type, String summary) {
			final ZLResource resource = ZLResource.resource("cancelMenu");
			Type = type;
			Title = resource.getResource(type.toString()).getValue();
			Summary = summary;
		}
	}

	private static class BookmarkDescription extends CancelActionDescription {
		final Bookmark Bookmark;

		BookmarkDescription(Bookmark b) {
			super(CancelActionType.returnTo, b.getText());
			Bookmark = b;
		}
	}

	private final ArrayList<CancelActionDescription> myCancelActionsList = new ArrayList<CancelActionDescription>();

	public List<CancelActionDescription> getCancelActionsList() {
		myCancelActionsList.clear();
		if (ShowPreviousBookInCancelMenuOption.getValue()) {
			final Book previousBook = Library.getPreviousBook();
			if (previousBook != null) {
				myCancelActionsList
						.add(new CancelActionDescription(
								CancelActionType.previousBook, previousBook
										.getTitle()));
			}
		}
		if (ShowPositionsInCancelMenuOption.getValue()) {
			if (Model != null && Model.Book != null) {
				for (Bookmark bookmark : Bookmark
						.invisibleBookmarks(Model.Book)) {
					myCancelActionsList.add(new BookmarkDescription(bookmark));
				}
			}
		}
		myCancelActionsList.add(new CancelActionDescription(
				CancelActionType.close, null));
		return myCancelActionsList;
	}

	public void runCancelAction(int index) {
		if (index < 0 || index >= myCancelActionsList.size()) {
			return;
		}

		final CancelActionDescription description = myCancelActionsList
				.get(index);
		switch (description.Type) {
		case previousBook:
			openBook(Library.getPreviousBook(), null);
			break;
		case returnTo: {
			final Bookmark b = ((BookmarkDescription) description).Bookmark;
			b.delete();
			gotoBookmark(b);
			break;
		}
		case close:
			closeWindow();
			break;
		}
	}

	private void updateInvisibleBookmarksList(Bookmark b) {
		if (Model.Book != null && b != null) {
			for (Bookmark bm : Bookmark.invisibleBookmarks(Model.Book)) {
				if (b.equals(bm)) {
					bm.delete();
				}
			}
			b.save();
			final List<Bookmark> bookmarks = Bookmark
					.invisibleBookmarks(Model.Book);
			for (int i = 3; i < bookmarks.size(); ++i) {
				bookmarks.get(i).delete();
			}
		}
	}

	public void addInvisibleBookmark(ZLTextWordCursor cursor) {
		if (cursor != null && Model != null && Model.Book != null
				&& getTextView() == BookTextView) {
			updateInvisibleBookmarksList(new Bookmark(Model.Book, getTextView()
					.getModel().getId(), cursor, 6, false));
		}
	}

	public void addInvisibleBookmark() {
		if (Model.Book != null && getTextView() == BookTextView) {
			updateInvisibleBookmarksList(addBookmark(6, false));
		}
	}

	public Bookmark addBookmark(int maxLength, boolean visible) {
		final FBView view = getTextView();
		final ZLTextWordCursor cursor = view.getStartCursor();

		if (cursor.isNull()) {
			return null;
		}

		return new Bookmark(Model.Book, view.getModel().getId(), cursor,
				maxLength, visible);
	}
}
