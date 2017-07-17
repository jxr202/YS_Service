package com.oudmon.algo.ppg;

/**
 * Created by jxr20 on 2017/6/22
 */

public class PpgAnalyzerData {

    /** 收缩血压值 **/
    private int sbp;	//systolic blood pressure
    /** 舒张血压值 **/
    private int dbp;	//diastole blood pressure
    /** 心率 **/
    private int heartRate;
    /** 呼吸率 **/
    private float breathRate;
    /** 血氧度 **/
    private float bloodOxygen;

    @Override
    public String toString() {
        return "PpgAnalyzerData{" +
                "sbp=" + sbp +
                ", dbp=" + dbp +
                ", heartRate=" + heartRate +
                ", breathRate=" + breathRate +
                ", bloodOxygen=" + bloodOxygen +
                '}';
    }

    public int getSbp() {
        return sbp;
    }

    public void setSbp(int sbp) {
        this.sbp = sbp;
    }

    public int getDbp() {
        return dbp;
    }

    public void setDbp(int dbp) {
        this.dbp = dbp;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public float getBreathRate() {
        return breathRate;
    }

    public void setBreathRate(float breathRate) {
        this.breathRate = breathRate;
    }

    public float getBloodOxygen() {
        return bloodOxygen;
    }

    public void setBloodOxygen(float bloodOxygen) {
        this.bloodOxygen = bloodOxygen;
    }
}
