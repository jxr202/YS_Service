package com.oudmon.algo.ecgBandHRVAnalyze;

public class EcgBandHRVAnalyzer {

	static {
		System.loadLibrary("EcgBandHRVAnalyze_V0_2_0");
	}

	static public native EcgBandHRVAnalyzer ecgBandHRVResultFromRRIntervals(
			int[] rrIntervals, int sampleRate, int testSeconds, String fatigueModel);

	public EcgBandHRVAnalyzer() {
		super();
	}

	private int fatigueLabel;	//疲劳标签，目前存在0/1/2/3，分别对应分析失败/精神饱满/精神一般/精神疲劳
	private String hrvInfoJson; //HRV相关指标，json字符串

	public EcgBandHRVAnalyzer(int fatigueLabel, String hrvInfoJson) {
		super();
		this.fatigueLabel = fatigueLabel;
		this.hrvInfoJson = hrvInfoJson;
	}

	public int getFatigueLabel() {
		return fatigueLabel;
	}

	public void setFatigueLabel(int fatigueLabel) {
		this.fatigueLabel = fatigueLabel;
	}

	public String getHrvInfoJson() {
		return hrvInfoJson;
	}

	public void setHrvInfoJson(String hrvInfoJson) {
		this.hrvInfoJson = hrvInfoJson;
	}

}
