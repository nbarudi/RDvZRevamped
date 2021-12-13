package ca.bungo.util.backend.player;

import java.util.ArrayList;
import java.util.List;

import ca.bungo.main.RDvZ;
import ca.bungo.util.backend.player.heros.Hero;
import ca.bungo.util.backend.player.heros.dragonWarrior.DragonWarrior;

public class HeroManager {
	
	private List<Hero> heros = new ArrayList<Hero>();
	
	public HeroManager(RDvZ pl) {
		Hero dWarrior = new DragonWarrior(pl);
		
		
		heros.add(dWarrior);
	}
	
	public Hero getHeroByName(String name) {
		Hero toReturn = null;
		
		for(Hero hero : heros) {
			if(hero.getName().equalsIgnoreCase(name)) {
				toReturn = hero;
				break;
			}
		}
		return toReturn;
	}

}
