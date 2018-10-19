package org.fogbowcloud.membershipservice.http;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.fogbowcloud.membershipservice.MembershipService;
import org.fogbowcloud.membershipservice.constants.ApiDocumentation;
import org.fogbowcloud.membershipservice.constants.Messages;
import org.fogbowcloud.membershipservice.service.WhiteList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping(value = Membership.ENDPOINT)
@Api(description = ApiDocumentation.Membership.API)
public class Membership {

    protected static final String ENDPOINT = "members";

    private static final Logger LOGGER = Logger.getLogger(Membership.class);

    private MembershipService membershipService;

    public Membership() {
        try {
            this.membershipService = new WhiteList();
        } catch (FileNotFoundException e) {
            LOGGER.error(String.format(Messages.Error.CONFIGURATION_FILE_NOT_FOUND), e);
        }
    }

    /**
     * Gets JSON response with a list of XMPP members ID.
     */
    @ApiOperation(value = ApiDocumentation.Membership.DESCRIPTION)
    @GetMapping
    public ResponseEntity<List<String>> listMembers() {
        if (this.membershipService == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        try {
            List<String> membersId = this.membershipService.listMembers();
            return new ResponseEntity<>(membersId, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error(Messages.Error.INTERNAL_SERVER_ERROR, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
