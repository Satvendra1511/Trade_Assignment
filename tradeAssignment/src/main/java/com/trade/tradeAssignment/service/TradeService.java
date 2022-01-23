package com.trade.tradeAssignment.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trade.tradeAssignment.dao.TradeDao;
import com.trade.tradeAssignment.dao.TradeRepository;
import com.trade.tradeAssignment.exception.InvalidTradeException;
import com.trade.tradeAssignment.model.Trade;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private static final Logger log = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    TradeDao tradeDao;

    @Autowired
    TradeRepository tradeRepository;

    public boolean isValid(Trade trade){
        if(validateMaturityDate(trade)) {
            Optional<Trade> exsitingTrade = tradeRepository.findById(trade.getTradeId());
             if (exsitingTrade.isPresent()) {
            	 if(trade.getVersion() >= exsitingTrade.get().getVersion()) {
            		 return true;
            	 } else {
            		 throw new InvalidTradeException(trade.getTradeId()+ " , Version=" +trade.getVersion()+","+ " Same tradeId should have same or greater version.");
            	 }
                 
             }
                 return true;
             
         }else {
        	 throw new InvalidTradeException(trade.getTradeId()+ " , " +trade.getMaturityDate()+ "  MaturityDate should be greater then CurrentDate."); 
         }
    }

    private boolean validateMaturityDate(Trade trade){
    	if(trade.getCreatedDate() == null) {
    		return trade.getMaturityDate().isBefore(LocalDate.now())  ? false:true;
    	} else {
    		return trade.getMaturityDate().isBefore(trade.getCreatedDate())  ? false:true;
    	}
        
    }

    public void  persist(Trade trade){
    	if(trade.getCreatedDate() == null) {
    		trade.setCreatedDate(LocalDate.now());
    	}
        tradeRepository.save(trade);
    }

    public List<Trade> findAll(){
       return  tradeRepository.findAll();
    }

    public void updateExpiryFlagOfTrade(){
        tradeRepository.findAll().stream().forEach(t -> {
                if (!validateMaturityDate(t)) {
                    t.setExpiredFlag("Y");
                    log.info("Trade which needs to updated {}", t);
                    tradeRepository.save(t);
                }
            });
        }


}
