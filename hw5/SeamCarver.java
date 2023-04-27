import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.StdOut;

import java.awt.*;

public class SeamCarver {
    Picture p;
    double[][] energyMatrix;
    public SeamCarver(Picture picture) {
        this.p = picture;
        storeToEnergyMatrix();
    }

    private void transposing(Picture pic) {
        Picture transposedPic = new Picture(pic.height(), pic.width());
        for (int tRow = 0; tRow < pic.width(); tRow++) {
            for (int tCol = 0; tCol < pic.height(); tCol++) {
                transposedPic.set(tCol, tRow,pic.get(tRow, tCol));
            }
        }
        this.p = transposedPic;
        storeToEnergyMatrix();
    }


    public Picture picture() {
        return this.p;
    }

    public int width() {
        return this.p.width();
    }

    public int height() {
        return this.p.height();
    }

    public double energy(int x, int y) {
        if (x >= this.width() || x < 0 || y >= this.height() || y < 0) {
            throw new IndexOutOfBoundsException(" x or y is outside its" +
                    "prescribed range");
        }
        double rX, gX, bX, rY, gY, bY;
        if (this.p.width() < 3) {
            rX = 0.0;
            gX = 0.0;
            bX = 0.0;
        }
        else if (x > 0 && x < this.p.width() -1) {
            rX = this.p.get(x - 1, y).getRed() -
                    this.p.get(x + 1, y).getRed();
            gX = this.p.get(x - 1, y).getGreen() -
                    this.p.get(x + 1, y).getGreen();
            bX = this.p.get(x - 1, y).getBlue() -
                    this.p.get(x + 1, y).getBlue();
        } else if (x == 0) {
            rX = this.p.get(this.p.width() - 1, y).getRed() -
                    this.p.get(x + 1, y).getRed();
            gX = this.p.get(this.p.width() - 1, y).getGreen() -
                    this.p.get(x + 1, y).getGreen();
            bX = this.p.get(this.p.width() - 1, y).getBlue() -
                    this.p.get(x + 1, y).getBlue();
        } else {
            rX = this.p.get(x - 1, y).getRed() -
                    this.p.get(0, y).getRed();
            gX = this.p.get(x - 1, y).getGreen() -
                    this.p.get(0, y).getGreen();
           bX = this.p.get(x - 1, y).getBlue() -
                    this.p.get(0, y).getBlue();
        }

        if (this.p.height() < 3) {
            rY = 0.0;
            gY = 0.0;
            bY = 0.0;
        }
        else if (y > 0 && y < this.p.height() - 1) {
            rY = this.p.get(x, y - 1).getRed() -
                    this.p.get(x , y + 1).getRed();
            gY = this.p.get(x, y - 1).getGreen() -
                    this.p.get(x , y + 1).getGreen();
            bY = this.p.get(x, y - 1).getBlue() -
                    this.p.get(x , y + 1).getBlue();
        } else if (y == 0) {
            rY = this.p.get(x, this.p.height() -1).getRed() -
                    this.p.get(x , y + 1).getRed();
            gY = this.p.get(x, this.p.height() -1).getGreen() -
                    this.p.get(x , y + 1).getGreen();
            bY = this.p.get(x, this.p.height() -1).getBlue() -
                    this.p.get(x , y + 1).getBlue();
        } else {
            rY = this.p.get(x, y - 1).getRed() -
                    this.p.get(x , 0).getRed();
            gY = this.p.get(x, y - 1).getGreen() -
                    this.p.get(x , 0).getGreen();
            bY = this.p.get(x, y - 1).getBlue() -
                    this.p.get(x , 0).getBlue();
        }
        return rX * rX + gX * gX + bX * bX
                + rY * rY + gY * gY + bY * bY;
    }

    private void storeToEnergyMatrix() {
        this.energyMatrix = new double[this.width()][this.height()];
        for (int row = 0; row < this.height(); row++) {
            for (int col = 0; col < this.width(); col++)
                energyMatrix[col][row] =  this.energy(col, row);
        }
    }


    public int[] findHorizontalSeam() {
        transposing(this.p);
        int[] seam = findVerticalSeam();
        transposing(this.p);
        return seam;
    }

    public int[] findVerticalSeam() {
        int[] seam = new int[this.height()];
        int mintColumn = 0;

        if (p.width() < 2) {
            return seam;
        }

        double minEnergy = findMinVerticalEnergy(mintColumn)[this.height()];
        for (int column = 1; column < this.width(); column++) {
            if (findMinVerticalEnergy(column)[this.height()] < minEnergy) {
                minEnergy = findMinVerticalEnergy(column)[this.height()];
                mintColumn = column;
            }
        }

        for (int i = 0; i < this.height(); i++) {
            seam[i] = (int) findMinVerticalEnergy(mintColumn)[i];
        }
        return seam;
    }

    private double[] findMinVerticalEnergy(int column) {
        //last index is energy, others are column
        double [] minEnergyPath = new double[this.height() + 1];
        minEnergyPath[0] = column;
        double min = energyMatrix[column][0];
        for (int row = 1; row < this.height(); row++) {
            if (column == 0) {
                if (energyMatrix[column][row] > energyMatrix[column + 1][row]) {
                    column = column + 1;
                }
            } else if (column == this.width() - 1) {
                if (energyMatrix[column][row] > energyMatrix[column - 1][row]) {
                    column = column - 1;
                }
            } else {
                if (energyMatrix[column][row] > energyMatrix[column - 1][row] &&
                        energyMatrix[column + 1][row] > energyMatrix[column - 1][row]) {
                    column = column - 1;
                } else if (energyMatrix[column][row] > energyMatrix[column + 1][row] &&
                        energyMatrix[column - 1][row] > energyMatrix[column + 1][row]) {
                    column = column + 1;
                }
            }
            minEnergyPath[row] = column;
            min = min + energyMatrix[column][row];
        }
        minEnergyPath[this.height()] = min;
        return minEnergyPath;
    }

    public void removeHorizontalSeam(int[] seam) {
        this.p = SeamRemover.removeHorizontalSeam(this.p, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        this.p = SeamRemover.removeVerticalSeam(this.p, seam);
    }
}
