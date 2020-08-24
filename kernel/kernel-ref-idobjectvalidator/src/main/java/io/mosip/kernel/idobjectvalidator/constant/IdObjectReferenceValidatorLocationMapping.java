package io.mosip.kernel.idobjectvalidator.constant;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The Enum IdObjectReferenceValidatorLocationMapping.
 *
 * @author condeis
 */
public enum IdObjectReferenceValidatorLocationMapping {
	
	COUNTRY("PAYS", "0"),
	REGION("REGION", "1"),
	PREFECTURE("PREFECTURE", "2"),
	SUBPREFECTURE_OR_COMMUNE("SOUS_PREFECTURE_OU_COMMUNE", "3"),
	DISTRICT("DISTRICT", "4"),
	SECTOR("SECTEUR", "5");

	private final String hierarchyName;
	private final String level;
	
	/**
	 * Instantiates a new id object reference validator location mapping.
	 *
	 * @param hierarchyName the hierarchy name
	 * @param level the level
	 */
	IdObjectReferenceValidatorLocationMapping(String hierarchyName, String level) {
		this.hierarchyName = hierarchyName;
		this.level = level;
	}

	/**
	 * Gets the hierarchy name.
	 *
	 * @return the hierarchy name
	 */
	public String getHierarchyName() {
		return hierarchyName;
	}

	/**
	 * Gets the level.
	 *
	 * @return the level
	 */
	public String getLevel() {
		return level;
	}
	
	/**
	 * Gets the all mapping.
	 *
	 * @return the all mapping
	 */
	public static Map<String, String> getAllMapping() {
		return Arrays.stream(values()).parallel()
				.collect(Collectors.toMap(IdObjectReferenceValidatorLocationMapping::getLevel,
						IdObjectReferenceValidatorLocationMapping::getHierarchyName));
	}
}
