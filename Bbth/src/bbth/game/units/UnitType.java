package bbth.game.units;

import android.graphics.Paint;
import bbth.engine.fastgraph.Wall;
import bbth.engine.particles.ParticleSystem;
import bbth.game.Team;

public enum UnitType {
	ATTACKING {
		@Override
		public Unit createUnit(UnitManager manager, Team team, Paint p,
				ParticleSystem particleSystem) {
			return new AttackingUnit(manager, team, p, particleSystem);
		}
	},
	DEFENDING {
		@Override
		public Unit createUnit(UnitManager manager, Team team, Paint p,
				ParticleSystem particleSystem) {
			return new DefendingUnit(manager, team, p, particleSystem);
		}
	},
	UBER {
		@Override
		public Unit createUnit(UnitManager manager, Team team, Paint p,
				ParticleSystem particleSystem) {
			return new UberUnit(manager, team, p, particleSystem);
		}
	},
	WALL {
		@Override
		public Unit createUnit(UnitManager manager, Team team, Paint p,
				ParticleSystem particleSystem) {
			return new WallUnit(new Wall(0, 0, 0, 0), manager, team, p,
					particleSystem);
		}
	};

	public abstract Unit createUnit(UnitManager manager, Team team, Paint p,
			ParticleSystem particleSystem);

	public static UnitType fromInt(int type) {
		switch (type) {
		case 0:
			return UnitType.ATTACKING;
		case 1:
			return UnitType.DEFENDING;
		}

		return null;
	}
}
