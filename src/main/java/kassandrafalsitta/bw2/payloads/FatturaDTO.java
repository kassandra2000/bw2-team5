package kassandrafalsitta.bw2.payloads;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record FatturaDTO(
        @NotEmpty(message = "La data è obbligatoria")
        @Size(min = 10, max = 10, message = "La data deve avere 10 caratteri")
        String dataFattura,
        @NotNull(message = "L'importo è obbligatorio")
        @Min(value = 1, message = "L'importo deve essere maggiore o uguale a 1")
        double importo,
        @NotEmpty(message = "Il numero è obbligatorio")
        @Size(min = 1, max = 30, message = "Il numero deve avere almeno 1 carattere")
        String numero,
        @NotEmpty(message = "Lo stato della fattura è obbligatorio")
        @Size(min = 3, max = 40, message = "Lo stato della fattura deve essere compreso tra 3 e 40 caratteri")
        String statoFattura,
        @NotEmpty(message = "L'UUID del cliente è obbligatorio")
        @Size(min = 36, max = 36, message = "L'UUID del cliente  deve avere 36 caratteri")
        String clienteID

) {
}
