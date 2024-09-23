package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record ClientiLoginRespDTO(
        @NotEmpty(message = "L'accessToken è obbligatorio")
        @Size(min = 20, max = 300, message = "L'accessToken deve essere compreso tra 20 e 300 caratteri")
        String accessToken) {
}
