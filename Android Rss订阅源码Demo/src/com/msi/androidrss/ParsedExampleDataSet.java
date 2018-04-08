package com.msi.androidrss;

public class ParsedExampleDataSet {
    private String extractedString = null;
    private int extractedInt = 0;

    //设置从xml中提取到的String
    public void setExtractedString(String extractedString) {
         this.extractedString = extractedString;
    }
    //设置从xml中提取到的Int
    public void setExtractedInt(int extractedInt) {
         this.extractedInt = extractedInt;
    }
    //将提取到的内容输出为String
    public String toString(){
         return "String from XML= " + this.extractedString
                   + "\nNumber from XML= " + this.extractedInt;
    }
}