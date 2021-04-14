package org.ehrbase.fhirbridge.config;

import org.ehrbase.fhirbridge.ehr.converter.ConversionService;
import org.ehrbase.fhirbridge.ehr.converter.specific.bloodgas.BloodGasPanelCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.bloodpressure.BloodPressureCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.bodyheight.BodyHeightCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.bodytemperature.BodyTemperatureCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.bodyweight.BodyWeightCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.clinicalFrailty.ClinicalFrailtyScaleScoreCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.coronavirusnachweistest.CoronavirusNachweisTestCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.d4lquestionnaire.D4lQuestionnaireCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.diagnose.DiagnoseCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.diagnosticreportlab.DiagnosticReportLabCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.fio2.FiO2CompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.geccoDiagnose.GECCODiagnoseCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.heartrate.HeartRateCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.historyoftravel.HistoryOfTravelConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.observationlab.ObservationLabCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.patient.PatientCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.patientinicu.PatientInIcuCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.pregnancystatus.PregnancyStatusCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.procedure.ProcedureCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.pulseoximetry.PulseOximetryCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.radiologischerBefund.RadiologischerBefundCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.respirationrate.RespiratoryRateCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.smokingstatus.SmokingStatusCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.sofascore.SofaScoreCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.symptom.SymptomCompositionConverter;
import org.ehrbase.fhirbridge.ehr.converter.specific.therapy.TherapyCompositionConverter;
import org.ehrbase.fhirbridge.fhir.common.Profile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConversionConfiguration {

    @Bean(name = "fhirResourceConversionService")
    public ConversionService conversionService() {
        ConversionService conversionService = new ConversionService();

        // Register Resource Converters
        registerConditionConverters(conversionService);
        registerConsentConverters(conversionService);
        registerDiagnosticReportConverters(conversionService);
        registerObservationConverters(conversionService);
        registerPatientConverters(conversionService);
        registerProcedureConverters(conversionService);
        registerQuestionnaireResponseConverter(conversionService);

        return conversionService;
    }

    private void registerConditionConverters(ConversionService conversionService) {
        conversionService.registerConverter(Profile.DEFAULT_CONDITION, new DiagnoseCompositionConverter());
        conversionService.registerConverter(Profile.SYMPTOMS_COVID_19, new SymptomCompositionConverter());
        GECCODiagnoseCompositionConverter diagnoseCommonConverter = new GECCODiagnoseCompositionConverter();
        conversionService.registerConverter(Profile.DIAGNOSE_LIVER_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_LUNG_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_DIABETES_MELLITUS, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_COVID_19, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_MALIGNANT_NEOPLASTIC_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_RHEUMATOLOGICAL_IMMUNOLOGICAL_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_CARDIOVASCULAR_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_CHRONIC_KIDNEY_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_CHRONIC_NEUROLOGICAL_MENTAL_DISEASE, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_GASTROINTESTINAL_ULCERS, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_HIV, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_ORGAN_RECIPIENT, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_COMPLICATIONS_COVID_19, diagnoseCommonConverter);
        conversionService.registerConverter(Profile.DIAGNOSE_DEPENDENCE_ON_VENTILATOR, diagnoseCommonConverter);
    }

    private void registerConsentConverters(ConversionService conversionService) {
        conversionService.registerConverter(Profile.DO_NOT_RESUSCITATE_ORDER, null); // TODO: @ErikTute, add your converter
    }

    private void registerDiagnosticReportConverters(ConversionService conversionService) {
        conversionService.registerConverter(Profile.DIAGNOSTIC_REPORT_LAB, new DiagnosticReportLabCompositionConverter());
        conversionService.registerConverter(Profile.DIAGNOSTIC_REPORT_RADIOLOGY, new RadiologischerBefundCompositionConverter());
    }

    private void registerObservationConverters(ConversionService conversionService) {
        conversionService.registerConverter(Profile.BODY_HEIGHT, new BodyHeightCompositionConverter());
        conversionService.registerConverter(Profile.BLOOD_GAS_PANEL, new BloodGasPanelCompositionConverter());
        conversionService.registerConverter(Profile.BLOOD_PRESSURE, new BloodPressureCompositionConverter());
        conversionService.registerConverter(Profile.BODY_TEMP, new BodyTemperatureCompositionConverter());
        conversionService.registerConverter(Profile.BODY_WEIGHT, new BodyWeightCompositionConverter());
        conversionService.registerConverter(Profile.CLINICAL_FRAILTY_SCALE, new ClinicalFrailtyScaleScoreCompositionConverter());
        conversionService.registerConverter(Profile.CORONARIRUS_NACHWEIS_TEST, new CoronavirusNachweisTestCompositionConverter());
        conversionService.registerConverter(Profile.FIO2, new FiO2CompositionConverter());
        conversionService.registerConverter(Profile.HEART_RATE, new HeartRateCompositionConverter());
        conversionService.registerConverter(Profile.PATIENT_IN_ICU, new PatientInIcuCompositionConverter());
        conversionService.registerConverter(Profile.PREGNANCY_STATUS, new PregnancyStatusCompositionConverter());
        conversionService.registerConverter(Profile.OBSERVATION_LAB, new ObservationLabCompositionConverter());
        conversionService.registerConverter(Profile.RESPIRATORY_RATE, new RespiratoryRateCompositionConverter());
        conversionService.registerConverter(Profile.SOFA_SCORE, new SofaScoreCompositionConverter());
        conversionService.registerConverter(Profile.SMOKING_STATUS, new SmokingStatusCompositionConverter());

        conversionService.registerConverter(Profile.TRAVEL_HISTORY, new HistoryOfTravelConverter());
        conversionService.registerConverter(Profile.OXYGEN_SATURATION, new PulseOximetryCompositionConverter());
    }

    private void registerPatientConverters(ConversionService conversionService) {
        conversionService.registerConverter(Profile.PATIENT, new PatientCompositionConverter());
    }

    private void registerProcedureConverters(ConversionService conversionService) {
        conversionService.registerConverter(Profile.PROCEDURE, new ProcedureCompositionConverter());

        TherapyCompositionConverter therapyCompositionConverter = new TherapyCompositionConverter();
        conversionService.registerConverter(Profile.APHERESIS_PROCEDURE, therapyCompositionConverter);
        conversionService.registerConverter(Profile.DIALYSIS_PROCEDURE, therapyCompositionConverter);
        conversionService.registerConverter(Profile.EXTRACORPOREAL_MEMBRANE_OXYGENATION_PROCEDURE, therapyCompositionConverter);
        conversionService.registerConverter(Profile.PRONE_POSITION_PROCEDURE, therapyCompositionConverter);
        conversionService.registerConverter(Profile.RADIOLOGY_PROCEDURE, therapyCompositionConverter);
        conversionService.registerConverter(Profile.RESPIRATORY_THERAPIES_PROCEDURE, therapyCompositionConverter);
    }

    private void registerQuestionnaireResponseConverter(ConversionService conversionService) {
        conversionService.registerConverter(Profile.DEFAULT_QUESTIONNAIRE_RESPONSE, new D4lQuestionnaireCompositionConverter());
    }
}