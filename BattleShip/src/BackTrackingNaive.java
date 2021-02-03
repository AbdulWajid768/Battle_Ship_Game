
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackTrackingNaive {

    boolean answer = false;
    Puzzle puzzle;
    List<Integer> ships_to_place = new ArrayList<>();
    List<Integer> ship_row = new ArrayList<>();
    List<Integer> ship_col = new ArrayList<>();
    List<Integer> ship_orien = new ArrayList<>();

    public BackTrackingNaive(Puzzle puzzle) {
        this.puzzle = puzzle;
        for (int i = 0; i < puzzle.ships.length; i++) {
            ships_to_place.add(puzzle.ships[i]);
        }
        ships_to_place = reverseArrayList((ArrayList<Integer>) ships_to_place);
        answer = backTrack();
    }

    private boolean backTrack() {
        if (ships_to_place.isEmpty()) {
            if (puzzle.respects_indicators() == false) {
                return false;
            } else if (no_adjacent_ships() == false) {
                return false;
            } else {
                return true;
            }
        }

        for (int i = 0; i < (this.ships_to_place).size(); i++) {
            System.out.println("i="+i);
            int size = this.ships_to_place.get(i);
            for (int row = 0; row < puzzle.m; row++) {
                System.out.println("row="+row);
                for (int col = 0; col < puzzle.n; col++) {
                    System.out.println("col="+col);
                    for (int orien = 0; orien < 2; orien++) {
                        boolean placed = puzzle.place_ship(row, col, size, orien);
                        if (placed == true) {
                            this.ship_row.add(row);
                            this.ship_col.add(col);
                            this.ship_orien.add(orien);
                            this.ships_to_place.remove(i);
                            boolean result = backTrack();
                            if (result == true) {
                                return true;
                            } else {
                                this.ships_to_place.add(i, size);
                                int pRow = this.ship_row.remove(this.ship_row.size() - 1);
                                System.out.println("pr=" + pRow);
                                int pCol = this.ship_col.remove(this.ship_col.size() - 1);
                                System.out.println("pc=" + pCol);
                                int pOrien = this.ship_orien.remove(this.ship_orien.size() - 1);
                                System.out.println("po="+pOrien );
                                puzzle.remove_ship(pRow, pCol, size, pOrien);
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public ArrayList<Integer> reverseArrayList(ArrayList<Integer> alist) {
        // Arraylist for storing reversed elements 
        ArrayList<Integer> revArrayList = new ArrayList<Integer>();
        for (int i = alist.size() - 1; i >= 0; i--) {

            // Append the elements in reverse order 
            revArrayList.add(alist.get(i));
        }

        // Return the reversed arraylist 
        return revArrayList;

    }

    private boolean no_adjacent_ships() {
        for (int i = 0; i < puzzle.ships.length; i++) {
            int size = puzzle.ships[i];
            int row = this.ship_row.get(i);
            int col = this.ship_col.get(i);
            int orien = this.ship_orien.get(i);
            if (orien == 0) {
                if (puzzle.get_node(row, col + size) != puzzle.empty) {
                    return false;
                }
                if (puzzle.get_node(row, col - 1) != puzzle.empty) {
                    return false;
                }
                for (int j = -1; j < size + 1; j++) {
                    if (puzzle.get_node(row + 1, col + j) != puzzle.empty) {
                        return false;
                    }
                    if (puzzle.get_node(row - 1, col + j) != puzzle.empty) {
                        return false;
                    }
                }
            } else {
                if (puzzle.get_node(row + size, col) != puzzle.empty) {
                    return false;
                }
                if (puzzle.get_node(row - 1, col) != puzzle.empty) {
                    return false;
                }
                for (int j = -1; j < size + 1; j++) {
                    if (puzzle.get_node(row + i, col + 1) != puzzle.empty) {
                        return false;
                    }
                    if (puzzle.get_node(row + i, col - 1) != puzzle.empty) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
