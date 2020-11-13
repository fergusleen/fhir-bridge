package org.ehrbase.fhirbridge.camel;

import ca.uhn.fhir.jpa.api.dao.IFhirResourceDao;
import org.apache.camel.builder.RouteBuilder;
import org.ehrbase.client.openehrclient.OpenEhrClient;
import org.ehrbase.fhirbridge.camel.processor.DefaultCreateResourceRequestValidator;
import org.ehrbase.fhirbridge.camel.processor.PatientIdProcessor;
import org.ehrbase.fhirbridge.ehr.mapper.CompositionConverter;
import org.ehrbase.fhirbridge.ehr.mapper.IntensivmedizinischesMonitoringKorpertemperaturCompositionConverter;
import org.ehrbase.fhirbridge.fhir.Profile;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ObservationRoutes extends RouteBuilder {

    private final IFhirResourceDao<Observation> observationDao;

    private final DefaultCreateResourceRequestValidator requestValidator;

    private final PatientIdProcessor patientIdProcessor;

    private final OpenEhrClient openEhrClient;

    public ObservationRoutes(
            IFhirResourceDao<Observation> observationDao,
            DefaultCreateResourceRequestValidator requestValidator,
            PatientIdProcessor patientIdProcessor, OpenEhrClient openEhrClient) {
        this.observationDao = observationDao;
        this.requestValidator = requestValidator;
        this.patientIdProcessor = patientIdProcessor;
        this.openEhrClient = openEhrClient;
    }

    @Override
    public void configure() {
        // @formatter:off
        from("obs-create:/service?audit=false&fhirContext=#fhirContext")
            .routeId("create-observation")
            .process(requestValidator)
            .bean(observationDao, "create(${body})")
            .setBody(simple("${body.resource}"))
            .process(patientIdProcessor)
            .process(exchange -> {
                UUID ehrId = exchange.getIn().getHeader(FhirBridgeHeaders.EHR_ID, UUID.class);
                Profile profile  = exchange.getIn().getHeader(FhirBridgeHeaders.PROFILE, Profile.class);
                Observation observation = exchange.getIn().getBody(Observation.class);

                CompositionConverter<?, Observation> converter = getCompositionConverter(profile);
                openEhrClient.compositionEndpoint(ehrId).mergeCompositionEntity(converter.toComposition(observation));
            });

        from("obs-read:/service?audit=false&fhirContext=#fhirContext")
            .routeId("read-observation")
            .to("log:read-observation?showAll=true");
        // @formatter:on
    }

    private CompositionConverter<?, Observation> getCompositionConverter(Profile profile) {
        switch (profile) {
            case BODY_TEMP:
                return new IntensivmedizinischesMonitoringKorpertemperaturCompositionConverter();
            default:
                throw new IllegalArgumentException();
        }
    }
}