package zw.co.dcl.engine.whatsapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zw.co.dcl.engine.whatsapp.entity.ConfigEntity;
import zw.co.dcl.engine.whatsapp.entity.dto.RestSaveSession;
import zw.co.dcl.engine.whatsapp.service.WebhookService;

@RestController
@RequestMapping("/webhook")
public class WebhookController {
    final WebhookService service;

    public WebhookController(WebhookService service) {
        this.service = service;
    }

    @GetMapping
    @ResponseBody
    private ResponseEntity<?> tokenVerifier(@RequestParam("hub.mode") String mode,
                                            @RequestParam("hub.challenge") String challenge,
                                            @RequestParam("hub.verify_token") String verifyToken,
                                            HttpServletRequest request
    ) {
        return service.verifyToken(mode, challenge, verifyToken, request);
    }

    @PostMapping
    private ResponseEntity<?> webhookHandler(@RequestBody Object payload, HttpServletRequest request) {
        System.out.println(payload);
        System.out.println(request);
        return ResponseEntity.ok(service.processWebhook(payload, request));
    }

    @PostMapping("/config")
    private ResponseEntity<?> setConfig(@RequestBody ConfigEntity config) {
        return ResponseEntity.ok(service.setConfig(config));
    }

    @GetMapping("/session/{user}/{key}")
    private ResponseEntity<?> getFromSession(@PathVariable String user, @PathVariable String key) {
        return ResponseEntity.ok(service.getDataFromSession(user, key));
    }

    @PostMapping("/session")
    private ResponseEntity<?> addToSession(@RequestBody RestSaveSession dto) {
        service.saveToSession(dto);
        return ResponseEntity.ok("Saved");
    }
}
