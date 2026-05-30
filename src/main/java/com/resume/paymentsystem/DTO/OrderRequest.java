package com.resume.paymentsystem.DTO;

import jakarta.annotation.Nonnull;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderRequest {

    @NonNull
    private Long amount;

    @NonNull
    private String currency;

    private String description;

}
