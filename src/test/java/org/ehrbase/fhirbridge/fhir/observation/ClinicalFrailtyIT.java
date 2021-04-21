package org.ehrbase.fhirbridge.fhir.observation;

import ca.uhn.fhir.rest.server.exceptions.UnprocessableEntityException;
import org.ehrbase.fhirbridge.comparators.CustomTemporalAcessorComparator;
import org.ehrbase.fhirbridge.ehr.converter.specific.clinicalFrailty.ClinicalFrailtyBeurteilung;
import org.ehrbase.fhirbridge.ehr.converter.specific.clinicalFrailty.ClinicalFrailtyScaleScoreCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.knownexposure.SarsCov2KnownExposureCompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.klinischefrailtyskalacomposition.KlinischeFrailtySkalaComposition;
import org.ehrbase.fhirbridge.ehr.opt.klinischefrailtyskalacomposition.definition.KlinischeFrailtySkalaCfsObservation;
import org.ehrbase.fhirbridge.ehr.opt.sarscov2expositioncomposition.SARSCoV2ExpositionComposition;
import org.ehrbase.fhirbridge.fhir.AbstractMappingTestSetupIT;
import org.hl7.fhir.r4.model.Observation;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.diff.Diff;
import org.javers.core.metamodel.clazz.ValueObjectDefinition;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ClinicalFrailtyIT extends AbstractMappingTestSetupIT {

    public ClinicalFrailtyIT() {
        super("Observation/ClinicalFrailty/", Observation.class); //fhir-Resource
    }

    @Test
    void createExposureOutput() throws IOException {
        create("create-clinical-frailty-scale-score.json");
    }

    // #####################################################################################
    // check payload
    //@Test
    //void mappingAbsent() throws IOException {
    //    testMapping("create-known-exposure-absent.json",
    //           "paragon-known-exposure-absent.json");
    //}

    // #####################################################################################
    // check exception
//    @Test
//    void createInvalidStatus() throws IOException {
//        try{
//            super.testFileLoader.loadResource("create-known-exposure-invalid-status.json");
//        }catch (DataFormatException dataFormatException){
//            assertEquals("[element=\"status\"] Invalid attribute value \"invalidTestCode\": Unknown ObservationStatus code 'invalidTestCode'", dataFormatException.getMessage());
//        }
//    }

    // #####################################################################################
    // default

    @Override
    public Javers getJavers() {

        return JaversBuilder.javers()
                .registerValue(TemporalAccessor.class, new CustomTemporalAcessorComparator())
                .registerValueObject(new ValueObjectDefinition(KlinischeFrailtySkalaComposition.class, List.of("location", "feederAudit")))
                .build();
    }

    @Override
    public Exception executeMappingException(String path) throws IOException {
        Observation obs = (Observation) testFileLoader.loadResource(path);
        return assertThrows(UnprocessableEntityException.class, () ->
                new ClinicalFrailtyScaleScoreCompositionConverter().convert(obs)
        );
    }

    @Override
    public void testMapping(String resourcePath, String paragonPath) throws IOException {
        Observation observation = (Observation)  super.testFileLoader.loadResource(resourcePath);
        ClinicalFrailtyScaleScoreCompositionConverter compositionConverter = new ClinicalFrailtyScaleScoreCompositionConverter();
        KlinischeFrailtySkalaComposition mapped = compositionConverter.convert(observation);
        Diff diff = compareCompositions(getJavers(), paragonPath, mapped);
        assertEquals(0, diff.getChanges().size());
    }


}

