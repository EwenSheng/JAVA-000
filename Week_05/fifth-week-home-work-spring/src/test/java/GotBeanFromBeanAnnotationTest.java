import org.home.work.config.BeanConfig;
import org.home.work.entity.JobInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/15 20:59
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = BeanConfig.class)
public class GotBeanFromBeanAnnotationTest {

    @Autowired
    private JobInfo jobInfo;

    @Test
    public void test() {

        System.out.println(jobInfo.toString());
    }

}
