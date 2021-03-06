package qualityAssurance.rules;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import qualityAssurance.QualityAssistent;
import qualityAssurance.issues.IssueType;
import qualityAssurance.issues.QualityFatal;
import qualityAssurance.issues.QualityInfo;
import search.SearchResult;
import core.Polymorphism;
import core.TestSample;

public class CheckForTooManyRCRSpos extends HaplogrepRule {

	static final Log log = LogFactory.getLog(CheckForTooManyRCRSpos.class);
	
	public CheckForTooManyRCRSpos(int priority) {
		super(priority);
	}

	@Override
	public void evaluate(QualityAssistent qualityAssistent, TestSample currentSample) {
		if(currentSample.getResults().size() != 0){
		SearchResult topResult = currentSample.getResults().get(0).getSearchResult();
		int numAlignWarning = 0;
		StringBuffer listHeteroplasmy = new StringBuffer();
		
		for(Polymorphism currentRemainingPoly : currentSample.getSample().getPolymorphisms()){
			if(currentRemainingPoly.equalsReference()){
			listHeteroplasmy.append(currentRemainingPoly+"\t");
				numAlignWarning++;
			}
		}
		
		if(numAlignWarning == 1)
			qualityAssistent.addNewIssue(new QualityInfo(qualityAssistent, currentSample, "The sample contains " + numAlignWarning + " variant according the rCRS: " + listHeteroplasmy, IssueType.ALIGN)); 
		else if(numAlignWarning > 1)
			qualityAssistent.addNewIssue(new QualityFatal(qualityAssistent, currentSample, "The sample contains " + numAlignWarning + " variants according the rCRS: " + listHeteroplasmy, IssueType.ALIGN)); 
		}
	}

	@Override
	public void suppressIssues(QualityAssistent qualityAssistent, TestSample currentSample) {
		// TODO Auto-generated method stub
		
	}

}
