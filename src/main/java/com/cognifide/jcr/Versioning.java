package com.cognifide.jcr;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import javax.jcr.version.Version;
import javax.jcr.version.VersionManager;

import org.apache.jackrabbit.core.TransientRepository;

// http://jackrabbit.apache.org/first-hops.html
public class Versioning {

	public static void main(String[] args) throws Exception {
		Commons.cleanUpDirs();

		Repository repository = new TransientRepository();
		Session session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
		try {
			Node root = session.getRootNode();
			versioningBasics(root, session);
		} finally {
			session.logout();
		}
	}

	public static void versioningBasics(Node parentNode, Session session) throws RepositoryException {
		VersionManager vManager = session.getWorkspace().getVersionManager();

		// create versionable node
		Node n = parentNode.addNode("childNode", "nt:unstructured");
		n.addMixin("mix:versionable");
		n.setProperty("property", "foo");
		session.save();
		Version firstVersion = vManager.checkin(n.getPath());
		printInfo(n, vManager);

		// modify the node
		vManager.checkout(n.getPath());
		n.setProperty("property", "bar");
		session.save();
		vManager.checkin(n.getPath());
		printInfo(n, vManager);

		// restore first version
		vManager.restore(firstVersion, true);
		printInfo(n, vManager);
	}

	private static void printInfo(Node n, VersionManager vManager) throws RepositoryException {
		System.out.println(String.format("Version id: %s, property: %s", vManager.getBaseVersion(n.getPath())
				.getIdentifier(), n.getProperty("property").getString()));
	}

}
