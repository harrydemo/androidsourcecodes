package com.crackedcarrot;

public class TrackerData {
	public Creature first;
	public Creature last;

	public TrackerData() {
		first = new Creature(0, 0, null, null, null, null, 0, null);
		last = new Creature(0, 0, null, null, null, null, 0, null);
		first.nextCreature = last;
		last.previousCreauture = first;
	}

	public void addCreatureToList(Creature creature) {
		creature.nextCreature = first.nextCreature;
		creature.previousCreauture = first;
		first.nextCreature = creature;
		creature.nextCreature.previousCreauture = creature;
	}

	public void removeCreatureFromList(Creature creature) {
		creature.previousCreauture.nextCreature = creature.nextCreature;
		creature.nextCreature.previousCreauture = creature.previousCreauture;
		creature.nextCreature = null;
		creature.previousCreauture = null;
	}

}
