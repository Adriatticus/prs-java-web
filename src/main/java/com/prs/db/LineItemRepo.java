package com.prs.db;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prs.model.LineItem;

public interface LineItemRepo extends JpaRepository<LineItem, Integer> {
	List<LineItem> findAllLineItemsByRequestId(int reqId);
	//findRequestId(String lineItem);
	//LineItem findRequestIdOfLineItem(LineItem lineItem);
}
