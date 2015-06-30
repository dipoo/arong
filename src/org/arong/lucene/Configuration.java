package org.arong.lucene;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
//import org.wltea.analyzer.lucene.IKAnalyzer;

public class Configuration {
	private static Directory dir = null; 
	
	private static Analyzer analyzer = null;
	
	static{
		try {
			dir = FSDirectory.open(new File("./indexLib"));
			//分词器为IK
//			analyzer = new IKAnalyzer(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Directory getDir() {
		return dir;
	}

	public static Analyzer getAnalyzer() {
		return analyzer;
	}
}
