package org.ehrbase.fhirbridge.ehr.opt.geccolaborbefundcomposition;

import com.nedap.archie.rm.archetyped.FeederAudit;
import com.nedap.archie.rm.datastructures.Cluster;
import com.nedap.archie.rm.generic.Participation;
import com.nedap.archie.rm.generic.PartyIdentified;
import com.nedap.archie.rm.generic.PartyProxy;
import org.ehrbase.client.annotations.Archetype;
import org.ehrbase.client.annotations.Entity;
import org.ehrbase.client.annotations.Id;
import org.ehrbase.client.annotations.Path;
import org.ehrbase.client.annotations.Template;
import org.ehrbase.client.classgenerator.interfaces.CompositionEntity;
import org.ehrbase.client.classgenerator.shareddefinition.Category;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.NullFlavour;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import org.ehrbase.client.openehrclient.VersionUid;
import org.ehrbase.fhirbridge.ehr.opt.geccolaborbefundcomposition.definition.LaborergebnisObservation;
import org.ehrbase.fhirbridge.ehr.opt.geccolaborbefundcomposition.definition.StatusDefiningCode;

import javax.annotation.processing.Generated;
import java.time.temporal.TemporalAccessor;
import java.util.List;

@Entity
@Archetype("openEHR-EHR-COMPOSITION.registereintrag.v1")
@Generated(
        value = "org.ehrbase.client.classgenerator.ClassGenerator",
        date = "2021-03-09T11:53:24.000636+01:00",
        comments = "https://github.com/ehrbase/openEHR_SDK Version: 1.3.0"
)
@Template("GECCO_Laborbefund")
public class GECCOLaborbefundComposition implements CompositionEntity {
    /**
     * Path: Laborbefund/category
     */
    @Path("/category|defining_code")
    private Category categoryDefiningCode;

    /**
     * Path: Laborbefund/context/Erweiterung
     * Description: Ergänzende Angaben zum Registereintrag.
     */
    @Path("/context/other_context[at0001]/items[at0002]")
    private List<Cluster> erweiterung;

    /**
     * Path: Laborbefund/context/Status
     * Description: Status der gelieferten Daten für den Registereintrag. Hinweis: Dies ist nicht der Status einzelner Komponenten.
     */
    @Path("/context/other_context[at0001]/items[at0004]/value|defining_code")
    private StatusDefiningCode statusDefiningCode;

    /**
     * Path: Laborbefund/context/Baum/Status/null_flavour
     */
    @Path("/context/other_context[at0001]/items[at0004]/null_flavour|defining_code")
    private NullFlavour statusNullFlavourDefiningCode;

    /**
     * Path: Laborbefund/context/Kategorie
     * Description: Die Klassifikation des Registereintrags (z.B. Typ der Observation des FHIR-Profils).
     */
    @Path("/context/other_context[at0001]/items[at0005]/value|value")
    private String kategorieValue;

    /**
     * Path: Laborbefund/context/Baum/Kategorie/null_flavour
     */
    @Path("/context/other_context[at0001]/items[at0005]/null_flavour|defining_code")
    private NullFlavour kategorieNullFlavourDefiningCode;

    /**
     * Path: Laborbefund/context/start_time
     */
    @Path("/context/start_time|value")
    private TemporalAccessor startTimeValue;

    /**
     * Path: Laborbefund/context/participations
     */
    @Path("/context/participations")
    private List<Participation> participations;

    /**
     * Path: Laborbefund/context/end_time
     */
    @Path("/context/end_time|value")
    private TemporalAccessor endTimeValue;

    /**
     * Path: Laborbefund/context/location
     */
    @Path("/context/location")
    private String location;

    /**
     * Path: Laborbefund/context/health_care_facility
     */
    @Path("/context/health_care_facility")
    private PartyIdentified healthCareFacility;

    /**
     * Path: Laborbefund/context/setting
     */
    @Path("/context/setting|defining_code")
    private Setting settingDefiningCode;

    /**
     * Path: Laborbefund/Laborergebnis
     * Description: Das Ergebnis - einschließlich der Befunde und der Interpretation des Labors - einer Untersuchung, die an Proben durchgeführt wurde, die von einer Einzelperson stammen oder mit dieser Person zusammenhängen.
     */
    @Path("/content[openEHR-EHR-OBSERVATION.laboratory_test_result.v1]")
    private LaborergebnisObservation laborergebnis;

