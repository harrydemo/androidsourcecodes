package com.parabola.main;
  
public class Cache {  
       
     private User User;  
       
     private Cache(){  
           
     }  
     /** ¹¹Ôìµ¥Àý */  
     private static class CacheHolder{  
         private static final Cache INSTANCE = new Cache();  
     }  
     public Cache getInstance(){  
         return CacheHolder.INSTANCE;  
     }  
     public User getUser() {  
         return User;  
     }  
     public void setUser(User User) {  
         this.User = User;  
     }  
   
}  