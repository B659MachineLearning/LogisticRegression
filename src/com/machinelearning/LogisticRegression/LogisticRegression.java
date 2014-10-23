package com.machinelearning.LogisticRegression;

import java.util.ArrayList;

public class LogisticRegression {
	
	public static double[] weights;
	static double step = Double.parseDouble(Config.readConfig("stepSize"));
	private static int numOfRuns = Integer.parseInt(Config.readConfig("iterations"));
	
	public static String label = null;
	public static int labelIndex;
	public static String trueClassLabel = "1";
	
	private static double sigmoid(double z){
	        return 1 / (1 + Math.exp(-z)); 
	}
	
	public static double classifier(Example instance){
		double linearSum = 0.0;
		for (int j = 0; j<DataLoader.numberOfFeatures; j++){
			if(j != labelIndex)
				linearSum += weights[j] * Double.parseDouble(instance.getFeature(j));
		}
		return sigmoid(linearSum);
	}
	
	public static void parameterComputation(ArrayList<Example> ex){
		double oldLikelyhood = 0.0;
		double logLikelyhood =0.0;
		for (int n = 0; n < numOfRuns; n++){
			double FCONV = 0.0;
			
			for (int i=0; i < ex.size(); i++){
					double predictedValue = classifier(ex.get(i));
					String classLabel = ex.get(i).getFeature(labelIndex);
					if(!classLabel.equalsIgnoreCase(trueClassLabel))
						classLabel = "0";
					for (int j = 0; j<DataLoader.numberOfFeatures; j++){
						if(j != labelIndex){
							weights[j]= weights[j] + step * (Double.parseDouble(classLabel) - predictedValue) * Double.parseDouble(ex.get(i).getFeature(j));
						}
					}
//					for (int k =0;k<weights.length;k++)
//						System.out.print("\nWieght for"+"  " + i  +" is  "+" "+weights[k]);
//					System.out.println();
					logLikelyhood += (1-Integer.parseInt(classLabel)) * Math.log(1-classifier(ex.get(i))) + Integer.parseInt(classLabel) * classifier(ex.get(i));
					
			}
			FCONV = Math.abs(logLikelyhood -  oldLikelyhood)/(Math.abs(oldLikelyhood) + 1e-6);
			System.out.println("Difference : "+FCONV);
			if(FCONV < 1.5e-8){
				System.out.println("Iterations Completed : "+n);
				break;
			}
			oldLikelyhood = logLikelyhood;
		}        
	}

	public static  void main(String[] args) {
		
		double  predictedClass1=-1.0;
		double  predictedClass0=-1.0;
		int incorrectCount = 0;
		int correctCount = 0;
		int predictedClassLabel = -1;
		int actualClassLabel = -1;
		//Read the file name for training data from config file
		String trainFilePath = Config.readConfig("trainFileName");
		String testFilePath = Config.readConfig("testFileName");
		
		ArrayList<Example> trainExamples = DataLoader.readRecords(trainFilePath);
		ArrayList<Example> testExamples = DataLoader.readRecords(testFilePath);
		
		//Index of the class label
		label = Config.readConfig("classLable");
		labelIndex = DataLoader.labels.indexOf(label);
				
		weights = new double [DataLoader.numberOfFeatures];
		//System.out.println(weights);
		
		
	//	System.out.println("Examples =" + trainExamples.size());
		
		
		//DEBUG
		/*for (int i =0; i<examples.size();i++){
			for (int j =0; j<DataLoader.numberOfFeatures;j++){
				
				System.out.print(examples.get(i).getFeature(j));
				
			}
			System.out.println();
		}*/
		
		parameterComputation(trainExamples);
		
//		for(int l=0; l<weights.length;l++)
//			System.out.println(weights[l]);
		
		for(int e = 0; e < testExamples.size();e++){
			
			predictedClass1 =  classifier(testExamples.get(e));
			predictedClass0 = 1 - predictedClass1;
			
			if(predictedClass0 > predictedClass1)
				predictedClassLabel = 0;
			else
				predictedClassLabel = 1;
			
		//	System.out.println("prediction : "+predictedClassLabel+" actual : "+testExamples.get(e).getFeature(labelIndex));
			if(Integer.parseInt(testExamples.get(e).getFeature(labelIndex)) != 1)
				actualClassLabel = 0;
			else
				actualClassLabel = 1;
			if(predictedClassLabel == actualClassLabel)
				correctCount++;
			else
				incorrectCount++;
		}
		
		System.out.println("Total Correct Predcitions = "+correctCount+" out of "+testExamples.size()+" examples");
		System.out.println("Total Incorrect Predcitions = "+incorrectCount+" out of "+testExamples.size()+" examples");
		
	}

}
