package org.fogbowcloud.membershipservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = MembershipController.ENDPOINT)
public class MembershipController {

    protected static final String ENDPOINT = "members";

    private MembershipService membershipService;

    public MembershipController() {
        this.membershipService = new Whitelist();
    }

    @GetMapping
    public ResponseEntity<List<String>> listMembers() {
        try {
            List<String> membersId = this.membershipService.listMembers();
            return new ResponseEntity<>(membersId, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
