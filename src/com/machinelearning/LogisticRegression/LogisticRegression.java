package com.machinelearning.LogisticRegression;
/*
 * Authors : Aniket Bhosale and Mayur Tare
 * Description : This class implements Logistic Regression algorithm using ABSCONV conversion criterion.
 */

import java.util.ArrayList;

public class LogisticRegression {
	
	public static double[] weights;
	static double step = Double.parseDouble(Config.readConfig("stepSize"));
	private static int numOfRuns = Integer.parseInt(Config.readConfig("iterations"));
	private static double conversionVal = Double.parseDouble(Config.readConfig("conversionValue"));
	
	public static String label = null;
	public static int labelIndex;
	public static String trueClassLabel = "1";
	
	//Calculate Probability using sigmoid function
	private static double sigmoid(double z){
	    return 1 / (1 + Math.exp(-z)); 	
	}
	
	//Return calculate (w.x)
	public static double classifier(Example instance){
		double linearSum = 0.0;
		for (int j = 0; j<DataLoader.numberOfFeatures; j++){
			if(j != labelIndex)
				linearSum += weights[j] * Double.parseDouble(instance.getFeature(j));
		}
		return sigmoid(linearSum);
	}
	
	public static void parameterComputation(ArrayList<Example> ex){
		double oldLikelihood = 0.0;
		double logLikelihood =0.0;
		for (int n = 0; n < numOfRuns; n++){
			double ABSFCONV = 0.0;
			
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
					//Calculate Likelihood for current iteration
					logLikelihood += (1-Integer.parseInt(classLabel)) * Math.log(1-classifier(ex.get(i))) + Integer.parseInt(classLabel) * Math.log(classifier(ex.get(i)));
			}
			//ABSFCONV : Convergence requires a small change in the log-likelihood function in subsequent iterations 
			ABSFCONV = Math.abs(logLikelihood -  oldLikelihood);
		
			if(ABSFCONV < conversionVal){
				System.out.println("Converged after "+n+" iterations for conversion value(ABSCONV) = "+conversionVal);
				break;
			}
			oldLikelihood = logLikelihood;
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
		
		//Train the algorithm of Train Data Set
		parameterComputation(trainExamples);
		
		//Check the algorithm predictions for Test Data set
		for(int e = 0; e < testExamples.size();e++){
			
			predictedClass1 =  classifier(testExamples.get(e));
			predictedClass0 = 1 - predictedClass1;
			
			if(predictedClass0 > predictedClass1)
				predictedClassLabel = 0;
			else
				predictedClassLabel = 1;
			
			if(Integer.parseInt(testExamples.get(e).getFeature(labelIndex)) != 1)
				actualClassLabel = 0;
			else
				actualClassLabel = 1;
			if(!(predictedClassLabel == actualClassLabel))
				incorrectCount++;
			else
				correctCount++;
		}
		System.out.println("Total Correct Predcitions = "+correctCount+" out of "+testExamples.size()+" examples");
		System.out.println("Total Incorrect Predcitions = "+incorrectCount+" out of "+testExamples.size()+" examples");
		
	}

}
