package com.msi.androidrss;

public class ParsedExampleDataSet {
    private String extractedString = null;
    private int extractedInt = 0;

    //���ô�xml����ȡ����String
    public void setExtractedString(String extractedString) {
         this.extractedString = extractedString;
    }
    //���ô�xml����ȡ����Int
    public void setExtractedInt(int extractedInt) {
         this.extractedInt = extractedInt;
    }
    //����ȡ�����������ΪString
    public String toString(){
         return "String from XML= " + this.extractedString
                   + "\nNumber from XML= " + this.extractedInt;
    }
}