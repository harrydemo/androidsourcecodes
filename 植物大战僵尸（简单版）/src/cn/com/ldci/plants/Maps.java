package cn.com.ldci.plants;

import java.util.ArrayList;



public class Maps {

	
	
	public static ArrayList<BestZomebie> getFirst(){
		ArrayList<BestZomebie> zombies = new ArrayList<BestZomebie>();
		zombies.add(new BestZomebie(40));
//		zombies.add(new BestZomebie(93));
//		zombies.add(new BestZomebie(146));
//		zombies.add(new BestZomebie(199));
//		zombies.add(new BestZomebie(252));
		
		return zombies;
		
	}
	public static ArrayList<Plants> getPlants(){
		ArrayList<Plants> plants = new ArrayList<Plants>();
		plants.add(new Plants(1,60,160));
		return plants;
	}
}
