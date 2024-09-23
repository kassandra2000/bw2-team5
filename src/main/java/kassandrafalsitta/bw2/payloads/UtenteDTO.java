package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UtenteDTO(
        @NotEmpty(message = "Lo username è obbligatorio")
        @Size(min = 3, max = 20, message = "Lo username deve essere compreso tra 3 e 20 caratteri")
        String username,

        @NotEmpty(message = "La password è obbligatoria")
        @Size(min = 6, message = "La password deve avere almeno 6 caratteri")
        String password,

        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il nome deve essere compreso tra 2 e 30 caratteri")
        String nome,

        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 2, max = 30, message = "Il cognome deve essere compreso tra 2 e 30 caratteri")
        String cognome,

        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email non è valida")
        String email,

        @Size(max = 255, message = "L'URL dell'avatar deve avere al massimo 255 caratteri")
        String avatar
) {
}

