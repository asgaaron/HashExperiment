/*
 *Author: Aaron Gardner
 */
public class HashTable {

	private final HashObject[] hashTable;
	private final int tableSize;
	private int size;
	private long probes;
	private int inserts;
	private int duplicates;

	// Constructor
	HashTable(int tableSize) {
		size = 0;
		probes = 0;
		duplicates = 0;
		inserts = 0;
		this.tableSize = tableSize;
		hashTable = new HashObject[tableSize];
		for (int i = 0; i < tableSize; i++) {
			hashTable[i] = null;
		}
	}

	/**
	 * Function to get number of inputs
	 *
	 * @return number of object-key values in the table
	 */
	public int getSize() {
		return size;
	}

	// /**
	// * Function to return the object of a key
	// *
	// * @param key of object desired
	// * @return HashObject from desired key
	// */
	// public HashObject get(int key) {
	// int hash = (getHash(key) % tableSize);
	// if (hashTable[hash] == null) {
	// return null;
	// } else {
	// HashObject entry = hashTable[hash];
	// return entry;
	// }
	// }

	/**
	 * Function to insert an element into the hash table using a linear hash
	 *
	 * @param object
	 *            the value of the object being stored
	 */
	public int insertLinear(Object object) {
		int insertProbes = 0;
		int probes = 0;
		int hash = getHash(object);
		while (true) {
			probes++;
			insertProbes++;
			if (hashTable[hash] == null) {
				HashObject<Object> hashObject = new HashObject<Object>(hash, object);
				hashTable[hash] = hashObject;
				inserts++;
				size++;
				this.probes += probes;
				return insertProbes;
			} else if (hashTable[hash].equals(object)) {
				duplicates++;
				hashTable[hash].incrementFrequency();
				return insertProbes;
			}

			if (++hash >= tableSize) {
				hash = 0;
			}
		}
	}

	/**
	 * Function to insert an element into the hash table using double hashing
	 * Primary hash function: h1(k) = k mod m Secondary hash function: h2(k) = 1
	 * + (k mod(m-2))
	 *
	 * @param object
	 *            the value of the object being stored
	 */
	public int insertDouble(Object object) {
		// initialize local variables
		int insertProbes = 0, probes = 0, i = 1;
		// get hash key from object
		int hash = getHash(object);
		// begin searching through list
		while (true) {
			// increment probes
			probes++;
			insertProbes++;
			// if we find an open spot
			if (hashTable[hash] == null) {
				// increment size, #of inserts
				size++;
				inserts++;
				// create HashObject and store in table
				HashObject<Object> hashObject = new HashObject<Object>(hash, object);
				hashTable[hash] = hashObject;
				// increment probes counter and exit function
				this.probes += probes;
				return insertProbes;
			} // if we find an identical object
			else if (hashTable[hash].equals(object)) {
				// increment duplicate count and object frequency
				duplicates++;
				hashTable[hash].incrementFrequency();
				// Exit method
				return insertProbes;
			}
			// if the index is not empty and not a duplicate, increment probe
			// counter and hash
			hash = Math.abs((hash + i++ * (1 + (hash % (tableSize - 2)))) % tableSize);
		}
	}

	/**
	 * Function for generating a hashkey based off an object
	 *
	 * @param o
	 *            object that key will be generated from
	 * @return integer hashkey
	 */
	private int getHash(Object o) {
		int hashKey = o.hashCode();
		hashKey = Math.abs(hashKey);
		hashKey %= tableSize;
		return hashKey;
	}

	/**
	 * Function to print out the hash table
	 */
	public void printHashTable() {
		for (int i = 0; i < tableSize; i++) {
			if (!(hashTable[i] == null)) {
				System.out.print("table[" + i + "]: " + hashTable[i].toString() + " " + hashTable[i].getFreq() + "\n");
			}
		}
		System.out.println("Table End");
	}

	int getDuplicates() {
		return duplicates;
	}

	double getAvgProbes() {
		return (probes / (float) inserts);
	}

}
