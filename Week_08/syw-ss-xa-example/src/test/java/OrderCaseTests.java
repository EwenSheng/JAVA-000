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
    public void inset() {
        orderService.insert();
    }

    @Test
    public void get() {
        System.out.println(orderService.get(Long.valueOf("543525333085716481")));
    }

    @Test
    public void update() {
        orderService.update(Long.valueOf("543525333085716481"), 1);
    }

    @Test
    public void delete() {
        orderService.delete(Long.valueOf("543525333085716481"));
    }

    @Test
    public void xaOperating() {
        orderService.xaOperating(Long.valueOf("543525333131853825"), Long.valueOf("543525333022801920"));
    }
}
