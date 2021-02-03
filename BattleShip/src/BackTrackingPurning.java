
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BackTrackingPurning {

    boolean answer = false;
    Puzzle puzzle;
    List<Integer> ships_to_place = new ArrayList<>();
    List<Integer> ship_row = new ArrayList<>();
    List<Integer> ship_col = new ArrayList<>();
    List<Integer> ship_orien = new ArrayList<>();
    int current_row_totals[];
    int current_column_totals[];

    public BackTrackingPurning(Puzzle puzzle) {
        this.puzzle = puzzle;
        for (int i = 0; i < puzzle.ships.length; i++) {
            ships_to_place.add(puzzle.ships[i]);
        }
        ships_to_place = reverseArrayList((ArrayList<Integer>) ships_to_place);
        //shuffleArray((ArrayList<Integer>) ships_to_place);
        current_row_totals = new int[puzzle.m];
        current_column_totals = new int[puzzle.n];
        Arrays.fill(current_row_totals, 0);
        Arrays.fill(current_column_totals, 0);
        answer = backTrack();
    }

    private boolean backTrack() {
        if (ships_to_place.isEmpty()) {
            if (puzzle.respects_indicators() == false) {
                return false;
            }
            return true;
        }
        for (int i = 0; i < (this.ships_to_place).size(); i++) {
            int size = this.ships_to_place.get(i);
            for (int z = 0; z < 2; z++) {
                List<List<Integer>> l = get_choice_set(size, z);
                for (int j = 0; j < l.size(); j++) {
                    int row = l.get(j).get(0);
                    int col = l.get(j).get(1);
                    boolean placed = puzzle.place_ship(row, col, size, z);
                    if (placed == true) {
                        update_totals(0, row, col, size, z);
                        this.ship_row.add(row);
                        this.ship_col.add(col);
                        this.ship_orien.add(z);
                        this.ships_to_place.remove(i);
                        boolean result = backTrack();
                        if (result == true) {
                            return true;
                        } else {
                            this.ships_to_place.add(i, size);
                            int pRow = this.ship_row.remove(this.ship_row.size() - 1);
                            int pCol = this.ship_col.remove(this.ship_col.size() - 1);
                            int pZ = this.ship_orien.remove(this.ship_orien.size() - 1);
                            puzzle.remove_ship(pRow, pCol, size, pZ);
                            update_totals(1, pRow, pCol, size, pZ);
                        }
                    }
                }
            }
        }
        return false;
    }

    private List<List<Integer>> get_choice_set(int size, int orien) {
        List<List<Integer>> cells = new ArrayList<List<Integer>>();
        int idx = 0;
        for (int row = 0; row < puzzle.m; row++) {
            for (int col = 0; col < puzzle.n; col++) {
                boolean adjacent = puzzle.adjacent_nodes(row, col, size, orien);
                boolean valid = true;
                if (puzzle.row_totals[row] == 0) {
                    valid = false;
                } else if (puzzle.col_totals[col] == 0) {
                    valid = false;
                } else if (adjacent == true) {
                    valid = false;
                } else {
                    try {
                        if (orien == 0) {
                            for (int i = col; i < col + size; i++) {
                                if (this.current_column_totals[i] + 1 > puzzle.col_totals[i]) {
                                    valid = false;
                                }
                            }
                            if (this.current_row_totals[row] + size > puzzle.row_totals[row]) {
                                valid = false;
                            }
                        } else {
                            for (int i = row; i < row + size; i++) {
                                if (this.current_row_totals[i] + 1 > puzzle.row_totals[i]) {
                                    valid = false;
                                }
                            }
                            if (this.current_column_totals[col] + size > puzzle.col_totals[col]) {
                                valid = false;
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        valid = false;
                    }
                }
                if (valid == true) {
                    List<Integer> temp = new ArrayList<>();
                    temp.add(row);
                    temp.add(col);
                    cells.add(temp);
                }
            }
        }
        return cells;
    }

    private void update_totals(int add_or_remove, int row, int col, int size, int orien) {
        if (orien == 0) {
            for (int i = col; i < col + size; i++) {
                if (add_or_remove == 0) {
                    this.current_column_totals[i]+=1;
                } else {
                    this.current_column_totals[i]-=1;
                }
            }
            if (add_or_remove == 0) {
                this.current_row_totals[row] += size;
            } else {
                this.current_row_totals[row] -= size;
            }
        } else {
            for (int i = row; i < row + size; i++) {
                if (add_or_remove == 0) {
                    this.current_row_totals[i]+=1;
                } else {
                    this.current_row_totals[i]-=1;
                }
            }
            if (add_or_remove == 0) {
                this.current_column_totals[row] += size;
            } else {
                this.current_column_totals[row] -= size;
            }
        }
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
    
    public void shuffleArray(ArrayList<Integer> alist) {
        int index;
        int temp;
        Random random = new Random();
        for (int i = alist.size() - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = alist.get(index);
            alist.add(index, alist.get(i));
            alist.add(i, temp);
        }
    }
}
