package networking;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import java.util.Set;

public class NTTable {

	// the base Network table that this class wraps
	NetworkTable table;

	public NTTable(String tableName) {

		table = NetworkTableInstance.getDefault().getTable(tableName);

	}

	// returns all of the set keys in the table in a String[] array.
	public String[] getKeys() {

		Set<String> rawKeys = table.getKeys();

		String[] keys = rawKeys.toArray(new String[0]);

		return keys;

	}

	// not functional
	public NTTable[] getSubtables() {

		// gets all of the sub table names inside of this table.
		Set<String> rawSubtableNames = table.getSubTables();

		// gets the keys of the current table
		String[] subtableNames = (String[])rawSubtableNames.toArray();

		// initializes a new array of NTTables
		NTTable[] subtables = new NTTable[subtableNames.length];

		for(int i = 0; i < subtables.length; i++) {

			// each of the subtables are added to the array
			subtables[i] = new NTTable(subtableNames[i]);

		}

		return subtables;

	}

	// gets the path of the NetworkTable
	public String getTablePath() {

		return table.getPath();

	}

	// gets the value at the key in this NetworkTable
	public Object get(String key) {

		return table.getEntry(key).getValue().getValue();
		
	}
	
	// sets the value at the key in this NetworkTable
	public void set(String key, Object value) {

		// this value is not bound to a specific type, so it can be set with any Object type
		table.getEntry(key).setValue(value);

	}

}