package game1024;

import java.util.*;

/*******************************************
@author: Ryan Beam
@Project: 1024 Project 2
@version: winter 2017
@Date: 3/2/2017
@Description: This program plays the 2048 game by initializing an array
of a given size and using shift and combining functions to change the
state of the board each time. If the board is filled and no moves can
be made the user has lost, if the winning value is reached the user wins,
an undo option is also employed to return the game to the state before the
most recent slide, all the way back to the first board.
 *******************************************/
public class NumberGame implements NumberSlider{

	/** Height of the game board */
	private int height;
	
	/** Width of the game board */
	private int width;
	
	/** Value needed to win the game*/
	private int winningValue;
	
	/** Array of the board tiles */
	private int[][] board;
	
	/** Current Status of the game */
	private GameStatus theStatus;
	
	/** Stack trace used to undo game moves */
	private Stack<int[][]> theStack = new Stack<int[][]>();

	/**
	 * Resize the game handle a board of a given dimension
	 * @param height the height of the board
	 * @param width the width of the board
	 * @param winningValue the value need to win game
	 * @throws IllegalArgumentException
	 */
	public void resizeBoard(int height, int width, int winningValue)
	{
		this.height = height; // Set the height and width of the board
		this.width = width;

		if((winningValue < 0) || ((winningValue % 2) != 0)) // If winning value less 
			throw new IllegalArgumentException();		 // than 0 or not divisible by 2
		else
			this.winningValue = winningValue; // else assign winning value

		if((height <= 0) || (width <= 0))
			throw new IllegalArgumentException(); // need dimensions larger than 0
		else
			this.board = new int[width][height]; // if acceptable assign dimensions to board
	}

