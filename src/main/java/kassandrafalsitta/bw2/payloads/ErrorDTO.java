package kassandrafalsitta.bw2.payloads;

import java.time.LocalDateTime;

public record ErrorDTO(String message,LocalDateTime timestamp) {
}
