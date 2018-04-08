package com.android.superdeskclock.expand;

public class Compute{

//  private static final int PLUS=0;
//  private static final int MINUS=1;
//  private static final int MULTIPLY=2;
//  private static final int DIVIDED=3;

  public int plus(int a,int b){
    return a+b;
  }
  
  public int minus(int a,int b){
    return a-b;
  }
  
  public int multiply(int a,int b){
    return a*b;
  }
  
  public int divided(int a,int b){
    return a/b;
  }
  
  public void com(int times){
    for (int i = 0; i < times; i++){
      
    }
  }
  
  public int getRules(){
    return this.getRandom(0, 4);
  }
  
  public int getRandom(int from,int to){
    return (int)(Math.random()*(to-from)+from);
  }
  
  public Object[] getFormula(){
    StringBuffer sb=new StringBuffer();
    int first=this.getRandom(0, 9);
    sb.append(first);
    int rule=this.getRules();
    switch (rule){
      case 0:
        sb.append("+");
        break;
      case 1:
        sb.append("-");
        break;
      case 2:
        sb.append("*");
        break;
      case 3:
        sb.append("/");
        break;

      default:
        sb.append("+");
        break;
    }
    int last=0;
    if(rule==3){
      last=this.getRandom(1, 9);
      sb.append(last);
    }
    else{
      last=this.getRandom(0, 9);
      sb.append(last);
    }
    int result=0;
    switch (rule){
      case 0:
        result=first+last;
        break;
      case 1:
        result=first-last;
        break;
      case 2:
        result=first*last;
        break;
      case 3:
        result=first/last;
        break;

      default:
        result=first+last;
        break;
    }
    
    Object[] objs={sb.toString(),result};
    return objs;
  }
  
  public boolean oneTime(Object[] objs,Integer result){
    return objs[1]==result?true:false;
  }
  
  public static void main(String[] args){
    Compute c=new Compute();
    Object[] objs=c.getFormula();
    System.out.println(objs[0]);
    System.out.println(objs[1]);
    System.out.println(c.oneTime(objs, (Integer) objs[1]));
  }
}
