package importer;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.StringTokenizer;

import org.junit.Test;

import importer.FastaImporter;
import importer.FastaImporter.References;

public class FastaImporterTests {

	@Test
	public void testRcrs() throws Exception {
		String file = "test-data/fasta/rCRS.fasta";
		StringBuilder actual = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);

		String[] splits = samples.get(0).split("\t");
		for (int i = 3; i < splits.length; i++) {
			actual.append(splits[i] + ",");
		}

		assertEquals(0, actual.length());
	}

	@Test
	public void testRsrs() throws Exception {
		String file = "test-data/fasta/rsrs.fasta";
		StringBuilder actual = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RSRS);

		String[] splits = samples.get(0).split("\t");
		for (int i = 3; i < splits.length; i++) {
			actual.append(splits[i] + ",");
		}

		assertEquals(0, actual.length());

	}

	@Test
	public void testRCrsWithRsrsReference() throws Exception {
		String file = "test-data/fasta/rCRS.fasta";
		StringBuilder actual = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RSRS);

		String[] splits = samples.get(0).split("\t");

		for (int i = 3; i < splits.length; i++) {
			actual.append(splits[i] + ",");
		}

		// exactly 52 differences between rsrs and rCRS
		assertEquals(52, (splits.length) - 3);

	}

	@Test
	public void testParseSampleWithDeletions() throws Exception {
		String file = "test-data/fasta/AY195749.fasta";
		StringBuilder actual = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RSRS);

		String[] splits = samples.get(0).split("\t");

		boolean deletion = false;
		for (int i = 3; i < splits.length; i++) {
			if (splits[i].equals("523-524d")) {
				deletion = true;
			}
			actual.append(splits[i] + ",");
		}

		assertEquals(true, deletion);

	}

	@Test
	public void testParseSampleWithInsertionsDeletions() throws Exception {
		String file = "test-data/fasta/InsertionTest.fasta";
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);

		String[] splits = samples.get(0).split("\t");
		HashSet<String> set = new HashSet<String>();
		
		for (int i = 3; i < splits.length; i++) {
			set.add(splits[i]);
		}

		assertEquals(true, set.contains("16182.1C"));
		assertEquals(true, set.contains("309.1CCT"));
		assertEquals(true, set.contains("3106-3106d"));
		assertEquals(true, set.contains("8270-8277d"));
	}
	
	// copied first two lines of fasta (including 309.1C etc to end of line)
	@Test
	public void testParseSampleWithInsertionsDeletionsShuffle() throws Exception {
		String file = "test-data/fasta/InsertionTest2.fasta";
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);

		String[] splits = samples.get(0).split("\t");
		HashSet<String> set = new HashSet<String>();
		
		for (int i = 3; i < splits.length; i++) {
			set.add(splits[i]);
		}

		assertEquals(true, set.contains("16182.1C"));
		assertEquals(true, set.contains("309.1CCT"));
		assertEquals(true, set.contains("3106-3106d"));
		assertEquals(true, set.contains("8270-8277d"));

	}
	
	// random shuffle
	@Test
	public void testParseSampleWithInsertionsDeletionsShuffleRandom() throws Exception {
		String file = "test-data/fasta/InsertionTest3.fasta";
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);

		String[] splits = samples.get(0).split("\t");
		HashSet<String> set = new HashSet<String>();
		
		for (int i = 3; i < splits.length; i++) {
			set.add(splits[i]);
		}

		assertEquals(true, set.contains("16182.1C"));
		assertEquals(true, set.contains("309.1CCT"));
		assertEquals(true, set.contains("3106-3106d"));
		assertEquals(true, set.contains("8270-8277d"));

	}
	
	// random shuffle
	@Test
	public void test() throws Exception {
		String file = "test-data/fasta/ANI152.fasta";
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);

		String[] splits = samples.get(0).split("\t");
		HashSet<String> set = new HashSet<String>();
		
		for (int i = 3; i < splits.length; i++) {
			set.add(splits[i]);
			System.out.println(splits[i]);
		}
	}
	
	@Test
	public void testB5a1() throws Exception {
		String file = "test-data/fasta/B5a1.fasta";
		StringBuilder actual = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);

		String[] splits = samples.get(0).split("\t");
		
		for (int i = 3; i < splits.length; i++) {
			actual.append(splits[i] + ",");
		}

	}
	
	@Test
	public void testParseSampleWithNs() throws Exception {
		String file = "test-data/fasta/B5a1_withN.fasta";
		StringBuilder actual = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.RCRS);
		
		String countNs = "";
		
		for (int s=0; s<samples.size(); s++) {
		String[] splits = samples.get(s).split("\t");

		System.out.println("ID:" + splits[0]+ " Range: " + splits[1]);
	
		}
		assertEquals("310N;", countNs);

	}
	
	
	@Test //COMPARE SNPS (without indels)
	public void testSARSCOV2_44() throws Exception {
		String file = "test-data/sarscov2/sarscov2_example_sequences_nextstrain_44.fasta";
		StringBuilder actual = new StringBuilder();
		StringBuilder compare = new StringBuilder();
		FastaImporter impFasta = new FastaImporter();
		ArrayList<String> samples = impFasta.load(new File(file), References.SARSCOV2);
		
		for (int s = 0; s < samples.size(); s++) {
			String[] splits = samples.get(s).split("\t");
			actual.append(splits[0]+"\t");
			for (int i = 3; i < splits.length; i++) {
				if (!splits[i].toUpperCase().contains("D") && !splits[i].contains(".")  ) {
					actual.append(splits[i] + ",");
				}
			}
			actual.append("\n");
		}
		
		//COMPARE WITH NEXTSTRAIN
		try  
		{  
		File fileCompareNextclade=new File("test-data/sarscov2/nextclade_nextstrain_results_44.csv");    
		FileReader fnextclade=new FileReader(fileCompareNextclade);   //reads the file  
		BufferedReader brnextclade=new BufferedReader(fnextclade);  //creates a buffering character input stream  
		String line;  
		brnextclade.readLine(); //SKIP Header
		while((line=brnextclade.readLine())!=null)  {
			String[] nextcladeline = line.split(";");
			compare.append(nextcladeline[0]+"\t");
			String[] nextcladeline_SNPs = nextcladeline[10].split(",");
			for (int i = 0; i < nextcladeline_SNPs.length; i++) {
				compare.append(nextcladeline_SNPs[i].subSequence(1, nextcladeline_SNPs[i].length()) + ",");
			}
			compare.append("\n");
		}

		System.out.println(actual.toString());
		System.out.println(compare.length());
		}
				catch(IOException e)  
		{  
		e.printStackTrace();  
		}  
		
		assertEquals(compare.toString(), actual.toString());

	}
	
	
}
