package com.seeplant.bean;

import com.google.protobuf.ByteString;
import com.irisking.protobuf.IrisData;

/**
 * 用于java到c++的JNI传递对象
 * 主要是提供protocol buff的IrisData中的ByteString转化成byte[] 
 * 并且提供和IrisData的数据互转
 * @author yuantao
 */

public class JNICloudIrisInfo {
    /**
     * 域
     */
    private int imgHight;
    private int imgWidth;
    private int imgType;
    private int irisRow;
    private int irisCol;
    private int irisRadius;
    private int focusScore;
    private int percentVisible;
    private int saturation;
    private int interlaceValue;
    private int qualityScore;
    private int jpegImSize;
    private byte[] imgData;
    private byte[] unknowInfo;
    private byte[] irisEnrTemplate;
    private byte[] irisRecTemplate;
    /**
     * 构造器
     * @param data
     */
    public JNICloudIrisInfo(IrisData data) {
        imgHight = data.getImgHight();
        imgWidth = data.getImgWidth();
        imgType = data.getImgType();
        irisRow = data.getIrisRow();
        irisCol = data.getIrisCol();
        irisRadius = data.getIrisRadius();
        focusScore = data.getFocusScore();
        percentVisible = data.getPercentVisible();
        saturation = data.getSaturation();
        interlaceValue = data.getInterlaceValue();
        qualityScore = data.getQualityScore();
        jpegImSize = data.getJpegImSize();
        imgData = data.getImgData().toByteArray();
        unknowInfo = data.getUnknowInfo().toByteArray();
        irisEnrTemplate = data.getIrisEnrTemplate().toByteArray();
        irisRecTemplate = data.getIrisRecTemplate().toByteArray();
    }
    
    public IrisData parseAsIrisData() {
        return IrisData.newBuilder()
            .setImgHight(imgHight)
            .setImgWidth(imgWidth)
            .setImgType(imgType)
            .setIrisRow(irisRow)
            .setIrisCol(irisCol)
            .setIrisRadius(irisRadius)
            .setFocusScore(focusScore)
            .setPercentVisible(percentVisible)
            .setSaturation(saturation)
            .setInterlaceValue(interlaceValue)
            .setQualityScore(qualityScore)
            .setJpegImSize(jpegImSize)
            .setImgData(ByteString.copyFrom(imgData))
            .setUnknowInfo(ByteString.copyFrom(unknowInfo))
            .setIrisEnrTemplate(ByteString.copyFrom(irisEnrTemplate))
            .setIrisRecTemplate(ByteString.copyFrom(irisRecTemplate))
            .build();
    }

    public int getImgHight() {
        return imgHight;
    }

    public void setImgHight(int imgHight) {
        this.imgHight = imgHight;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgType() {
        return imgType;
    }

    public void setImgType(int imgType) {
        this.imgType = imgType;
    }

    public int getIrisRow() {
        return irisRow;
    }

    public void setIrisRow(int irisRow) {
        this.irisRow = irisRow;
    }

    public int getIrisCol() {
        return irisCol;
    }

    public void setIrisCol(int irisCol) {
        this.irisCol = irisCol;
    }

    public int getIrisRadius() {
        return irisRadius;
    }

    public void setIrisRadius(int irisRadius) {
        this.irisRadius = irisRadius;
    }

    public int getFocusScore() {
        return focusScore;
    }

    public void setFocusScore(int focusScore) {
        this.focusScore = focusScore;
    }

    public int getPercentVisible() {
        return percentVisible;
    }

    public void setPercentVisible(int percentVisible) {
        this.percentVisible = percentVisible;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }

    public int getInterlaceValue() {
        return interlaceValue;
    }

    public void setInterlaceValue(int interlaceValue) {
        this.interlaceValue = interlaceValue;
    }

    public int getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(int qualityScore) {
        this.qualityScore = qualityScore;
    }

    public int getJpegImSize() {
        return jpegImSize;
    }

    public void setJpegImSize(int jpegImSize) {
        this.jpegImSize = jpegImSize;
    }

    public byte[] getImgData() {
        return imgData;
    }

    public void setImgData(byte[] imgData) {
        this.imgData = imgData;
    }

    public byte[] getUnknowInfo() {
        return unknowInfo;
    }

    public void setUnknowInfo(byte[] unknowInfo) {
        this.unknowInfo = unknowInfo;
    }

    public byte[] getIrisEnrTemplate() {
        return irisEnrTemplate;
    }

    public void setIrisEnrTemplate(byte[] irisEnrTemplate) {
        this.irisEnrTemplate = irisEnrTemplate;
    }

    public byte[] getIrisRecTemplate() {
        return irisRecTemplate;
    }

    public void setIrisRecTemplate(byte[] irisRecTemplate) {
        this.irisRecTemplate = irisRecTemplate;
    }
    
}
