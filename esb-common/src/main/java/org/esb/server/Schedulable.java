package org.esb.server;

import scala.concurrent.duration.FiniteDuration;


/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated 定时执行接口
 */
public interface Schedulable {

    /**
     * @return
     * @see scala.concurrent.duration.Duration;
     * @see java.util.concurrent.TimeUnit; <br />
     * {@code Duration.create(50, TimeUnit.MILLISECONDS); <br />
     * Duration.Zero();
     * }
     */
    FiniteDuration getDelay();

    /**
     * @return
     * @see scala.concurrent.duration.Duration;
     * @see java.util.concurrent.TimeUnit; <br />
     * {@code Duration.create(50, TimeUnit.MILLISECONDS); <br />
     * Duration.Zero();
     * }
     */
    FiniteDuration getPeriod();
}
