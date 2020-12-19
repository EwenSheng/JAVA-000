package consumer.controller;

import com.syw.rpc.example.api.model.RMBAccount;
import com.syw.rpc.example.api.model.USDAccount;
import com.syw.rpc.example.api.service.RMBAccountService;
import com.syw.rpc.example.api.service.USDAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 19:15
 * @description:
 */
@RestController()
public class DemoController {

    @Autowired
    private USDAccountService usdAccountService;

    @Autowired
    private RMBAccountService rmbAccountService;

    @GetMapping("/test")
    public void test() {
        System.out.println("test begin =>>>>>");

        System.out.println("usd=>" + usdAccountService.transfer(new USDAccount()));

        System.out.println("rmb=>" + rmbAccountService.transfer(new RMBAccount()));

        System.out.println("test end =>>>>>");
    }
}
