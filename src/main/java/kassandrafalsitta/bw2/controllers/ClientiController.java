package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.entities.Cliente;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiRuoloDTO;
import kassandrafalsitta.bw2.services.ClientiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/clienti")
public class ClientiController {
    @Autowired
    private ClientiService clientiService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<Cliente> getAllClienti(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size,
                                       @RequestParam(defaultValue = "id") String sortBy) {
        return this.clientiService.findAll(page, size, sortBy);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente getClienteById(@PathVariable UUID userId) {
        return clientiService.findById(userId);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findClienteByIdAndUpdate(@PathVariable UUID userId, @RequestBody @Validated ClientiDTO body) {
        return clientiService.findByIdAndUpdate(userId, body);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente findByIdAndUpdateRole(@PathVariable UUID userId, @RequestBody @Validated ClientiRuoloDTO body) {
        return clientiService.findByIdAndUpdateRuolo(userId, body);
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findClienteByIdAndDelete(@PathVariable UUID userId) {
        clientiService.findByIdAndDelete(userId);
    }

    @PostMapping("/{authorId}/avatar")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Cliente uploadCover(@PathVariable UUID userId, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.clientiService.uploadImage(userId, image);
    }

    // me
    @GetMapping("/me")
    public Cliente getProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        return currentAuthenticatedCliente;
    }

    @PutMapping("/me")
    public Cliente updateProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @RequestBody ClientiDTO body) {
        return this.clientiService.findByIdAndUpdate(currentAuthenticatedCliente.getId(), body);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Cliente currentAuthenticatedCliente) {
        this.clientiService.findByIdAndDelete(currentAuthenticatedCliente.getId());
    }

    @PostMapping("/me/avatar")
    public Cliente uploadMyCover(@AuthenticationPrincipal Cliente currentAuthenticatedCliente, @RequestParam("avatar") MultipartFile image) throws IOException {
        return this.clientiService.uploadImage(currentAuthenticatedCliente.getId(), image);
    }
}
