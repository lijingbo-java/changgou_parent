package com.changgou.file;

import com.changgou.entity.IdWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PublicKey;
import java.util.Date;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/14 16:12
 * @description ：
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
public class IdWorkTest {

    @Test
    public void idWorkerTest(){
        IdWorker idWorker = new IdWorker(0, 0);
        for (int i = 0; i < 100; i++) {
            long id=idWorker.nextId();
            System.out.println(id);
        }
    }

}
