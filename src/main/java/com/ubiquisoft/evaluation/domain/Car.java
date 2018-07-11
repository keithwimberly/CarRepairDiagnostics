package com.ubiquisoft.evaluation.domain;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Car {

	private String year;
	private String make;
	private String model;

	private List<Part> parts;
	static private Map<PartType, Integer> requiredParts = getRequiredParts();

	/**
	 * Return map of the part types missing.
	 *
	 * Each car requires one of each of the following types:
	 *      ENGINE, ELECTRICAL, FUEL_FILTER, OIL_FILTER
	 * and four of the type: TIRE
	 *
	 * Example: a car only missing three of the four tires should return a map like this:
	 *
	 *      {
	 *          "TIRE": 3
	 *      }
	 */
	public Map<PartType, Integer> getMissingPartsMap() {
		Map<PartType, Integer> missingPartsMap = new HashMap<PartType, Integer>();
		missingPartsMap.putAll(requiredParts);

		if (parts != null) {
			for (Part part : parts) {
				PartType partType = part.getType();
				Integer partCount = missingPartsMap.get(partType);
				if (partCount != null) {
					--partCount;
				}
				if (partCount == 0) {
					missingPartsMap.remove(partType);
				} else {
					missingPartsMap.replace(partType, partCount);
				}
			}
		}

		return missingPartsMap;
	}

	@Override
	public String toString() {
		return "Car{" +
				       "year='" + year + '\'' +
				       ", make='" + make + '\'' +
				       ", model='" + model + '\'' +
				       ", parts=" + parts +
				       '}';
	}

	/**
	 * This method populated the static map that indicates how many of each part makes
	 * up a proper car.
	 *
	 * @return
	 */
	protected static Map<PartType, Integer> getRequiredParts() {
		return Collections.unmodifiableMap(Stream.of(
				new AbstractMap.SimpleEntry<>(PartType.ELECTRICAL, 1),
				new AbstractMap.SimpleEntry<>(PartType.ENGINE, 1),
				new AbstractMap.SimpleEntry<>(PartType.FUEL_FILTER, 1),
				new AbstractMap.SimpleEntry<>(PartType.OIL_FILTER, 1),
				new AbstractMap.SimpleEntry<>(PartType.TIRE, 4))
				.collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters *///region
	/* --------------------------------------------------------------------------------------------------------------- */

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public List<Part> getParts() {
		return parts;
	}

	public void setParts(List<Part> parts) {
		this.parts = parts;
	}

	/* --------------------------------------------------------------------------------------------------------------- */
	/*  Getters and Setters End *///endregion
	/* --------------------------------------------------------------------------------------------------------------- */

}