	/**
	 * Remove all numbered tiles from the board and place
     * TWO non-zero values at random location, the game
     * is now in progress.
	 */
	public void reset()
	{
		this.board = new int[width][height]; // assign new board

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				board[i][j] = 0; // initialize all board tiles as 0
			}
		}
		placeRandomValue(); // Place two random values to start
		placeRandomValue();
		getStatus(); // The game is now in progress
	}

	/**
	 * Set the game board to the desired dimensions and 
	 * tile values given in the 2D reference array.
	 * @param ref the given reference array
	 */
	public void setValues(int[][] ref)
	{
		this.width = ref.length; // Dimensions are equal to length of reference
		this.height = ref.length;
		this.board = new int[width][height]; // set board to dimensions

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				this.board[i][j] = ref[i][j]; // for each tile copy reference to board
			}
		}
	}

	/**
	 * Inserts one random tile into an empty spot on the board
	 * given that there is a spot available.
	 * @return Cell
	 * @throws IllegalStateException
	 */
	public Cell placeRandomValue() 
	{
		Random rand = new Random(); // new random call

		// random value can be 2, 4, 8
		double value = Math.ceil(Math.pow(2, rand.nextInt(2)+1));
		
		int row = 0; // Initialize the row and column for value
		int column = 0;
		
		if(getNonEmptyTiles() == null) // if no empty tiles throw exception
		{
			throw new IllegalStateException();
		}
		do{
			row = rand.nextInt(height); // try to set row and column until
			column = rand.nextInt(width); // finds an empty tile
		}while(board[row][column] != 0);
		this.board[row][column] = (int)value; // once found assign value to the tile

		// Assign the cell position and value to be returned
		Cell thePlace = new Cell(row,column,(int)value);

		return thePlace; // return the cell
	}
	
	/**
	 * Combine the tiles for a slide of down or right
	 * when given the appropriate matrix and column
	 * @param j the current column
	 */
	public void makeDownCombine(int j)
	{
		for(int i = height-2; i >=0; i--) // from bottom of board to top
		{
			if(board[i][j] != 0) // if tile has a value
			{
				if(board[i][j] == board[i+1][j]) // if tile values are equal
				{
					board[i+1][j] = 2* board[i][j]; // combine the tiles
					board[i][j] = 0; // clear tile above 
				}
			}
		}
	}
	
	/**
	 * Shift the tiles for a slide of down or right
	 * when given appropriate matrix and column
	 * @param j the current column
	 */
	public void makeDownShift(int j)
	{
		for(int i = height-2; i >= 0; i--) // from bottom of board to top
		{
			int hold = i;
			if(board[hold][j] != 0) // if tile has value
			{
				while(board[hold+1][j] == 0){ // while tile below is empty
					board[hold+1][j] = board[hold][j]; // move the tile
					board[hold][j] = 0;
					hold++; // move hold down the board
					if(hold == height-1) // if at bottom break
						break;
				}
			}
		}
	}
	
	/**
	 * Combine the tiles for a slide of up or left
	 * when given the appropriate matrix and column
	 * @param j the current column
	 */
	public void makeUpCombine(int j)
	{
		for(int i = 1; i < height; i++) // from top of board to bottom
		{
			if(board[i][j] != 0) // if has value
			{
				if(board[i][j] == board[i-1][j]) // if their value is the same
				{
					board[i-1][j] = 2* board[i][j]; // combine the tiles
					board[i][j] = 0; // clear tile below
				}	
			}
		}
	}
	
	/**
	 * Shift the tiles for a slide of up or left
	 * when given appropriate matrix and column
	 * @param j the current column
	 */
	public void makeUpShift(int j)
	{
		for(int i = 1; i <= height-1; i++) // from top of board to bottom
		{
			int hold = i;
			if(board[hold][j] != 0) // if tile has value
			{
				while(board[hold-1][j] == 0){ // while tile above is empty
					board[hold-1][j] = board[hold][j]; // move the tile
					board[hold][j] = 0; 
					hold--; // move hold up the board
					if(hold == 0) // if at top break
						break;
				}
			}
		}
	}

	/**
	 * Given the side direction call the appropriate
	 * functions to shift and combine tiles 
	 * @param direction the direction for moving board
	 */
	public void thenMove(SlideDirection direction)
	{
		if((direction == SlideDirection.DOWN) || (direction == SlideDirection.RIGHT))
		{
			if(direction == SlideDirection.RIGHT) // If right need to transpose matrix
				board = transpose();
			for(int j = 0; j < width; j++) // for each column
			{
				makeDownShift(j); // first shift down
				makeDownCombine(j); // combine the tiles
				makeDownShift(j); // last shift
			}
			if(direction == SlideDirection.RIGHT) // transpose back
				board = transpose();
		}
		else if((direction == SlideDirection.UP) || (direction == SlideDirection.LEFT) )
		{
			if(direction == SlideDirection.LEFT) // If left need to transpose matrix
				board = transpose(); 
			for(int j = 0; j < width; j++) // for each column
			{
				makeUpShift(j); // first shift down
				makeUpCombine(j); // combine the tiles
				makeUpShift(j); // last shift
			}
			if(direction == SlideDirection.LEFT) // transpose back
				board = transpose();
		}
	}

	/**
	 * Transpose the matrix for when sliding left or right,
	 * this is to reduce repeating code and is called before
	 * as well as after shifting and combing the tiles.
	 * @return Array
	 */
	public int[][] transpose()
	{
		int[][] transposed = new int[height][width]; // create new array
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				transposed[j][i] = board[i][j]; // Swap all row and column values
			}
		}
		return transposed; // return the transposed matrix
	}
	
	/**
	 * Slide all the tiles in the board in the requested direction
	 * and determine whether or not to place a random value
	 * @param dir the direction for moving board
	 * @return boolean
	 */
	public boolean slide(SlideDirection dir)
	{
		int[][] before = new int[height][width]; // Array before the slide
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				before[i][j] = board[i][j]; // copy each tile
			}
		}
		theStack.push(board); // push current board to stack for possible undo
		
		thenMove(dir); // Make the slide
		
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(before[i][j] != board[i][j]) // if board has changed
				{
					placeRandomValue(); // place a random value
					return true; // return true board changed
				}

			}
		}
		return false; // escaped loop so board is same return false
	}

	/**
	 * Returns a list of cells whose value is not 0
	 * @return ArrayList
	 */
	public ArrayList<Cell> getNonEmptyTiles()
	{
		ArrayList<Cell> theList = new ArrayList<Cell>(); // Create List of cells

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(board[i][j] != 0) // if tile is not empty
				{
					Cell hold = new Cell(i,j,board[i][j]); // give cell the position and value
					theList.add(hold); // add cell to array list
				}
			}
		}
		return theList; // return the list
	}

	/**
	 * Returns the game status depending on the winning value
	 * and number of open moves.
	 * @return GameStatus
	 */
	public GameStatus getStatus()
	{
		int openMoves = 0; // initialize Moves and Tiles
		int openTiles = 0;
		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				if(board[i][j] == 0) // if tile empty add to variable
					openTiles = openTiles + 1;

				if(board[i][j] == winningValue) // if winning value found user wins
					theStatus = GameStatus.USER_WON;
			}

		}

		if(openTiles == 0) // if no empty tiles check for available moves
		{
			for(int i = 0; i < height; i++)
			{
				for(int j = 0; j < width-1; j++)
				{
					if(board[i][j] == board[i][j+1]) // if horizontal adjacent tiles match
						openMoves = openMoves + 1; // add possible move
				}
			}
			for(int i = 0; i < height-1; i++)
			{
				for(int j = 0; j < width; j++)
				{
					if(board[i][j] == board[i+1][j]) // if vertical adjacent tiles match
						openMoves = openMoves + 1; // add possible move
				}
			}
			if(openMoves == 0) // if no open moves and board full user has lost
				theStatus = GameStatus.USER_LOST;
		}
		else
			theStatus = GameStatus.IN_PROGRESS;

		return theStatus; // return the game status
	}

	/**
	 * Undo's the board to the state before the current. 
	 * Done using the stack, throws exception when reaches
	 * the start of the game.
	 * @throws IllegalStateException
	 */
	public void undo()
	{
		int[][] save = new int[height][width]; // new array for stack display
		
		try{
		save = theStack.pop(); // attempt to pop top board
		}catch(IllegalStateException e){
			e.printStackTrace(); // catch if stack is empty
		}

		for(int i = 0; i < height; i++)
		{
			for(int j = 0; j < width; j++)
			{
				board[i][j] = save[i][j]; // set board to popped array
			}
		}
	}	
}
