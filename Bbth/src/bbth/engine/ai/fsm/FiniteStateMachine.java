package bbth.engine.ai.fsm;

import java.util.HashMap;

public class FiniteStateMachine {

	private HashMap<String, FiniteState> m_states;

	private FiniteState m_current_state;

	public FiniteStateMachine() {
		m_states = new HashMap<String, FiniteState>();
		m_current_state = null;
	}

	public void addState(String name) {
		FiniteState s = new FiniteState(name);
		addState(name, s);
	}

	public void addState(String name, FiniteState s) {
		m_states.put(name, s);
		if (m_current_state == null) {
			m_current_state = s;
		}
	}

	public String getStateName() {
		if (m_current_state == null) {
			return null;
		}
		return m_current_state.getName();
	}

	public FiniteState getStateByName(String name) {
		return m_states.get(name);
	}

	public FiniteState getCurrState() {
		return m_current_state;
	}

	public boolean update(HashMap<String, Float> inputs) {
		if (m_current_state == null) {
			return false;
		}
		FiniteState new_state = m_current_state.checkTransitions(inputs);
		if (new_state != m_current_state) {
			m_current_state = new_state;
			return true;
		}
		return false;
	}

	public void applyAction(HashMap<String, Float> inputs) {
		if (m_current_state == null) {
			return;
		}

		m_current_state.applyAction(inputs);
	}
}
