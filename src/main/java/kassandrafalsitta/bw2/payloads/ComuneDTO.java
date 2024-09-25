package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ComuneDTO(
        @NotEmpty(message = "Il nome del comune è obbligatorio")
        @Size(min = 2, max = 40, message = "Il nome del comune deve essere compreso tra 2 e 40 caratteri")
        String nome,

        @NotEmpty(message = "La provincia è obbligatoria")
        String provinciaId
) {
}
