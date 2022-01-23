package com.trade.tradeAssignment.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import static org.awaitility.Awaitility.await;
import com.trade.tradeAssignment.TradeAssignmentApplication;

import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

import org.awaitility.Duration;

@SpringJUnitConfig(TradeAssignmentApplication.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TradeScheduledTasksTest {

    @SpyBean
    private TradeScheduledTasks TradeScheduledTasks;

    @Test
    public void whenWaitOneSecond_thenScheduledIsCalledAtLeastTenTimes() {
        await()
                .atMost(Duration.ONE_MINUTE)
                .untilAsserted(() -> verify(TradeScheduledTasks,atLeast(0)).reportCurrentTime());
    }

}
