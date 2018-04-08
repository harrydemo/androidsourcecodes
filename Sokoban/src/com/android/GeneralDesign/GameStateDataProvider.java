package com.android.GeneralDesign;

import com.android.GameData.GameDataStruct;

public class GameStateDataProvider
{
	//////////////////////////////////////////////////////////
	/* get map data for every state*/
	//////////////////////////////////////////////////////////
	public static int[][] getGameStateData(int stateNo)
	{
		stateNo %= gameStateDataArray.length;
		return gameStateDataArray[stateNo];
	}
	public static int getGameStateToltalNumer()
	{
		return gameStateDataArray.length;
	}
	static final int[][][] gameStateDataArray =
	{
		//-----------------------------------------------------------
		//state 1
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 2
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 3
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 4
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 5
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 6
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 7
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 8
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 9
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 10
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 11
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 12
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 13
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 14
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 15
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 16
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 17
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 18
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 19
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 20
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 21
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 22
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 23
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 24
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 25
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 26
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 27
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 28
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.DEST,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.DEST,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 29
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 30
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 31
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 32
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		 //-----------------------------------------------------------
		 //state 33
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.NULL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 9 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 10 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 11 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 12 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL
		     }
		 },
		 //-----------------------------------------------------------
		 //state 34
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.DEST,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.DEST,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.DEST,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.DEST,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 9 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.DEST,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.DEST,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 10 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 11 */GameDataStruct.DATA_FLAG.DEST,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 11 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.DEST,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.DEST,
		         /* 10 */GameDataStruct.DATA_FLAG.DEST,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 12 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 13 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     }
		 },
		 //-----------------------------------------------------------
		 //state 35
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 9 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 10 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.NULL,
		         /* 05 */GameDataStruct.DATA_FLAG.NULL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL
		     }
		 },
		//-----------------------------------------------------------
		//state 36
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		 //-----------------------------------------------------------
		 //state 37
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.DEST,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.NULL
		     }
		 },
		 //-----------------------------------------------------------
		 //state 38
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.DEST,
		         /* 03 */GameDataStruct.DATA_FLAG.DEST,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 9 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 10 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 11 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 12 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL
		     }
		 },
		 //-----------------------------------------------------------
		 //state 39
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.DEST,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 9 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 10 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL
		     }
		 },
		//-----------------------------------------------------------
		//state 40
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 14 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 41
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 42
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 43
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 44
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 45
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 46
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 14 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 47
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 48
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 49
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 50
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 51
		{

		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 52
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL,
		       /* 15 */GameDataStruct.DATA_FLAG.NULL,
		       /* 16 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL,
		       /* 15 */GameDataStruct.DATA_FLAG.NULL,
		       /* 16 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL,
		       /* 15 */GameDataStruct.DATA_FLAG.WALL,
		       /* 16 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.PATH,
		       /* 15 */GameDataStruct.DATA_FLAG.PATH,
		       /* 16 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST,
		       /* 13 */GameDataStruct.DATA_FLAG.DEST,
		       /* 14 */GameDataStruct.DATA_FLAG.DEST,
		       /* 15 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 16 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.PATH,
		       /* 15 */GameDataStruct.DATA_FLAG.PATH,
		       /* 16 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL,
		       /* 15 */GameDataStruct.DATA_FLAG.WALL,
		       /* 16 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL,
		       /* 15 */GameDataStruct.DATA_FLAG.NULL,
		       /* 16 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL,
		       /* 15 */GameDataStruct.DATA_FLAG.NULL,
		       /* 16 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 53
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL,
		       /* 12 */GameDataStruct.DATA_FLAG.NULL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 54
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL,
		       /* 12 */GameDataStruct.DATA_FLAG.NULL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 11 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL,
		       /* 12 */GameDataStruct.DATA_FLAG.NULL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 12 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL,
		       /* 12 */GameDataStruct.DATA_FLAG.NULL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 55
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 56
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 11 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 12 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 57
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 11 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 12 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 58
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   }
		},
		//-----------------------------------------------------------
		//state 59
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.NULL,
		       /* 13 */GameDataStruct.DATA_FLAG.NULL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.DEST,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.DEST,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 11 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 12 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   },
		   /* 13 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.NULL,
		       /* 02 */GameDataStruct.DATA_FLAG.NULL,
		       /* 03 */GameDataStruct.DATA_FLAG.NULL,
		       /* 04 */GameDataStruct.DATA_FLAG.NULL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.NULL
		   }
		},
		//-----------------------------------------------------------
		//state 60
		{
		   /* 1 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 2 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST,
		       /* 13 */GameDataStruct.DATA_FLAG.DEST,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 3 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.DEST,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.DEST,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 4 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 5 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 6 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 7 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 03 */GameDataStruct.DATA_FLAG.PATH,
		       /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 05 */GameDataStruct.DATA_FLAG.PATH,
		       /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 07 */GameDataStruct.DATA_FLAG.PATH,
		       /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 09 */GameDataStruct.DATA_FLAG.PATH,
		       /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 11 */GameDataStruct.DATA_FLAG.PATH,
		       /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		       /* 13 */GameDataStruct.DATA_FLAG.PATH,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 8 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 04 */GameDataStruct.DATA_FLAG.DEST,
		       /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 06 */GameDataStruct.DATA_FLAG.DEST,
		       /* 07 */GameDataStruct.DATA_FLAG.DEST,
		       /* 08 */GameDataStruct.DATA_FLAG.DEST,
		       /* 09 */GameDataStruct.DATA_FLAG.DEST,
		       /* 10 */GameDataStruct.DATA_FLAG.DEST,
		       /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST,
		       /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 9 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.DEST,
		       /* 03 */GameDataStruct.DATA_FLAG.DEST,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.WALL,
		       /* 06 */GameDataStruct.DATA_FLAG.WALL,
		       /* 07 */GameDataStruct.DATA_FLAG.WALL,
		       /* 08 */GameDataStruct.DATA_FLAG.WALL,
		       /* 09 */GameDataStruct.DATA_FLAG.WALL,
		       /* 10 */GameDataStruct.DATA_FLAG.WALL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.DEST,
		       /* 13 */GameDataStruct.DATA_FLAG.DEST,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   },
		   /* 10 */
		   {
		       /* 01 */GameDataStruct.DATA_FLAG.WALL,
		       /* 02 */GameDataStruct.DATA_FLAG.WALL,
		       /* 03 */GameDataStruct.DATA_FLAG.WALL,
		       /* 04 */GameDataStruct.DATA_FLAG.WALL,
		       /* 05 */GameDataStruct.DATA_FLAG.NULL,
		       /* 06 */GameDataStruct.DATA_FLAG.NULL,
		       /* 07 */GameDataStruct.DATA_FLAG.NULL,
		       /* 08 */GameDataStruct.DATA_FLAG.NULL,
		       /* 09 */GameDataStruct.DATA_FLAG.NULL,
		       /* 10 */GameDataStruct.DATA_FLAG.NULL,
		       /* 11 */GameDataStruct.DATA_FLAG.WALL,
		       /* 12 */GameDataStruct.DATA_FLAG.WALL,
		       /* 13 */GameDataStruct.DATA_FLAG.WALL,
		       /* 14 */GameDataStruct.DATA_FLAG.WALL
		   }
		},
		//-----------------------------------------------------------
		//state 61
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 62
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 63
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 64
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 65
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 66
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 67
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 14 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 68
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 69
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 70
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 71
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 72
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 73
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 74
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 75
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 76
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 77
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 78
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 79
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 80
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 81
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 14 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 82
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 83
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 14 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 84
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 85
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 86
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.DEST,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 14 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 87
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 88
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 89
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 90
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 91
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 92
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		 //-----------------------------------------------------------
		 //state 93
		 {
		     /* 1 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.NULL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL,
		         /* 15 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 2 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL,
		         /* 15 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 3 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.NULL,
		         /* 12 */GameDataStruct.DATA_FLAG.NULL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL,
		         /* 15 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 4 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.PATH,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.NULL,
		         /* 14 */GameDataStruct.DATA_FLAG.NULL,
		         /* 15 */GameDataStruct.DATA_FLAG.NULL
		     },
		     /* 5 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.WALL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 6 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.DEST,
		         /* 06 */GameDataStruct.DATA_FLAG.DEST,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.DEST,
		         /* 09 */GameDataStruct.DATA_FLAG.DEST,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 7 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 8 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.PATH,
		         /* 04 */GameDataStruct.DATA_FLAG.PATH,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.DEST,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 9 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.WALL,
		         /* 03 */GameDataStruct.DATA_FLAG.WALL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 10 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.PATH,
		         /* 10 */GameDataStruct.DATA_FLAG.PATH,
		         /* 11 */GameDataStruct.DATA_FLAG.PATH,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 11 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.PATH,
		         /* 06 */GameDataStruct.DATA_FLAG.PATH,
		         /* 07 */GameDataStruct.DATA_FLAG.PATH,
		         /* 08 */GameDataStruct.DATA_FLAG.PATH,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.WALL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.PATH,
		         /* 13 */GameDataStruct.DATA_FLAG.PATH,
		         /* 14 */GameDataStruct.DATA_FLAG.PATH,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     },
		     /* 12 */
		     {
		         /* 01 */GameDataStruct.DATA_FLAG.NULL,
		         /* 02 */GameDataStruct.DATA_FLAG.NULL,
		         /* 03 */GameDataStruct.DATA_FLAG.NULL,
		         /* 04 */GameDataStruct.DATA_FLAG.WALL,
		         /* 05 */GameDataStruct.DATA_FLAG.WALL,
		         /* 06 */GameDataStruct.DATA_FLAG.WALL,
		         /* 07 */GameDataStruct.DATA_FLAG.WALL,
		         /* 08 */GameDataStruct.DATA_FLAG.WALL,
		         /* 09 */GameDataStruct.DATA_FLAG.WALL,
		         /* 10 */GameDataStruct.DATA_FLAG.NULL,
		         /* 11 */GameDataStruct.DATA_FLAG.WALL,
		         /* 12 */GameDataStruct.DATA_FLAG.WALL,
		         /* 13 */GameDataStruct.DATA_FLAG.WALL,
		         /* 14 */GameDataStruct.DATA_FLAG.WALL,
		         /* 15 */GameDataStruct.DATA_FLAG.WALL
		     }
		 },
		//-----------------------------------------------------------
		//state 94
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 95
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 96
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    }
		},
		//-----------------------------------------------------------
		//state 97
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.DEST,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 98
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.DEST,
		        /* 13 */GameDataStruct.DATA_FLAG.DEST,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 99
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.NULL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.DEST,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.DEST,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 12 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.PATH,
		        /* 03 */GameDataStruct.DATA_FLAG.PATH,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 13 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.NULL,
		        /* 11 */GameDataStruct.DATA_FLAG.NULL,
		        /* 12 */GameDataStruct.DATA_FLAG.NULL,
		        /* 13 */GameDataStruct.DATA_FLAG.NULL,
		        /* 14 */GameDataStruct.DATA_FLAG.NULL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL
		    }
		},
		//-----------------------------------------------------------
		//state 100
		{
		    /* 1 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 2 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.NULL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 3 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.NULL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 4 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 5 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.WALL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 6 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.NULL,
		        /* 06 */GameDataStruct.DATA_FLAG.NULL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 7 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.PATH,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    },
		    /* 8 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH,
		        /* 05 */GameDataStruct.DATA_FLAG.PATH,
		        /* 06 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 09 */GameDataStruct.DATA_FLAG.PATH,
		        /* 10 */GameDataStruct.DATA_FLAG.PATH,
		        /* 11 */GameDataStruct.DATA_FLAG.PATH,
		        /* 12 */GameDataStruct.DATA_FLAG.PATH,
		        /* 13 */GameDataStruct.DATA_FLAG.PATH,
		        /* 14 */GameDataStruct.DATA_FLAG.PATH,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 9 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.WALL,
		        /* 03 */GameDataStruct.DATA_FLAG.WALL,
		        /* 04 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.MP,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.PATH | GameDataStruct.DATA_FLAG.BOX,
		        /* 08 */GameDataStruct.DATA_FLAG.PATH,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST | GameDataStruct.DATA_FLAG.BOX,
		        /* 11 */GameDataStruct.DATA_FLAG.DEST,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.NULL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 10 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.WALL,
		        /* 05 */GameDataStruct.DATA_FLAG.DEST,
		        /* 06 */GameDataStruct.DATA_FLAG.DEST,
		        /* 07 */GameDataStruct.DATA_FLAG.DEST,
		        /* 08 */GameDataStruct.DATA_FLAG.DEST,
		        /* 09 */GameDataStruct.DATA_FLAG.DEST,
		        /* 10 */GameDataStruct.DATA_FLAG.DEST,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.NULL
		    },
		    /* 11 */
		    {
		        /* 01 */GameDataStruct.DATA_FLAG.NULL,
		        /* 02 */GameDataStruct.DATA_FLAG.NULL,
		        /* 03 */GameDataStruct.DATA_FLAG.NULL,
		        /* 04 */GameDataStruct.DATA_FLAG.NULL,
		        /* 05 */GameDataStruct.DATA_FLAG.WALL,
		        /* 06 */GameDataStruct.DATA_FLAG.WALL,
		        /* 07 */GameDataStruct.DATA_FLAG.WALL,
		        /* 08 */GameDataStruct.DATA_FLAG.WALL,
		        /* 09 */GameDataStruct.DATA_FLAG.WALL,
		        /* 10 */GameDataStruct.DATA_FLAG.WALL,
		        /* 11 */GameDataStruct.DATA_FLAG.WALL,
		        /* 12 */GameDataStruct.DATA_FLAG.WALL,
		        /* 13 */GameDataStruct.DATA_FLAG.WALL,
		        /* 14 */GameDataStruct.DATA_FLAG.WALL,
		        /* 15 */GameDataStruct.DATA_FLAG.WALL,
		        /* 16 */GameDataStruct.DATA_FLAG.WALL
		    }
		}
	};
}
