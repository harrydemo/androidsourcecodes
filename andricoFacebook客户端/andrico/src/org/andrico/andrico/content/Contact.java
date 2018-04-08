 /*************************************************************************** 
 *              Copyright (C) 2009 Andrico Team                             * 
 *              http://code.google.com/p/andrico/                           *
 *                             												*
 * Licensed under the Apache License, Version 2.0 (the "License");			*
 * you may not use this file except in compliance with the License.			*
 * 																			*	
 * You may obtain a copy of the License at 									*
 * http://www.apache.org/licenses/LICENSE-2.0								*
 *																			*
 * Unless required by applicable law or agreed to in writing, software		*
 * distributed under the License is distributed on an "AS IS" BASIS,		*
 *																			*
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.	*
 * See the License for the specific language governing permissions and		*
 * limitations under the License.											*
 ****************************************************************************/

package org.andrico.andrico.content;

public class Contact{
	private String fbId;//facebook id
    private String name;
    private String secondName;
    private String dateOfBirth;
    private String adress;
    private String page;
    private String smallPic;
    private byte[] photo;
    private int id; //id in db
    
    
    
    public Contact()
    {
    	name = "";
    	secondName = "";
    	dateOfBirth = "";
    	adress = "";
    	page = "";
    	smallPic = "";
    	byte[] ar = {0};
    	photo = ar;
    }
    
    public boolean Equals(Contact cont)
    {
    	if (!(this.getName().equals(cont.getName())) || !(this.getSecondName().equals(cont.getSecondName())) || 
    				(!this.getFBid().equals(cont.getFBid())) || (!this.getAdress().equals(cont.getAdress())) || 
    				(!this.getPage().equals(cont.getPage())) || (!this.getPic().equals(cont.getPic())))
    	{	
    		return false;
    	}
    	else
    	{
    		return  true;
    	}
    }
    
    public int getId() 
    {
            return id;
    }
    
    public void setId(int id) 
    {
            this.id = id;
    }
    
    public String getName() 
    {
    	if (name != null)
    	{
            return name;
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public void setName(String name) 
    {
    	if (name != null)
    	{
    		this.name = name;
    	}
    	else
    	{
    		this.name = "";
    	}
    }
    
    public String getSecondName() 
    {
    	if (secondName != null)
    	{
            return secondName;
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public void setSecondName(String secondName) 
    {
    	if (secondName != null)
    	{
            this.secondName = secondName;
    	}
    	else
    	{
    		this.secondName = "";
    	}
    }
    
    public String getDateOfBirth() 
    {
    	if (dateOfBirth != null)
    	{
    	    return dateOfBirth;
    	}
    	else
    	{
    		return "";
    	}
    	
    }
    
    public void setDateOfBirth(String date) 
    {
    	if (date != null)
    	{
    		this.dateOfBirth = date;
    	}
    	else
    	{
    		this.dateOfBirth = "";
    	}
    }
            
    public String getAdress() 
    {
    	if (adress != null)
    	{
            return adress;
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public void setAdress(String info) 
    {
    	if (info != null)
    	{
            this.adress = info;
    	}
    	else
    	{
    		this.adress = "";
    	}
    }
    
    public String getPage() 
    {
    	if (page != null)
    	{
            return page;
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public void setPage(String info) 
    {
    	if (info != null)
    	{
    		this.page = info;
    	}
    	else
    	{
    		this.page = "";
    	}
    }
    
    public String getFBid() 
    {
    	return fbId;
    }
    
    public void setFBid(String info) 
    {
    	this.fbId = info;
    }
    
    public String getPic() 
    {
    	if (smallPic != null)
    	{
            return smallPic;
    	}
    	else
    	{
    		return "";
    	}
    }
    
    public void setPic(String url) 
    {
    	if (url != null)
    	{
            this.smallPic = url;
    	}
    	else
    	{
            this.smallPic = "";
    	}
    }
    
    public byte[] getPhoto() 
    {
    	if (photo != null)
    	{
            return photo;
    	}
    	else
    	{
    		byte[] arr = {0};
    		return arr;
    	}
    }
    
    public void setPhoto(byte[] photo) 
    {
    	if(photo != null)
    	{
            this.photo = photo;
    	}
    	else
    	{
    		byte[] arr = {0};
    		this.photo = arr;
    	}
    }
    
    
    public String toString() 
    {
            return "facebook id: " + fbId +"name: " + name + " second name: " + secondName + " date of birth: " + dateOfBirth + " adress: " + adress + " id: " + id;
    }
    
    public void copyTo(Contact newContact)
    {
    	newContact.setDateOfBirth(this.getDateOfBirth());
		newContact.setAdress(this.getAdress());
		newContact.setFBid(this.getFBid());
		newContact.setName(this.getName());
		newContact.setPage(this.getPage());
		newContact.setPic(this.getPic());
		newContact.setPhoto(this.getPhoto());
		newContact.setSecondName(this.getSecondName());
    }
}