package uk.gov.dhsc.htbhf.dwp.entity.uc;

import lombok.*;
import uk.gov.dhsc.htbhf.dwp.entity.BaseEntity;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Data
@Builder
@Table(name = "dwp_uc_adult")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class UCAdult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dwp_uc_household_id", nullable = false)
    private UCHousehold household;

    @Size(max = 9)
    @Column(name = "nino")
    private String nino;

    @Size(max = 500)
    @Column(name = "surname")
    private String surname;

    @Size(max = 500)
    @Column(name = "address_line_1")
    private String addressLine1;

    @Size(max = 10)
    @Column(name = "address_postcode")
    private String postcode;

}
