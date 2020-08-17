package io.mosip.kernel.idobjectvalidator.constant;

/**
 * The Class IdObjectReferenceValidatorConstant.
 *
 * @author ondeis
 */
public class IdObjectReferenceValidatorConstant {
	
	public static final String ROOT_PATH = "identity";  
	public static final String IDENTITY_ARRAY_VALUE_FIELD = "value";
	public static final String IDENTITY_REFERENCE_IDENTITY_NUMBER_PATH = "identity.referenceIdentityNumber"; 
	public static final String IDENTITY_LANGUAGE_PATH = "identity.*.*.language"; 
	public static final String IDENTITY_GENDER_LANGUAGE_PATH = "identity.gender.*.language";
	public static final String IDENTITY_GENDER_VALUE_PATH = "identity.gender.*.value";
	public static final String IDENTITY_REGION_LANGUAGE_PATH = "identity.region.*.language";
	public static final String IDENTITY_REGION_VALUE_PATH = "identity.region.*.value";
	public static final String IDENTITY_PREFECTURE_LANGUAGE_PATH = "identity.prefecture.*.language";
	public static final String IDENTITY_PREFECTURE_VALUE_PATH = "identity.prefecture.*.value";
	public static final String IDENTITY_SUBPREFECTURE_OR_COMMUNE_LANGUAGE_PATH = "identity.subPrefectureOrCommune.*.language";
	public static final String IDENTITY_SUBPREFECTURE_OR_COMMUNE_VALUE_PATH = "identity.subPrefectureOrCommune.*.value";
	public static final String IDENTITY_DISTRICT_LANGUAGE_PATH = "identity.district.*.language";
	public static final String IDENTITY_DISTRICT_VALUE_PATH = "identity.district.*.value";
	public static final String IDENTITY_SECTOR_LANGUAGE_PATH = "identity.sector.*.language";
	public static final String IDENTITY_SECTOR_VALUE_PATH = "identity.sector.*.value";
	public static final String IDENTITY_RESIDENCE_STATUS_LANGUAGE_PATH = "identity.residenceStatus.*.language";
	public static final String IDENTITY_RESIDENCE_STATUS_VALUE_PATH = "identity.residenceStatus.*.value";
	public static final String MASTERDATA_LANGUAGE_PATH = "response.languages.*";
	public static final String MASTERDATA_LOCATIONS_PATH = "locations.*";
	public static final String MASTERDATA_LANGUAGE_URI = "mosip.kernel.idobjectvalidator.masterdata.languages.rest.uri";
	public static final String MASTERDATA_GENDERTYPES_URI = "mosip.kernel.idobjectvalidator.masterdata.gendertypes.rest.uri";
	public static final String MASTERDATA_DOCUMENT_CATEGORIES_URI = "mosip.kernel.idobjectvalidator.masterdata.documentcategories.rest.uri";
	public static final String MASTERDATA_DOCUMENT_TYPES_URI = "mosip.kernel.idobjectvalidator.masterdata.documenttypes.rest.uri";
	public static final String MASTERDATA_LOCATIONS_URI = "mosip.kernel.idobjectvalidator.masterdata.locations.rest.uri";
	public static final String MASTERDATA_LOCATION_HIERARCHY_URI = "mosip.kernel.idobjectvalidator.masterdata.locationhierarchy.rest.uri";
	public static final String MASTERDATA_INDIVIDUAL_TYPES_URI = "mosip.kernel.idobjectvalidator.masterdata.individualtypes.rest.uri";
	public static final String LOCATION_NA = "mosip.kernel.idobjectvalidator.masterdata.locations.locationNotAvailable";
	public static final String DOB_FORMAT = "uuuu/MM/dd";
	public static final String IDENTITY_DOB_PATH = "identity.dateOfBirth";
	public static final String EMAIL_FORMAT="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-zA-Z]{2,})$";
	public static final String IDENTITY_EMAIL_PATH = "identity.email";
	public static final String PHONE_NUMBER_FORMAT="^(6[256]{1})([0-9]{7})$";
	public static final String IDENTITY_PHONE_NUMBER_PATH="identity.phone";

}
