import com.syw.ss.xa.example.ExampleApplication;
import com.syw.ss.xa.example.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/9 11:58
 * @description:
 */
@SpringBootTest(classes = ExampleApplication.class)
public class OrderCaseTests {

    @Autowired
    OrderService orderService;

    @Test
    public void xa() {
        orderService.insert();
    }
}
