package library.management.Dto.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record LivroRequest
        (String titulo,

         String autor,

         String editora,

         String descricao,
         @NotNull
         @Min(0)
         Integer quantidade) {
}
