package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ClientiRuoloDTO(
        @NotEmpty(message = "Il ruolo è obbligatorio")
        @Size(min = 3, max = 15, message = "Il ruolo deve essere compreso tra 3 e 15 caratteri")
        String ruolo) {
}
