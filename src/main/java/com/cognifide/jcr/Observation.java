package com.cognifide.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.jackrabbit.core.TransientRepository;

public class Observation {

	public static void main(String[] args) throws Exception {
		Commons.cleanUpDirs();

		Repository repository = new TransientRepository();
		Session session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
		try {
			Node root = session.getRootNode();
			observation(root, session);
		} finally {
			session.logout();
		}
	}

	public static void observation(Node parentNode, Session session) throws RepositoryException {
		ObservationManager oManager = session.getWorkspace().getObservationManager();
		oManager.addEventListener(STDOUT_EVENT_LISTENER, Event.NODE_ADDED | Event.NODE_REMOVED, "/observed",
				true, null, null, false);

		Node observedRoot = parentNode.addNode("observed");
		observedRoot.addNode("node1");
		observedRoot.addNode("node2");
		session.save();

		observedRoot.getNode("node1").remove();
		observedRoot.getNode("node2").remove();
		session.save();
	}

	private static final EventListener STDOUT_EVENT_LISTENER = new EventListener() {
		public void onEvent(EventIterator events) {
			while (events.hasNext()) {
				try {
					Event ev = events.nextEvent();
					System.out.println(String.format("Path: %s, Type: %s", ev.getPath(), getEventName(ev)));
				} catch (RepositoryException e) {
					e.printStackTrace();
				}
			}
		}
	};

	private static String getEventName(Event e) {
		switch (e.getType()) {
			case Event.NODE_ADDED:
				return "NODE_ADDED";

			case Event.NODE_MOVED:
				return "NODE_MOVED";

			case Event.NODE_REMOVED:
				return "NODE_REMOVED";

			case Event.PERSIST:
				return "PERSIST";

			case Event.PROPERTY_ADDED:
				return "PROPERTY_ADDED";

			case Event.PROPERTY_CHANGED:
				return "PROPERTY_CHANGED";

			case Event.PROPERTY_REMOVED:
				return "PROPERTY_REMOVED";

			default:
				return "?";
		}
	}
}
