package consumer.controller;

import com.syw.rpc.example.api.model.RMBAccount;
import com.syw.rpc.example.api.model.USDAccount;
import com.syw.rpc.example.api.service.RMBAccountService;
import com.syw.rpc.example.api.service.USDAccountService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 19:15
 * @description:
 */
@RestController()
public class DemoController {

    @Reference
    private USDAccountService usdAccountService;

    @Reference
    private RMBAccountService accountService;

    @GetMapping("/test")
    public void test() {
        System.out.println("test begin =>>>>>");

        usdAccountService.transfer(new USDAccount());

        accountService.transfer(new RMBAccount());

        System.out.println("test end =>>>>>");
    }
}
