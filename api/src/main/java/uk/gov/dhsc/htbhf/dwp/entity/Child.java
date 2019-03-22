package uk.gov.dhsc.htbhf.dwp.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@SuperBuilder
@Entity
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public abstract class Child extends BaseEntity {

    @Size(max = 500)
    @Column(name = "forename")
    private String forename;

    @Size(max = 500)
    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

}
