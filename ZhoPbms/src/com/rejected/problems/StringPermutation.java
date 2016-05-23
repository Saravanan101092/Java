package com.rejected.problems;

import java.util.ArrayList;
import java.util.List;


public class StringPermutation {
	
	public void FindPermutations(String str){
		List<String> combinations=null;
		String stringbuilder = new String(str);
		for(int i=0; i<str.length();i++){
			combinations = getPermutations(combinations, stringbuilder);
		}
		System.out.println(combinations);
	}

	public List<String> getPermutations(List<String> oldCombinations, String str){
		List<String> thisCombinations = new ArrayList<String>();
		if(oldCombinations!=null){
			
			for(String oldString : oldCombinations){
				for(int i=0;i<str.length();i++){
					String temp = str.substring(i, i+1);
					if(!oldString.contains(temp)){
						thisCombinations.add(oldString+temp);
					}
				}
			}
			
		}else{
			for(int i=0;i<str.length();i++){
				thisCombinations.add(new String(str.substring(i, i+1)));
			}
		}
		
		return thisCombinations;
	}
	public static void main(String[] args) {
		new StringPermutation().FindPermutations("abc");;

	}

}
