package org.esb.server;

import scala.concurrent.duration.FiniteDuration;

/**
 * 定时执行接口
 * @author JiangFeng 
 * @version 0.0.1
 */
public interface Schedulable {

	/**
	 * @see scala.concurrent.duration.Duration;
	 * @see java.util.concurrent.TimeUnit; <br />
	 * {@code Duration.create(50, TimeUnit.MILLISECONDS); <br />
	 * 	Duration.Zero();
	 * }
	 * @return
	 */
	FiniteDuration getDelay();
	
	/**
	 * @see scala.concurrent.duration.Duration;
	 * @see java.util.concurrent.TimeUnit; <br />
	 * {@code Duration.create(50, TimeUnit.MILLISECONDS); <br />
	 * 	Duration.Zero();
	 * }
	 * @return
	 */
	FiniteDuration getPeriod();
}
