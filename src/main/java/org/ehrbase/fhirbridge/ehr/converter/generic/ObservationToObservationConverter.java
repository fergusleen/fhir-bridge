package org.ehrbase.fhirbridge.ehr.converter.generic;

import org.ehrbase.client.classgenerator.interfaces.EntryEntity;
import org.hl7.fhir.r4.model.Observation;
import org.springframework.lang.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.TemporalAccessor;

public abstract class ObservationToObservationConverter<E extends EntryEntity> extends EntryEntityConverter<Observation, E>  {

    @Override
    public E convert(@NonNull Observation resource) {
        E entryEntity = super.convert(resource);
        invokeTimeValues(entryEntity, resource);
        return entryEntity;
    }

     public void invokeTimeValues(E entryEntity, Observation resource) {
        invokeOriginValue(entryEntity, resource);
        invokeSetTimeValue(entryEntity, resource);
    }

     public void invokeSetTimeValue(E entryEntity, Observation resource){
        try {
            Method setOriginValue = entryEntity.getClass().getMethod("setOriginValue", TemporalAccessor.class);
            setOriginValue.invoke(entryEntity, TimeConverter.convertObservationTime(resource));
        } catch (IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        } catch (NoSuchMethodException ignored){
            //ignored
        }
    }

     public void invokeOriginValue(E entryEntity, Observation resource){
        try {
            Method setTimeValue = entryEntity.getClass().getMethod("setTimeValue", TemporalAccessor.class);
            setTimeValue.invoke(entryEntity, TimeConverter.convertObservationTime(resource));
        } catch ( IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
        }catch (NoSuchMethodException ignored){
            //ignored
        }
    }

}