
import java.util.Arrays;
import java.util.Random;

public final class Puzzle {

    char grid[][];
    char populated_grid[][];
    int row_totals[];
    int col_totals[];
    int n = 10;
    int m = 10;
    int ships[];
    char empty = '-';
    char node = 'o';

    int battleship_size = 4;
    int cruiser_size = 3;
    int destroyer_size = 2;
    int submarine_size = 1;

    int battleships;
    int cruisers;
    int destroyers;
    int submarines;

    public Puzzle(int n, int m, int battleships_count, int cruisers_count, int destroyers_count, int submarines_count) {
        this.n = n;
        this.m = m;
        this.battleships = battleships_count;
        this.cruisers = cruisers_count;
        this.destroyers = destroyers_count;
        this.submarines = submarines_count;
        grid = new char[m][];
        for (int i = 0; i < m; i++) {
            grid[i] = new char[n];
            Arrays.fill(grid[i], empty);
        }
        populated_grid = new char[m][];
        for (int i = 0; i < m; i++) {
            populated_grid[i] = new char[n];
            Arrays.fill(populated_grid[i], empty);
        }

        row_totals = new int[m];
        col_totals = new int[n];
        Arrays.fill(row_totals, 0);
        Arrays.fill(col_totals, 0);
        build_ship_list();

    }

    public void build_ship_list() {
        ships = new int[this.battleships + this.cruisers + this.destroyers + this.submarines];
        int j = 0;
        for (int i = 0; i < this.battleships; i++) {
            this.ships[j] = this.battleship_size;
            j++;
        }
        for (int i = 0; i < this.cruisers; i++) {
            this.ships[j] = this.cruiser_size;
            j++;
        }
        for (int i = 0; i < this.destroyers; i++) {
            this.ships[j] = this.destroyer_size;
            j++;
        }
        for (int i = 0; i < this.submarines; i++) {
            this.ships[j] = this.submarine_size;
            j++;
        }
    }

    public void generate() {
        boolean placed = false;
        int i;
        while (true) {
            for (i = 0; i < ships.length; i++) {
                placed = false;
                int size = ships[i];
                int cells[][] = get_empty_cells();
                for (int j = 0; j < cells.length; j++) {
                    int row = cells[j][0];
                    int col = cells[j][1];
                    Random random = new Random();
                    int orien = random.nextInt(2);
                    boolean adjacent = adjacent_nodes(row, col, size, orien);
                    if (adjacent == false) {
                        placed = place_ship(row, col, size, orien);
                    }
                    if (placed == false) {
                        adjacent = adjacent_nodes(row, col, size, 1 - orien);
                        if (adjacent == false) {
                            placed = place_ship(row, col, size, 1 - orien);
                        }
                    }
                    if (placed == true) {
                        break;
                    }
                }
                if (placed == false) {
                    break;
                }
            }
            if (i == (this.ships.length) && placed == true) {
                set_totals();
                copy_grid();
                erase_grid();
                return;
            } else {
                erase_grid();
            }
        }
    }

    public void randomly_generate() {
        boolean placed = false;
        int i = 0;
        while (true) {
            for (i = 0; i < ships.length; i++) {
                placed = false;
                int size = ships[i];
                int cells[][] = get_empty_cells();
                for (int j = 0; j < cells.length; j++) {
                    int row = cells[j][0];
                    int col = cells[j][1];
                    Random random = new Random();
                    int orien = random.nextInt(2);
                    placed = place_ship(row, col, size, orien);
                    if (placed == false) {
                        placed = place_ship(row, col, size, 1 - orien);
                    }
                    if (placed == true) {
                        break;
                    }
                }
                if (placed == false) {
                    break;
                }
            }
            if (i == ((this.ships).length) && placed == true) {
                set_totals();
                copy_grid();
                erase_grid();
                return;
            } else {
                erase_grid();
            }
        }
    }

    private void set_totals() {
        for (int i = 0; i < m; i++) {
            this.row_totals[i] = get_row_total(i);
        }
        for (int i = 0; i < m; i++) {
            this.col_totals[i] = get_col_total(i);
        }
    }

    private void copy_grid() {
        for (int i = 0; i < m; i++) {
            System.arraycopy(this.grid[i], 0, this.populated_grid[i], 0, n);
        }
    }

