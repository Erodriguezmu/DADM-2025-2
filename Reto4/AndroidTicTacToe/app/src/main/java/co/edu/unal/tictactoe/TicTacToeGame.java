package co.edu.unal.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TicTacToeGame {

    public static final char HUMAN_PLAYER = 'X';
    public static final char COMPUTER_PLAYER = 'O';
    public static final char OPEN_SPOT = ' ';
    public static final int BOARD_SIZE = 9;

    // The computer's difficulty levels
    public enum DifficultyLevel { Easy, Harder, Expert };

    private char[] mBoard;
    private Random mRand;
    private DifficultyLevel mDifficultyLevel = DifficultyLevel.Expert;

    public TicTacToeGame() {
        mBoard = new char[BOARD_SIZE];
        mRand = new Random();
        clearBoard();
    }

    public void clearBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            mBoard[i] = OPEN_SPOT;
        }
    }

    public void setMove(char player, int location) {
        if (location >= 0 && location < BOARD_SIZE && mBoard[location] == OPEN_SPOT) {
            mBoard[location] = player;
        }
    }

    public DifficultyLevel getDifficultyLevel() {
        return mDifficultyLevel;
    }

    public void setDifficultyLevel(DifficultyLevel difficultyLevel) {
        mDifficultyLevel = difficultyLevel;
    }

    public int getComputerMove() {
        int move = -1;

        if (mDifficultyLevel == DifficultyLevel.Easy) {
            move = getRandomMove();
        } else if (mDifficultyLevel == DifficultyLevel.Harder) {
            move = getWinningMove();
            if (move == -1) move = getRandomMove();
        } else { // Expert
            move = getWinningMove();
            if (move == -1) move = getBlockingMove();
            if (move == -1) move = getRandomMove();
        }

        return move;
    }

    private int getRandomMove() {
        List<Integer> available = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) available.add(i);
        }
        if (available.size() > 0) return available.get(mRand.nextInt(available.size()));
        return -1;
    }

    private int getWinningMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) {
                mBoard[i] = COMPUTER_PLAYER;
                int result = checkForWinner();
                mBoard[i] = OPEN_SPOT;
                if (result == 3) return i; // O would win
            }
        }
        return -1;
    }

    private int getBlockingMove() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) {
                mBoard[i] = HUMAN_PLAYER;
                int result = checkForWinner();
                mBoard[i] = OPEN_SPOT;
                if (result == 2) return i; // X would win next, so block
            }
        }
        return -1;
    }

    /**
     * Check for a winner and return:
     * 0 -> no winner yet
     * 1 -> tie
     * 2 -> X (human) won
     * 3 -> O (computer) won
     */
    public int checkForWinner() {
        // Check rows
        for (int i = 0; i <= 6; i += 3) {
            if (mBoard[i] == mBoard[i+1] && mBoard[i+1] == mBoard[i+2]) {
                if (mBoard[i] == HUMAN_PLAYER) return 2;
                else if (mBoard[i] == COMPUTER_PLAYER) return 3;
            }
        }
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (mBoard[i] == mBoard[i+3] && mBoard[i+3] == mBoard[i+6]) {
                if (mBoard[i] == HUMAN_PLAYER) return 2;
                else if (mBoard[i] == COMPUTER_PLAYER) return 3;
            }
        }
        // Diagonals
        if (mBoard[0] == mBoard[4] && mBoard[4] == mBoard[8]) {
            if (mBoard[0] == HUMAN_PLAYER) return 2;
            else if (mBoard[0] == COMPUTER_PLAYER) return 3;
        }
        if (mBoard[2] == mBoard[4] && mBoard[4] == mBoard[6]) {
            if (mBoard[2] == HUMAN_PLAYER) return 2;
            else if (mBoard[2] == COMPUTER_PLAYER) return 3;
        }
        // Check for any open spots
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (mBoard[i] == OPEN_SPOT) return 0; // game not over
        }
        return 1; // tie
    }
}
