package org.ehrbase.fhirbridge.ehr.opt.geccoprozedurcomposition.definition;

import java.lang.String;
import org.ehrbase.client.classgenerator.EnumValueSet;

public enum KategorieDefiningCode implements EnumValueSet {
  DIAGNOSTIC_PROCEDURE("Diagnostic procedure", "", "SNOMED Clinical Terms", "103693007"),

  IMAGING("Imaging", "", "SNOMED Clinical Terms", "363679005"),

  OTHER_CATEGORY("Other category", "", "SNOMED Clinical Terms", "394841004"),

  THERAPEUTIC_PROCEDURE("Therapeutic procedure", "", "SNOMED Clinical Terms", "277132007"),

  RESPIRATORY_THERAPY_PROCEDURE("Respiratory therapy (procedure)", "", "SNOMED Clinical Terms", "53950000"),

  SURGICAL_PROCEDURE("Surgical procedure", "", "SNOMED Clinical Terms", "387713003"),

  ADMINISTRATION_OF_MEDICINE("Administration of medicine", "", "SNOMED Clinical Terms", "18629005"),

  PROCEDURES_RELATING_TO_POSITIONING_AND_SUPPORT_PROCEDURE("Procedures relating to positioning and support (procedure)", "", "SNOMED Clinical Terms", "225287004");

  private String value;

  private String description;

  private String terminologyId;

  private String code;

  KategorieDefiningCode(String value, String description, String terminologyId, String code) {
    this.value = value;
    this.description = description;
    this.terminologyId = terminologyId;
    this.code = code;
  }

  public String getValue() {
     return this.value ;
  }

  public String getDescription() {
     return this.description ;
  }

  public String getTerminologyId() {
     return this.terminologyId ;
  }

  public String getCode() {
     return this.code ;
  }
}