    private void erase_grid() {
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.grid[i][j] = this.empty;
            }
        }
    }

    private int[][] get_empty_cells() {
        int size = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (this.grid[i][j] == this.empty) {
                    size++;
                }
            }
        }
        int cells[][] = new int[size][2];
        int k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (this.grid[i][j] == this.empty) {
                    cells[k][0] = i;
                    cells[k][1] = j;
                    k++;
                }
            }
        }
        shuffleArray(cells);
        return cells;
    }

    public boolean adjacent_nodes(int row, int col, int size, int orien) {
        if (orien == 0) {
            if (get_node(row, col + size) != this.empty) {
                return true;
            }
            if (get_node(row, col - 1) != this.empty) {
                return true;
            }
            for (int i = -1; i < size + 1; i++) {
                if (get_node(row + 1, col + i) != this.empty) {
                    return true;
                }
                if (get_node(row - 1, col + i) != this.empty) {
                    return true;
                }
            }
        } else {
            if (get_node(row + size, col) != this.empty) {
                return true;
            }
            if (get_node(row - 1, col) != this.empty) {
                return true;
            }
            for (int i = -1; i < size + 1; i++) {
                if (get_node(row + i, col + 1) != this.empty) {
                    return true;
                }
                if (get_node(row + i, col - 1) != this.empty) {
                    return true;
                }
            }
        }
        return false;
    }

    public char get_node(int row, int col) {
        if (row < 0 || col < 0) {
            return this.empty;
        }
        char value;
        try {
            value = this.grid[row][col];
        } catch (ArrayIndexOutOfBoundsException e) {
            value = this.empty;
        }
        return value;
    }

    public boolean place_ship(int row, int col, int size, int orien) {
        try {
            if (orien == 0) {
                for (int j = col; j < col + size; j++) {
                    if (this.grid[row][j] != this.empty) {
                        return false;
                    }
                }
                for (int j = col; j < col + size; j++) {
                    this.grid[row][j] = this.node;
                }
            } else {
                for (int i = row; i < row + size; i++) {
                    if (this.grid[i][col] != this.empty) {
                        return false;
                    }
                }
                for (int i = row; i < row + size; i++) {
                    this.grid[i][col] = this.node;
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    public void remove_ship(int row, int col, int size, int orien) {
        if (orien == 0) {
            for (int i = col; i < col + size; i++) {
                this.grid[row][i] = this.empty;
            }
        } else {
            for (int i = row; i < row + size; i++) {
                this.grid[i][col] = this.empty;
            }
        }
    }

    private int get_row_total(int row) {
        int total = 0;
        for (int i = 0; i < n; i++) {
            if (this.grid[row][i] != this.empty) {
                total++;
            }
        }
        return total;
    }

    private int get_col_total(int col) {
        int total = 0;
        for (int i = 0; i < m; i++) {
            if (this.grid[i][col] != this.empty) {
                total++;
            }
        }
        return total;
    }

    public boolean respects_indicators() {
        for (int i = 0; i < m; i++) {
            if (get_row_total(i) != row_totals[i]) {
                return false;
            }
        }
        for (int j = 0; j < n; j++) {
            if (get_col_total(j) != col_totals[j]) {
                return false;
            }
        }
        return true;
    }

    public void print_alg_solution() {
        System.out.print("  ");
        for (int i = 0; i < n; i++) {
            System.out.print(col_totals[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < m; i++) {
            System.out.print(row_totals[i] + " ");
            for (int j = 0; j < n; j++) {
                System.out.print(this.grid[i][j] + " ");
            }
            System.out.println();
        }

    }

    public void print_solution() {
        System.out.print("  ");
        for (int i = 0; i < n; i++) {
            System.out.print(col_totals[i] + " ");
        }
        System.out.println();
        for (int i = 0; i < m; i++) {
            System.out.print(row_totals[i] + " ");
            for (int j = 0; j < n; j++) {
                System.out.print(this.populated_grid[i][j] + " ");
            }
            System.out.println();
        }

    }

    private static void shuffleArray(int[][] array) {
        int index;
        int temp[];
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public boolean empty_nodes(int row, int col, int size, int orien) {
        try {
            if (orien == 0) {
                for (int j = col; j < col + size; j++) {
                    if (this.grid[row][j] != this.empty) {
                        return false;
                    }
                }
            } else {
                for (int i = row; i < row + size; i++) {
                    if (this.grid[i][col] != this.empty) {
                        return false;
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }
        return true;
    }

    private void elseif(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
