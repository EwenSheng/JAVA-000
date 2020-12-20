package consumer.controller;

import consumer.serivce.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @author: Ewen-Sheng
 * @date: 2020/12/19 19:15
 * @description:
 */
@RestController()
public class DemoController {

    @Autowired
    private TransferService service;

    @GetMapping("/test_a")
    public void testA() {
        System.out.println("test_a begin =>>>>>");

        // 用户 A 的美元账户和人民币账户都在 A 库,使用 1 美元兑换 7 人民币;
        service.transferToRMB(1L, new BigDecimal(1));

        System.out.println("test_a end =>>>>>");
    }

    @GetMapping("/test_b")
    public void testB() {
        System.out.println("test_b begin =>>>>>");

        // 用户 B 的美元账户和人民币账户都在 B 库,使用 7 人民币兑换 1 美元;
        service.transferToUsd(2L, new BigDecimal(7));

        System.out.println("test_b end =>>>>>");
    }
}
