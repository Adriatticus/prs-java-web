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
import com.prs.model.LineItem;
import com.prs.model.Request;
import com.prs.model.RequestCreateDTO;
import com.prs.model.RequestRejectDTO;
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
		r.setUser(requestDTO.getUser());
		r.setRequestNumber(ReqNumGen());
		r.setDescription(requestDTO.getDescription());
		r.setJustification(requestDTO.getJustification());
		r.setDateNeeded(requestDTO.getDateNeeded());
		r.setDeliveryMode(requestDTO.getDeliveryMode());
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

	@PutMapping("/submit-review/{id}")
	public void putRequestSubmitReview(@PathVariable int id) {
		Request request = requestRepo.findById(id).get();
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (requestRepo.existsById(request.getId())) {
			if (request.getTotal() <= 50) {
				request.setStatus(RequestStatus.APPROVED);
				request.setSubmittedDate(LocalDateTime.now());
			} else if (request.getTotal() > 50) {
				request.setStatus(RequestStatus.REVIEW);
				request.setSubmittedDate(LocalDateTime.now());

			}
			requestRepo.save(request);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id, " + id + ".");
		}
	}

	@GetMapping("/list-review/{userId}")
	public List<Request> getLineItemsByReq(@PathVariable int userId) {
		List<Request> r = requestRepo.listInReview(userId);
		if (r.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Nothing to review.");
		}
		return r;

	}

	@PutMapping("/approve/{id}")
	public void putApproveRequest(@PathVariable int id) {
		Request request = requestRepo.findById(id).get();
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (requestRepo.existsById(request.getId())) {
			request.setStatus(RequestStatus.APPROVED);
			requestRepo.save(request);
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found for id, " + id + ".");
		}

	}

	@PutMapping("/reject/{id}")
	public void putRejectRequest(@PathVariable int id, @RequestBody RequestRejectDTO requestDTO) {
		Request request = requestRepo.findById(id).get();
		if (id != request.getId()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request id mismatch vs URL.");
		} else if (requestRepo.existsById(request.getId())) {
			request.setStatus(RequestStatus.REJECTED);
			request.setReasonForRejection(requestDTO.getReasonForRejection());
			requestRepo.save(request);
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
