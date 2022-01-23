package com.trade.tradeAssignment.dao;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.trade.tradeAssignment.model.Trade;

public interface TradeDao {


    public  static Map<String,Trade> tradeMap = new ConcurrentHashMap<>();


    public void save(Trade trade);

    List<Trade> findAll();

    Trade findTrade(String tradeId);
}
