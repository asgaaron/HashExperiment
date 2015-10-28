
/*
 *Author: Aaron Gardner
 */
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class HashTest {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		int inputType = 0;
		double loadFactor = 0;
		int debugLevel = 999;
		try {
			inputType = Integer.parseInt(args[0]);
			loadFactor = Double.parseDouble(args[1]);
			validateArgs(inputType, loadFactor, args);

			if (args.length == 3) {
				debugLevel = Integer.parseInt(args[2]);
			}
		} catch (Exception ex) {
			System.out.println("ERROR DEFINING VARIABLES FROM COMMAND LINE");
			ex.printStackTrace();
		}
		final int tableSize = 95959;
		final int numKeys = (int) (loadFactor * tableSize);
		HashTable linearTable = new HashTable(tableSize);
		HashTable doubleTable = new HashTable(tableSize);
		if (debugLevel == 0) {
			printSummary(tableSize, inputType, numKeys);
		}
		switch (inputType) {
		case 1: {
			randomList(debugLevel, numKeys, linearTable, loadFactor, doubleTable);
			break;
		}
		case 2: {
			sysClock(debugLevel, numKeys, linearTable, loadFactor, doubleTable);
			break;
		}
		case 3: {
			wordList(debugLevel, linearTable, numKeys, loadFactor, doubleTable);
			break;
		}
		}
	}

	private static void validateArgs(final int inputType, final double loadFactor, String[] args) {
		if ((args.length != 2 && args.length != 3)) {
			fail();
		}
		if (inputType > 3 | inputType < 1) {
			fail();
		}

		if (loadFactor > 1 | loadFactor < 0) {
			fail();
		}
	}

	private static void randomList(int debugLevel, final int numKeys, HashTable linearTable, final double loadFactor,
			HashTable doubleTable) {
		Random keyGenerator = new Random();
		printDebugHeaderLinear(debugLevel);
		while (linearTable.getSize() < numKeys) {
			int value = keyGenerator.nextInt();
			if (debugLevel == 2) {
				System.out.println("# of probes for this input into LinearTable: " + linearTable.insertLinear(value));
			} else {
				linearTable.insertLinear(value);
			}
		}
		if (debugLevel == 1) {
			System.out.println("Linear Table:");
		}
		printDebugResult(debugLevel, numKeys, linearTable, loadFactor);

		printDebugHeaderDouble(debugLevel);
		while (doubleTable.getSize() < numKeys) {
			int value = keyGenerator.nextInt();
			if (debugLevel == 2) {
				System.out.println("# of probes for this input into DoubleTable: " + doubleTable.insertDouble(value));
			} else {
				doubleTable.insertDouble(value);
			}
		}
		if (debugLevel == 1) {
			System.out.println("Double Table:");
		}
		printDebugResult(debugLevel, numKeys, doubleTable, loadFactor);
	}

	private static void printDebugHeaderLinear(int debugLevel) {
		if (debugLevel == 0) {
			System.out.println("Using Linear Hashing...");
		}
	}

	private static void sysClock(int debugLevel, final int numKeys, HashTable linearTable, final double loadFactor,
			HashTable doubleTable) {
		printDebugHeaderLinear(debugLevel);
		while (linearTable.getSize() < numKeys) {
			if (debugLevel == 2) {
				System.out.println("# of probes for this input into LinearTable: "
						+ linearTable.insertLinear(System.currentTimeMillis()));
			} else {
				linearTable.insertLinear(System.currentTimeMillis());
			}
		}
		if (debugLevel == 1) {
			System.out.println("Linear Table:");
		}
		printDebugResult(debugLevel, numKeys, linearTable, loadFactor);

		printDebugHeaderDouble(debugLevel);
		while (doubleTable.getSize() < numKeys) {
			if (debugLevel == 2) {
				System.out.println("# of probes for this input into DoubleTable: "
						+ doubleTable.insertDouble(System.currentTimeMillis()));
			} else {
				doubleTable.insertDouble(System.currentTimeMillis());
			}

		}
		if (debugLevel == 1) {
			System.out.println("Double Table:");
		}
		printDebugResult(debugLevel, numKeys, doubleTable, loadFactor);
	}

	private static void wordList(int debugLevel, HashTable linearTable, final int numKeys, final double loadFactor,
			HashTable doubleTable) {
		String filename = "word-list";
		String line = null;
		try {
			// BEGIN LINEAR TABLE
			printDebugHeaderLinear(debugLevel);
			FileReader linearFileReader = new FileReader(filename);
			BufferedReader linearBufferedReader = new BufferedReader(linearFileReader);
			while (((line = linearBufferedReader.readLine()) != null) && linearTable.getSize() < numKeys) {
				if (debugLevel == 2) {
					System.out
							.println("# of probes for this input into LinearTable: " + linearTable.insertLinear(line));
				} else {
					// System.out.println(line);
					linearTable.insertLinear(line);
				}
			}
			System.out.println("Finished inserting to table");
			linearBufferedReader.close();
			linearFileReader.close();
			if (debugLevel == 1) {
				System.out.println("Linear Table:");
			}
			printDebugResult(debugLevel, numKeys, linearTable, loadFactor);

			// BEGIN DOUBLE TABLE
			printDebugHeaderDouble(debugLevel);
			FileReader fileReader = new FileReader(filename);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			while (((line = bufferedReader.readLine()) != null) && doubleTable.getSize() < numKeys) {
				if (debugLevel == 2) {
					System.out
							.println("# of probes for this input into DoubleTable: " + doubleTable.insertDouble(line));
				} else {
					doubleTable.insertDouble(line);
				}
			}
			bufferedReader.close();
			if (debugLevel == 1) {
				System.out.println("Double Table:");
			}
			printDebugResult(debugLevel, numKeys, doubleTable, loadFactor);
		} catch (FileNotFoundException ex) {
			System.out.print("Unable to open " + filename);
		} catch (IOException ex) {
		}
	}

	private static void printDebugHeaderDouble(int debugLevel) {
		if (debugLevel == 0) {
			System.out.println("Using Double Hashing...");
		}
	}

	private static void printDebugResult(int debugLevel, final int numKeys, HashTable table, final double loadFactor) {
		if (debugLevel == 0) {
			System.out.println("Inserted " + table.getSize() + " elements, " + table.getDuplicates() + " duplicates\n"
					+ "load factor = " + loadFactor + ", Avg. no of probes " + table.getAvgProbes() + "\n");
		}
		if (debugLevel == 1) {
			table.printHashTable();
		}
	}

	private static int fail() {
		System.out.print("Usage: please use command line arguments: " + "<data source> <load factor> [<debug level>]\n"
				+ "data sources:\n" + "1 for java.util.Random\n" + "2 for System.currentTimeMillis()\n"
				+ "3 for word-list\n" + "load factor: 0 <= loadfactor <= 1\n" + "debug level:"
				+ "1 for summary of experiment\n" + "2 for print out of has tables\n"
				+ "3 for print number of probes for each new insert\n");
		return 0;

	}

	private static void printSummary(int tableSize, int inputType, int numKeys) {
		System.out.println("A good table size is found: " + tableSize + "\n");
		// System.out.println("numkeys: " + numKeys);
		String dataSource = null;
		switch (inputType) {
		case 1: {
			dataSource = "java.util.Random";
			break;
		}
		case 2: {
			dataSource = "System.currentTimeMillis()";
			break;
		}
		case 3: {
			dataSource = "word-list";
			break;
		}
		}
		System.out.println("Data source type: " + dataSource);
	}

	// private static void testDuplicates() {
	// HashTable testTable = new HashTable(10);
	// testTable.insertDouble(9);
	// testTable.insertDouble(9);
	// System.out.println(testTable.getDuplicates());
	//
	// }
}
