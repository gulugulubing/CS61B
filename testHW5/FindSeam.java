import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class FindSeam {
    public int height() {
        return 6;
    }

    public int width() {
        return 4;
    }

    public int[] findVerticalSeam(double[][] energyMatrix) {
        int[] seam = new int[this.height()];



        //minim energy to this pix
        double[][] minEnergy = new double[this.width()][this.height()];
        //edge to this pix represented by column
        //first row is useless
        int[][] edgeTo = new int[this.width()][this.height()];

        //initial minEnergy's first row
        for (int col = 0; col < this.width(); col++) {
            minEnergy[col][0] = energyMatrix[col][0];
        }

        //calculate the rest minEnergy
        for(int row = 1; row < this.height(); row++) {
            for(int col = 0; col < this.width();col++) {
                //System.out.println(energyMatrix[col][row]);
                minEnergy[col][row] = energyMatrix[col][row] + minEdgeToEnergy(col, row, minEnergy, edgeTo);
            }
        }
        for (int col = 0; col < this.width(); col++) {
            System.out.println(minEnergy[col][5]);
        }

        //get the bottom pix at the seam
        double minBottomEnergy = Double.MAX_VALUE;
        int bottomCol = -1;
        for (int col = 0; col < this.width(); col++) {
            if (minEnergy[col][this.height() - 1] < minBottomEnergy) {
                bottomCol = col;
                minBottomEnergy = minEnergy[col][this.height() - 1];
            }
        }

        System.out.println("boCol: "+bottomCol);
        seam[this.height() - 1] = bottomCol;
        //from bottom pix to the top
        for (int row = this.height() - 1; row > 0; row --) {
            seam[row - 1] = edgeTo[bottomCol][row];
            bottomCol = seam[row - 1];
        }
        return seam;
    }

    //help find the right edge
    private double minEdgeToEnergy(int col, int row, double[][]minEnergy, int[][] edgeTo) {
        double middleEdgeEnergy = minEnergy[col][row - 1];
        double leftEdgeEnergy;
        double rightEdgeEnergy;
        if (col == 0) {
            leftEdgeEnergy = Double.MAX_VALUE;
        } else {
            leftEdgeEnergy = minEnergy[col - 1][row - 1];
        }
        if (col == this.width() - 1) {
            rightEdgeEnergy = Double.MAX_VALUE;
        } else {
            rightEdgeEnergy = minEnergy[col + 1][row - 1];
        }

        if (leftEdgeEnergy <= rightEdgeEnergy && leftEdgeEnergy <= middleEdgeEnergy) {
            edgeTo[col][row] = col - 1;
            return leftEdgeEnergy;
        } else if (rightEdgeEnergy <= leftEdgeEnergy && rightEdgeEnergy <= middleEdgeEnergy) {
            edgeTo[col][row] = col + 1;
            return rightEdgeEnergy;
        } else {
            edgeTo[col][row] = col;
            return middleEdgeEnergy;
        }
    }

    public static void main(String[] args) {
        double[][] energyMatrix1 = {{7.00, 22.00, 7.00, 13.00},
                {11.00, 8.00, 6.00,  7.00 },
                {4.00 , 14.00,8.00, 8.00},
                {19.00, 8.00, 18.00 ,  8.00},
                {18.00, 46.00, 16.00,  23.00},
                {4.00,  15.00 , 4.00 , 18.00 }};
        double[][] energyMatrix = {{7.00, 11.00, 4.00, 19.00, 18.00, 4.00},
                {22.00, 8.00, 14.00,  8.00, 46, 15},
                {7.00 , 6.00,8.00, 18.00, 16, 4},
                {13.00, 7.00, 8.00 , 8.00, 23, 18}};
        FindSeam fs = new FindSeam();
        int[] seam = fs.findVerticalSeam(energyMatrix);
        for (int i = 0; i < seam.length; i++ ) {
            System.out.println(seam[i]);
        }
    }
}