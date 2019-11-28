package com.changgou;

import com.changgou.utils.BCrypt;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author ：jingbo.li
 * @date ：Created in 2019/8/18 10:21
 * @description ：Bcrypt测试类
 * @version: 1.0
 */
@RunWith(SpringRunner.class)
public class BcryptTest {
    public static void main(String[] args) {
        String gensalt = BCrypt.gensalt();
        System.out.println(gensalt);
        String p = BCrypt.hashpw("123456", gensalt);
        System.out.println(p);
        boolean checkpw = BCrypt.checkpw("123456", p);
        System.out.println(checkpw);

    }
}
