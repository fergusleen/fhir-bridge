package org.ehrbase.fhirbridge.ehr.converter;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import com.nedap.archie.rm.generic.PartySelf;
import org.ehrbase.client.classgenerator.shareddefinition.Category;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.client.classgenerator.shareddefinition.Setting;
import org.ehrbase.client.classgenerator.shareddefinition.Territory;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConversionException;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.GECCOProzedurComposition;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.KategorieDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.NameDerProzedurDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.GeraetenameDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.KoerperstelleDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.GeccoProzedurKategorieElement;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.ProzedurAction;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.UnbekannteProzedurEvaluation;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.MedizingeraetCluster;
import org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition.NichtDurchgefuehrteProzedurEvaluation;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.DateTimeType;
import org.hl7.fhir.r4.model.Coding;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class TherapyCompositionConverter implements CompositionConverter<GECCOProzedurComposition, Procedure> {

    private static final Map<String, KategorieDefiningCode> kategorieMap = new HashMap<>();
    private static final Map<String, NameDerProzedurDefiningCode> nameDerProzedurMap = new HashMap<>();
    private static final Map<String, KoerperstelleDefiningCode> koerperstelleMap = new HashMap<>();
    private static final Map<String, GeraetenameDefiningCode> geraetenameMap = new HashMap<>();

    private static String SNOMED_SYSTEM = "http://snomed.info/sct";


    static {
        for (KategorieDefiningCode kategorie : KategorieDefiningCode.values()) {
            kategorieMap.put(kategorie.getCode(), kategorie);
        }

        for(NameDerProzedurDefiningCode nameDerProzedurDefiningCode: NameDerProzedurDefiningCode.values()) {
            nameDerProzedurMap.put(nameDerProzedurDefiningCode.getCode(), nameDerProzedurDefiningCode);
        }

        for(KoerperstelleDefiningCode koerperstelleDefiningCode: KoerperstelleDefiningCode.values()) {
            koerperstelleMap.put(koerperstelleDefiningCode.getCode(), koerperstelleDefiningCode);
        }

        for(GeraetenameDefiningCode geraetenameDefiningCode: GeraetenameDefiningCode.values()) {
            geraetenameMap.put(geraetenameDefiningCode.getCode(), geraetenameDefiningCode);
        }
    }


    @Override
    public Procedure fromComposition(GECCOProzedurComposition composition) throws CompositionConversionException {
        // TODO: Implement
        return null;
    }

    @Override
    public GECCOProzedurComposition toComposition(Procedure procedure) throws CompositionConversionException {
        if (procedure == null) {
            return null;
        }

        GECCOProzedurComposition result = new GECCOProzedurComposition();


        // Map Kategorie

        result.setKategorie(new ArrayList<>());

        for (Coding coding : procedure.getCategory().getCoding()) {
            if (coding.getSystem().equals(SNOMED_SYSTEM) && kategorieMap.containsKey(coding.getCode())) {
                GeccoProzedurKategorieElement element =  new GeccoProzedurKategorieElement();
                element.setValue(kategorieMap.get(coding.getCode()));

                result.getKategorie().add(element);
            }
        }


        switch (procedure.getStatus()) {
            case UNKNOWN:
                mapUnknown(procedure, result);
                break;
            case NOTDONE:
                mapNotDone(procedure, result);
                break;
            case ENTEREDINERROR:
                throw new CompositionConversionException("Invalid status");
            default:
                mapDone(procedure, result);
        }


        // Map Start Time
        if (procedure.getExtension().get(0).getValue() instanceof DateTimeType) {
            result.setStartTimeValue(((DateTimeType) procedure.getExtension().get(0).getValue()).getValueAsCalendar().toZonedDateTime());
        } else {
            result.setStartTimeValue(procedure.getPerformedDateTimeType().getValueAsCalendar().toZonedDateTime());
        }


        // ======================================================================================
        // Required fields by API

        result.setLanguage(Language.DE);
        result.setLocation("test");
        result.setSettingDefiningCode(Setting.SECONDARY_MEDICAL_CARE);
        result.setTerritory(Territory.DE);
        result.setCategoryDefiningCode(Category.EVENT);
        result.setComposer(new PartySelf());


        return result;
    }

    private void mapDone(Procedure procedure, GECCOProzedurComposition composition) {

        ProzedurAction durchgefuehrteProzedur = new ProzedurAction();

        try {

            Coding coding = procedure.getCode().getCoding().get(0);

            if(coding.getSystem().equals(SNOMED_SYSTEM) && nameDerProzedurMap.containsKey(coding.getCode())) {
                durchgefuehrteProzedur.setNameDerProzedurDefiningCode(nameDerProzedurMap.get(coding.getCode()));
            } else {
                throw new UnprocessableEntityException("Invalid name of procedure");
            }

            if(durchgefuehrteProzedur.getNameDerProzedurDefiningCode().equals(NameDerProzedurDefiningCode.PLAIN_RADIOGRAPHY)) {
                // Map body site for PLAIN_RADIOGRAPHY

                Coding bodySiteCoding = procedure.getBodySite().get(0).getCoding().get(0);

                if(bodySiteCoding.getSystem().equals(SNOMED_SYSTEM) &&
                        koerperstelleMap.containsKey(bodySiteCoding.getCode())) {
                    durchgefuehrteProzedur.setKoerperstelleDefiningCode(koerperstelleMap.get(bodySiteCoding.getCode()));
                } else {
                    throw new UnprocessableEntityException("Invalid body site for PLAIN_RADIOGRAPHY");
                }
            } else if(durchgefuehrteProzedur.getNameDerProzedurDefiningCode().equals(NameDerProzedurDefiningCode.ARTIFICIAL_RESPIRATION_PROCEDURE)) {
                // Map Medizingeraet for RESP
                Coding usedCodeCoding = procedure.getUsedCode().get(0).getCoding().get(0);

                if(usedCodeCoding.getSystem().equals(SNOMED_SYSTEM) && geraetenameMap.containsKey(usedCodeCoding.getCode())) {

                    MedizingeraetCluster medizingeraetCluster = new MedizingeraetCluster();

                    medizingeraetCluster.setGeraetenameDefiningCode(geraetenameMap.get(usedCodeCoding.getCode()));

                    durchgefuehrteProzedur.setMedizingeraet(new ArrayList<>());
                    durchgefuehrteProzedur.getMedizingeraet().add(medizingeraetCluster);
                } else {
                    throw new UnprocessableEntityException("Invalid used code");
                }
            }

            durchgefuehrteProzedur.setArtDerProzedurDefiningCode(composition.getKategorie().get(0).getValue());


            if(procedure.getExtension().get(1).getValue() instanceof Coding)
            {
                durchgefuehrteProzedur.setDurchfuehrungsabsichtValue(((Coding) procedure.getExtension().get(1).getValue()).getDisplay());
            } else {
                throw new UnprocessableEntityException("Could not find extension durchfuehrungsabsicht.");
            }

            durchgefuehrteProzedur.setKommentarValue(procedure.getNote().toString());

            durchgefuehrteProzedur.setTimeValue(procedure.getPerformedDateTimeType().getValueAsCalendar().toZonedDateTime());

        } catch (Exception e) {
            throw new CompositionConversionException("Some parts of the present procedure did not contain the required elements. "
                    + e.getMessage(), e);
        }

        durchgefuehrteProzedur.setLanguage(Language.DE);
        durchgefuehrteProzedur.setSubject(new PartySelf());

        composition.setProzedur(durchgefuehrteProzedur);

    }

    private void mapNotDone(Procedure procedure, GECCOProzedurComposition composition) {

        NichtDurchgefuehrteProzedurEvaluation nichtDurchgefuehrteProzedur = new NichtDurchgefuehrteProzedurEvaluation();

        // TODO: Check whether this has to be an enum type
        nichtDurchgefuehrteProzedur.setAussageUeberDenAusschlussValue(procedure.getStatus().getDisplay());

        try {
            Coding coding = procedure.getCode().getCoding().get(0);

            if(coding.getSystem().equals(SNOMED_SYSTEM) && nameDerProzedurMap.containsKey(coding.getCode())) {
                nichtDurchgefuehrteProzedur.setEingriffDefiningCode(nameDerProzedurMap.get(coding.getCode()));
            } else {
                throw new UnprocessableEntityException("Invalid name of procedure");
            }

        } catch (Exception e) {
            throw new CompositionConversionException("Some parts of the not present procedure did not contain the required elements. "
                    + e.getMessage(), e);
        }

        nichtDurchgefuehrteProzedur.setSubject(new PartySelf());
        nichtDurchgefuehrteProzedur.setLanguage(Language.DE);

        composition.setNichtDurchgefuehrteProzedur(nichtDurchgefuehrteProzedur);

    }

    private void mapUnknown(Procedure procedure, GECCOProzedurComposition composition) {

        UnbekannteProzedurEvaluation unbekannteProzedur = new UnbekannteProzedurEvaluation();

        // TODO: Check whether this has to be an enum type
        unbekannteProzedur.setAussageUeberDieFehlendeInformationValue(procedure.getStatus().getDisplay());

        try {
            Coding coding = procedure.getCode().getCoding().get(0);

            if(coding.getSystem().equals(SNOMED_SYSTEM) && nameDerProzedurMap.containsKey(coding.getCode())) {
                unbekannteProzedur.setUnbekannteProzedurDefiningCode(nameDerProzedurMap.get(coding.getCode()));
            } else {
                throw new UnprocessableEntityException("Invalid name of procedure");
            }

        } catch (Exception e) {
            throw new CompositionConversionException("Some parts of the unknown procedure did not contain the required elements. "
                    + e.getMessage(), e);
        }

        unbekannteProzedur.setSubject(new PartySelf());
        unbekannteProzedur.setLanguage(Language.DE);


        composition.setUnbekannteProzedur(unbekannteProzedur);
    }
}
