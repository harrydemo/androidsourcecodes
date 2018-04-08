package bbth.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import bbth.engine.fastgraph.Wall;
import bbth.game.units.Unit;

public class GridAcceleration {
	private static class Cell {
		public ArrayList<Unit> units = new ArrayList<Unit>();
		public ArrayList<Wall> walls = new ArrayList<Wall>();
	}

	private int cellsInX;
	private int cellsInY;
	private float cellWidth;
	private float cellHeight;
	private ArrayList<Cell> cells = new ArrayList<Cell>();

	public GridAcceleration(float width, float height, float spacing) {
		cellsInX = (int) Math.ceil(width / spacing);
		cellsInY = (int) Math.ceil(height / spacing);
		cellWidth = width / cellsInX;
		cellHeight = height / cellsInY;
		for (int i = 0, n = cellsInX * cellsInY; i < n; i++) {
			cells.add(new Cell());
		}
	}

	public void clearUnits() {
		for (int i = 0, n = cellsInX * cellsInY; i < n; i++) {
			cells.get(i).units.clear();
		}
	}

	public void clearWalls() {
		for (int i = 0, n = cellsInX * cellsInY; i < n; i++) {
			cells.get(i).walls.clear();
		}
	}

	/**
	 * Adds each unit to exactly one cell. Call this after calling clearUnits()
	 * when the positions of the units have changed.
	 */
	public void insertUnits(List<Unit> units) {
		for (int i = 0, n = units.size(); i < n; i++) {
			Unit unit = units.get(i);
			int x = Math.max(0, Math.min(cellsInX - 1, (int) (unit.getX() / cellWidth)));
			int y = Math.max(0, Math.min(cellsInY - 1, (int) (unit.getY() / cellHeight)));
			cells.get(x + y * cellsInX).units.add(unit);
		}
	}

	/**
	 * Adds each wall to all cells in the axis-aligned bounding box. Call this
	 * after calling clearWalls() when the positions of the walls have changed.
	 */
	public void insertWalls(List<Wall> walls) {
		for (int i = 0, n = walls.size(); i < n; i++) {
			Wall wall = walls.get(i);
			int xmin = Math.max(0, Math.min(cellsInX - 1, (int) (wall.getMinX() / cellWidth)));
			int ymin = Math.max(0, Math.min(cellsInY - 1, (int) (wall.getMinY() / cellHeight)));
			int xmax = Math.max(0, Math.min(cellsInX - 1, (int) (wall.getMaxX() / cellWidth)));
			int ymax = Math.max(0, Math.min(cellsInY - 1, (int) (wall.getMaxY() / cellHeight)));
			for (int y = ymin; y <= ymax; y++) {
				for (int x = xmin; x <= xmax; x++) {
					cells.get(x + y * cellsInX).walls.add(wall);
				}
			}
		}
	}

	/**
	 * Put all enemies whose centers potentially lie in the given axis-aligned
	 * bounding box inside the given set (they might be slightly outside,
	 * however). This will remove all existing entities in the set first.
	 */
	public void getUnitsInAABB(float xmin_, float ymin_, float xmax_, float ymax_, HashSet<Unit> units) {
		// Convert from game space to grid space
		int xmin = Math.max(0, Math.min(cellsInX - 1, (int) (xmin_ / cellWidth)));
		int ymin = Math.max(0, Math.min(cellsInY - 1, (int) (ymin_ / cellHeight)));
		int xmax = Math.max(0, Math.min(cellsInX - 1, (int) (xmax_ / cellWidth)));
		int ymax = Math.max(0, Math.min(cellsInY - 1, (int) (ymax_ / cellHeight)));

		// Copy units from all cells in the AABB into units
		units.clear();
		for (int y = ymin; y <= ymax; y++) {
			for (int x = xmin; x <= xmax; x++) {
				// Manually iterate over the collection for performance (instead
				// of using addAll)
				ArrayList<Unit> toAdd = cells.get(x + y * cellsInX).units;
				for (int j = 0; j < toAdd.size(); j++) {
					units.add(toAdd.get(j));
				}
			}
		}
	}

	/**
	 * Put all enemies whose centers potentially lie in the given axis-aligned
	 * bounding box inside the given set (they might be slightly outside,
	 * however). This will remove all existing entities in the set first.
	 */
	public void getWallsInAABB(float xmin_, float ymin_, float xmax_, float ymax_, HashSet<Wall> walls) {
		// Convert from game space to grid space
		int xmin = Math.max(0, Math.min(cellsInX - 1, (int) (xmin_ / cellWidth)));
		int ymin = Math.max(0, Math.min(cellsInY - 1, (int) (ymin_ / cellHeight)));
		int xmax = Math.max(0, Math.min(cellsInX - 1, (int) (xmax_ / cellWidth)));
		int ymax = Math.max(0, Math.min(cellsInY - 1, (int) (ymax_ / cellHeight)));

		// Copy units from all cells in the AABB into units
		walls.clear();
		for (int y = ymin; y <= ymax; y++) {
			for (int x = xmin; x <= xmax; x++) {
				// Manually iterate over the collection for performance (instead
				// of using addAll)
				ArrayList<Wall> toAdd = cells.get(x + y * cellsInX).walls;
				for (int j = 0; j < toAdd.size(); j++) {
					walls.add(toAdd.get(j));
				}
			}
		}
	}
}
