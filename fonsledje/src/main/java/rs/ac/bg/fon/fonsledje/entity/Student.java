package rs.ac.bg.fon.fonsledje.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "student")
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
public class Student extends Osoba {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "Ово поље је обавезно")
    @Column(name = "brojIndeksa", nullable = false)
    private String brojIndeksa;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;
}
