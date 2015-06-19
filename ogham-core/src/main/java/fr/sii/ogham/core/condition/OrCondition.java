package fr.sii.ogham.core.condition;

import java.util.List;

/**
 * Condition that provides a logical OR operation on manipulated conditions.
 * 
 * @author Aurélien Baudet
 *
 * @param <T>
 *            the type of the object to test
 */
public class OrCondition<T> extends CompositeCondition<T> {

	@SafeVarargs
	public OrCondition(Condition<T>... conditions) {
		super(conditions);
	}

	public OrCondition(List<Condition<T>> conditions) {
		super(conditions);
	}

	@Override
	public boolean accept(T obj) {
		for (Condition<T> condition : getConditions()) {
			// if the condition accepts the object => stop now
			if (condition.accept(obj)) {
				return true;
			}
		}
		// none condition has accepted the object => it is rejected
		return false;
	}

}
