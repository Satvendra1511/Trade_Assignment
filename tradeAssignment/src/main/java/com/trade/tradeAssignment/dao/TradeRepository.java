package com.trade.tradeAssignment.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trade.tradeAssignment.model.Trade;

@Repository
public interface TradeRepository extends JpaRepository<Trade,String> {
}
