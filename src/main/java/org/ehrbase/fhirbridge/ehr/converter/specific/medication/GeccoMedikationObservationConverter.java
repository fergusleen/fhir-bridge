package org.ehrbase.fhirbridge.ehr.converter.specific.medication;

import com.nedap.archie.rm.datavalues.DvCodedText;
import org.ehrbase.fhirbridge.ehr.converter.ConversionException;
import org.ehrbase.client.classgenerator.interfaces.EntryEntity;
import org.ehrbase.fhirbridge.ehr.converter.LoggerMessages;
import org.ehrbase.fhirbridge.ehr.converter.generic.MedicationStatementToObservationConverter;
import org.ehrbase.fhirbridge.ehr.converter.parser.DvCodedTextParser;
import org.ehrbase.fhirbridge.ehr.opt.geccomedikationcomposition.definition.StatusCluster;
import org.ehrbase.fhirbridge.ehr.opt.geccomedikationcomposition.definition.StatusDefiningCode2;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public abstract class GeccoMedikationObservationConverter<E extends EntryEntity> extends MedicationStatementToObservationConverter<E> {

    private static final Logger LOG = LoggerFactory.getLogger(GeccoMedikationObservationConverter.class);

    @Override
    public E convert(@NonNull MedicationStatement resource) {
        E entryEntity = super.convert(resource);
        invokeStatus(entryEntity, resource);
        return entryEntity;
    }

    public void invokeStatus(E entryEntity, MedicationStatement resource) {
        try {
            // Normal setStatus (setStatusTree) is not set since only obligatory contained in the template, the information is not contained in the fhir resource at all
            Method setStatus = entryEntity.getClass().getMethod("setItemTreeStatus", StatusCluster.class);
            setStatus.invoke(entryEntity, mapStatus(resource));
        } catch (IllegalAccessException | InvocationTargetException exception) {
            LOG.error(LoggerMessages.printInvokeError(exception));
        } catch (NoSuchMethodException ignored) {
            //ignored
        }
    }

    protected Optional<DvCodedText> getGrundDefiningCode(MedicationStatement resource) {
        if (resource.hasReasonCode() && resource.getReasonCode().size()>0 && resource.getReasonCode().get(0).hasCoding()) {
                return DvCodedTextParser.parseFHIRCoding(resource.getReasonCode().get(0).getCoding().get(0));
            }
        return Optional.empty();
    }

    protected StatusCluster mapStatus(MedicationStatement resource) {
        StatusCluster statusCluster = new StatusCluster();
        String fhirStatus = resource.getStatusElement().getCode();
        if (fhirStatus.equals(StatusDefiningCode2.ABGESCHLOSSEN.getValue()) || fhirStatus.equals("completed")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.ABGESCHLOSSEN);
        } else if (fhirStatus.equals(StatusDefiningCode2.AKTIV.getValue()) || fhirStatus.equals("active")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.AKTIV);
        } else if (fhirStatus.equals(StatusDefiningCode2.ANGEHALTEN.getValue()) || fhirStatus.equals("on-hold")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.ANGEHALTEN);
        } else if (fhirStatus.equals(StatusDefiningCode2.BEABSICHTIGT.getValue()) || fhirStatus.equals("intended")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.BEABSICHTIGT);
        } else if (fhirStatus.equals(StatusDefiningCode2.FEHLERHAFTE_ANWENDUNG.getValue()) || fhirStatus.equals("entered-in-error")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.FEHLERHAFTE_ANWENDUNG);
        } else if (fhirStatus.equals(StatusDefiningCode2.GESTOPPT.getValue()) || fhirStatus.equals("stopped")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.GESTOPPT);
        } else if (fhirStatus.equals(StatusDefiningCode2.NICHT_GENOMMEN.getValue()) || fhirStatus.equals("not-taken")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.NICHT_GENOMMEN);
        } else if (fhirStatus.equals(StatusDefiningCode2.UNBEKANNT.getValue()) || fhirStatus.equals("unknown")) {
            statusCluster.setStatusDefiningCode(StatusDefiningCode2.UNBEKANNT);
        } else {
            throw new ConversionException("Invalid Status code " + fhirStatus);
        }
        return statusCluster;
    }


}
