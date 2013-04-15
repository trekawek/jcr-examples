package com.cognifide.jcr;

import javax.jcr.Repository;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.Node;
import org.apache.jackrabbit.core.TransientRepository;

// http://jackrabbit.apache.org/first-hops.html
public class HelloWorld {

	public static void main(String[] args) throws Exception {
		Commons.cleanUpDirs();

		Repository repository = new TransientRepository();
		Session session = repository.login(new SimpleCredentials("username", "password".toCharArray()));
		try {
			Node root = session.getRootNode();

			// Store content
			Node hello = root.addNode("hello");
			Node world = hello.addNode("world");
			world.setProperty("message", "Hello, World!");
			session.save();

			// Retrieve content
			Node node = root.getNode("hello/world");
			System.out.println(node.getPath());
			System.out.println(node.getProperty("message").getString());

			// Remove content
			root.getNode("hello").remove();
			session.save();
		} finally {
			session.logout();
		}
	}

}
