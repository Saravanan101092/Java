package com.rejected.problems;


public class MatrixFilling {
	
	public void fillMatrix(int n){
		int[][] matrix = new int[n][n];
		int level = n;
		int x=0,y=n-1;
		while(x!=y){
			
			matrix = fillLevel(matrix,x,y,level);
			show(matrix);
			x++;y--;
			level-=2;;
		}
		matrix[x][x] =1;
		show(matrix);
		
	}

	public int[][] fillLevel(int[][] matrix, int x, int y, int level){
		
		int[][] newMatrix = (int[][]) matrix.clone();
		int fillValue = level*level;
		
		for(int j=y;j>x;j--){
			newMatrix[x][j] = fillValue;
			fillValue--;
		}
		for(int i=x;i<y;i++){
			newMatrix[i][x] = fillValue;
			fillValue--;
		}
		for(int j=x;j<y;j++){
			newMatrix[y][j] = fillValue;
			fillValue--;
		}
		for(int i=y;i>x;i--){
			newMatrix[i][y] = fillValue;
			fillValue--;
		}
		
		
		return newMatrix;
	}
	
	private void show(int[][] matrix) {
		
		System.out.println();
		for(int i=0;i<matrix.length;i++){
			for(int j=0;j<matrix[i].length;j++){
				System.out.print(matrix[i][j]+"\t");
			}
			System.out.println();
		}
		System.out.println();

	}
	public static void main(String[] args) {

		new MatrixFilling().fillMatrix(9);
	}

}
