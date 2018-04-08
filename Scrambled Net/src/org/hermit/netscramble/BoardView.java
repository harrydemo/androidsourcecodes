
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
import java.util.Random;
import java.util.Vector;

import net.goui.util.MTRandom;

import org.hermit.netscramble.NetScramble.Sound;
import org.hermit.netscramble.NetScramble.State;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.RelativeLayout;


/**
 * This implements the game board by laying out a grid of Cell objects.
 * 
 * Unlike the original knetwalk, we have to deal with a physical display
 * whose size can vary dramatically, but which we would like to fill;
 * on the other hand, it may be too small for a "full-sized" game,
 * particularly bearing in mind the minimum size a cell can be and still
 * allow a finger to select it.  We therefore put a lot of work into
 * figuring out how big the board should be.
 */
public class BoardView
	extends RelativeLayout
{

	// ******************************************************************** //
	// Configuration Constants.
	// ******************************************************************** //
	
	/**
	 * Maximum major dimension of the board.  The "long" dimension of the
	 * board can't be more than this.  This must match up with the dimensions
	 * given in enum Skill below.
	 */
	private static final int BOARD_MAJOR = 9;
	
	/**
	 * Maximum minor dimension of the board.  The "short" dimension of the
	 * board can't be more than this.  This must match up with the dimensions
	 * given in enum Skill below.
	 */
	private static final int BOARD_MINOR = 6;

	/**
	 * The minimum cell size in pixels.
	 */
	private static final int CELL_MIN = 28;

	/**
	 * The maximum cell size in pixels.
	 */
	private static final int CELL_MAX = 64;

	
	// ******************************************************************** //
	// Public Types.
	// ******************************************************************** //

	/**
	 * Enumeration defining a game skill level.  Each enum member also
	 * stores the configuration parameters for that skill level.
	 * 
	 * Traditional knetwalk has these board sizes:
	 *     Novice:  5x5  = 25 tiles = 31%
	 *     Normal:  7x7  = 49 tiles = 60%
	 *     Expert:  9x9  = 81 tiles = 100%
	 *     Master:  9x9  = 81 tiles = 100% wrapped
	 *     
	 * We have to deal with a small screen, and we want the cells to be big
	 * enough to touch.  So, we set the board sizes as shown below.  We also
	 * introduce the wrinkle of blind tiles, to make an insane level:
	 *     Novice:  4x5
	 *     Normal:  6x5
	 *     Expert:  6x9
	 *     Master:  6x9 wrapped
	 *     Insane:  6x9 wrapped blind
	 */
	public enum Skill {
		//     Name                   id                 dims  brch  wrap blind
		NOVICE(R.string.skill_novice, R.id.skill_novice, 4, 5,    2, false,  9),
		NORMAL(R.string.skill_normal, R.id.skill_normal, 6, 5,    2, false,  9),
		EXPERT(R.string.skill_expert, R.id.skill_expert, 6, 9,    2, false,  9),
		MASTER(R.string.skill_master, R.id.skill_master, 6, 9,    3, true,   9),
		INSANE(R.string.skill_insane, R.id.skill_insane, 6, 9,    3, true,   3);

		private Skill(int lab, int i, int mn, int mj, int br, boolean w, int bd) {
			label = lab;
			id = i;
			minDim = mn;
			majDim = mj;
			branches = br;
			wrapped = w;
			blind = bd;
		}

		public int label;	  	  // Res. ID of the label for this skill level.
		public int id;	  	  	  // Numeric ID for this skill level.
		public int minDim;        // Shorter dimension of the playable board.
		public int majDim;        // Longer dimension of the playable board.
		public int branches;      // Max branches off each square; at least 2.
		public boolean wrapped;   // If true, network wraps around the edges.
		public int blind;         // Squares with this many or more connections
		                          // are blind.
	}


	// ******************************************************************** //
	// Constructor.
	// ******************************************************************** //
  	
	/**
	 * Construct a board view.
	 * 
	 * @param	parent			The application context we're running in.
	 */
    public BoardView(NetScramble parent) {
        super(parent);
        parentApp = parent;
        
        // Find out the device's screen dimensions and calculate the
        // size and shape of the cell matrix.
    	findMatrix(BOARD_MAJOR, BOARD_MINOR);
        
        // Create all the cells in the calculated board.  In onSizeChanged()
    	// we will take care of positioning them.  Set the cell grid and root
    	// so we have a valid state to save.
        Log.i(TAG, "Create board " + gridWidth + "x" + gridHeight);
        cellMatrix = new Cell[gridWidth][gridHeight];
        for (int y = 0; y < gridHeight; ++y) {
        	for (int x = 0; x < gridWidth; ++x) {
            	Cell cell = new Cell(parent, this, x, y);
            	cell.setId(y * gridWidth + x + 1);
                cellMatrix[x][y] = cell;
            	addView(cell);
            }
        }
        rootCell = cellMatrix[0][0];

        // Create the animation objects used to animate moves.
        cellRotator = new CellRotation();
        cellBlinker = new CellBlink();
        
        // Create the connected flags and connecting cell connectingCells
        // (used in updateConnections()).
        isConnected = new boolean[gridWidth][gridHeight];
        connectingCells = new Vector<Cell>();
       
		// Handle key events on the board.
		setFocusable(true);
        focusedCell = null;
        setFocus(rootCell);
    }


    /**
     * Find the size of board that can fit in the window.
     * 
     * @param	width			Width of the window, in pixels.
     * @param	height			Height of the window, in pixels.
     * @param	desiredCells			Number of cells we'd like to have.
     */
    private void findMatrix(int maj, int min) {
    	WindowManager wm =
    		(WindowManager) parentApp.getSystemService(Context.WINDOW_SERVICE);
    	Display disp = wm.getDefaultDisplay();
    	int width = disp.getWidth();
    	int height = disp.getHeight();
        Log.v(TAG, "findMatrix: screen=" + width + "x" + height);

        if (width > height) {
        	gridWidth = maj;
        	gridHeight = min;
        } else {
        	gridWidth = min;
        	gridHeight = maj;
        }
    }


    // ******************************************************************** //
	// Initialization.
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

    	// We usually get a zero-sized resize, which is useless;
    	// ignore it.
    	if (width < 1 || height < 1)
    		return;

        // Calculate the cell size which makes the board fit.  Make the cells
        // square.
    	cellWidth = width / gridWidth;
    	cellHeight = height / gridHeight;
    	if (cellWidth < cellHeight)
    		cellHeight = cellWidth;
    	else if (cellHeight < cellWidth)
    		cellWidth = cellHeight;

    	// See if we have a usable size.
    	if (cellWidth < CELL_MIN || cellHeight < CELL_MIN)
    		throw new RuntimeException("Screen size is not playable.");
    	if (cellWidth > CELL_MAX || cellHeight > CELL_MAX) {
        	cellWidth = CELL_MAX;
        	cellHeight = CELL_MAX;
    	}
    	
    	// Set up the board configuration.
        Log.i(TAG, "Layout board " + gridWidth + "x" + gridHeight + ", " +
        		"cells " + cellWidth + "x" + cellHeight);

    	// Center the board in the window.
    	int xpad = (width - gridWidth * cellWidth) / 2;
    	int ypad = (height - gridHeight * cellHeight) / 2;
    	setPadding(xpad, ypad, xpad, ypad);
    	
    	// Set the cell geometries and positions.
    	for (int x = 0; x < gridWidth; ++x) {
    		for (int y = 0; y < gridHeight; ++y) {
    			RelativeLayout.LayoutParams layout =
    				new RelativeLayout.LayoutParams(cellWidth, cellHeight);
    			if (x > 0)
    				layout.addRule(RelativeLayout.RIGHT_OF,
    						y * gridWidth + x);
    			else
    				layout.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
    			if (y > 0)
    				layout.addRule(RelativeLayout.BELOW,
    						(y - 1) * gridWidth + x + 1);
    			else
    				layout.addRule(RelativeLayout.ALIGN_PARENT_TOP);
    			cellMatrix[x][y].setLayoutParams(layout);
    		}
    	}
    	
    	requestFocus();
    }
    
    
	// ******************************************************************** //
	// Board Setup.
	// ******************************************************************** //

    /**
     * Set up the board for a new game.
     * 
     * @param	sk				Skill level for the game; set the board up
     * 							accordingly.
     */
    public void setupBoard(Skill sk) {
    	gameSkill = sk;
    	
        // Reset the board for this game.
        resetBoard(sk);

        // Require at least 85% of the cells active.
        int minCells = (int) (boardWidth * boardHeight * 0.85);

        // Loop doing board setup until we get a valid board.
        int tries = 0, cells = 0;
        for (tries = 0; cells < minCells && tries < 10; ++tries)
        	cells = createNet(sk);
        Log.i(TAG, "Created net in " + tries + " tries with " +
        						cells + " cells (min " + minCells + ")");

        // Jumble the board.  Also, if we're in blind mode, tell the
        // appropriate cells to go blind.
        for (int x = boardStartX; x < boardEndX; x++) {
            for (int y = boardStartY; y < boardEndY; y++) {
            	cellMatrix[x][y].rotate(rng.nextInt(4) * 90);
                if (cellMatrix[x][y].numDirs() >= sk.blind)
                	cellMatrix[x][y].setBlind(true);
            }
        }

        // Figure out the active connections.
        updateConnections();
    }


    /**
     * Reset the board for a given skill level.
     * 
     * @param	sk				Skill level for the game; set the board up
     * 							accordingly.
     */
    private void resetBoard(Skill sk) {
    	// Save the width and height of the playing board for this skill
    	// level, and the board placement within the overall cell grid.
    	if (gridWidth > gridHeight) {
    		boardWidth = sk.majDim;
    		boardHeight = sk.minDim;
    	} else {
    		boardWidth = sk.minDim;
    		boardHeight = sk.majDim;
    	}
        boardStartX = (gridWidth - boardWidth) / 2;
        boardEndX = boardStartX + boardWidth;
        boardStartY = (gridHeight - boardHeight) / 2;
        boardEndY = boardStartY + boardHeight;
        
    	// Reset the cells.  If we're wrapped, set the surrounding cells
    	// to None; else Free, to show that there's no wraparound.
        Log.i(TAG, "Reset board " + gridWidth + "x" + gridHeight);
        boolean wrap = gameSkill.wrapped;
    	Cell u, d, l, r;
    	for (int x = 0; x < gridWidth; x++) {
    		for (int y = 0; y < gridHeight; y++) {
    			cellMatrix[x][y].reset(wrap ? Cell.Dir.NONE : Cell.Dir.FREE);
    			
    			// Re-calculate who this cell's neighbours are.
    			u = d = l = r = null;
    			if (wrap || y > boardStartY)
    				u = cellMatrix[x][decr(y, boardStartY, boardEndY)];
    			if (wrap || y < boardEndY - 1)
    				d = cellMatrix[x][incr(y, boardStartY, boardEndY)];
    			if (wrap || x > boardStartX)
    				l = cellMatrix[decr(x, boardStartX, boardEndX)][y];
    			if (wrap || x < boardEndX - 1)
    				r = cellMatrix[incr(x, boardStartX, boardEndX)][y];
    			cellMatrix[x][y].setNeighbours(u, d, l, r);
    		}
    	}
    }
    
    
    /**
     * Create a network layout.  This function may be called multiple times
     * after resetBoard(), to get a network with enough cells.
     * 
     * @param	sk				Skill level for the game; create the network
     * 							accordingly.
     * @return					The number of cells used in the layout.
     */
    private int createNet(Skill sk) {
        Log.i(TAG, "Create net " + boardStartX + "-" + boardEndX + ", " +
        		boardStartY + "-" + boardEndY);
    	
    	// Reset the cells' directions, and reset the root cell.
        for (int x = boardStartX; x < boardEndX; x++) {
            for (int y = boardStartY; y < boardEndY; y++) {
            	cellMatrix[x][y].setDirs(Cell.Dir.FREE);
            	cellMatrix[x][y].setRoot(false);
            }
        }

        // Set the rootCell cell (the server) to a random cell.
        int rootX = rng.nextInt(boardWidth) + boardStartX;
        int rootY = rng.nextInt(boardHeight) + boardStartY;
        rootCell = cellMatrix[rootX][rootY];
        rootCell.setConnected(true);
        rootCell.setRoot(true);
        Log.i(TAG, "Root cell " + rootCell.x() + "," + rootCell.y() + " (" +
        		rootX + "," + rootY + ")");
        setFocus(rootCell);

        // Set up the connectingCells of cells awaiting connection.  Start
        // by adding the root cell.
        Vector<Cell> list = new Vector<Cell>();
        list.add(rootCell);
        if (rng.nextBoolean())
            addRandomDir(list);

        // Loop while there are still cells to be connected, connecting
        // them in random directions.
        while (!list.isEmpty()) {
            // Randomly do the first cell, or defer it and do the next one.
            // This prevents unduly long, straight branches.
            if (rng.nextBoolean()) {
            	// Add a random direction from this cell.
                addRandomDir(list);
                
                // 50% of the time, add a second direction, if we can
                // find one.
                if (rng.nextBoolean())
                    addRandomDir(list);

                // A third pass makes networks more complex, but also
                // introduces 4-way crosses.
                if (sk.branches >= 3 && rng.nextInt(3) == 0)
                    addRandomDir(list);
            } else
            	list.add(list.firstElement());
            
            // Pop the first element off the connectingCells.
            list.remove(0);
        }

        // Count the number of connected cells in this board.
        int cells = 0;
        for (int x = boardStartX; x < boardEndX; x++)
            for (int y = boardStartY; y < boardEndY; y++)
                if (cellMatrix[x][y].dirs() != Cell.Dir.FREE)
                    ++cells;
        
        Log.i(TAG, "Created net with " + cells + " cells");
        return cells;
    }
    
    
    /**
     * Add a connection in a random direction from the first cell
     * in the given cell connectingCells.  We enumerate the free adjacent
     * cells around the starting cell, then pick one to connect to at random.
     * If there is no free adjacent cell, we do nothing.
     * 
     * If we connect to a cell, it is added to the passed-in connectingCells.
     * 
     * @param	list			Current list of cells awaiting connection.
     */
    private void addRandomDir(Vector<Cell> list) {
    	// Start with the first cell in the cell connectingCells.
        Cell cell = list.firstElement();

        // List the adjacent cells which are free.
        EnumMap<Cell.Dir, Cell> freecells =
        						new EnumMap<Cell.Dir, Cell>(Cell.Dir.class);
        Cell ucell = cell.next(Cell.Dir.U___);
        if (ucell != null && ucell.dirs() == Cell.Dir.FREE)
            freecells.put(Cell.Dir.U___, ucell);
        
        Cell rcell = cell.next(Cell.Dir._R__);
        if (rcell != null && rcell.dirs() == Cell.Dir.FREE)
            freecells.put(Cell.Dir._R__, rcell);
        
        Cell dcell = cell.next(Cell.Dir.__D_);
        if (dcell != null && dcell.dirs() == Cell.Dir.FREE)
            freecells.put(Cell.Dir.__D_, dcell);
        
        Cell lcell = cell.next(Cell.Dir.___L);
        if (lcell != null && lcell.dirs() == Cell.Dir.FREE)
            freecells.put(Cell.Dir.___L, lcell);
        
        if (freecells.isEmpty()) {
        	Log.d(TAG, "addRandomDir: no free adjacents");
            return;
        }
     
        // Pick one of the free adjacents at random.
        Object[] keys = freecells.keySet().toArray();
        Cell.Dir key = (Cell.Dir) keys[rng.nextInt(keys.length)];
        Cell dest = freecells.get(key);
        
        // Make a link to that cell, and a corresponding link back.
        cell.addDir(key);
        dest.addDir(contrdirs.get(key));
        
        // Add the new cell to the outstanding connectingCells.
        list.add(dest);
    	Log.d(TAG, "addRandomDir: connected to " + dest.x() + "," + dest.y());
    }


	// ******************************************************************** //
	// Board Logic.
	// ******************************************************************** //

    /**
     * Scan the board to see which cells are connected to the server.
     * Update the state of every cell accordingly.  This function is
     * called each time a cell is rotated, to re-compute the connectedness
     * of every cell.
     * 
     * @return					true iff one or more cells have been
     *							connected that previously weren't.
     */
    private boolean updateConnections() {
    	// Reset the array of connected flags per cell.
        for (int x = 0; x < gridWidth; x++)
            for (int y = 0; y < gridHeight; y++)
                isConnected[x][y] = false;

        // Clear the list of cells which are connected but
        // haven't had their onward connections checked yet.
        connectingCells.clear();
        
        // If the root cell is rotated, then it's not connected to
        // anything -- no-one is connected.  Otherwise, flag the root
        // cell as connected and add it to the connectingCells.
        if (!rootCell.isRotated()) {
            isConnected[rootCell.x()][rootCell.y()] = true;
            connectingCells.add(rootCell);		
        }

        // While there are still cells to investigate, check them for
        // connections that we haven't flagged yet, and add those cells
        // to the connectingCells.
        while (!connectingCells.isEmpty()) {
            Cell cell = connectingCells.firstElement();

            if (hasNewConnection(cell, Cell.Dir.U___, isConnected))
                connectingCells.add(cell.next(Cell.Dir.U___));
            if (hasNewConnection(cell, Cell.Dir._R__, isConnected))
                connectingCells.add(cell.next(Cell.Dir._R__));
            if (hasNewConnection(cell, Cell.Dir.__D_, isConnected))
                connectingCells.add(cell.next(Cell.Dir.__D_));
            if (hasNewConnection(cell, Cell.Dir.___L, isConnected))
                connectingCells.add(cell.next(Cell.Dir.___L));
            connectingCells.remove(0);
        }

        // Finally, scan the connection flags.  Set every cell's connected
        // status accordingly.  Count connected cells, and cells that are
        // connected but weren't previously.
        int connections = 0;
        int newConnections = 0;
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
            	if (isConnected[x][y]) {
            		++connections;
            		if (!cellMatrix[x][y].isConnected())
            			++newConnections;
            	}
                cellMatrix[x][y].setConnected(isConnected[x][y]);
            }
        }
        
        Log.d(TAG, "updateConnections: " + connections +
        		   " connected (" + newConnections + " new)");

        // Tell the caller whether we got a new one.
        return newConnections != 0;
    }


    /**
     * Determine whether we have a connection from the given cell in the
     * given direction which hasn't already been logged in got[][].
     * 
     * @param	cell			Starting cell.
     * @param	dir				Direction to look in.
     * @param	got				Array of flags showing which cells we have
     * 							already found connections for.  If we find
     * 							a new connection, we will set the flag for it
     * 							in here.
     * @return					true iff we found a new connection in the
     * 							given direction.
     */
    private boolean hasNewConnection(Cell cell, Cell.Dir dir, boolean got[][]) {
    	// Find the cell we're going to, if any.
    	Cell other = cell.next(dir);
    	Cell.Dir otherdir = contrdirs.get(dir);

    	// If there's no cell there, then there's no connection.  If we
    	// have already marked it connected, we're done.
    	if (other == null || got[other.x()][other.y()])
    		return false;
    	
    	// See if there's an actual connection.  If either cell is rotated,
    	// there's no connection.
    	if (!cell.hasConnection(dir) || !other.hasConnection(otherdir) ||
    				cell.isRotated() || other.isRotated())
    		return false;

    	// OK, there's a connection, and it's new.  Mark it.
    	got[other.x()][other.y()] = true;
    	return true;
    }
    

    /**
     * Determine whether the board is currently in a solved state -- i.e.
     * all terminals are connected to the server.
     * 
     * Note that in some layouts (particularly in Expert mode), it is
     * possible to connect all the terminals without using all the cable
     * sections.  Since the game intro asks the user to connect all the
     * terminals, which makes sense, we look for unconnected terminals
     * specifically.
     * 
     * NOTE: We assume that updateConnections() has been called to
     * set the connected states of all cells correctly for the current
     * board state.
     * 
     * @return				true iff the board is currently in a solved
     * 						state -- ie. every terminal cell is connected
     * 						to the server.
     */
    boolean isSolved() {
    	// Scan the board; any non-connected non-empty cell means
    	// we're not done yet.
    	for (int x = boardStartX; x < boardEndX; x++) {
    		for (int y = boardStartY; y < boardEndY; y++) {
    			Cell cell = cellMatrix[x][y];
    			
    			// If there's an unconnected terminal, we're not solved.
    			if (cell.numDirs() == 1 && !cell.isConnected())
    				return false;
    		}
    	}
    	
    	return true;
    }


    /**
     * Count the number of unconnected cells in the board.
     * 
     * Note that in some layouts (particularly in Expert mode), it is
     * possible to connect all the terminals without using all the cable
     * sections, so the answer may be non-0 on a solved board.
     * 
     * NOTE: We assume that updateConnections() has been called to
     * set the connected states of all cells correctly for the current
     * board state.
     * 
     * @return				The number of unconnected cells in the board.
     */
    int unconnectedCells() {
    	int unused = 0;
    	
    	for (int x = boardStartX; x < boardEndX; x++) {
    		for (int y = boardStartY; y < boardEndY; y++) {
    			Cell cell = cellMatrix[x][y];
    			if (cell.dirs() != Cell.Dir.FREE && !cell.isConnected())
    				++unused;
    		}
    	}
    	
    	return unused;
    }


	// ******************************************************************** //
	// Input Handling.
	// ******************************************************************** //

    /**
	 * Handle key input.
	 * 
     * @param	keyCode			The key code.
     * @param	event			The KeyEvent object that defines the
     * 							button action.
     * @return					True if the event was handled, false otherwise.
	 */
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// Don't allow a move while we're doing the previous one.
    	if (cellBlinker.isRunning() || cellRotator.isRunning())
    		return false;
    	
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_CENTER:
		case KeyEvent.KEYCODE_ENTER:
			cellRotate(focusedCell, 1);
			return true;
		case KeyEvent.KEYCODE_Z:
		case KeyEvent.KEYCODE_N:
		case KeyEvent.KEYCODE_4:
			cellRotate(focusedCell, -1);
			return true;
		case KeyEvent.KEYCODE_X:
		case KeyEvent.KEYCODE_M:
		case KeyEvent.KEYCODE_6:
			cellRotate(focusedCell, 1);
			return true;
		case KeyEvent.KEYCODE_SPACE:
		case KeyEvent.KEYCODE_0:
			cellToggleLock(focusedCell);
			return true;
		case KeyEvent.KEYCODE_P:
		case KeyEvent.KEYCODE_9:
			pauseGame();
			return true;
			
		case KeyEvent.KEYCODE_DPAD_UP:
			moveFocus(Cell.Dir.U___, 0, -1);
			return true;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			moveFocus(Cell.Dir._R__, 1, 0);
			return true;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			moveFocus(Cell.Dir.__D_, 0, 1);
			return true;
 		case KeyEvent.KEYCODE_DPAD_LEFT:
 			moveFocus(Cell.Dir.___L, -1, 0);
			return true;
		}
		
		return false;
	}
	

    /**
     * Handle trackball motion events.
     * 
     * @param	event			The motion event.
     * @return					True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTrackballEvent(MotionEvent event) {
    	// Actually, just let these come through as D-pad events.
    	return false;
    }
    

    /**
     * The given cell has been tapped by the user.
     * 
     * @param	cell			The cell.
     */
	void cellTapped(Cell cell) {
    	// Don't allow a move while we're doing the previous one.
    	if (cellBlinker.isRunning() || cellRotator.isRunning())
    		return;
    	
		setFocus(cell);
		cellRotate(cell, 1);
	}

	
	/**
	 * Move the cell focus in the given direction.
	 * 
	 * Note that this moves the variable focusedCell; we do our own focus,
	 * rather than using system focus, as there seems to be no way to
	 * make that wrap on a dynamic layout.
	 * 
	 * @param	dir			The direction to move in.
	 * @param	dx			X-delta of that direction (for convenience).
	 * @param	dy			Y-delta of that direction (for convenience).
	 */
	private void moveFocus(Cell.Dir dir, int dx, int dy) {
		if (focusedCell == null)
			return;
		
		// Try using the cell's idea of it's neighbour.
		Cell goCell = focusedCell.next(dir);
		
		// Otherwise wrap around the board.
		if (goCell == null) {
			int nx = (focusedCell.x() + dx + gridWidth) % gridWidth;
			int ny = (focusedCell.y() + dy + gridHeight) % gridHeight;
			goCell = cellMatrix[nx][ny];
		}
			
		setFocus(goCell);
	}
	
	
	// ******************************************************************** //
	// Cell Actions.
	// ******************************************************************** //

	/**
	 * Set the focused cell to the given cell.
     *
	 * Note that this moves the variable focusedCell; we do our own focus,
	 * rather than using system focus, as there seems to be no way to
	 * make that wrap on a dynamic layout.  (focusSearchInDescendants
	 * doesn't get called.)
	 *
	 * @param	cell			The cell; null to clear focus.
	 */
	private void setFocus(Cell cell) {
		if (focusedCell != null)
			focusedCell.setFocused(false);	
		focusedCell = cell;
		if (focusedCell != null)
			focusedCell.setFocused(true);	
	}
	
	
    /**
     * The given cell has been told to rotate.
     * 
	 * @param	cell			The cell.
	 * @param	dirn			Direction: -1 for left, 1 for right.
     */
	void cellRotate(Cell cell, int dirn) {
		// Check for board consistency.
		int x = cell.x();
		int y = cell.y();
		if (cell != cellMatrix[x][y])
			throw new RuntimeException("Cell identity mismatch");
		
		Log.i(TAG, "Cell clicked: " + x + "," + y);
		
		// See if the cell is empty or locked; give the user some negative
		// feedback if so.
        Cell.Dir d = cell.dirs();
        if (d == Cell.Dir.FREE || d == Cell.Dir.NONE || cell.isLocked()) {
            parentApp.makeSound(Sound.CLICK);
            blink(cell);
            return;
        }

        // Give the user a click.  Set up an animation to do the rotation.
        parentApp.makeSound(Sound.TURN);
        cellRotator.start(cell, dirn);
	}
	
	
	/**
	 * Toggle the locked state of the given cell.
	 * 
	 * @param	cell			The cell to toggle.
	 */
	void cellToggleLock(Cell cell) {
		cell.setLocked(!cell.isLocked());
	}


	/**
	 * Pause the game.
	 */
	void pauseGame() {
		parentApp.setState(State.PAUSED);
	}


    /**
     * Blink the given cell, to indicate a mis-click etc.
     * 
     * @param	cell			The cell to blink.
     */
    private void blink(Cell cell) {
        cellBlinker.start(cell);
    }


	/**
	 * Set the board to display the game as solved.
	 */
	void setSolved() {
		// Display the fully-connected version of the server.
		rootCell.setSolved(true);
	}


	// ******************************************************************** //
	// Animation.
	// ******************************************************************** //

    // This animation rotates a cell 90 degrees.
    private final class CellRotation extends Timer {
    	CellRotation() {
    		// Tick every 20 ms.
    		super(20);
    	}
    	
    	public void start(Cell cell, int dirn) {
    		// Don't touch the parameters if we're running.  This prevents
    		// more than one animation at once, which is what we want
    		// as we're only going to allocate one of these objects.
    		if (isRunning())
    			return;
    		
    		// Reset our state (including invocation counter).
    		reset();
    		this.cell = cell;
    		this.angle = 0;
    		
    		// Rotate 6 degrees at a time, positive or negative.
    		this.delta = 6 * dirn;
    		
    		super.start();
    	}
    	
    	@Override
		protected boolean step(int count, long time) {
            cell.rotate(delta);
            angle += delta;
            
            // The first time, update the connections to show the cell
            // disconnected.
            if (count == 0)
            	updateConnections();
            return Math.abs(angle) >= 90;
        }
        
    	@Override
		protected void done() {
    		if (cell.isRotated())
    			throw new RuntimeException("Cell rotate finished wrong");
    		
    		// Update the connections now the cell is in its new state.
        	if (updateConnections())
                parentApp.makeSound(Sound.CONNECT);
            
        	// Tell the parent we clicked this cell.
            parentApp.cellClicked(cell);

            // If we're done, report it.
        	if (isSolved()) {
        		// Un-blind all cells.
        		for (int x = boardStartX; x < boardEndX; x++)
        			for (int y = boardStartY; y < boardEndY; y++)
        				cellMatrix[x][y].setBlind(false);

        		blink(cell);
        		parentApp.gameSolved();
        	}
        }

        private Cell cell;
        private int angle = 0;
        private int delta = 0;
    }


    // This animation blinks a cell.
    private final class CellBlink extends Timer {

    	CellBlink() {
    		super(20);
    	}

    	public void start(Cell cell) {
    		// Don't touch the parameters if we're running.  This prevents
    		// more than one animation at once, which is what we want
    		// as we're only going to allocate one of these objects.
    		if (isRunning())
    			return;
    		
    		// Reset our state (including invocation counter).
    		reset();
    		this.cell = cell;
    		this.offset = 0;
    		super.start();
    	}

    	@Override
		protected boolean step(int count, long time) {
    		// Move the "light" band on the cell.
    		cell.setLight(offset);
            offset += 4;
            return offset >= cellWidth * 2;
        }

    	@Override
		protected void done() {
    		// Clear the "light" band on the cell.
            cell.setLight(0);
        }

        private Cell cell;
        private int offset;
    }

    
    // ******************************************************************** //
    // State Save/Restore.
    // ******************************************************************** //

    /**
     * Save game state so that the user does not lose anything
     * if the game process is killed while we are in the 
     * background.
     * 
	 * @param	outState		A Bundle in which to place any state
	 * 							information we wish to save.
     */
    void saveState(Bundle outState) {
    	// Save the game state of the board.
    	outState.putInt("gridWidth", gridWidth);
    	outState.putInt("gridHeight", gridHeight);
    	outState.putInt("rootX", rootCell.x());
    	outState.putInt("rootY", rootCell.y());
    	outState.putInt("focusX", focusedCell.x());
    	outState.putInt("focusY", focusedCell.y());
    	
    	// Save the states of all the cells which are in use.
        for (int x = 0; x < gridWidth; ++x) {
            for (int y = 0; y < gridHeight; ++y) {
            	String key = "cell " + x + "," + y;
                outState.putBundle(key, cellMatrix[x][y].saveState());
            }
        }
    }

    
    /**
     * Restore our game state from the given Bundle.
     * 
     * @param	map			A Bundle containing the saved state.
     * @param	skill		Skill level of the saved game.
     * @return				true if the state was restored OK; false
     * 						if the saved state was incompatible with the
     * 						current configuration.
     */
    boolean restoreState(Bundle map, Skill skill) {
    	// Restore the game state of the board.
    	gameSkill = skill;
    	resetBoard(gameSkill);
    	
    	// Check that the saved board size is compatible with what we
    	// have now.  If it is identical, then do a straight restore; if
    	// it's rotated, then restore and rotate.
    	int sgw = map.getInt("gridWidth");
    	int sgh = map.getInt("gridHeight");
    	if (sgw == gridWidth && sgh == gridHeight)
    		return restoreNormal(map);
    	else if (sgw == gridHeight && sgh == gridWidth) {
    		if (gridWidth > gridHeight)
    			return restoreRotLeft(map);
    		else
    			return restoreRotRight(map);
    	} else
    		return false;
    }
    

    /**
     * Restore our game state from the given Bundle.
     * 
     * @param	map			A Bundle containing the saved state.
     * @return				true if the state was restored OK; false
     * 						if the saved state was incompatible with the
     * 						current configuration.
     */
    private boolean restoreNormal(Bundle map) {
    	// Set up the root cell and focused cell.
    	int rx = map.getInt("rootX");
    	int ry = map.getInt("rootY");
    	rootCell = cellMatrix[rx][ry];
    	int fx = map.getInt("focusX");
    	int fy = map.getInt("focusY");
    	setFocus(cellMatrix[fx][fy]);

    	// Restore the states of all the cells which are in use.
        for (int x = 0; x < gridWidth; ++x) {
            for (int y = 0; y < gridHeight; ++y) {
            	String key = "cell " + x + "," + y;
            	cellMatrix[x][y].restoreState(map.getBundle(key));
            }
        }

        return true;
    }
    

    /**
     * Restore our game state from the given Bundle, rotating the board
     * left as we do so.
     * 
     * @param	map			A Bundle containing the saved state.
     * @return				true if the state was restored OK; false
     * 						if the saved state was incompatible with the
     * 						current configuration.
     */
    private boolean restoreRotLeft(Bundle map) {
    	// Set up the root cell.  Flip the co-ordinates.
    	int rx = map.getInt("rootX");
    	int ry = map.getInt("rootY");
    	rootCell = cellMatrix[ry][gridHeight - rx - 1];
    	int fx = map.getInt("focusX");
    	int fy = map.getInt("focusY");
    	setFocus(cellMatrix[fy][gridHeight - fx - 1]);

    	// Restore the states of all the cells which are in use.
        for (int y = 0; y < gridHeight; ++y) {
        	for (int x = 0; x < gridWidth; ++x) {
            	if (x >= cellMatrix.length || y >= cellMatrix[x].length)
            		return false;
            	String key = "cell " + y + "," + x;
            	Bundle cmap = map.getBundle(key);
            	if (cmap == null)
            		return false;
            	cellMatrix[x][gridHeight - y - 1].restoreState(cmap);
            	cellMatrix[x][gridHeight - y - 1].rotate(-90);
            }
        }

        return true;
    }
    

    /**
     * Restore our game state from the given Bundle, rotating the board
     * right as we do so.
     * 
     * @param	map			A Bundle containing the saved state.
     * @return				true if the state was restored OK; false
     * 						if the saved state was incompatible with the
     * 						current configuration.
     */
    private boolean restoreRotRight(Bundle map) {
    	// Set up the root cell.  Flip the co-ordinates.
    	int rx = map.getInt("rootX");
    	int ry = map.getInt("rootY");
    	rootCell = cellMatrix[gridWidth - ry - 1][rx];
    	int fx = map.getInt("focusX");
    	int fy = map.getInt("focusY");
    	setFocus(cellMatrix[gridWidth - fy - 1][fx]);

    	// Restore the states of all the cells which are in use.
        for (int y = 0; y < gridHeight; ++y) {
        	for (int x = 0; x < gridWidth; ++x) {
            	if (x >= cellMatrix.length || y >= cellMatrix[x].length)
            		return false;
            	String key = "cell " + y + "," + x;
            	Bundle cmap = map.getBundle(key);
            	if (cmap == null)
            		return false;
            	cellMatrix[gridWidth - x - 1][y].restoreState(cmap);
            	cellMatrix[gridWidth - x - 1][y].rotate(90);
            }
        }

        return true;
    }
    
    
    // ******************************************************************** //
    // Utilities.
    // ******************************************************************** //

    /**
     * Return the given value plus one, but wrapped within the given range.
     * 
     * @param	v				Value to increment.
     * @param	min				Minimum allowed value, inclusive.
     * @param	max				Maximum allowed value, not inclusive.
     * @return					The incremented value, wrapped around
     * 							to stay in range.
     */
    private static final int incr(int v, int min, int max) {
    	return v < max - 1 ? ++v : min;
    }
    

    /**
     * Return the given value minus one, but wrapped within the given range.
     * 
     * @param	v				Value to decrement.
     * @param	min				Minimum allowed value, inclusive.
     * @param	max				Maximum allowed value, not inclusive.
     * @return					The decremented value, wrapped around
     * 							to stay in range.
     */
    private static final int decr(int v, int min, int max) {
    	return v > min ? --v : max - 1;
    }
    
    
    // ******************************************************************** //
    // Class Data.
    // ******************************************************************** //

    // Debugging tag.
	private static final String TAG = "netscramble";

	// A mapping from each of the main directions to the contrary direction.
	private static final EnumMap<Cell.Dir, Cell.Dir> contrdirs =
							new EnumMap<Cell.Dir, Cell.Dir>(Cell.Dir.class);
	static {
		contrdirs.put(Cell.Dir.U___, Cell.Dir.__D_);
		contrdirs.put(Cell.Dir._R__, Cell.Dir.___L);
		contrdirs.put(Cell.Dir.__D_, Cell.Dir.U___);
		contrdirs.put(Cell.Dir.___L, Cell.Dir._R__);
	}

    // Random number generator for the game.  We use a Mersenne Twister,
	// which is a high-quality and fast implementation of java.util.Random.
    private static final Random rng = new MTRandom();

	
	// ******************************************************************** //
	// Private Data.
	// ******************************************************************** //
    
    // The parent application.
    private NetScramble parentApp;

    // Width and height of the playing board, in cells.  This is tailored
	// to suit the screen size and orientation.  It should be invariant on
	// any given device except that it will rotate 90 degrees when the
	// screen rotates.
	private int gridWidth;
	private int gridHeight;

	// The Cell objects which make up the board.  This matrix is gridWidth
    // by gridHeight, which is large enough to contain the game board at
	// any skill level.
	private Cell[][] cellMatrix;
	
    // Width and height of the cells in the board,  in pixels.
	private int cellWidth;
	private int cellHeight;

	// Size of the game board, and offset of the first and last active cells.
	// These are set up to define the actual board area in use for a given
	// game.  These change depending on the skill level.
	private int boardWidth;
	private int boardHeight;
	private int boardStartX;
	private int boardStartY;
	private int boardEndX;
	private int boardEndY;

	// Animation object used to manage a cell rotation.
	private CellRotation cellRotator;

	// Animation object used to blink a cell.
	private CellBlink cellBlinker;

	// The skill level of the current game.
	private Skill gameSkill;

	// The rootCell cell of the layout; where the server is.
	private Cell rootCell;
	
	// The cell which currently has the focus.
	private Cell focusedCell;

	// Connected flags for each cell in the board; used in updateConnections().
	private boolean isConnected[][];
	
	// List of outstanding connected cells; used in updateConnections().
    Vector<Cell> connectingCells;

}

