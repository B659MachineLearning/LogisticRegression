package com.machinelearning.PA2;

import java.beans.FeatureDescriptor;
import java.util.ArrayList;

public class LogisticRegression {

	public static void main(String[] args) {
		
		//Read the file name for training data from config file
		String trainFilePath = Config.readConfig("trainFileName");
		
		ArrayList<Example> examples = DataLoader.readRecords(trainFilePath);
		System.out.println("Examples =" + examples.size());
		
		
		//DEBUG
		/*for (int i =0; i<examples.size();i++){
			for (int j =0; j<DataLoader.numberOfFeatures;j++){
				
				System.out.print(examples.get(i).getFeature(j));
				
			}
			System.out.println();
		}*/
	}

}
