package com.machinelearning.PA2;

import java.util.ArrayList;

public class Example{
	
	public ArrayList<String> features;
	
	public Example(){
		features = new ArrayList<String>();
	}
	
	public String getFeature(int index){
		return this.features.get(index);
	}
}
