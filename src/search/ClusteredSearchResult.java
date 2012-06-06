package search;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.jdom.Element;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import search.ranking.results.RankedResult;
import core.Haplogroup;
import core.Polymorphism;



public class ClusteredSearchResult implements Comparable<ClusteredSearchResult>{
	private int rankedPosition = -1;
	private ArrayList<SearchResult> cluster =  new ArrayList<SearchResult>();
	
	public ClusteredSearchResult(int position) {
		rankedPosition = position;
	}

	public static ArrayList<ClusteredSearchResult> createClusteredSearchResult(List<RankedResult> unclusteredResults,Haplogroup hg)
	{
		 ArrayList<ClusteredSearchResult> clusteredSearchResult = new ArrayList<ClusteredSearchResult>();
		 
		 int i = -1;
		 double currentRank = -100;
		 
		 for(RankedResult currentResult : unclusteredResults)
		 {
		 currentResult.getPhyloSearchData().getDetailedResult().updateResult();
		 }
		 for(RankedResult currentResult : unclusteredResults)
		 {
			
			 
			 //Only process the top rated results
			 if(i == 50)
				 break;
			 
			 if(currentRank != currentResult.getDistance()) 
			 {
				 i++;
				  
				 currentRank = currentResult.getDistance();
								 
				 clusteredSearchResult.add(new ClusteredSearchResult(i));
				 clusteredSearchResult.get(i).getCluster().add(currentResult.getPhyloSearchData());
			 }
			 else
			 clusteredSearchResult.get(i).getCluster().add(currentResult.getPhyloSearchData());
			 
			
		 }		 
		return clusteredSearchResult;	
	}
	
//	public int getRankedPosition() {
//		return rankedPosition;
//	}
//
//	public void setPhyolTreeString(String phyolTreeString) {
//		this.phyolTreeString = phyolTreeString;
//	}
//	
//	public String getPhyolTreeString() {
//		return phyolTreeString;
//	}
//	
	public Element getDetailsXML(String haplogroup)
	{
		for(SearchResult currentResult : cluster)
		{
			if(currentResult.getHaplogroup().equals(new Haplogroup(haplogroup)))
			{
				return currentResult.getDetailedResult().toXML();
			}
		}
		
		return null;
	}
	
	public String getHaplogroup()
	{
		return getCluster().get(0).getHaplogroup().toString();
	}
	
	public boolean containsSuperhaplogroup(Haplogroup haplogroup)
	{
		//if(haplogroup.isSuperHaplogroup(getCluster().get(0).getHaplogroup()) == false)
		//{
	/*	for(SearchResult currentResult : getCluster())
		 {	
			if(haplogroup.isSuperHaplogroup(currentResult.getHaplogroup()))
				return true;
		 }*/
		
		//}
		if(haplogroup.isSuperHaplogroup(getCluster().get(0).getPhyloTree(),getCluster().get(0).getHaplogroup()))
			return true;
		
		 return false;
	}
	
	@Override
	public int compareTo(ClusteredSearchResult o) {
		if(this.getCluster().get(0).getRank() > o.getCluster().get(0).getRank())
			return -1;
		if(this.getCluster().get(0).getRank() < o.getCluster().get(0).getRank())
			return 1;	
		else
			return 0;
		
	}
	
	public String toString()
	{
		String result = "";
		DecimalFormat df = new DecimalFormat( "0.000" );
		result += "\t-------------------------------------------------------------------\n";
		
		for(SearchResult currentResult : getCluster())
		 {			
			result += "\tHaplogroup: " + currentResult.getHaplogroup() +"\n";
			result += "\tFinalRank: " + currentResult.getRank() + " \n";
			result += "\tCorrect polys in test sample ratio: " + currentResult.getCorrectPolyInTestSampleRatio()
			+ " ("+df.format(currentResult.getWeightFoundPolys()) + " / " +df.format(currentResult.getUsedWeightPolys())    + ")"+"\n"; 
			result += "\tCorrect polys in haplogroup ratio: " + df.format(currentResult.getCorrectPolyInHaplogroupRatio())
			+ " ("+df.format(currentResult.getWeightFoundPolys()) + " / " +df.format(currentResult.getExpectedWeightPolys())    + ")"+"\n"; 
			
			
			
			//result += "\t";
			
			//Collections.sort(currentResult.getExpectedPolysFullRange());
			//for(Polymorphism currentMissing: currentResult.getExpectedPolysFullRange())
			//	result +=currentMissing+ "(" + currentMissing.getMutationRate(currentMissing.getMutation()) + ") ";
			
			//result += "=" + currentResult.getExpectedWeightFullRangePolys() + "\n";
			
			result += "\t\tExpected\tCorrect\t\tUsed polys\tWeight\n";
			
			Collections.sort(currentResult.getDetailedResult().getCheckedPolys());
			
			
			ArrayList<Polymorphism> unusedPolys = new ArrayList<Polymorphism>();
			unusedPolys.addAll(currentResult.getSample().getPolymorphismn());
			for(Polymorphism current : currentResult.getDetailedResult().getCheckedPolys())
			{
				
				String fluctString = df.format( currentResult.getPhyloTree().getMutationRate(current));
				
				
				result +=  "\t\t"+ current.toString();
				
				result +=  "\t\t";
				if(currentResult.getFoundPolys().contains(current))
					result +=  current.toString();
				
				result +=  "\t\t";
				
				//The polymorphism is  contained in this haplogroup
				if(currentResult.getSample().getPolymorphismn().contains(current))
				{
					result +=  current.toString();	
					unusedPolys.remove(current);
				}
								
				
				
					
				result += "\t\t" + fluctString;
				result +=  "\n";
						
			}
			
			//Write unused polymorphismn in this haplogroup
			for(Polymorphism current : unusedPolys)
			{
				String fluctString = df.format( currentResult.getPhyloTree().getMutationRate(current));
				result +=  "\t\t\t\t\t\t" + current; 
				result += "\t\t" + fluctString + "\n";
			}
			result += "\t\t-------------------------------------------------------------\n";
			result += "\t\t" + df.format(currentResult.getExpectedWeightPolys())
				+"\t\t"+ df.format(currentResult.getWeightFoundPolys()) 
				+"\t\t"+ df.format(currentResult.getUsedWeightPolys()) +"\n";
			
			result += "\n\n";
		 }
		
		return result;
	}

