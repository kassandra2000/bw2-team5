package kassandrafalsitta.bw2.controllers;

import kassandrafalsitta.bw2.exceptions.BadRequestException;
import kassandrafalsitta.bw2.payloads.ClientiDTO;
import kassandrafalsitta.bw2.payloads.ClientiLoginDTO;
import kassandrafalsitta.bw2.payloads.ClientiLoginRespDTO;
import kassandrafalsitta.bw2.payloads.ClientiRespDTO;
import kassandrafalsitta.bw2.services.AuthService;
import kassandrafalsitta.bw2.services.ClientiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private ClientiService ClientiService;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ClientiLoginRespDTO login(@RequestBody ClientiLoginDTO payload) {
        return new ClientiLoginRespDTO(this.authService.checkCredentialsAndGenerateToken(payload));
    }


    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ClientiRespDTO createClienti(@RequestBody  @Validated ClientiDTO body, BindingResult validationResult) {
        if(validationResult.hasErrors())  {
            String messages = validationResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Ci sono stati errori nel payload. " + messages);
        } else {
            return new ClientiRespDTO(this.ClientiService.saveClienti(body).getId());
        }
    }
}
