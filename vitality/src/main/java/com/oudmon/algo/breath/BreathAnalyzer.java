package com.oudmon.algo.breath;

public class BreathAnalyzer {

	static {
		System.loadLibrary("Breath_V1_0_1");
	}
	
	public BreathAnalyzer() {
		super();
	}


	static public native int breathRateFromWavFile(String filePath);

	static public native int pulmonaryFromWavFile(String filePath);
}
