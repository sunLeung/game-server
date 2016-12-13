package logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by uc on 2016/12/13.
 */
public class Test {
    public static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        System.out.println(Test.class.getName());
        System.out.println(Test.class);
        logger.info("abc");
    }

}
