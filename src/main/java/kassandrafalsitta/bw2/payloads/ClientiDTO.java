package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClientiDTO(
        @NotEmpty(message = "Lo username è obbligatorio")
        @Size(min = 3, max = 40, message = "Lo username deve essere compreso tra 3 e 40 caratteri")
        String username,
        @NotEmpty(message = "Il nome proprio è obbligatorio")
        @Size(min = 3, max = 40, message = "Il nome proprio deve essere compreso tra 3 e 40 caratteri")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 40, message = "Il cognome deve essere compreso tra 3 e 40 caratteri")
        String cognome,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,
        @NotEmpty(message = "La password è obbligatoria")
        @Size(min = 8, message = "La password deve avere almeno 8 caratteri")
        String password,
        @NotEmpty(message = "La Partita IVA è obbligatoria")
        @Size(min = 7, message = "La Partita IVA deve avere almeno 7 caratteri")
        String partitaIva,
        @NotEmpty(message = "La data è obbligatoria")
        @Size(min = 10, max = 10, message = "La data deve avere 10 caratteri")
        String dataInserimento,
        @NotEmpty(message = "La data è obbligatoria")
        @Size(min = 10, max = 10, message = "La data deve avere 10 caratteri")
        String dataUltimoContatto,
        @NotEmpty(message = "Il fatturato annuale è obbligatorio")
        @Size(min = 1, max = 20, message = "Il fatturato annuale deve avere almeno 1 carattere")
        String fatturatoAnnuale,
        @NotEmpty(message = "La Pec è obbligatoria")
        @Size(min = 3, max = 40, message = "La Pec deve essere compresa tra 3 e 40 caratteri")
        String pec,
        @NotNull(message = "Il telefono è obbligatorio")
        @Size(min = 8, max = 15, message = "Il telefono deve essere compresa tra 8 e 15 caratteri")
        String telefono,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String emailDiContatto,
        @NotEmpty(message = "Il nome di contatto è obbligatorio")
        @Size(min = 3, max = 40, message = "Il nome di contatto deve essere compreso tra 3 e 40 caratteri")
        String nomeDiContatto,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 40, message = "Il cognome deve essere compreso tra 3 e 40 caratteri")
        String cognomeDiContatto,
        @NotNull(message = "Il telefono è obbligatorio")
        @Size(min = 8, max = 15, message = "Il telefono deve essere compresa tra 8 e 15 caratteri")
        String telefonoDiContatto,
        @NotEmpty(message = "Il logo aziendale è obbligatorio")
        @Size(min = 3, max = 40, message = "Il logo aziendale deve essere compreso tra 3 e 40 caratteri")
        String logoAziendale,
        @NotEmpty(message = "Il tipo di clienti è obbligatorio")
        @Size(min = 1, max = 40, message = "Il tipo di clienti deve essere compreso tra 1 e 40 caratteri")
        String tipoClienti
) {
}
