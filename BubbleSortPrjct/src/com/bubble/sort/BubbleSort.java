package com.bubble.sort;


public class BubbleSort {

	public static void bubbleSort(int[] intArray){
		for(int i=(intArray.length-1);i>0;i--){
			
			intArray = sort(intArray,i);
			show(intArray);
		}
	}
	
	public static void show(int[] arr){
		System.out.println();
		for(int i=0;i<arr.length;i++){
			System.out.print(arr[i]+"\t");
		}
		System.out.println();
	}
	public static int[] sort(int[] intArray, int n){
		for(int i=0;i<n;i++){
			if(intArray[i]>intArray[i+1]){
				int temp = intArray[i+1];
				intArray[i+1] = intArray[i];
				intArray[i]= temp;
			}
		}
		return intArray;
	}
	public static void main(String[] args){
		int[] arr = {8,2,3,9,12,4,16,20,11,1};
		bubbleSort(arr);
	}
}
