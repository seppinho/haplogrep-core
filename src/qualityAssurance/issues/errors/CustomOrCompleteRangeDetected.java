package qualityAssurance.issues.errors;

import java.util.ArrayList;

import qualityAssurance.CorrectionMethod;
import qualityAssurance.QualityAssistent;
import qualityAssurance.issues.QualityFatal;
import qualityAssurance.issues.QualityIssue;


import core.TestSample;

public class CustomOrCompleteRangeDetected extends QualityFatal {

	class SetControlRange extends CorrectionMethod
    {
      public SetControlRange(int methodID,QualityIssue issue) {
			super("Change to metabo chip sample range",methodID,issue);
		}

	public void execute(TestSample testSample)
      {
		testSample.getSample().getSampleRanges().clear();
		testSample.getSample().getSampleRanges().addControlRange();
      }
    }
	
	public CustomOrCompleteRangeDetected(QualityAssistent assistent, TestSample sampleOfIssue) {
		super(assistent, sampleOfIssue, "Control range recognized");
		correctionMethods.add(new SetControlRange(correctionMethods.size(),this));
	}

	public ArrayList<CorrectionMethod> getChildren(){
		return correctionMethods;
	}
	
	public void executeCorrectionMethodeByID(TestSample testSample,int methodID){
		correctionMethods.get(methodID).execute(testSample);
	}
}
