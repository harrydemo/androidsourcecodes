package com.crackedcarrot.fileloader;

import java.io.IOException;
import java.io.InputStream;
import android.content.Context;
import com.crackedcarrot.Coords;
import com.crackedcarrot.Scaler;
import com.crackedcarrot.Sprite;
import com.crackedcarrot.Tower;
import com.crackedcarrot.Waypoints;
import com.crackedcarrot.menu.R;

/**
 * A class that reads the requested mapfile and returns an map object.
 */
public class MapLoader {

	private Context context;
	private InputStream in;
	private Scaler s;
	private Waypoints wps;
	private Sprite[] bckgrd;
	private Tower[][] twg;

	/**
	 * Constructor
	 * 
	 * @param Context
	 *            The context of the activity that requested the maploader.
	 * @param Scaler
	 *            A scaler for the waypoint.
	 */
	public MapLoader(Context context, Scaler s) {
		this.context = context;
		this.s = s;
		twg = new Tower[8][11];
		for (int x = 0; x < twg.length; x++) {
			for (int y = 0; y < twg[0].length; y++) {
				// At the moment all towers are the same size.
				// Number of frames can be changed together with the
				// Texture.
				twg[x][y] = new Tower(R.drawable.tesla1, 0, null, null);
				twg[x][y].draw = false;
			}
		}
	}

	/**
	 * Will read a file and turn all the data from the file to a Map object
	 * <p>
	 * This method is called from GameInit.
	 * 
	 * @param String
	 *            The filename of the requested lvl
	 * @return Map A new map object
	 */
	public Map readLevel(String lvlFile) {
		int resID = context.getResources().getIdentifier(lvlFile, "raw",
				context.getPackageName());
		in = context.getResources().openRawResource(resID);
		int i = 0;
		int lineNo = 0;
		int nbrWayP = 0;
		int nbrObs = 0;
		int textureFile = 0;

		try {
			String buf = "";
			while ((i = in.read()) != -1) {
				char c = (char) i;
				if (c != '\n') {
					buf += c;
				} else if (c == '\n') {
					lineNo++;
					if (lineNo <= 3) {
						// Contains info about the level. Do nothing here.
					} else if (lineNo == 4) {
						resID = context.getResources().getIdentifier(
								buf.trim(), "drawable",
								context.getPackageName());

						// If no map was found
						if (resID == 0)
							return null;

						// Gamemap
						Sprite background = new Sprite(resID,
								Sprite.BACKGROUND, 0);
						background.setWidth(s.getScreenResolutionX());
						background.setHeight(s.getScreenResolutionY());
						bckgrd = new Sprite[1];
						bckgrd[0] = background;
					} else if (lineNo == 5)
						textureFile = context.getResources().getIdentifier(
								buf.trim(), "raw", context.getPackageName());
					else if (lineNo == 6) {

						// Nbr of waypoints
						nbrWayP = Integer.parseInt(buf.trim());
						wps = new Waypoints(nbrWayP, s);
					} else if (lineNo >= 7 && lineNo <= 6 + nbrWayP) {
						// Each waypoint
						String[] wp = buf.split(",");
						int tmpgridx = Integer.parseInt(wp[0].trim());
						int tmpgridy = Integer.parseInt(wp[1].trim());
						int wpNbr = Integer.parseInt(wp[2].trim());
						Coords tmpCoord = s.getPosFromGrid(tmpgridx, tmpgridy);
						wps.way[wpNbr] = tmpCoord;

						if (wpNbr != 0) {
							Coords cp = wps.way[wpNbr - 1];
							cp = s.getGridXandY(cp.x, cp.y);

							while (!(cp.y == tmpgridy && cp.x == tmpgridx)) {
								if (tmpgridx > cp.x) {
									cp.x = cp.x + 1;
								} else if (tmpgridx < cp.x) {
									cp.x = cp.x - 1;
								} else if (cp.y > tmpgridy) {
									cp.y = cp.y - 1;
								} else if (cp.y < tmpgridy) {
									cp.y = cp.y + 1;
								}
								if (cp.x < s.getGridWidth() && cp.x >= 0
										&& cp.y < s.getGridHeight()
										&& cp.y >= 0)
									twg[cp.x][cp.y] = null;
							}
						}
					} else if (lineNo == 8 + nbrWayP) {
						// Nbr of obstacle
						nbrObs = Integer.parseInt(buf.trim());
					} else if (lineNo >= 8 + nbrWayP
							&& lineNo <= 8 + nbrWayP + nbrObs) {
						String[] wp = buf.split(",");
						int tmpgridx = Integer.parseInt(wp[0].trim());
						int tmpgridy = Integer.parseInt(wp[1].trim());
						twg[tmpgridx][tmpgridy] = null;
					}

					buf = "";
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return new Map(wps, bckgrd, twg, s, textureFile);
	}
}
