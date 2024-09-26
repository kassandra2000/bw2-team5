package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IndirizzoDTO(
        @NotEmpty(message = "La via è obbligatoria")
        @Size(min = 2, max = 50, message = "La via deve essere compresa tra 2 e 50 caratteri")
        String via,

        @NotEmpty(message = "Il civico è obbligatorio")
        @Size(min = 1, max = 10, message = "Il civico deve essere compreso tra 1 e 10 caratteri")
        String civico,

        @NotEmpty(message = "La località è obbligatoria")
        @Size(min = 2, max = 40, message = "La località deve essere compresa tra 2 e 40 caratteri")
        String localita,

        @NotEmpty(message = "Il CAP è obbligatorio")
        @Size(min = 5, max = 5, message = "Il CAP deve essere di 5 caratteri")
        String cap,

        @NotNull(message = "L'ID del comune è obbligatorio")
        String comuneId, // Cambiato da UUID a String

        @NotEmpty(message = "L'UUID del cliente è obbligatorio")
        @Size(min = 36, max = 36, message = "L'UUID del cliente  deve avere 36 caratteri")
        String clienteId,

        @NotEmpty(message = "Il tipo indirizzo è obbligatorio")
        @Size(min = 3, max = 40, message = "Il tipo indirizzo  deve essere  compreso tra 3 e 40 caratteri")
        String tipoIndirizzo

) {
}
