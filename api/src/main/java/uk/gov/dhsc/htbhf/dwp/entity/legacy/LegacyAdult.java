package uk.gov.dhsc.htbhf.dwp.entity.legacy;

import lombok.*;
import uk.gov.dhsc.htbhf.dwp.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@Table(name = "dwp_legacy_adult")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class LegacyAdult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dwp_legacy_household_id", nullable = false)
    private LegacyHousehold household;

    @Size(max = 9)
    @Column(name = "nino")
    private String nino;

    @Size(max = 500)
    @Column(name = "forename")
    private String forename;

    @Size(max = 500)
    @Column(name = "surname")
    private String surname;

}
