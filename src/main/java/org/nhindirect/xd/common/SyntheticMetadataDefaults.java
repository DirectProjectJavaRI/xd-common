package org.nhindirect.xd.common;

/**
 * Holds configurable default values applied to XDS document metadata when required fields
 * are absent (e.g., XDM limited-metadata packages, CDA documents that omit facility/setting).
 * Construct via Spring using {@code @Value}-injected properties and pass as a constructor
 * argument to {@link DirectDocuments} and {@link DefaultMimeXdsTransformer}.
 */
public class SyntheticMetadataDefaults {

    public static final String DEFAULT_CLASS_CODE = "34133-9";
    public static final String DEFAULT_CONFIDENTIALITY_CODE = "N";
    public static final String DEFAULT_HEALTHCARE_FACILITY_TYPE_CODE = "Outpatient";
    public static final String DEFAULT_PRACTICE_SETTING_CODE = "General Medicine";

    private final String classCode;
    private final String confidentialityCode;
    private final String healthcareFacilityTypeCode;
    private final String practiceSettingCode;

    public SyntheticMetadataDefaults() {
        this(DEFAULT_CLASS_CODE, DEFAULT_CONFIDENTIALITY_CODE,
                DEFAULT_HEALTHCARE_FACILITY_TYPE_CODE, DEFAULT_PRACTICE_SETTING_CODE);
    }

    public SyntheticMetadataDefaults(String classCode, String confidentialityCode,
            String healthcareFacilityTypeCode, String practiceSettingCode) {
        this.classCode = classCode;
        this.confidentialityCode = confidentialityCode;
        this.healthcareFacilityTypeCode = healthcareFacilityTypeCode;
        this.practiceSettingCode = practiceSettingCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getConfidentialityCode() {
        return confidentialityCode;
    }

    public String getHealthcareFacilityTypeCode() {
        return healthcareFacilityTypeCode;
    }

    public String getPracticeSettingCode() {
        return practiceSettingCode;
    }
}
