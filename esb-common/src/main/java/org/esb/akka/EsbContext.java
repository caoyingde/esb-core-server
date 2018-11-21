package org.esb.akka;

/**
 * @deprecated
 * @author Andy.Cao
 * @date 2018-11-21
 */
public class EsbContext {

	private static AkkaEsbSystem akkaEsbSystem;

	public static AkkaEsbSystem getAkkaEsbSystem() {
		return akkaEsbSystem;
	}

	protected static void setAkkaEsbSystem(AkkaEsbSystem akkaEsbSystem) {
		EsbContext.akkaEsbSystem = akkaEsbSystem;
	}

}
