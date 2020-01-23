import java.awt.Color;
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private Picture picture;
	private double [][] energy; //example [1][5] 1 - col, 5- row
	private int maxRow, maxCol;
	
	private double computeEnergy(int col, int row) {
		double energy = 0;
		
		if(col == 0 || col == maxCol-1 || row == 0 || row ==  maxRow-1) //borders set to 1000
			   return 1000.0;

		for(int rowColSwitch = 0; rowColSwitch < 2; rowColSwitch++)	{
			int i,j;
			if(rowColSwitch == 0) {i = 1; j = 0;}
			else { i = 0; j = 1;}
			
			//System.out.println(col + " " + row);
			
		   int color1 = picture.getRGB(col-i , row -j ); 
		   int color2 = picture.getRGB(col+i , row + j);
		   double red1X = (double) (color1 >> 16 & 0xFF);
		   double red2X = (double) (color2 >> 16 & 0xFF); 
		   double green1X = (double) (color1 >> 8 & 0xFF);
		   double green2X = (double) (color2 >> 8 & 0xFF);
		   double blue1X = (double) (color1 & 0xFF);
		   double blue2X = (double) (color2 & 0xFF);
			
			   /*Color color1 = picture.get(col-i , row -j ); 
			   Color color2 = picture.get(col+i , row + j);
			   double red1X = (double) color1.getRed();
			   double red2X = (double) color2.getRed(); 
			   double green1X = (double) color1.getGreen();
			   double green2X = (double) color2.getGreen();
			   double blue1X = (double) color1.getBlue();
			   double blue2X = (double) color2.getBlue();*/
		    
		   energy += Math.pow(red1X - red2X, 2) + Math.pow(blue1X - blue2X, 2) +  Math.pow(green1X - green2X, 2);
			}
		 return Math.sqrt(energy);
	}
	
	// create a seam carver object based on the given picture
	   public SeamCarver(Picture picture) {
		   if (picture == null) throw new IllegalArgumentException("Picture is null");
		   this.picture = new Picture(picture);
		   energy = new double[picture.width()][picture.height()];
		   //System.out.println(picture.width() + " picture.height " + picture.height());
		   maxRow = picture.height();
		   maxCol = picture.width();
		   
		   for(int col = 0; col < maxCol; col++) {
			   for(int row = 0; row < maxRow; row++) {
				   if(col == 0 || col == maxCol-1 || row == 0 || row ==  maxRow-1) {
					   energy[col][row] = 1000;
				   }
				   else {
					   energy[col][row] = computeEnergy(col,row);
				   }
			   }
		   }
		   
	   }
	   
	   // current picture
	   public Picture picture() {
		   Picture temp = new Picture(maxCol, maxRow);
		   for(int col = 0; col < maxCol; col++) {
			   for(int row = 0; row < maxRow; row++) {
				   temp.set(col, row, this.picture.get(col, row));
			   }
		   }
		   picture = temp;
		   temp = new Picture(picture);
		   return temp;
	   }

	   // width of current picture
	   public int width() {
		   return maxCol;
	   }

	   // height of current picture
	   public int height() {
		   return maxRow;
	   }

	   // energy of pixel at column x and row y
	   public double energy(int x, int y) {
		   if (y < 0 || y > maxRow-1 || x < 0 ||  x > maxCol-1 ) throw new IllegalArgumentException("Pixel out of image bounds!");
		   return energy[x][y];
	   }
	   
	   private class Point{
		   //point from where this point is originated
		   public int col;
		   public int row;
		   //current points' cumulative energy
		   public double energyTotal;
		   
		public Point(int col, int row, double energyTotal) {
			this.col = col;
			this.row = row;
			this.energyTotal = energyTotal;
		}
	   }
	   
	   private double checkCumulativeEnergy(int col, int row, Point [][] paths) {
		   if (paths[col][row] == null) return energy(col,row);
		   else return paths[col][row].energyTotal;
	   }
	   
	   // sequence of indices for horizontal seam
	   public int[] findHorizontalSeam() {
		   Point [][] pathsFROM = new Point [maxCol][maxRow];
		   
		   if (maxRow == 1) return new int[maxCol];
		   
		   for(int col = 1; col < maxCol; col++) {
			   for(int row = 0; row < maxRow; row++) {

				   //corner cases
				   if (row == 0 ) {
					   pathsFROM[col][row] = new Point(col-1, row+1, checkCumulativeEnergy(col-1,row+1,pathsFROM) + energy(col,row));
					   continue;
				   }
				   
				   if (row == maxRow-1 ) {
					   pathsFROM[col][row] = new Point(col-1, row-1, checkCumulativeEnergy(col-1,row-1,pathsFROM) + energy(col,row));
					   continue;
				   }
				   //chose point with min energy from 3 points above as father node
				   //[left][mid][right] potential father points' energy
				   double leftEnergy = checkCumulativeEnergy(col-1, row-1, pathsFROM);
				   double midEnergy = checkCumulativeEnergy(col -1 , row, pathsFROM);
				   double rightEnergy = checkCumulativeEnergy(col -1, row+1, pathsFROM);
				   
				   //check mid first, so for second row we got straight up nodes as fathers
				   if (midEnergy <= leftEnergy && midEnergy <= rightEnergy) {
					   pathsFROM[col][row] = new Point(col-1, row, checkCumulativeEnergy(col-1, row,pathsFROM) + energy(col,row));
					   }
			 	   else if(leftEnergy <= midEnergy && leftEnergy <= rightEnergy){
			 		  pathsFROM[col][row] = new Point(col-1, row-1, checkCumulativeEnergy(col-1,row-1,pathsFROM) + energy(col,row));
			 	   }
			 	   else {
			 		  pathsFROM[col][row] = new Point(col-1, row+1, checkCumulativeEnergy(col-1, row+1,pathsFROM) + energy(col,row));
			 	   }
				   
			   }
		   }
		   
		   //find end point with lowest cumulative energy
		   double minPointEnergy = Double.POSITIVE_INFINITY;
		   Point minPoint = null;
		   int [] minSeam = new int[maxCol];
		   int j = maxCol-1;
		   
		   for(int i = 0; i < maxRow-1; i++) {
;
			   if (checkCumulativeEnergy(maxCol-1,i,pathsFROM)< minPointEnergy) {
				   minPointEnergy = checkCumulativeEnergy(maxCol-1,i,pathsFROM);
				   minPoint = pathsFROM[maxCol-1][i];
				   minSeam[j] = i; //end point of seam
			   }
		   }
		   --j;
		   
		   //System.out.println("minSeam " + Arrays.toString(minSeam));

		   while(j >= 0) {
			   minSeam[j--] = minPoint.row;
			   minPoint = pathsFROM[minPoint.col][minPoint.row];
		   }
		   
		   return minSeam;
	   }
	   
	   // sequence of indices for vertical seam
	   public int[] findVerticalSeam() {
		   
		   if (maxCol == 1) return new int[maxRow];
		   
		   Point [][] pathsFROM = new Point [maxCol][maxRow];
		   for(int row = 1; row < maxRow; row++) {
			   for(int col = 0; col < maxCol; col++) {
				   
				   //corner cases
				   if (col == 0 ) {
					   pathsFROM[col][row] = new Point(col+1, row-1, checkCumulativeEnergy(col+1,row-1,pathsFROM) + energy(col,row));
					   continue;
				   }
				   
				   if (col == maxCol-1 ) {
					   pathsFROM[col][row] = new Point(col-1, row-1, checkCumulativeEnergy(col-1,row-1,pathsFROM) + energy(col,row));
					   continue;
				   }
				   
				   //chose point with min energy from 3 points above as father node
				   //[left][mid][right] potential father points' energy
				   double leftEnergy = checkCumulativeEnergy(col-1, row-1,pathsFROM);
				   double midEnergy = checkCumulativeEnergy(col, row-1,pathsFROM);
				   double rightEnergy = checkCumulativeEnergy(col+1, row-1,pathsFROM);
 				   
				   //check mid first, so for second row we got straight up nodes as fathers
				   if (midEnergy <= leftEnergy && midEnergy <= rightEnergy) {
					   pathsFROM[col][row] = new Point(col, row-1, checkCumulativeEnergy(col,row-1,pathsFROM) + energy(col,row));
					   }
			 	   else if(leftEnergy <= midEnergy && leftEnergy <= rightEnergy){
			 		  pathsFROM[col][row] = new Point(col-1, row-1, checkCumulativeEnergy(col-1,row-1,pathsFROM) + energy(col,row));
			 	   }
			 	   else {
			 		  pathsFROM[col][row] = new Point(col+1, row-1, checkCumulativeEnergy(col+1,row-1,pathsFROM) + energy(col,row));
			 	   }
				   
			   }
		   }

		   //find end point with lowest cumulative energy
		   double minPointEnergy = Double.POSITIVE_INFINITY;
		   Point minPoint = null;
		   int [] minSeam = new int[maxRow];
		   int j = maxRow-1;

		   
		   for(int i = 0; i < maxCol-1; i++) {			   
			   if (checkCumulativeEnergy(i,maxRow-1,pathsFROM) < minPointEnergy) {
				   minPointEnergy = checkCumulativeEnergy(i,maxRow-1,pathsFROM);
				   minPoint = pathsFROM[i][maxRow-1];
				   minSeam[j] = i; //end point of seam
			   }
		   }
		   --j;

		   while(j >= 0 ) {
			   minSeam[j--] = minPoint.col;
			   minPoint = pathsFROM[minPoint.col][minPoint.row];
		   }
		   
		   return minSeam;
	   }

	   // remove horizontal seam from current picture
	   public void removeHorizontalSeam(int[] seam) {
		   if (seam == null ) throw new IllegalArgumentException("Wrong seam input in removeHorizontalSeam!");
		   if (maxRow <= 1) throw new IllegalArgumentException("Only one row left!");
		   if (seam.length != maxCol) throw new IllegalArgumentException("Seam bigger then vertical size of the image!");
		   if (!checkSeam(seam,true)) throw new IllegalArgumentException("Array is not a valid seam!");
		   --maxRow;
		   for(int col = 0; col< maxCol; col++) {
			   for(int row = seam[col]; row < maxRow; row++) {
				   picture.set(col, row, picture.get(col, row+1));
			   } 
		   }
		   for(int col = 0; col< maxCol; col++) {
			   for(int row = 0; row < maxRow; row++) {
				   energy[col][row] = computeEnergy(col,row);
			   } 
		   }
	   }

	   // remove vertical seam from current picture
	   public void removeVerticalSeam(int[] seam) {
		   if (seam == null ) throw new IllegalArgumentException("Wrong seam input in removeVerticalSeam!");
		   if (maxCol <= 1) throw new IllegalArgumentException("Only one col left!");
		   if (seam.length != maxRow) throw new IllegalArgumentException("Seam bigger then vertical size of the image!");
		   if (!checkSeam(seam,false)) throw new IllegalArgumentException("Array is not a valid seam!");
		   --maxCol;
		   for(int row = 0; row< maxRow; row++) {
			   for(int col = seam[row]; col < maxCol; col++) {
				   picture.set(col, row, picture.get(col+1, row));
			   } 
		   }
		   for(int row = 0; row< maxRow; row++) {
			   for(int col = 0; col < maxCol; col++) {
				   energy[col][row] = computeEnergy(col,row);
			   } 
		   }
	   }
	   
	   private boolean checkSeam(int[] seam, boolean isHorizontal) {
		   for(int i = 1; i < seam.length; i++) {
			   if (Math.abs(seam[i] - seam[i-1]) > 1 || seam[i] < 0)//|| seam[i] > seam.length-1 
				   return false;
			   if (isHorizontal == true && seam[i] > maxRow -1) {
				   return false;
			   }
			   if (isHorizontal == false && seam[i] > maxCol -1) {
				   return false;
			   }

		   }
		   return true;
	   }


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Picture picture = new Picture("C:\\Users\\Vladimir.Saverchenko\\eclipse-workspace\\SeamCarving\\seam\\chameleon.png");
		picture.show();
		SeamCarver picEdited = new SeamCarver(picture);
		
		//removeHorizontalSeam test

		for (int i = 0 ; i < 100; i++) {
			picEdited.removeHorizontalSeam(picEdited.findHorizontalSeam());
		}
		
		//int [] seam = picEdited.findVerticalSeam();
		
		//removeVerticalSeam test
		for (int i = 0 ; i < 200; i++) {
			int [] seam = picEdited.findVerticalSeam();
			//System.out.println(Arrays.toString(seam));
			picEdited.removeVerticalSeam(seam);
		}
		
		
		
		
		picEdited.picture().show();
		 
		Picture picture2 = new Picture(2, 2);
		
		picture2.setRGB(0,0,0x040106);
		picture2.setRGB(1,0,0x020001);
		picture2.setRGB(0,1,0x050705);
		picture2.setRGB(1,1,0x030909);
		
		picture2.show();
		SeamCarver picEdited2 = new SeamCarver(picture2);
		picEdited2.findVerticalSeam();
		
	}

}
