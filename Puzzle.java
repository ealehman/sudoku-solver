/*
 * Puzzle.java
 *
 * Implementation of a class that represents a Sudoku puzzle and solves
 * it using recursive backtracking.
 *
 * Computer Science S-111, Harvard University
 *
 * skeleton code by the course staff
 *
 * Modified by:     <Alex Lehman>, <ealehman91@gmail.com>
 * Date modified:   <Sunday July 21, 2013>
 */

import java.io.*;
import java.util.Scanner;

public class Puzzle {
    // the dimension of the puzzle grid
    public static final int DIM = 9;
    
    // the dimension of the smaller subgrids within the grid
    public static final int SUBGRID_DIM = 3; 
    
    // The current contents of the cells of the puzzle. 
    // values[r][c] gives the value in the cell at row r, column c.
    // The rows and columns are numbered from 0 to DIM-1.
    private int[][] values;
    
    // Indicates whether the value in a given cell is fixed 
    // (i.e., part of the original puzzle).
    // valIsFixed[r][c] is true if the value in the cell 
    // at row r, column c is fixed, and valIsFixed[r][c] is false 
    // if the value in that cell is not fixed.
    private boolean[][] valIsFixed;

    // These matrices allow us to determine if a given
    // row or column already contains a given value.
    // For example, rowHasValue[3][4] will be true if row 3
    // already has a 4 in it.
    private boolean[][] rowHasValue;
    private boolean[][] colHasValue;
    
    private boolean[][] subGridHasValue;

    /** 
     * Constructs a new Puzzle object, which initially
     * has all empty cells.
     */
    public Puzzle() {
        values = new int[DIM][DIM];
        valIsFixed = new boolean[DIM][DIM + 1];
        rowHasValue = new boolean[DIM][DIM + 1];
        colHasValue = new boolean[DIM][DIM + 1];
        // 9 possible subgrids with 9 possible values each
        subGridHasValue = new boolean[DIM ][DIM + 1];        

    }
    
    /**
     * This is the key recursive-backtracking method.
     * Returns true if a solution has been found, and false otherwise.
     * 
     * Each invocation of the method is responsible for finding the
     * value of a single cell of the puzzle. The parameter n
     * is the number of the cell that a given invocation of the method
     * is responsible for. We recommend that you consider the cells
     * one row at a time, from top to bottom and left to right,
     * which means that they would be numbered as follows:
     * 
     *     0  1  2  3  4  5  6  7  8
     *     9 10 11 12 13 14 15 16 17
     *    18 ...
     */
    public boolean solve(int n) {
        if (n > 80)
        	return true;
        
        // calculates current row and column from the value n
        int currentRow = n / 9;
        int currentCol = n - currentRow * 9;
        
        // loop through possible values
        for (int i = 0; i < 9; i++)
        {
        	// skip over if the value is fixed 
        	if (valIsFixed[currentRow][currentCol])
        	{
        		if (solve(n + 1))
        			return true;
        		else 
        			return false;
        	}
        	if( !(rowHasValue[currentRow][i + 1] || colHasValue[currentCol][i + 1] || subGridHasValue[(currentRow/3)*2 + (currentCol/3 + currentRow/3)][i + 1]))
        		{
        	   	    placeVal(i + 1, currentRow, currentCol);
        			if (solve(n + 1))
        				return true;
        			removeVal(i + 1, currentRow, currentCol);
        		}
        }
        return false;
    }
    
    /**
     * place the specified value in the cell with the
     * specified coordinates, and update the state of
     * the puzzle accordingly.
     */
    public void placeVal(int val, int row, int col) {
        values[row][col] = val;
        rowHasValue[row][val] = true;
        colHasValue[col][val] = true;

        // subgrids are counted starting at index 0 for the first one
        // the outer array calculates the subgrid in which the current spot exists
        // based on the values of its row and column
        subGridHasValue[(row/3)*2 + (col/3 + row/3)][val] = true;
    }
    
    /**
     * remove the specified value from the cell with the
     * specified coordinates, and update the state of
     * the puzzle accordingly.
     */
    public void removeVal(int val, int row, int col) {
        values[row][col] = 0;
        rowHasValue[row][val] = false;
        colHasValue[col][val] = false;
        subGridHasValue[(row/3)*2 + (col/3 + row/3)][val] = false;
        
    }
    
    /**
     * Reads in a puzzle specification from the specified Scanner,
     * and uses it to initialize the state of the puzzle.  The
     * specification should consist of one line for each row, with the
     * values in the row specified as digits separated by spaces.  A
     * value of 0 should be used to indicate an empty cell.
     */ 
    public void readFrom(Scanner input) {
        for (int r = 0; r < DIM; r++) {
            for (int c = 0; c < DIM; c++) {
                int val = input.nextInt();
                placeVal(val, r, c);
                if (val != 0)
                    valIsFixed[r][c] = true;
            }
            input.nextLine();
        }
    }
    
    /**
     * Displays the current state of the puzzle.
     * You should not change this method.
     */
    public void display() {
        for (int r = 0; r < DIM; r++) {
            printRowSeparator();
            for (int c = 0; c < DIM; c++) {
                System.out.print("|");
                if (values[r][c] == 0)
                    System.out.print("   ");
                else
                    System.out.print(" " + values[r][c] + " ");
            }
            System.out.println("|");
        }
        printRowSeparator();
    }
    
    // A private helper method used by display() 
    // to print a line separating two rows of the puzzle.
    private static void printRowSeparator() {
        for (int i = 0; i < DIM; i++)
            System.out.print("----");
        System.out.println("-");
    }
}
