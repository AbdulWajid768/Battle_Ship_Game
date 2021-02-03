
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FF {

    boolean answer = false;
    Puzzle puzzle;
    List<Integer> ships_to_place = new ArrayList<>();
    List<Integer> ship_row = new ArrayList<>();
    List<Integer> ship_col = new ArrayList<>();
    List<Integer> ship_orien = new ArrayList<>();
    int current_row_totals[];
    int current_column_totals[];

    public FF(Puzzle puzzle) {
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
    }

    public boolean run() {
        for (int i = 0; i < (this.ships_to_place).size(); i++) {
            int size = this.ships_to_place.get(i);
            int l[] = firstFit(size);
            if (l == null) {
                return false;
            } else {
                int row = l[0];
                int col = l[1];
                int orien = l[2];
                boolean placed = puzzle.place_ship(row, col, size, orien);
                if (placed == true) {
                    update_totals(0, row, col, size, orien);
                    this.ship_row.add(row);
                    this.ship_col.add(col);
                    this.ship_orien.add(orien);
                } else {
                    return false;
                }
            }
        }
        if (puzzle.respects_indicators() == false) {
            return false;
        }
        return false;
    }

    private void update_totals(int add_or_remove, int row, int col, int size, int orien) {
        if (orien == 0) {
            for (int i = col; i < col + size; i++) {
                if (add_or_remove == 0) {
                    this.current_column_totals[i] += 1;
                } else {
                    this.current_column_totals[i] -= 1;
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
                    this.current_row_totals[i] += 1;
                } else {
                    this.current_row_totals[i] -= 1;
                }
            }
            if (add_or_remove == 0) {
                this.current_column_totals[col] += size;
            } else {
                this.current_column_totals[col] -= size;
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

    private int[] firstFit(int size) {
        for (int row = 0; row < puzzle.m; row++) {
            for (int col = 0; col < puzzle.n; col++) {
                for (int orien = 0; orien < 2; orien++) {
                    if (puzzle.row_totals[row] == 0) {
                        break;
                    } else if (puzzle.col_totals[col] == 0) {
                        break;
                    }
                    boolean valid = true;
                    boolean empty = puzzle.empty_nodes(row, col, size, orien);
                    if (empty == false) {
                        valid = false;
                    } else {
                        boolean adjacent = puzzle.adjacent_nodes(row, col, size, orien);
                        if (adjacent == true) {
                            valid = false;
                        }
                        if (adjacent == true) {
                            try {
                                if (orien == 0) {
                                    for (int i = col; i < col + size; i++) {
                                        if (current_column_totals[i] + 1 > puzzle.col_totals[i]) {
                                            valid = false;
                                        }
                                    }
                                    if (current_row_totals[row] + size > puzzle.row_totals[row]) {
                                        valid = false;
                                    }
                                } else {
                                    for (int i = row; i < row + size; i++) {
                                        if (current_row_totals[i] + 1 > puzzle.row_totals[i]) {
                                            valid = false;
                                        }
                                    }
                                    if (current_column_totals[col] + size > puzzle.col_totals[col]) {
                                        valid = false;
                                    }
                                }
                            } catch (ArrayIndexOutOfBoundsException e) {
                                valid = false;
                            }
                        }
                    }
                    if(valid == true){
                        int data[] = {row,col,orien};
                        return data;
                    }
                }
            }
        }
        return null;
    }

}
