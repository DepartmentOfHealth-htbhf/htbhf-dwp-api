package uk.gov.dhsc.htbhf.dwp.entity.legacy;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
@Table(name = "dwp_legacy_household")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@SuppressWarnings("PMD.DataClass")
public class LegacyHousehold {

    @Id
    @Getter(AccessLevel.NONE)
    @Access(AccessType.PROPERTY)
    @EqualsAndHashCode.Include
    private UUID id;

    @Size(min = 1, max = 50)
    @Column(name = "household_identifier")
    private String householdIdentifier;

    @Column(name = "file_import_number")
    private Integer fileImportNumber;

    @Column(name = "award")
    @Size(min = 1, max = 500)
    private String award;

    @Size(min = 1, max = 500)
    @Column(name = "address_line_1")
    private String addressLine1;

    @Size(min = 1, max = 500)
    @Column(name = "address_line_2")
    private String addressLine2;

    @Size(min = 1, max = 500)
    @Column(name = "address_town_or_city")
    private String townOrCity;

    @Size(min = 1, max = 10)
    @Column(name = "address_postcode")
    private String postcode;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "household", orphanRemoval = true)
    @ToString.Exclude
    private final Set<LegacyAdult> adults = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "household", orphanRemoval = true)
    @ToString.Exclude
    private final Set<LegacyChild> children = new HashSet<>();


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

    public LegacyHousehold addAdult(LegacyAdult adult) {
        adult.setHousehold(this);
        this.adults.add(adult);
        return this;
    }

    public Set<LegacyAdult> getAdults() {
        return unmodifiableSet(adults);
    }

    public void setAdults(Set<LegacyAdult> adults) {
        this.adults.clear();
        adults.forEach(adult -> this.addAdult(adult));
    }

    public LegacyHousehold addChild(LegacyChild child) {
        child.setHousehold(this);
        this.children.add(child);
        return this;
    }

    public Set<LegacyChild> getChildren() {
        return unmodifiableSet(children);
    }

    public void setChildren(Set<LegacyChild> children) {
        this.children.clear();
        children.forEach(child -> this.addChild(child));
    }

}
