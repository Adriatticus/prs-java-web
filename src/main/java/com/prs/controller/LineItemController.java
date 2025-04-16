package com.prs.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.LineItemRepo;
import com.prs.db.ProductRepo;
import com.prs.db.RequestRepo;
import com.prs.model.LineItem;
import com.prs.model.Request;

@CrossOrigin
@RestController
@RequestMapping("api/lineitems")
public class LineItemController {

	@Autowired
	private LineItemRepo lineItemRepo;
	@Autowired
	private RequestRepo requestRepo;
	@Autowired
	private ProductRepo productRepo;

	@GetMapping("/")
	public List<LineItem> getAll() {
		return lineItemRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<LineItem> getById(@PathVariable int id) {
		Optional<LineItem> li = lineItemRepo.findById(id);
		if (li.isPresent()) {
			return li;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Line item not found for id, " + id + ".");
		}
	}

	@PostMapping("")
	public LineItem add(@RequestBody LineItem lineItem) {
		LineItem li = new LineItem();
		li.setProduct(productRepo.findById(lineItem.getProduct().getId()).get());
		li.setRequest(requestRepo.findById(lineItem.getRequest().getId()).get());
		li.setQuantity(lineItem.getQuantity());
		lineItemRepo.save(li);
		RecalculateRequestTotal(lineItem.getRequest().getId());
		return li;
	}

	@PutMapping("/{id}")
	public void putLineItem(@PathVariable int id, @RequestBody LineItem lineItem) {
		if (id != lineItem.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Line item id mismatch vs URL.");
		} else if (lineItemRepo.existsById(lineItem.getId())) {
			lineItemRepo.save(lineItem);
			RecalculateRequestTotal(lineItem.getRequest().getId());
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found for id, " + id + ".");
		}
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		LineItem lineItem = lineItemRepo.findById(id).get();
		int reqId = lineItem.getRequest().getId();
		if (lineItemRepo.existsById(id)) {
			lineItemRepo.deleteById(id);
			RecalculateRequestTotal(reqId);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Line item not found for id, " + id + ".");
		}
	}

	@GetMapping("/lines-for-req/{reqId}")
	public List<LineItem> getLineItemsByReq(@PathVariable int reqId) {
		List<LineItem> li = lineItemRepo.findAllLineItemsByRequestId(reqId);
//		if (li.isEmpty()) {
//			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
//					"Request may be invalid or a valid request may have no line items.");
//		}
		return li;
	}

	private void RecalculateRequestTotal(int reqId) {
		Request request = requestRepo.findById(reqId).get();
		List<LineItem> lineItems = lineItemRepo.findAllLineItemsByRequestId(reqId);
		double total = 0.0;
		for (LineItem lineItem : lineItems) {
			total += lineItem.product.getPrice() * lineItem.quantity;
		}
		request.setTotal(total);
		requestRepo.save(request);
	}
}
