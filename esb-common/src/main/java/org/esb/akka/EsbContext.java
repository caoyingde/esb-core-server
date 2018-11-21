package org.esb.akka;


public class EsbContext {

	private static AkkaEsbSystem akkaEsbSystem;

	public static AkkaEsbSystem getAkkaEsbSystem() {
		return akkaEsbSystem;
	}

	protected static void setAkkaEsbSystem(AkkaEsbSystem akkaEsbSystem) {
		EsbContext.akkaEsbSystem = akkaEsbSystem;
	}

}
