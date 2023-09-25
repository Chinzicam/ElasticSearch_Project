package cn.itcast.hotel;

import cn.itcast.hotel.service.impl.HotelService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author : chinzicam
 * @create 2023/9/25 18:57
 */
@SpringBootTest
public class HotelTest {
    @Autowired
    private HotelService hotelService;
    @Test
    void testFindAll() {
        int count = hotelService.count();
        System.out.println(count);
    }
}
