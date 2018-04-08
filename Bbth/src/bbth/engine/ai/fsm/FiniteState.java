package bbth.engine.ai.fsm;

import java.util.ArrayList;
import java.util.HashMap;

public class FiniteState {
	private ArrayList<StateTransition> m_transitions;
	private String m_name;

	private FSMAction m_action;

	public FiniteState(String name) {
		m_name = name;
		m_action = null;
		m_transitions = new ArrayList<StateTransition>();
	}

	public FiniteState checkTransitions(HashMap<String, Float> inputs) {
		int size = m_transitions.size();
		for (int i = 0; i < size; i++) {
			StateTransition s = m_transitions.get(i);
			if (s.checkConditions(inputs)) {
				return s.getNewState();
			}
		}

		return this;
	}

	public void addTransition(StateTransition t) {
		m_transitions.add(t);
	}

	public void setAction(FSMAction action) {
		m_action = action;
	}

	public String getName() {
		return m_name;
	}

	public void applyAction(HashMap<String, Float> inputs) {
		if (m_action == null) {
			return;
		}

		m_action.apply(inputs);
	}
}
