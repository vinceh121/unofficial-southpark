package me.vinceh121.unofficialsouthpark;

import org.junit.Test;

import java.io.IOException;

import me.vinceh121.unofficialsouthpark.entities.Geolocation;

import static org.junit.Assert.*;

public class ApiTest {
	@Test
	public void simpleGet() throws IOException {
		final SPManager sp = new SPManager();
		sp.loadData(Geolocation.EN);
		System.out.println(sp.getData());
	}
}