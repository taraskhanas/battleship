package com.example.battleship_solver;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class HelloController {

    @FXML
    private GridPane battleGrid;

    @FXML
    private GridPane mapGrid;

    @FXML
    private Button placeShipsButton;

    @FXML
    private Button solveButton;

    // Game board representation
    private int[][] board = new int[10][10];
    private int[][][] map = new int[10][10][2];

    private final int HIT = 3;
    private final int SHIP = 2;
    private final int MISS = 1;
    private final int EMPTY = 0;

    @FXML
    public void initialize() {
        initializeGrid();
        initializeMap();
    }

    @FXML
    private void initializeGrid() {
        battleGrid.getChildren().clear(); // Clear existing children if any
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.LIGHTBLUE);
                rect.setStroke(Color.BLACK);
                Label label = new Label();
                label.setTextFill(Color.BLACK);
                label.setText(String.valueOf(i) + ", " + String.valueOf(j));

                StackPane stack = new StackPane(); // StackPane to hold the rectangle and text
                stack.getChildren().addAll(rect, label); // Add both rectangle and text to stack

                battleGrid.add(stack, j, i); // Add the stack to the grid at the appropriate position
            }
        }
    }
    @FXML
    private void initializeMap() {
        mapGrid.getChildren().clear(); // Clear existing children if any
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                Rectangle rect = new Rectangle(30, 30);
                rect.setFill(Color.LIGHTGRAY);
                rect.setStroke(Color.BLACK);
                Label label = new Label();
                label.setTextFill(Color.BLACK);
                label.setText(String.valueOf(0));

                StackPane stack = new StackPane(); // StackPane to hold the rectangle and text
                stack.getChildren().addAll(rect, label); // Add both rectangle and text to stack

                mapGrid.add(stack, j, i); // Add the stack to the grid at the appropriate position
            }
        }
    }

    private void updateGrid() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                StackPane stack = (StackPane) battleGrid.getChildren().get(i * 10 + j);
                Rectangle rect = (Rectangle) stack.getChildren().get(0);
                if (board[i][j] == SHIP) {
                    rect.setFill(Color.GRAY); // Ship color
                } else {
                    rect.setFill(Color.LIGHTBLUE); // Water color
                }
            }
        }
    }
    private void updateMap() {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                StackPane stack = (StackPane) mapGrid.getChildren().get(i * 10 + j);
                Rectangle rect = (Rectangle) stack.getChildren().get(0);
                Label label = (Label) stack.getChildren().get(1); // Access the label within the StackPane
                label.setText(String.valueOf(map[i][j][0])); // Update the label text
                if (map[i][j][1] == HIT) {
                    rect.setFill(Color.RED); // Ship color
                } else if(map[i][j][1] == MISS){
                    rect.setFill(Color.DARKBLUE); // Miss color
                } else {
                    rect.setFill(Color.LIGHTGRAY); // Water color
                }
            }
        }
    }


    @FXML
    private void onPlaceShips(){
        Random rnd = new Random();

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                board[i][j] = EMPTY;
            }
        }
        updateGrid();
        int[] shipSizes = {4,3,3,2,2,2}; // Sizes of the ships to place
        System.out.println("--------");
        for (int size : shipSizes) {
            boolean placed = false;
            while (!placed) {
                int x = rnd.nextInt(10);
                int y = rnd.nextInt(10);

                if(board[x][y] == EMPTY){
                    canPlaceShip(x, y, size);
                    if(facesList.size() != 0){
//                        System.out.println(facesList);
//                        System.out.println(facesList.size());
                        String face = facesList.get(rnd.nextInt(facesList.size()));
//                        System.out.println("+" + face);
                        switch (face){
                            case "West":
                                for (int i = 0; i < size; i++) {
                                    board[x][y-i] = SHIP;
                                }
                                placed = true;
                                break;
                            case "East":
                                for (int i = 0; i < size; i++) {
                                    board[x][y+i] = SHIP;
                                }
                                placed = true;
                                break;
                            case "North":
                                for (int i = 0; i < size; i++) {
                                    board[x-i][y] = SHIP;
                                }
                                placed = true;
                                break;
                            case "South":
                                for (int i = 0; i < size; i++) {
                                    board[x+i][y] = SHIP;
                                }
                                placed = true;
                                break;
                            default:
                                break;
                        }
                    }
                }
                System.out.println(x + "," + y);
                updateGrid();
            }
        }

    }

    ArrayList<String> facesList = new ArrayList<>();
    private void canPlaceShip(int x, int y, int size) {
        facesList.clear();
        int x1,y1,x2,y2;

        System.out.println("-" + (x - (size - 1) > 0));

//        boolean forwardEmpty = true;
//        for (int i = 0; i < size; i++) {
//            if(board[x-i][y] == MISS){
//                forwardEmpty = false;
//                break;
//            }
//        }
        if (x - (size - 1) >= 0) {
            x1 = Math.max(0, x - size);
            y1 = Math.max(0, y - 1);
            x2 = Math.min(9, x + 1);
            y2 = Math.min(9, y + 1);
//            System.out.println("North " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("North"); }
        }

        System.out.println("-" + (x + (size - 1) < 10));
        if(x + (size - 1) < 10) {
            x1 = Math.max(0, x - 1);
            y1 = Math.max(0, y - 1);
            x2 = Math.min(9, x + size);
            y2 = Math.min(9, y + 1);
//            System.out.println("South " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("South"); }
        }

        System.out.println("-" + (y - (size - 1) > 0));
        if(y - (size - 1) >= 0) {
            x1 = Math.max(0, x - 1);
            y1 = Math.max(0, y - size);
            x2 = Math.min(9, x + 1);
            y2 = Math.min(9, y + 1);
//            System.out.println("West " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("West"); }
        }

//        System.out.println("-" + (y + (size - 1) < 10));
        if(y + (size - 1) < 10) {
            x1 = Math.max(0, x - 1);
            y1 = Math.max(0, y - 1);
            x2 = Math.min(9, x + 1);
            y2 = Math.min(9, y + size);
            System.out.println("East " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
//            System.out.println("=Check=");
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
//                    System.out.println(j + " " + i + " : " + board[j][i]);
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("East"); }
        }
    }

    private void canPlaceShipMap(int x, int y, int size) {
        facesList.clear();
        int x1,y1,x2,y2;

        System.out.println("-" + (x - (size - 1) > 0));

//        boolean forwardEmpty = true;
//        for (int i = 0; i < size; i++) {
//            if(board[x-i][y] == MISS){
//                forwardEmpty = false;
//                break;
//            }
//        }
        if (x - (size - 1) >= 0) {
            x1 = Math.max(0, x - size);
            y1 = Math.max(0, y - 1);
            x2 = Math.min(9, x + 1);
            y2 = Math.min(9, y + 1);
//            System.out.println("North " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("North"); }
        }

        System.out.println("-" + (x + (size - 1) < 10));
        if(x + (size - 1) < 10) {
            x1 = Math.max(0, x - 1);
            y1 = Math.max(0, y - 1);
            x2 = Math.min(9, x + size);
            y2 = Math.min(9, y + 1);
//            System.out.println("South " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("South"); }
        }

        System.out.println("-" + (y - (size - 1) > 0));
        if(y - (size - 1) >= 0) {
            x1 = Math.max(0, x - 1);
            y1 = Math.max(0, y - size);
            x2 = Math.min(9, x + 1);
            y2 = Math.min(9, y + 1);
//            System.out.println("West " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("West"); }
        }

//        System.out.println("-" + (y + (size - 1) < 10));
        if(y + (size - 1) < 10) {
            x1 = Math.max(0, x - 1);
            y1 = Math.max(0, y - 1);
            x2 = Math.min(9, x + 1);
            y2 = Math.min(9, y + size);
            System.out.println("East " + x1 + " " +y1 + " " +x2 + " " +y2);
            boolean allEmpty = true;
//            System.out.println("=Check=");
            for (int i = y1; i <= y2; i++) {
                for (int j = x1; j <= x2; j++) {
//                    System.out.println(j + " " + i + " : " + board[j][i]);
                    if (board[j][i] == SHIP) {
                        allEmpty = false;
                        break;
                    }
                }
                if (!allEmpty) { break; }
            }
            if (allEmpty) { facesList.add("East"); }
        }
    }

    // Method to find the maximum value in a 2D array
    public int getMaxValue(int[][][] array) {
        int maxValue = Integer.MIN_VALUE; // Initialize to the smallest possible value

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j][0] > maxValue) {
                    maxValue = array[i][j][0]; // Update maxValue if current element is greater
                }
            }
        }

        return maxValue;
    }

    // Method to get a random cell with the maximum value
    public int[] getRandomMaxCell(int[][][] array) {
        int maxValue = getMaxValue(array);
        List<int[]> maxCells = new ArrayList<>();

        // Collect all cells with the maximum value
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j][0] == maxValue) {
                    maxCells.add(new int[]{i, j});
                }
            }
        }

        // Choose a random cell from the list of maximum value cells
        Random rnd = new Random();
        return maxCells.get(rnd.nextInt(maxCells.size()));
    }

    @FXML
    private void onSolveGame() {
        Random rnd = new Random();
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j][0] = 0;
            }
        }

        int[] shipSizes = {4, 3, 2}; // Sizes of the ships
        for (int size : shipSizes) {
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    canPlaceShip(i, j, size);
                    map[i][j][0] += facesList.size();
                    StackPane stack = (StackPane) mapGrid.getChildren().get(i * 10 + j);
                    Label label = (Label) stack.getChildren().get(1); // Access the label within the StackPane
                    label.setText(String.valueOf(map[i][j][0])); // Update the label text
                }
            }
        }
        // Get a random cell with the maximum value after updating the map
        int[] randomMaxCell = getRandomMaxCell(map);
        System.out.println("Random cell with max value: (" + randomMaxCell[0] + ", " + randomMaxCell[1] + ")");
        if (board[randomMaxCell[0]][randomMaxCell[1]] == SHIP){
            map[randomMaxCell[0]][randomMaxCell[1]][1] = HIT;
        }
        else{
            map[randomMaxCell[0]][randomMaxCell[1]][1] = MISS;
        }
        updateMap(); // Refresh the map display
    }
}
