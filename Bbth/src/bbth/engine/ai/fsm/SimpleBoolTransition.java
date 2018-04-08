package bbth.engine.ai.fsm;

import java.util.HashMap;

public class SimpleBoolTransition extends StateTransition {

	String m_input_name;

	public SimpleBoolTransition(FiniteState start_state, FiniteState end_state) {
		super(start_state, end_state);
	}

	public SimpleBoolTransition(FiniteState start_state, FiniteState end_state,
			String inputname) {
		super(start_state, end_state);
		m_input_name = inputname;
	}

	public void setInputName(String inputname) {
		m_input_name = inputname;
	}

	@Override
	public boolean checkConditions(HashMap<String, Float> inputs) {
		if (!inputs.containsKey(m_input_name)) {
			return false;
		}
		return inputs.get(m_input_name) != 0;
	}

}
