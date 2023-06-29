package com.speedscale.decoy.spaceboot;

// https://github.com/spring-guides/gs-rest-service/blob/main/complete/src/main/java/com/example/restservice/Greeting.java
public class Greeting {
  private final long id;
	private final String content;

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}
}
