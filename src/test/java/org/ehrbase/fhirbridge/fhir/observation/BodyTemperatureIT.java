package org.ehrbase.fhirbridge.fhir.observation;

import org.ehrbase.fhirbridge.comparators.CustomTemporalAcessorComparator;
import org.ehrbase.fhirbridge.ehr.converter.ConversionException;
import org.ehrbase.fhirbridge.ehr.converter.specific.bodytemperature.KoerpertemperaturCompositionConverter;
import org.ehrbase.fhirbridge.ehr.opt.koerpertemperaturcomposition.KoerpertemperaturComposition;
import org.ehrbase.fhirbridge.ehr.opt.koerpertemperaturcomposition.definition.KoerpertemperaturObservation;
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

class BodyTemperatureIT extends AbstractMappingTestSetupIT {

    public BodyTemperatureIT() {
        super("Observation/BodyTemperature/", Observation.class); //fhir-Resource
    }

    @Test
    void createBodyTemperature() throws IOException {
        create("create-body-temp-1.json");
    }

    @Test
    void mappingNormal() throws IOException {
        testMapping("create-body-temp-1.json",
                "paragon-create-body-temp-1.json");
    }

    @Test
    void mappingNormal2() throws IOException {
        testMapping("create-body-temp-2.json",
                "paragon-create-body-temp-2.json");
    }

    @Test
    void mappingNormal3() throws IOException {
        testMapping("create-body-temp-3.json",
                "paragon-create-body-temp-3.json");
    }

    // #####################################################################################
    // default

    @Override
    public Javers getJavers() {
        return JaversBuilder.javers()
                .registerValue(TemporalAccessor.class, new CustomTemporalAcessorComparator())
                .registerValueObject(new ValueObjectDefinition(KoerpertemperaturComposition.class, List.of("location", "feederAudit")))
                .registerValueObject(KoerpertemperaturObservation.class)
                .build();
    }

    @Override
    public Exception executeMappingException(String path) throws IOException {
        Observation obs = (Observation) testFileLoader.loadResource(path);
        return assertThrows(ConversionException.class, () ->
                new KoerpertemperaturCompositionConverter().convert(obs)
        );
    }

    @Override
    public void testMapping(String resourcePath, String paragonPath) throws IOException {
        Observation observation = (Observation) super.testFileLoader.loadResource(resourcePath);
        KoerpertemperaturCompositionConverter bodyTemperatureCompositionConverter = new KoerpertemperaturCompositionConverter();
        KoerpertemperaturComposition mapped = bodyTemperatureCompositionConverter.convert(observation);
        Diff diff = compareCompositions(getJavers(), paragonPath, mapped);
        assertEquals(0, diff.getChanges().size());
    }
}
