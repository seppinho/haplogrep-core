package qualityAssurance.rules;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import qualityAssurance.QualityAssistent;
import qualityAssurance.QualityError;
import core.Polymorphism;
import core.TestSample;
import exceptions.parse.sample.InvalidPolymorphismException;

public class CheckForSampleRSRSAligned implements HaplogrepRule {
	static ArrayList<Polymorphism> uniqueRSRSPolys = null;
	
	public CheckForSampleRSRSAligned(){
		if(uniqueRSRSPolys == null){
			uniqueRSRSPolys = new ArrayList<Polymorphism>();
			
			loadUniqueRSRSPositions();
		}
	}
	
	private void loadUniqueRSRSPositions() {
		try {
			InputStream phyloFile = this.getClass().getClassLoader().getResourceAsStream("RSRSPolymorphisms");
			
			if(phyloFile == null)
				phyloFile = new  FileInputStream(new File("testDataFiles/RSRSPolymorphisms"));
			
			BufferedReader reader = new BufferedReader(new InputStreamReader(phyloFile));

			String currentLine = reader.readLine();

			while(currentLine != null){
			Polymorphism newUniquePoly = new Polymorphism(currentLine.trim());
			uniqueRSRSPolys.add(newUniquePoly);
			currentLine = reader.readLine();
			}
			

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidPolymorphismException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	@Override
	public void evaluate(QualityAssistent qualityAssistent, TestSample currentSample) {

		int numRSRSPolysFound = 0;
	
			
		for(Polymorphism currentUniqueRSRSPoly : uniqueRSRSPolys)	
			if(currentSample.getSample().contains(currentUniqueRSRSPoly)) 
				numRSRSPolysFound++;
		
		
			if(numRSRSPolysFound > 1){
				qualityAssistent.addNewIssue(new QualityError(qualityAssistent, currentSample, numRSRSPolysFound + " common RSRS polymorphims found! " +
						"The sample seems to be aligned to RSRS. Haplogrep only supports rCRS aligned samples."));
			}
			
					
		} 

}