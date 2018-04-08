package wordroid.model;

public class BookList {
private String ID;
private String name;
private String generateTime;
private String numOfList;
private int numOfWord;


public int getNumOfWord() {
	return numOfWord;
}

public void setNumOfWord(int numOfWord) {
	this.numOfWord = numOfWord;
}

public BookList(){
	
}

public BookList(String ID,String name,String generateTime,String numOfList){
this.ID=ID;
this.numOfList=numOfList;
this.name=name;
this.generateTime=generateTime;
}

public void setID(String ID){
	this.ID=ID;
}
public String getID(){
	return ID;
}
public String getNumOfList() {
	return numOfList;
}

public void setNumOfList(String numOfList) {
	this.numOfList = numOfList;
}
public void setName(String name){
	this.name=name;
}
public String getName(){
	return name;
}
public void setGenerateTime(String generateTime){
	this.generateTime=generateTime;
}
public String getGenerateTime(){
	return generateTime;
}
}