    /**
     * Path: Laborbefund/composer
     */
    @Path("/composer")
    private PartyProxy composer;

    /**
     * Path: Laborbefund/language
     */
    @Path("/language")
    private Language language;

    /**
     * Path: Laborbefund/feeder_audit
     */
    @Path("/feeder_audit")
    private FeederAudit feederAudit;

    /**
     * Path: Laborbefund/territory
     */
    @Path("/territory")
    private Territory territory;

    @Id
    private VersionUid versionUid;

    public Category getCategoryDefiningCode() {
        return this.categoryDefiningCode;
    }

    public void setCategoryDefiningCode(Category categoryDefiningCode) {
        this.categoryDefiningCode = categoryDefiningCode;
    }

    public List<Cluster> getErweiterung() {
        return this.erweiterung;
    }

    public void setErweiterung(List<Cluster> erweiterung) {
        this.erweiterung = erweiterung;
    }

    public StatusDefiningCode getStatusDefiningCode() {
        return this.statusDefiningCode;
    }

    public void setStatusDefiningCode(StatusDefiningCode statusDefiningCode) {
        this.statusDefiningCode = statusDefiningCode;
    }

    public NullFlavour getStatusNullFlavourDefiningCode() {
        return this.statusNullFlavourDefiningCode;
    }

    public void setStatusNullFlavourDefiningCode(NullFlavour statusNullFlavourDefiningCode) {
        this.statusNullFlavourDefiningCode = statusNullFlavourDefiningCode;
    }

    public String getKategorieValue() {
        return this.kategorieValue;
    }

    public void setKategorieValue(String kategorieValue) {
        this.kategorieValue = kategorieValue;
    }

    public NullFlavour getKategorieNullFlavourDefiningCode() {
        return this.kategorieNullFlavourDefiningCode;
    }

    public void setKategorieNullFlavourDefiningCode(NullFlavour kategorieNullFlavourDefiningCode) {
        this.kategorieNullFlavourDefiningCode = kategorieNullFlavourDefiningCode;
    }

    public TemporalAccessor getStartTimeValue() {
        return this.startTimeValue;
    }

    public void setStartTimeValue(TemporalAccessor startTimeValue) {
        this.startTimeValue = startTimeValue;
    }

    public List<Participation> getParticipations() {
        return this.participations;
    }

    public void setParticipations(List<Participation> participations) {
        this.participations = participations;
    }

    public TemporalAccessor getEndTimeValue() {
        return this.endTimeValue;
    }

    public void setEndTimeValue(TemporalAccessor endTimeValue) {
        this.endTimeValue = endTimeValue;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public PartyIdentified getHealthCareFacility() {
        return this.healthCareFacility;
    }

    public void setHealthCareFacility(PartyIdentified healthCareFacility) {
        this.healthCareFacility = healthCareFacility;
    }

    public Setting getSettingDefiningCode() {
        return this.settingDefiningCode;
    }

    public void setSettingDefiningCode(Setting settingDefiningCode) {
        this.settingDefiningCode = settingDefiningCode;
    }

    public LaborergebnisObservation getLaborergebnis() {
        return this.laborergebnis;
    }

    public void setLaborergebnis(LaborergebnisObservation laborergebnis) {
        this.laborergebnis = laborergebnis;
    }

    public PartyProxy getComposer() {
        return this.composer;
    }

    public void setComposer(PartyProxy composer) {
        this.composer = composer;
    }

    public Language getLanguage() {
        return this.language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    public FeederAudit getFeederAudit() {
        return this.feederAudit;
    }

    public void setFeederAudit(FeederAudit feederAudit) {
        this.feederAudit = feederAudit;
    }

    public Territory getTerritory() {
        return this.territory;
    }

    public void setTerritory(Territory territory) {
        this.territory = territory;
    }

    public VersionUid getVersionUid() {
        return this.versionUid;
    }

    public void setVersionUid(VersionUid versionUid) {
        this.versionUid = versionUid;
    }
}
