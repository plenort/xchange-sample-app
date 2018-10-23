package com.xchange.sample.web.rest;

import com.xchange.sample.service.AlertService;
import com.xchange.sample.service.dto.AlertDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/alert")
public class AlertResource {

    @Autowired
    private AlertService alertService;

    @GetMapping
    public ResponseEntity<List<AlertDTO>> list(@RequestParam(name="pair", required = false) String pair,
                                               @RequestParam(name="limit", required = false) Long limit){
        return ResponseEntity.ok(alertService.listAlerts());
    }

    @PutMapping
    public ResponseEntity create(@RequestParam(name="pair") String pair,
                                 @RequestParam(name="limit") Long limit) {
        alertService.addAlert(pair, limit);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    public ResponseEntity delete(@RequestParam(name="pair") String pair,
                                 @RequestParam(name="limit") Long limit) {
        alertService.removeAlert(pair, limit);
        return ResponseEntity.ok().build();
    }
}
