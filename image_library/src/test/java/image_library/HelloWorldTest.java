package image_library;

import static org.junit.Assert.*;

import org.junit.Test;

public class HelloWorldTest {

	@Test
	public void testHelloWorld() {
		HelloWorld helloWorld = new HelloWorld("text");
		assertEquals("test", helloWorld.toString());
	}

}
