
import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author abdul
 */
public class Driver {

    public static void main(String[] args) {
        //To give Row Constraints.
        int row[] = {1, 3, 1, 2, 1, 2};
        //To give Column Constraints.
        int col[] = {1, 3, 1, 2, 2, 1};
        char grid[][] = new char[10][10];
        for (int i = 0; i < 10; i++) {
            Arrays.fill(grid[i], '-');
        }
        //To give Pieces of Grid
//        grid[0][9] = 'o';
//        grid[4][2] = 'o';
//        grid[8][8] = 'o';
//        grid[0][3] = 'o';

        Puzzle p = new Puzzle(6, 6, 0, 1, 2, 3);
 
        for (int i = 0; i < 6; i++) {
            p.row_totals[i] = row[i];
            p.col_totals[i] = col[i];
        }

        BackTrackingPurning b = new BackTrackingPurning(p);
        System.out.println("\n\nCreating Puzzle Solution by Using Back Tracking");
        p.print_alg_solution();

    }

}