	public void setCluster(ArrayList<SearchResult> cluster) {
		this.cluster = cluster;
	}

	public ArrayList<SearchResult> getCluster() {
		return cluster;
	}


public JSONObject toJson() throws JSONException {
		
		DecimalFormat df = new DecimalFormat( "0.000",new DecimalFormatSymbols(Locale.US));
		JSONObject child =  new JSONObject();
		JSONObject child1 =  new JSONObject();
		JSONArray a = new JSONArray();
		
		int i = 1;
		for(SearchResult currentResult : getCluster())
		 {	
		   	if(i == 1)
			{
		   		child.put("pos",this.rankedPosition);
		   		child.put("name",currentResult.getHaplogroup().toString());
				child.put("id",currentResult.getHaplogroup().toString());
				System.out.println(currentResult.getRank());
				child.put("rank",df.format(currentResult.getRank()));
				child.put("rankHG",df.format(currentResult.getCorrectPolyInHaplogroupRatio()));
				child.put("rankS",df.format(currentResult.getCorrectPolyInTestSampleRatio()));
				child.put("iconCls", "icon-treegrid");
				child.put("expanded", true);
				if(getCluster().size()==1) child.put("leaf", true); else child.put("leaf", false); 
		   		
			}
		   	
		   	else
		   	{
		   		child1 =  new JSONObject();
		   		child1.put("pos","");
		   		child1.put("name",currentResult.getHaplogroup().toString());
		   		child1.put("id",currentResult.getHaplogroup().toString());
				child1.put("rank",df.format(currentResult.getRank()).toString());
				child1.put("rankHG",df.format(currentResult.getCorrectPolyInHaplogroupRatio()));
				child1.put("rankS",df.format(currentResult.getCorrectPolyInTestSampleRatio()));
				child1.put("iconCls", "icon-treegridSW");
				child1.put("leaf", true);
				a.put(child1);
		   	}	
		   	
			   	i++;	
			   	
			   
		 }
		child.put("children", a);
		
		
		return child;
	}
	
//public PhyloTreePath getPhyloTreePath(Haplogroup haplogroup) {
//	for(SearchResult currentResult : cluster)
//	{
//		if(currentResult.getHaplogroup().equals(haplogroup))
//		{
//			return currentResult.getUsedPath();
//		}
//	}
//	
//	return null;
//}
//
//public PhyloTreePath getPhyloTreePath(int index) {
//	
//	return cluster.get(index).getUsedPath();
//	/*for(SearchResult currentResult : cluster)
//	{
//		if(currentResult.getHaplogroup().equals(haplogroup))
//		{
//			return currentResult.getUsedPath();
//		}
//	}
//	
//	return null;*/
//}

	public Element getUnusedPolysXML(String haplogroup) {
		for(SearchResult currentResult : cluster)
		{
			if(currentResult.getHaplogroup().equals(new Haplogroup(haplogroup)))
			{
				return currentResult.getDetailedResult().getUnusedPolysXML(true);
			}
		}
		
		return null;
	}
	
	public Element getNotInRangePolysXML(String haplogroup) {
		for(SearchResult currentResult : cluster)
		{
			if(currentResult.getHaplogroup().equals(new Haplogroup(haplogroup)))
			{
				return currentResult.getNotInRangePolysXML();
			}
		}
		
		return null;
	}

	public SearchResult getSearchResult(Haplogroup haplogroup) {
		for(SearchResult currentResult : cluster)
		{
			if(currentResult.getHaplogroup().equals(haplogroup))
			{
				return currentResult;
			}
		}
		return null;
	}
	
	public static SearchResult getSearchResultByHaplogroup(List<ClusteredSearchResult> allResults,Haplogroup haplogroup){

		SearchResult result = null;
			for(ClusteredSearchResult cr : allResults){
			
				result = cr.getSearchResult(haplogroup);
				if(result != null)
				break;
			
		}
			return result;
	}
}
