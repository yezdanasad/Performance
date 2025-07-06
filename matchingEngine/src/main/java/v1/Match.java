package v1;

import java.util.UUID;

public record Match(UUID buyOrderId,
                    UUID sellOrderId,
                    int quantity,
                    double price,
                    Long timeOfMatch) {

}