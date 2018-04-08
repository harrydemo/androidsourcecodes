package bbth.engine.ai.fsm;

import java.util.HashMap;

public class SimpleLessTransition extends StateTransition {
	private String m_input_name;
	private Float m_val;

	public SimpleLessTransition(FiniteState start_state, FiniteState end_state) {
		super(start_state, end_state);
	}

	public void setInputName(String inputname) {
		m_input_name = inputname;
	}

	public void setVal(float value) {
		m_val = value;
	}

	@Override
	public boolean checkConditions(HashMap<String, Float> inputs) {
		if (!inputs.containsKey(m_input_name)) {
			return false;
		}
		return inputs.get(m_input_name) < m_val;
	}
}
