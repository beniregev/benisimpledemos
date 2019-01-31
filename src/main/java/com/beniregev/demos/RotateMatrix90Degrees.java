package com.beniregev.demos;

/**
 * Given a (N x N) matrix (N been the matrix size) populated by numbers 1..n^2
 * Method {@link #rotate90degrees} rotates the matrix by 90 degrees clockwise.
 */
public class RotateMatrix90Degrees {
    private final int matrixSize = 5;
    private int[][] matrix = new int[matrixSize][matrixSize];

    /**
     * Default Constructor populates the maxtix and prints the matrix initial state.
     */
    public RotateMatrix90Degrees() {
        populateMatrix();
        printMatrix();
    }

    /**
     * Rotate the matrix 90 degrees clock wise.
     */
    void rotate90degrees() {

    }

    /**
     * Reset the maxtrix, populate it with initial state values.
     */
    void populateMatrix() {
        for(int r=0; r < matrixSize; r++) {
            for(int c=0; c < matrixSize; c++) {
                matrix[r][c] = matrixSize * r + c + 1;
            }
        }
    }

    /**
     * Prints the matrix current state.
     */
    void printMatrix() {
        for(int r=0; r < matrixSize; r++) {
            for(int c=0; c < matrixSize; c++) {
                System.out.print(String.format("  %d", matrix[r][c]));
            }
            System.out.println(' ');
        }
        System.out.println("\n");
    }

    public static void main(String[] args) {
        RotateMatrix90Degrees matrix = new RotateMatrix90Degrees();
        System.out.println("Matrix has been created");
    }
}
