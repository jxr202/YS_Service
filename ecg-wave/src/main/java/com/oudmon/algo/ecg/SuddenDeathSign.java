package com.oudmon.algo.ecg;

public class SuddenDeathSign {
    static {
        System.loadLibrary("SuddenDeathSign_0_5_3");
    }
    
    /*设置心电数据导联脱落时设定的值，默认为10000*/
    public static native void setupLeadLossECGValue(int leadLossECGValue);
    
    /*从心电数据中提取猝死征兆的相关指标*/
    public static native SuddenDeathSign extractSuddenDeathSignFrom(int[] ecgData, int sampleRate);

    int[] accendingMyocardialIschimaRIndexs; //上升型心肌缺血所在R波位置序列
    int[] decendingMyocardialIschimaRIndexs; //下降型心肌缺血所在R波位置序列
    int[] myocardialInfarctionRIndexs; //心肌梗死R波所在位置序列
    int[] hypokalemmicRIndexs; //低血钾R波所在位置序列
    int[] rIndexs; //R波位置序列
    int[] prematureVentricularExtrasystoleRIndexs; //室性早搏所在R波位置序列
    int[] prematureAtrialExtrasystoleRIndexs; //房性早搏所在R波位置序列
    int[] bigeminyRIndexs; //二联律现象所在R波位置序列
    int[] trigeminyRIndexs; //三联律现象所在R波位置序列
    int[] rWaveTags; //R波标注列表，取出后记得转成字符
    
    public SuddenDeathSign(int[] accendingMISs, int[] decendingMISs,
                           int[] MINs, int[] Hs, int[] rIndexs,
						   int[] pvs, int[] pas, int[] bigeminy,
						   int[] trigeminy, int[] rWaveTags) {
        super();
        this.accendingMyocardialIschimaRIndexs = accendingMISs;
        this.decendingMyocardialIschimaRIndexs = decendingMISs;
        this.myocardialInfarctionRIndexs = MINs;
        this.hypokalemmicRIndexs = Hs;
        this.rIndexs = rIndexs;
		this.prematureVentricularExtrasystoleRIndexs = pvs;
		this.prematureAtrialExtrasystoleRIndexs = pas;
		this.bigeminyRIndexs = bigeminy;
		this.trigeminyRIndexs = trigeminy;
		this.rWaveTags = rWaveTags;
    }
    

    public int[] getAccendingMyocardialIschimaRIndexs() {
        return accendingMyocardialIschimaRIndexs;
    }

    public void setAccendingMyocardialIschimaRIndexs(int[] accendingMyocardialIschimaRIndexs) {
        this.accendingMyocardialIschimaRIndexs = accendingMyocardialIschimaRIndexs;
    }

    public int[] getDecendingMyocardialIschimaRIndexs() {
        return decendingMyocardialIschimaRIndexs;
    }

    public void setDecendingMyocardialIschimaRIndexs(int[] decendingMyocardialIschimaRIndexs) {
        this.decendingMyocardialIschimaRIndexs = decendingMyocardialIschimaRIndexs;
    }

    public int[] getMyocardialInfarctionRIndexs() {
        return myocardialInfarctionRIndexs;
    }

    public void setMyocardialInfarctionRIndexs(int[] myocardialInfarctionRIndexs) {
        this.myocardialInfarctionRIndexs = myocardialInfarctionRIndexs;
    }
    
    public int[] getHypokalemmicRIndexs() {
        return hypokalemmicRIndexs;
    }

    public void setHypokalemmicRIndexs(int[] hypokalemmicRIndexs) {
        this.hypokalemmicRIndexs = hypokalemmicRIndexs;
    }
    
    public int[] getRIndexs() {
        return rIndexs;
    }

    public void setRIndexs(int[] rIndexs) {
        this.rIndexs = rIndexs;
    }
	
	public int[] getPrematureVentricularExtrasystoleRIndexs() {
        return prematureVentricularExtrasystoleRIndexs;
    }

    public void setPrematureVentricularExtrasystoleRIndexs(int[] pvs) {
        this.prematureVentricularExtrasystoleRIndexs = pvs;
    }

	public int[] getPrematureAtrialExtrasystoleRIndexs() {
        return prematureAtrialExtrasystoleRIndexs;
    }

    public void setPrematureAtrialExtrasystoleRIndexs(int[] pas) {
        this.prematureAtrialExtrasystoleRIndexs = pas;
    }
	
	public int[] getbigeminyRIndexs() {
        return bigeminyRIndexs;
    }

    public void setBigeminyRIndexs(int[] bigeminy) {
        this.bigeminyRIndexs = bigeminy;
    }
	
	public int[] getTrigeminyRIndexs() {
        return trigeminyRIndexs;
    }

    public void setTrigeminyRIndexs(int[] trigeminy) {
        this.trigeminyRIndexs = trigeminy;
    }

	public int[] getRWaveTags() {
        return rWaveTags;
    }

    public void setRWaveTags(int[] rWaveTags) {
        this.rWaveTags = rWaveTags;
    }
}
