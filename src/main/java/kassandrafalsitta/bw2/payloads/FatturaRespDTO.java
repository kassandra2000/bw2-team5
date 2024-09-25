package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FatturaRespDTO(
        @NotNull(message = "L'UUID è obbligatorio")
        UUID fatturaId) {
}
