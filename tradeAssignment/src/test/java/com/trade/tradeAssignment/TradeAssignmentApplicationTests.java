package com.trade.tradeAssignment;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.trade.tradeAssignment.controller.TradeController;
import com.trade.tradeAssignment.exception.InvalidTradeException;
import com.trade.tradeAssignment.model.Trade;

@SpringBootTest
class TradeAssignmentApplicationTests {

	@Autowired
	private TradeController tradeController;

	@Test
	void testTradeSuccessful() {
		ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T1",1,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
	}

	@Test
	void testTradeWhenMaturityDatePast() {
		try {
			LocalDate localDate = getLocalDate(2015, 05, 21);
			ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T2", 1, localDate));
		}catch (InvalidTradeException ie) {
			Assertions.assertEquals("Invalid Trade: T2 , 2015-05-21  MaturityDate should be greater then CurrentDate.", ie.getMessage());
		}
	}

	@Test
	void testTradeByPassingOldVersion() {
		ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList.get(0).getVersion());
		Assertions.assertEquals("B1",tradeList.get(0).getBookId());
		try {
			ResponseEntity responseEntity1 = tradeController.tradeValidateStore(createTrade("T1", 1, LocalDate.now()));

		}catch (InvalidTradeException e){
			Assertions.assertEquals("Invalid Trade: T1 , Version=1, Same tradeId should have same or greater version.", e.getMessage());
		}
		List<Trade> tradeList1 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList1.size());
		Assertions.assertEquals("T1",tradeList1.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList1.get(0).getVersion());
		Assertions.assertEquals("B1",tradeList.get(0).getBookId());
	}

	@Test
	void testTradeByPasssingSameVersionTrade(){
		ResponseEntity responseEntity = tradeController.tradeValidateStore(createTrade("T1",2,LocalDate.now()));
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity);
		List<Trade> tradeList =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList.size());
		Assertions.assertEquals("T1",tradeList.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList.get(0).getVersion());
		Assertions.assertEquals("B1",tradeList.get(0).getBookId());

		Trade trade2 = createTrade("T1",2,LocalDate.now());
		trade2.setBookId("T1B1V2");
		ResponseEntity responseEntity2 = tradeController.tradeValidateStore(trade2);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity2);
		List<Trade> tradeList2 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList2.size());
		Assertions.assertEquals("T1",tradeList2.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList2.get(0).getVersion());
		Assertions.assertEquals("T1B1V2",tradeList2.get(0).getBookId());

		Trade trade3 = createTrade("T1",2,LocalDate.now());
		trade3.setBookId("T1B1V3");
		ResponseEntity responseEntity3 = tradeController.tradeValidateStore(trade3);
		Assertions.assertEquals(ResponseEntity.status(HttpStatus.OK).build(),responseEntity3);
		List<Trade> tradeList3 =tradeController.findAllTrades();
		Assertions.assertEquals(1, tradeList3.size());
		Assertions.assertEquals("T1",tradeList3.get(0).getTradeId());
		Assertions.assertEquals(2,tradeList3.get(0).getVersion());
		Assertions.assertEquals("T1B1V3",tradeList3.get(0).getBookId());

	}
	private Trade createTrade(String tradeId,int version,LocalDate  maturityDate){
		Trade trade = new Trade();
		trade.setTradeId(tradeId);
		trade.setBookId("B1");
		trade.setVersion(version);
		trade.setCounterPartyId("CP-1");
		trade.setMaturityDate(maturityDate);
		trade.setExpiredFlag("N");
		return trade;
	}

	public static LocalDate getLocalDate(int year,int month, int day){
		LocalDate localDate = LocalDate.of(year,month,day);
		return localDate;
	}
}
