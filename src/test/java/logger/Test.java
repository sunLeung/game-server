package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by uc on 2016/12/13.
 */
public class Test {
    public static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        try {
            String a = null;
            a.split(",");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
