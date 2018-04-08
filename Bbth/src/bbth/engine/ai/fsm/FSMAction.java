package bbth.engine.ai.fsm;

import java.util.HashMap;

public interface FSMAction {

	void apply(HashMap<String, Float> inputs);

}
