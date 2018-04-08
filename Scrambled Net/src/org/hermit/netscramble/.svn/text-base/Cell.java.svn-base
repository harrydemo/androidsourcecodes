
/**
 * NetScramble: unscramble a network and connect all the terminals.
 * 
 * This is an Android implementation of the KDE game "knetwalk".
 * The player is given a network diagram with the parts of the network
 * randomly rotated; he/she must rotate them to connect all the terminals
 * to the server.
 * 
 * Original author:
 *   QNetwalk, Copyright (C) 2004, Andi Peredri <andi@ukr.net>
 *
 * Ported to kde by:
 *   Thomas Nagy <tnagyemail-mail@yahoo@fr>
 *
 * Cell-locking implemented by:
 *   Reinhold Kainhofer <reinhold@kainhofer.com>
 *
 * Ported to Android by:
 *   Ian Cameron Smith <johantheghost@yahoo.com>
 *
 *   This program is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License version 2
 *   as published by the Free Software Foundation (see COPYING).
 * 
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 */


package org.hermit.netscramble;


import java.util.EnumMap;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;


/**
 * This class implements a cell in the game board.  It handles the logic
 * and state of the cell, and as a subclass of View, it implements
 * the visible view of the cell.
 */
class Cell
	extends View
{

	// ******************************************************************** //
	// Public Types.
	// ******************************************************************** //
	
	/**
	 * Define the connected direction combinations.  The enum is
	 * carefully organised so that the ordinal() of each value is a
	 * bitmask representing the connected directions.  This allows us
	 * to manipulate directions more easily.
	 * 
	 * Each enum value also stores the ID of the bitmap representing
	 * it, or zero if none.  Note that this is the bitmap for
	 * the cabling layer, not the background or foreground (terminal etc).
	 */
	public enum Dir {
		FREE(0),						// Unconnected cell.
		___L(R.drawable.cable0001),
		__D_(R.drawable.cable0010),
		__DL(R.drawable.cable0011),
		_R__(R.drawable.cable0100),
		_R_L(R.drawable.cable0101),
		_RD_(R.drawable.cable0110),
		_RDL(R.drawable.cable0111),
		U___(R.drawable.cable1000),
		U__L(R.drawable.cable1001),
		U_D_(R.drawable.cable1010),
		U_DL(R.drawable.cable1011),
		UR__(R.drawable.cable1100),
		UR_L(R.drawable.cable1101),
		URD_(R.drawable.cable1110),
		URDL(R.drawable.cable1111),
		NONE(0);						// Not a cell.

		Dir(int img) {
			imageId = img;
		}
		
		public final int imageId;
	}


	// ******************************************************************** //
	// Constructor.
	// ******************************************************************** //

	/**
	 * Set up this cell.
	 * 
	 * @param	parent			Parent application context.
	 * @param	board			This cell's parent board.
	 * @param	x				This cell's x-position in the board grid.
	 * @param	y				This cell's y-position in the board grid.
	 */
	Cell(NetScramble parent, BoardView board, int x, int y) {
		super(parent);
		
		parentBoard = board;
		xindex = x;
		yindex = y;

		// Create the temp. objects used in drawing.
		cellBounds = new Rect(0, 0, 0, 0);
		cellWidth = 0;
		cellHeight = 0;
		cellPaint = new Paint();

		// Reset the cell's state.
		reset(Dir.NONE);
	}


	// ******************************************************************** //
	// Image Setup.
	// ******************************************************************** //

	/**
	 * Initialize the pixmaps used by the Cell class.
	 * 
	 * @param	res				Handle on the application resources.
	 */
	static void initPixmaps(Resources res) {
		// Load all the cable pixmaps.
		for (Dir d : Dir.values()) {
			if (d.imageId == 0)
				continue;
			
			// Load the pixmap for this cable configuration.
			Bitmap pixmap = BitmapFactory.decodeResource(res, d.imageId);
			cableNPixmaps.put(d, pixmap);

			// Create a greyed-out version of the image for the
			// disconnected version of the node.
			cableGPixmaps.put(d, greyOut(pixmap));
		}
		
		// Load the other pixmaps we use.
		for (Image i: Image.values()) {
			Bitmap pixmap = BitmapFactory.decodeResource(res, i.resid);
			otherPixmaps.put(i, pixmap);
		}
	}

	
	/**
	 * Create a greyed-out version of the given pixmap.
	 * 
	 * @param	pixmap			Base pixmap.
	 * @return					Greyed-out version of this pixmap.
	 */
	private static Bitmap greyOut(Bitmap pixmap) {
		// Get the pixel data from the pixmap.
		int w = pixmap.getWidth();
		int h = pixmap.getHeight();
		int[] pixels = new int[w * h];
		pixmap.getPixels(pixels, 0, w, 0, 0, w, h);

		// Grey-out the image in the pixel data.
		for (int y = 0; y < h; ++y) {
			for (int x = 0; x < w; ++x) {
				int pix = pixels[y * w + x];
				int r = (3 * Color.red(pix)) / 5 + 100;
				int g = (3 * Color.green(pix)) / 5 + 100;
				int b = (3 * Color.blue(pix)) / 5 + 100;
				pixels[y * w + x] = Color.argb(Color.alpha(pix), r, g, b);
			}
		}

		// Create and return a pixmap from the greyed-out pixel data.
		return Bitmap.createBitmap(pixels, w, h, pixmap.getConfig());
	}


	// ******************************************************************** //
	// Public Methods.
	// ******************************************************************** //

	/**
	 * Reset the state of this cell, and set the cell's isConnected directions
	 * to the given value.
	 * 
	 * @param	d			Connection directions to set for the cell.
	 */
	void reset(Dir d) {
		connectedDirs = d;
		isConnected = false;
		isFullyConnected = false;
		isRoot = false;
		isLocked = false;
		isBlind = false;
		currentAngle = 0;
		highlightPos = 0;
		haveFocus = false;

		invalidate();
	}

	   
    // ******************************************************************** //
	// Geometry.
	// ******************************************************************** //

    /**
     * This is called during layout when the size of this view has
     * changed.  This is where we first discover our window size, so set
     * our geometry to match.
     * 
     * @param	width			Current width of this view.
     * @param	height			Current height of this view.
     * @param	oldw			Old width of this view.  0 if we were
     * 							just added.
     * @param	oldh			Old height of this view.   0 if we were
     * 							just added.
     */
	@Override
	protected void onSizeChanged(int width, int height, int oldw, int oldh) {
    	super.onSizeChanged(width, height, oldw, oldh);
		cellBounds.left = 0;
		cellBounds.top = 0;
		cellBounds.right = width;
		cellBounds.bottom = height;
		cellWidth = width;
		cellHeight = height;
//		invalidate();
    }
    

	// ******************************************************************** //
	// Basic Cell Info.
	// ******************************************************************** //

	/**
	 * Get the x-position of this cell in the game board.
	 * 
	 * @return				The x-position of this cell in the game board.
	 */
	int x() {
		return xindex;
	}

	
	/**
	 * Get the y-position of this cell in the game board.
	 * 
	 * @return				The y-position of this cell in the game board.
	 */
	int y() {
		return yindex;
	}


	// ******************************************************************** //
	// Neighbouring Cell Tracking.
	// ******************************************************************** //

	/**
	 * Set the cell's neighbours in the game matrix.  A neighbour may be
	 * null if there is no neighbour in that direction.  If wrapping
	 * is enabled, the neighbour setup should reflect this.
	 * 
	 * This information can change between games, due to board size and
	 * wrapping changes.
	 * 
	 * @param	u			Neighbouring cell up from this one.
	 * @param	d			Neighbouring cell down from this one.
	 * @param	l			Neighbouring cell left from this one.
	 * @param	r			Neighbouring cell right from this one.
	 */
	void setNeighbours(Cell u, Cell d, Cell l, Cell r) {
		nextU = u;
		nextD = d;
		nextL = l;
		nextR = r;
	}


	/**
	 * Get the neighbouring cell in the given direction from this cell.
	 * 
	 * @param	d			The direction to look in.
	 * @return				The next cell in the given direction; may be
	 * 						null, or may actually be at the other edge of
	 * 						the board if wrapping is on.
	 */
	Cell next(Dir d) {
    	switch (d) {
    	case U___:
    		return nextU;
    	case _R__:
    		return nextR;
    	case __D_:
    		return nextD;
    	case ___L:
    		return nextL;
    	default:
    		throw new RuntimeException("Cell.next() called with bad dir");
    	}
	}

	
	// ******************************************************************** //
	// Connection State.
	// ******************************************************************** //

	/**
	 * Return the directions that this cell is connected to, outwards
	 * (ie. ignoring whether there is a matching inward connection
	 * in the next cell).
	 * 
	 * @return				The directions that this cell is connected to,
	 *						outwards.
	 */
	Dir dirs() {
		return connectedDirs;
	}


	/**
	 * Query whether this cell has a connection in the given direction(s).
	 *
	 * @param	d			Direction(s) to check.
	 * @return				true iff the cell is connected in all the
	 * 						given directions; else false.
	 */
	boolean hasConnection(Dir d) {
		return (connectedDirs.ordinal() & d.ordinal()) == d.ordinal();
	}
	
	
	/**
	 * Determine how many connections this cell has outwards (ie. ignoring
	 * whether there is a matching inward connection in the next cell).
	 * 
	 * @return				The number of outward connections from this cell.
	 */
	int numDirs() {
		if (connectedDirs == Dir.NONE)
			return 0;
		
		int bits = connectedDirs.ordinal();
		int n = 0;
		for (int i = 0; i < 4; ++i) {
			n += bits & 0x01;
			bits >>= 1;
		}
		return n;
	}


	/**
	 * Add the given direction as a direction this cell is connected to,
	 * outwards (ie. no attempt is made to make the reciprocal connection
	 * in the other cell).
	 * 
	 * @param	d			New connected direction to add for this cell.
	 */
	void addDir(Dir d) {
		int bits = connectedDirs.ordinal();
		if ((bits & d.ordinal()) == d.ordinal())
			return;
		
		bits |= d.ordinal();
		Dir[] dirs = Dir.values();
		setDirs(dirs[bits]);
	}


	/**
	 * Set the connected directions of this cell to the given value.
	 * 
	 * @param	d			New connected directions for this cell.
	 */
	void setDirs(Dir d) {
		if (d == connectedDirs)
			return;
		connectedDirs = d;
		invalidate();
	}


	// ******************************************************************** //
	// Rotation and Highlight State.
	// ******************************************************************** //

	/**
	 * Move the rotation currentAngle for this cell.  This changes the cell's
	 * idea of its connections, and with fractions of 90 degrees, is used to
	 * animate rotation of the cell's connections.  Setting this causes the
	 * cell's connections to be drawn at the given currentAngle; beyond +/-
	 * 45 degrees, the connections are changed in accordance with the
	 * direction the cell is now facing.
	 * 
	 * @param	a			Delta to add to the current rotation currentAngle
	 * 						for this cell, in degrees, clockwise positive.
	 * 						Should be in the range -360 to 360.
	 */
	void rotate(int a) {
		currentAngle += a;
		
		// If we've gone past 45 degrees, change the connected directions.
		// Rotate the directions bits (the bottom 4 bits of the ordinal)
		// right or left, as appropriate.
		int bits = connectedDirs.ordinal();	
		while (currentAngle >= 45) {
			currentAngle -= 90;
			bits = ((bits & 0x01) << 3) | ((bits & 0x0e) >> 1);
		}
		while (currentAngle < -45) {
			currentAngle += 90;
			bits = ((bits & 0x08) >> 3) | ((bits & 0x07) << 1);
		}

		// If we changed direction, update the current direction.
		// The new value's ordinal is in bits.  Otherwise, just
		// invalidate to show the rotated image.
		if (bits != connectedDirs.ordinal()) {
			Dir[] dirs = Dir.values();
			setDirs(dirs[bits]);
		} else
			invalidate();
	}


	/**
	 * Query whether this cell is currently rotated off the orthogonal.
	 * 
	 * @return				true iff the cell is not at its base angle.
	 */
	boolean isRotated() {
		return currentAngle != 0;
	}


	/**
	 * Set the highlight state of the cell.
	 * 
	 * @param	l			The offset of a diagonal highlight band to be
	 * 						drawn across the cell.  Valid range is
	 * 						zero to cellWidth * 2.
	 */
	void setLight(int l) {
		highlightPos = l;
		invalidate();
	}


	// ******************************************************************** //
	// Display Options.
	// ******************************************************************** //

	/**
	 * Set the "root" flag on this cell.  The root cell displays the server
	 * image.
	 * 
	 * @param	b			New "root" flag for this cell.
	 */
	void setRoot(boolean b) {
		if (isRoot == b)
			return;
		isRoot = b;
		invalidate();
	}


	/**
	 * Set this cell's "blind" flag.  A blind cell doesn't display its
	 * connections; it does display the server or terminal if appropriate.
	 * This is used to make the game harder.
	 * 
	 * @param	b			The new "blind" flag (true = blind).
	 */
	void setBlind(boolean b) {
		if (b != isBlind) {
			isBlind = b;
			invalidate();
		}
	}


	/**
	 * Determine whether this cell's "locked" flag is set.
	 * 
	 * @return				This cell's "locked" flag.
	 */
	boolean isLocked() {
		return isLocked;
	}


	/**
	 * Set the "locked" flag on this cell.  A locked cell is marked
	 * by a highlighted background.
	 * 
	 * @param	b			New "locked" flag for this cell.
	 */
	void setLocked(boolean newlocked) {
		if (isLocked == newlocked)
			return;
		isLocked = newlocked;
		invalidate();
	}


	/**
	 * Determine whether this cell's "connected" flag is set.
	 * 
	 * @return				This cell's "connected" flag.
	 */
	boolean isConnected() {
		return isConnected;
	}
	
	
	/**
	 * Set this cell's "connected" flag; this determines how the cell
	 * is displayed (cables are greyed-out if not connected).
	 * 
	 * @param	b			New "connected" flag for this cell.
	 */
	void setConnected(boolean b) {
		if (isConnected == b) return;
		isConnected = b;
		invalidate();
	}


	/**
	 * Set this cell's "fully connected" flag; this determines how the cell
	 * is displayed.  For the server, this is used to indicate victory.
	 * 
	 * @param	b			New "fully connected" flag for this cell.
	 */
	void setSolved(boolean b) {
		if (isFullyConnected == b) return;
		isFullyConnected = b;
		invalidate();
	}


    /**
     * Set whether this cell is focused or not.
     * 
     * This is part of our own focus system; we don't use the system
     * focus, as there's no way to make it wrap correctly in a dynamic
     * layout.  (focusSearch() isn't called.)
     */
	void setFocused(boolean focused) {
    	// We do our own focus highlight in onDraw(), so when the focus
    	// state changes, we have to redraw.
    	haveFocus = focused;
    	invalidate();
    }


	// ******************************************************************** //
	// Cell Drawing.
	// ******************************************************************** //
	
	/**
	 * This method is called to ask the cell to draw itself.
	 * 
	 * @param	canvas		Canvas to draw into.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background tile.
		{
			Image bgImage = Image.BG;
			if (connectedDirs == Dir.NONE)
				bgImage = Image.NOTHING;
			else if (connectedDirs == Dir.FREE)
				bgImage = Image.EMPTY;
			else if (isLocked)
				bgImage = Image.LOCKED;
			Bitmap bitmap = otherPixmaps.get(bgImage);
			canvas.drawBitmap(bitmap, null, cellBounds, cellPaint);
		}

		// Draw the highlight band, if active.
		if (highlightPos != 0) {
			cellPaint.setStyle(Paint.Style.STROKE);
			cellPaint.setStrokeWidth(5f);
			cellPaint.setColor(Color.WHITE);
			if (highlightPos < cellWidth)
				canvas.drawLine(0,
								highlightPos,
								highlightPos,
								cellBounds.top, cellPaint);
			else {
				int hp = highlightPos - cellWidth;
				canvas.drawLine(hp,
							    cellBounds.bottom,
							    cellBounds.right,
							    hp,
							    cellPaint);
			}
		}

		// If we're not empty, draw the cables / equipment.
		if (connectedDirs != Dir.FREE && connectedDirs != Dir.NONE) {
			if (!isBlind) {
				// We need to rotate the drawing matrix if the cable is
				// rotated.
				canvas.save();
				if (currentAngle != 0) {
					int midx = cellWidth / 2;
					int midy = cellHeight / 2;
					canvas.rotate(currentAngle, midx, midy);
				}

				// Draw the cable pixmap.
				Bitmap pixmap = isConnected ? cableNPixmaps.get(connectedDirs) :
					cableGPixmaps.get(connectedDirs);
				canvas.drawBitmap(pixmap, null, cellBounds, cellPaint);
				canvas.restore();
			}

			// Draw the equipment (terminal, server) if any.
			{
				Image equipImage = null;
				if (isRoot) {
					if (isFullyConnected)
						equipImage = Image.SERVER1;
					else
						equipImage = Image.SERVER;
				} else if (numDirs() == 1) {
					if (isConnected)
						equipImage = Image.COMP2;
					else
						equipImage = Image.COMP1;
				}
				if (equipImage != null) {
					Bitmap sysPix = otherPixmaps.get(equipImage);
					canvas.drawBitmap(sysPix, null, cellBounds, cellPaint);
				}
			}
		}
		
		// If this is the focused cell, indicate this by drawing a red
		// border around it.
		if (haveFocus) {
			cellPaint.setStyle(Paint.Style.STROKE);
			cellPaint.setColor(Color.RED);
			cellPaint.setStrokeWidth(0f);

			canvas.drawRect(0, 0, cellWidth - 1, cellHeight - 1, cellPaint);
		}
	}


	// ******************************************************************** //
	// Input Event Handling.
	// ******************************************************************** //
	
    /**
     * Handle MotionEvent events.
     * 
     * @param	event			The motion event.
     * @return					True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		parentBoard.cellTapped(this);
    		return true;
    	} else
    		return false;
    }


    // ******************************************************************** //
    // State Save/Restore.
    // ******************************************************************** //

    /**
     * Save the game state of this cell, as part of saving the
     * overall game state.
     * 
     * @return				A Bundle containing the saved state.
     */
    Bundle saveState() {
        Bundle map = new Bundle();

    	// Save the aspects of the state which aren't part of the board
        // configuration (that gets re-created on reload).
        map.putString("connectedDirs", connectedDirs.toString());
        map.putInt("currentAngle", currentAngle);
        map.putInt("highlightPos", highlightPos);
        map.putBoolean("isConnected", isConnected);
        map.putBoolean("isFullyConnected", isFullyConnected);
        map.putBoolean("isBlind", isBlind);
        map.putBoolean("isRoot", isRoot);
        map.putBoolean("isLocked", isLocked);
        
        // Note: we don't save the focus state; focus save and restore
        // is done in BoardView.
    	
        return map;
    }

    
    /**
     * Restore the game state of this cell from the given Bundle, as
     * part of restoring the overall game state.
     * 
     * @param	map			A Bundle containing the saved state.
     */
    void restoreState(Bundle map) {
    	connectedDirs = Dir.valueOf(map.getString("connectedDirs"));
    	currentAngle = map.getInt("currentAngle");
    	highlightPos = map.getInt("highlightPos");
    	isConnected = map.getBoolean("isConnected");
    	isFullyConnected = map.getBoolean("isFullyConnected");
    	isBlind = map.getBoolean("isBlind");
    	isRoot = map.getBoolean("isRoot");
    	isLocked = map.getBoolean("isLocked");
    	
    	// Phew!  Time for a redraw... but we'll invalidate() at the
    	// board level.
    }
    

    // ******************************************************************** //
    // Private Types.
    // ******************************************************************** //

	/**
	 * This enumeration defines the images, other than the cable images,
	 * which we use.
	 */
	private enum Image {
		NOTHING(R.drawable.nothing),
		EMPTY(R.drawable.empty),
		LOCKED(R.drawable.background_locked),
		BG(R.drawable.background),
		COMP1(R.drawable.computer1),
		COMP2(R.drawable.computer2),
		SERVER(R.drawable.server),
		SERVER1(R.drawable.server1);

		private Image(int r) { resid = r; }
		public int resid;
	}

	
    // ******************************************************************** //
    // Class Data.
    // ******************************************************************** //

    // Debugging tag.
	@SuppressWarnings("unused")
	private static final String TAG = "netscramble";
	
	// Pxmaps for the network elements: cables (normal), cables (greyed-
	// out), and other stuff (backgrounds, terminals, the server).
	private static final EnumMap<Dir, Bitmap> cableNPixmaps =
									new EnumMap<Dir, Bitmap>(Dir.class);
	private static final EnumMap<Dir, Bitmap> cableGPixmaps =
									new EnumMap<Dir, Bitmap>(Dir.class);
	private static final EnumMap<Image, Bitmap> otherPixmaps =
									new EnumMap<Image, Bitmap>(Image.class);

	
	// ******************************************************************** //
	// Private Data.
	// ******************************************************************** //

	// Parent board.
	private BoardView parentBoard;

	// The cell's position in the board.
	private final int xindex, yindex;
	
	// Our neighbouring cells up, down, left and right.  This changes
	// from game to game as each skill level has its own board size
	// and may or may not wrap.  null if there is no neighbour in that
	// direction.
	private Cell nextU;
	private Cell nextD;
	private Cell nextL;
	private Cell nextR;

	// True iff this cell has the focus.
	private boolean haveFocus;
	
	// The directions in which this cell is isConnected.  This is set up
	// at the start of each game.
	private Dir connectedDirs;

	// This cell's current currentAngle, in degrees; used during animations of
	// the cell rotation.
	private int currentAngle;

	// If non-0, the current offset of the highlight band across the cell.
	// This is used to draw a diagonal band of highlightPos flicking across the
	// cell, to highlight it when it is mis-clicked etc.
	private int highlightPos;

	// True iff the cell is currently isConnected (directly or not)
	// to the server.  This causes it to be displayed dark (not grey).
	private boolean isConnected;

	// True iff the cell is currently part of a fully connected network.
	// This causes it to be displayed differently.
	private boolean isFullyConnected;
	
	// True iff the cell is blind; ie. doesn't display its connections.
	// This is a difficulty factor.
	private boolean isBlind;
	
	// True iff this is the root cell of the network; ie. the server.
	private boolean isRoot;
	
	// True iff the cell has been isLocked by the user; this causes the
	// background to be highlighted.
	private boolean isLocked;

	// Cell's current bounds; used in onDraw().
	private Rect cellBounds;

	// Cell's current width; used in onDraw().
	private int cellWidth;

	// Cell's current height; used in onDraw().
	private int cellHeight;
	
	// Painter used in onDraw().
	private Paint cellPaint;
	
}

