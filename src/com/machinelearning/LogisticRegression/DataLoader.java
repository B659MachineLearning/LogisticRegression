package com.machinelearning.LogisticRegression;
/*
 * Authors : Aniket Bhosale and Mayur Tare
 * 
 * Description :
 * Class to read a CSV file and store the data set
 * into a ArrayList of a ArrayList of Strings.
 * First element is a List of names of Features.
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class DataLoader {
	public static int numberOfFeatures;
    public static ArrayList<String> labels = new ArrayList<String>();
    public static ArrayList<Integer> catFeatures = new ArrayList<Integer>();
    
    public static ArrayList<Example> readRecords(String FILE_PATH){
		BufferedReader reader = null;
		ArrayList<Example> records = new ArrayList<Example>();

        try { 
           File f = new File(FILE_PATH);
           FileInputStream fis = new FileInputStream(f); 
           reader = new BufferedReader(new InputStreamReader(fis));
           
           String line;
           String currLable;
           String currFeature;
           Example ex = null;
           
           //Read first record from the files as Name of lables 
           line = reader.readLine();
           StringTokenizer st = new StringTokenizer(line, ",");
           numberOfFeatures = st.countTokens();
           for(int i = 0; i<numberOfFeatures; i++){
        	   currLable = st.nextToken();
        	   if(labels.size()<numberOfFeatures)
        		   labels.add(currLable);
           }
           
           //Read all records
           while ((line = reader.readLine()) != null) {
               st = new StringTokenizer(line, ",");
               ex = new Example();
               for(int i = 0; i<numberOfFeatures; i++){
            	   currFeature = st.nextToken();
            	   //Find all categorical features
            	   if(Double.parseDouble(currFeature)>1 && !catFeatures.contains(i))
            		   catFeatures.add(i);
            	   ex.features.add(currFeature);
               }
               records.add(ex);
           }
           reader.close();
        }
        catch(Exception e){
        	e.printStackTrace();
        }
		return records;  
    }
           
}
