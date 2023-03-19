package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.RunWith;
import org.junit.runner.notification.Failure;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EcommerceApplication.class)
@Slf4j
public class EcommerceApplicationTests {

    @Test
    public void contextLoads() {
    }

    @Test
    public void allTest() {
        Result result = JUnitCore.runClasses(TestSuite.class);
        for (Failure failure : result.getFailures()) {
            log.info(failure.getTestHeader());
            log.info(failure.getTrace());
            log.info(failure.toString());
        }
        ;
        Assert.assertEquals(0,result.getFailures().size());
        Assert.assertTrue(result.wasSuccessful());
    }

}
