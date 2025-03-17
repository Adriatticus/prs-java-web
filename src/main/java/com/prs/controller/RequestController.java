package com.prs.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.prs.db.RequestRepo;
import com.prs.model.Request;
import com.prs.model.RequestCreateDTO;
import com.prs.model.RequestStatus;

@CrossOrigin
@RestController
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	private RequestRepo requestRepo;

	@GetMapping("/")
	public List<Request> getAll() {
		return requestRepo.findAll();
	}

	@GetMapping("/{id}")
	public Optional<Request> getById(@PathVariable int id) {
		Optional<Request> r = requestRepo.findById(id);
		if (r.isPresent()) {
			return r;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id, " + id + ".");
		}
	}

	@PostMapping("")
	public Request add(@RequestBody RequestCreateDTO requestDTO) {
		Request r = new Request();
		r.setUser(requestDTO.user);
		r.setRequestNumber(ReqNumGen());
		r.setDescription(requestDTO.description);
		r.setJustification(requestDTO.justification);
		r.setDateNeeded(requestDTO.dateNeeded);
		r.setDeliveryMode(requestDTO.deliveryMode);
		r.setStatus(RequestStatus.NEW);
		r.setSubmittedDate(LocalDateTime.now());
		r.setTotal(0.0);
		return requestRepo.save(r);
	}

	@PutMapping("/{id}")
	public void putRequest(@PathVariable int id, @RequestBody Request request) {
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (requestRepo.existsById(request.getId())) {
			requestRepo.save(request);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id, " + id + ".");
		}

	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable int id) {
		if (requestRepo.existsById(id)) {
			requestRepo.deleteById(id);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id, " + id + ".");
		}
	}

	public String ReqNumGen() {
		LocalDate now = LocalDate.now();
		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyMMdd");
		StringBuilder requestNbr = new StringBuilder("R");
		String dateStr = now.format(dateFormat).toString();
		requestNbr.append(dateStr);
		String prefixn = requestRepo.findMaxRequestNumber();
		if (prefixn == null) {
			requestNbr.append("0001");
		} else {
			String lastFour = prefixn.substring(8);
			Integer counter = Integer.parseInt(lastFour);
			counter++;
			lastFour = counter.toString();
			lastFour = String.format("%04d", Integer.parseInt(lastFour));
			requestNbr.append(lastFour);
		}
		return requestNbr.toString();
	}

}
