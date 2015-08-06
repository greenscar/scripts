package com.freeipodsoftware.abc;

import java.util.HashSet;
import java.util.Set;

/**
 * Simple general purpose event dispatcher.
 * 
 * @author Flo
 * 
 */
public class EventDispatcher {

	private Set<EventListener> listenerSet = new HashSet<EventListener>();

	public void addListener(EventListener listener) {
		listenerSet.add(listener);
	}

	public void removeListener(EventListener listener) {
		listenerSet.remove(listener);
	}

	public void raiseEvent(String eventId) {
		if (eventId != null) {
			for (EventListener listener : listenerSet) {
				listener.onEvent(eventId);
			}
		}
	}
}
