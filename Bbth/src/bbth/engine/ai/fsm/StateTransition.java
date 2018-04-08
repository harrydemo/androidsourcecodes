package bbth.engine.ai.fsm;

import java.util.HashMap;

public abstract class StateTransition {
	private FiniteState m_start_state;
	private FiniteState m_end_state;

	public StateTransition(FiniteState start_state, FiniteState end_state) {
		m_start_state = start_state;
		m_end_state = end_state;
	}

	public abstract boolean checkConditions(HashMap<String, Float> inputs);

	public FiniteState getNewState() {
		return m_end_state;
	}

	public FiniteState getStartState() {
		return m_start_state;
	}
}
