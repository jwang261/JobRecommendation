package com.jwang261;

import com.jwang261.dao.HistoryDao;
import com.jwang261.dao.ItemDao;
import com.jwang261.pojo.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootTest
class JobRecommendationApplicationTests {

    @Autowired
    DataSource dataSource;

    @Autowired
    ItemDao itemDao;

    @Autowired
    HistoryDao historyDao;

    @Test
    void contextLoads() throws SQLException {
        Item item = Item.builder()
                .itemId("577")
                .name("test")
                .build();


        itemDao.saveItem(item);


    }

    @Test
    public void test(){
    }

}
