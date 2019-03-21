package uk.gov.dhsc.htbhf.dwp.entity.uc;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import static java.util.Collections.unmodifiableSet;

@Entity
@Data
@Builder(toBuilder = true)
@Table(name = "dwp_uc_household")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("PMD.DataClass")
public class UCHousehold {

    @Id
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Include
    private UUID id;

    @Size(min = 1, max = 50)
    @Column(name = "household_identifier")
    private String householdIdentifier;

    @Column(name = "file_import_number")
    private Integer fileImportNumber;

    @Column(name = "award_date")
    private LocalDate awardDate;

    @Column(name = "last_assessment_period_start")
    private LocalDate lastAssessmentPeriodStart;

    @Column(name = "last_assessment_period_end")
    private LocalDate lastAssessmentPeriodEnd;

    @Column(name = "household_member_pregnant")
    private Boolean householdMemberPregnant;

    @Column(name = "earnings_threshold_exceeded")
    private Boolean earningsThresholdExceeded;

    @Column(name = "no_of_children_under_four")
    private Integer childrenUnderFour;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "household", orphanRemoval = true)
    @ToString.Exclude
    private final Set<UCAdult> adults = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "household", orphanRemoval = true)
    @ToString.Exclude
    private final Set<UCChild> children = new HashSet<>();


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

    public UCHousehold addAdult(UCAdult adult) {
        adult.setHousehold(this);
        this.adults.add(adult);
        return this;
    }

    public Set<UCAdult> getAdults() {
        return unmodifiableSet(adults);
    }

    public void setAdults(Set<UCAdult> adults) {
        this.adults.clear();
        adults.forEach(adult -> this.addAdult(adult));
    }

    public UCHousehold addChild(UCChild child) {
        child.setHousehold(this);
        this.children.add(child);
        return this;
    }

    public Set<UCChild> getChildren() {
        return unmodifiableSet(children);
    }

    public void setChildren(Set<UCChild> children) {
        this.children.clear();
        children.forEach(child -> this.addChild(child));
    }
}
