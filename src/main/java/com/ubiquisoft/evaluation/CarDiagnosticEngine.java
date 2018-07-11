package com.ubiquisoft.evaluation;

import com.ubiquisoft.evaluation.domain.Car;
import com.ubiquisoft.evaluation.domain.ConditionType;
import com.ubiquisoft.evaluation.domain.Part;
import com.ubiquisoft.evaluation.domain.PartType;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CarDiagnosticEngine {



	public void executeDiagnostics(Car car) {

		// Step 1: Validate basic 3 car data fields are present
		boolean basicCarInformationMissing = validateBasicCarInformation(car);
		if (basicCarInformationMissing) {
			// Return if any validation fails
			return;
		}


		// Step 2: Validate that there are not missing parts
		Map<PartType, Integer> missingPartsMap = car.getMissingPartsMap();
        for (Map.Entry<PartType, Integer> entry : missingPartsMap.entrySet()) {
            PartType missingPartType = entry.getKey();
            Integer missingPartCount = entry.getValue();
            printMissingPart(missingPartType, missingPartCount);
        }
        if (missingPartsMap.size() > 0) {
            return;
        }

        // Step 3: Validate for damaged parts
        boolean foundDamagedParts = false;
        List<Part> parts = car.getParts();
        for (Part part : parts) {
            ConditionType condition = part.getCondition();
            if (condition == null || !part.isInWorkingCondition()) {
                foundDamagedParts = true;
                PartType partType = part.getType();
                ConditionType partCondition = part.getCondition();
                printDamagedPart(partType, partCondition);
            }
        }
        if (foundDamagedParts) {
            return;
        }
        else {
            System.out.println("Car checks out good!");
        }


        /*
		 * Implement basic diagnostics and print results to console.
		 *
		 * The purpose of this method is to find any problems with a car's data or parts.
		 *
		 * Diagnostic Steps:
		 *      First   - Validate the 3 data fields are present, if one or more are
		 *                then print the missing fields to the console
		 *                in a similar manner to how the provided methods do.
		 *
		 *      Second  - Validate that no parts are missing using the 'getMissingPartsMap' method in the Car class,
		 *                if one or more are then run each missing part and its count through the provided missing part method.
		 *
		 *      Third   - Validate that all parts are in working condition, if any are not
		 *                then run each non-working part through the provided damaged part method.
		 *
		 *      Fourth  - If validation succeeds for the previous steps then print something to the console informing the user as such.
		 * A damaged part is one that has any condition other than NEW, GOOD, or WORN.
		 *
		 * Important:
		 *      If any validation fails, complete whatever step you are actively one and end diagnostics early.
		 *
		 * Treat the console as information being read by a user of this application. Attempts should be made to ensure
		 * console output is as least as informative as the provided methods.
		 */


	}

	private boolean validateBasicCarInformation(Car car)
	{
		boolean missingInformation = false;
		List<String> missingCarInformation = new LinkedList<String>();

		String make = car.getMake();
		if (make == null) {
			missingCarInformation.add("Make");
		}
		String model = car.getModel();
		if (model == null) {
			missingCarInformation.add("Model");
		}
		String year = car.getYear();
		if (year == null) {
			missingCarInformation.add("Year");
		}
		if (missingCarInformation.size() > 0) {
			missingInformation = true;
			printMissingCarInformation(missingCarInformation);
		}
		return missingInformation;
	}

	private void printMissingCarInformation(List missingCarInformation)
	{
		// Print missing field to the console in a similar manner to how the provided methods do
		if (missingCarInformation != null && missingCarInformation.size() > 0) {
			System.out.println(String.format("Missing Car Information Detected: %s", missingCarInformation));
		}
	}

	private void printMissingPart(PartType partType, Integer count) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (count == null || count <= 0) throw new IllegalArgumentException("Count must be greater than 0");

		System.out.println(String.format("Missing Part(s) Detected: %s - Count: %s", partType, count));
	}

	private void printDamagedPart(PartType partType, ConditionType condition) {
		if (partType == null) throw new IllegalArgumentException("PartType must not be null");
		if (condition == null) throw new IllegalArgumentException("ConditionType must not be null");

		System.out.println(String.format("Damaged Part Detected: %s - Condition: %s", partType, condition));
	}

	public static void main(String[] args) throws JAXBException {

	    for (String arg : args) {

	        String carFilename = arg + ".xml";

	        System.out.println("Diagnosing " + carFilename);
	        System.out.println("------------------------");

            // Load classpath resource
            InputStream xml = ClassLoader.getSystemResourceAsStream(carFilename);

            // Verify resource was loaded properly
            if (xml == null) {
                System.err.println("An error occurred attempting to load " + carFilename);

                System.exit(1);
            }

            // Build JAXBContext for converting XML into an Object
            JAXBContext context = JAXBContext.newInstance(Car.class, Part.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            Car car = (Car) unmarshaller.unmarshal(xml);

            // Build new Diagnostics Engine and execute on deserialized car object.

            CarDiagnosticEngine diagnosticEngine = new CarDiagnosticEngine();

            diagnosticEngine.executeDiagnostics(car);

            System.out.println();
            System.out.println();
        }

	}

}
