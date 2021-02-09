package org.ehrbase.fhirbridge.ehr.converter.RadiologischerBefund;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.ehrbase.client.classgenerator.shareddefinition.Language;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConversionException;
import org.ehrbase.fhirbridge.camel.component.ehr.composition.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.geccoradiologischerbefundcomposition.GECCORadiologischerBefundComposition;
import org.ehrbase.fhirbridge.ehr.opt.geccoradiologischerbefundcomposition.definition.KategorieDefiningCode;
import org.ehrbase.fhirbridge.ehr.opt.geccoradiologischerbefundcomposition.definition.RadiologischerBefundKategorieElement;
import org.ehrbase.fhirbridge.ehr.opt.geccoradiologischerbefundcomposition.definition.StatusDefiningCode;
import org.hl7.fhir.r4.model.CodeableConcept;
import org.hl7.fhir.r4.model.Coding;
import org.hl7.fhir.r4.model.DiagnosticReport;

import java.util.LinkedList;
import java.util.List;

public class RadiologischerBefundConverter implements CompositionConverter<GECCORadiologischerBefundComposition, DiagnosticReport> {

    @Override
    public DiagnosticReport fromComposition(GECCORadiologischerBefundComposition composition) throws CompositionConversionException {
        return new DiagnosticReport();
    }

    @Override
    public GECCORadiologischerBefundComposition toComposition(DiagnosticReport diagnosticReport) throws CompositionConversionException {
        GECCORadiologischerBefundComposition geccoRadiologischerBefundComposition = new GECCORadiologischerBefundComposition();
        geccoRadiologischerBefundComposition.setLanguage(Language.DE);
        mapStatus(geccoRadiologischerBefundComposition, diagnosticReport);
        mapKategorie(geccoRadiologischerBefundComposition, diagnosticReport);
        System.out.println("Kategory ------" + geccoRadiologischerBefundComposition.getKategorie().get(0).getValue());
        System.out.println("Status ------" + geccoRadiologischerBefundComposition.getStatusDefiningCode());
        geccoRadiologischerBefundComposition.setBildgebendesUntersuchungsergebnis(new BildgebendesUntersuchungsergebnisConverter().map(diagnosticReport));
        return geccoRadiologischerBefundComposition;
    }

    private void mapStatus(GECCORadiologischerBefundComposition geccoRadiologischerBefundComposition, DiagnosticReport diagnosticReport) {
        String status = diagnosticReport.getStatusElement().getCode();
        if (status.equals(StatusDefiningCode.FINAL.getValue())) {
            geccoRadiologischerBefundComposition.setStatusDefiningCode(StatusDefiningCode.FINAL);
        } else if (status.equals(StatusDefiningCode.GEAENDERT.getValue())) {
            geccoRadiologischerBefundComposition.setStatusDefiningCode(StatusDefiningCode.GEAENDERT);
        } else if (status.equals(StatusDefiningCode.REGISTRIERT.getValue())) {
            geccoRadiologischerBefundComposition.setStatusDefiningCode(StatusDefiningCode.REGISTRIERT);
        } else if (status.equals(StatusDefiningCode.VORLAEUFIG.getValue())) {
            geccoRadiologischerBefundComposition.setStatusDefiningCode(StatusDefiningCode.VORLAEUFIG);
        } else {
            throw new UnprocessableEntityException("The Status " + diagnosticReport.getStatus().toString() + " is not valid for radiology report.");
        }
    }

    private void mapKategorie(GECCORadiologischerBefundComposition geccoRadiologischerBefundComposition, DiagnosticReport diagnosticReport) {
        for (CodeableConcept codeableConcept : diagnosticReport.getCategory()
        ) {
            for (Coding coding : codeableConcept.getCoding()
            ) {
                mapCode(geccoRadiologischerBefundComposition, coding);
            }
        }
    }

    private void mapCode(GECCORadiologischerBefundComposition geccoRadiologischerBefundComposition, Coding coding) {
        List<RadiologischerBefundKategorieElement> radiologischerBefundKategorieElementList = getRadiologischerBefundKategorieElementsList(geccoRadiologischerBefundComposition);
        RadiologischerBefundKategorieElement radiologischerBefundKategorieElement = new RadiologischerBefundKategorieElement();
        radiologischerBefundKategorieElement.setValue(KategorieDefiningCode.RADIOLOGY);
        geccoRadiologischerBefundComposition.setKategorie(List.of(radiologischerBefundKategorieElement));

/*
        if(coding.getCode().equals(KategorieDefiningCode.RADIOLOGY.getCode())){
                radiologischerBefundKategorieElement.setValue(KategorieDefiningCode.RADIOLOGY);
            }else if(coding.getCode().equals(KategorieDefiningCode.RADIOLOGY_STUDIES_SET.getCode())){
                radiologischerBefundKategorieElement.setValue(KategorieDefiningCode.RADIOLOGY_STUDIES_SET);
            }else{
                throw new UnprocessableEntityException("The LOINC code: " +coding.getCode()+ " is not valid for radiology report!");
            }
            radiologischerBefundKategorieElementList.add(radiologischerBefundKategorieElement);
            geccoRadiologischerBefundComposition.setKategorie(radiologischerBefundKategorieElementList);*/
    }

    private List getRadiologischerBefundKategorieElementsList(GECCORadiologischerBefundComposition geccoRadiologischerBefundComposition) {
        if (geccoRadiologischerBefundComposition.getKategorie() == null) {
            return new LinkedList();
        }
        return geccoRadiologischerBefundComposition.getKategorie();
    }

}
