package logic;

import java.util.ArrayList;
import java.util.Collections;

public class SudokuBacktracking {

	public static boolean solve(int[][] puzzle) {
		//finding the next unassigned cell
		int x = 0, y = 0;
		boolean found = false; 
		while (x < puzzle.length && !found) {
			y = 0;
			while (y < puzzle.length && !found) {
				found = puzzle[x][y] == 0;
				y++;
			}
			x++;
		}
		x--;
		y--;
		//No more empty cells, solution found!
		if(!found)
			return true;
		
		for(int digit = 1; digit <= puzzle.length; digit++) {
			if(!conflict(puzzle, digit, x, y)) {
				puzzle[x][y] = digit;
				if(solve(puzzle))
					return true;
				else
					puzzle[x][y] = 0;
			}
		}
		return false;
	}

	//check for conflict inside the row, column, and box
	private static boolean conflict(int[][] puzzle, int digit, int x, int y) {
		
		for(int i = 0; i < puzzle.length; i++)
			if((puzzle [x][i] == digit) || (puzzle[i][y] == digit))
				return true;
		
		int boxSize = (int) Math.sqrt(puzzle.length);
		int sx = x - x % boxSize, sy = y - y % boxSize;
		for(int i = sx; i < sx+boxSize;i++)
			for(int j = sy; j < sy+boxSize; j++)
				if(puzzle[i][j] == digit)
					return true;
		
		return false;
	}
	
	public static int[][] generatePuzzle(int difficulty, int n) {
		int grid[][];
		int tries;
		do {
			tries = n;
			grid = new int[n][n];
			grid[0] = getRandomPermutation(n);
			int shiftSteps[] = new int[] {3, 3, 1, 3, 3, 1, 3, 3};
			for(int i = 1; i < n; i++) {
				System.arraycopy(grid[i-1], 0, grid[i], 0, n);
				shift(grid[i], shiftSteps[i-1]);
			}
			
			//grid is now a complete solution,
			//start removing the clues until there is only the arranged
			//for the desired difficulty
			int sqrtN = (int) Math.sqrt(n);
			int backup[][] = new int[n][n];
			int i = 0;
			while (i < 5) {
				int x = sqrtN, y = sqrtN;
				x += Math.random()*sqrtN;
				y += Math.random()*sqrtN;
				if(grid[x][y] !=0 && solve(backup)) {
					grid[x][y] = 0;
					i++;
				}
			}
			
			int clues = 81-i;
			int target = 34 - 5*difficulty;
			
			while(clues > target && tries > 0) {
				int x = (int) (Math.random()*n);
				int y = (int) (Math.random()*n);
				//verifying if it is not attempting to remove from
				//the inner sqare or from already removed.
				if(x < sqrtN || x >= 2*sqrtN || y < sqrtN || y >= 2*sqrtN) 
						if(grid[x][y] !=0) {
							//for(int k = 0; k < n; k++)
							//	System.arraycopy(grid[k], 0, backup[k], 0, n);
							
							//backup[x][y] = 0;
							//backup[n-x-1][n-y-1] = 0;
							//if there is a solution, we can remotion
							//if(solve(backup)) {
								grid[x][y] = 0;
								grid[n-x-1][n-y-1] = 0;
								clues -= 2;
							//} else 
							//	tries--;
						}
			}
		}while (tries == 0);
		
		return grid;
	}
	
	public static void shift(int[] array, int shift) {
		int temp[] = new int[shift];
		System.arraycopy(array, 0, temp, 0, shift);
		for(int i = shift; i < array.length; i++)
			array[i-shift] = array[i];
		
		System.arraycopy(temp, 0, array, array.length-shift, shift);
	}
	
	public static int[] getRandomPermutation(int n) {
		ArrayList<Integer> perm = new ArrayList<Integer>(n);
		for (int i = 0; i < n; i++)
			perm.add(i+1);
		
		Collections.shuffle(perm);
		
		int ret[] = new int[n];
		for(int i = 0; i < n; i++)
			ret[i] = perm.get(i);

		return ret;
	}
}
