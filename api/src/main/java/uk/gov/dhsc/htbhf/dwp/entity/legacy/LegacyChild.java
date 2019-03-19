package uk.gov.dhsc.htbhf.dwp.entity.legacy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@Table(name = "dwp_legacy_child")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class LegacyChild {

    @Id
    @Getter(AccessLevel.NONE)
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dwp_legacy_household_id", nullable = false)
    private LegacyHousehold household;

    @Size(max = 500)
    @Column(name = "forename")
    private String forename;

    @Size(max = 500)
    @Column(name = "surname")
    private String surname;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    /**
     * Adding a custom getter for the id so that we can compare an entity before and after its initial
     * persistence and they will be the same.
     *
     * @return The id for the entity.
     */
    public UUID getId() {
        if (id == null) {
            this.id = UUID.randomUUID();
        }
        return this.id;
    }

}
