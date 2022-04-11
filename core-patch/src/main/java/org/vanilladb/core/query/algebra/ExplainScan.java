package org.vanilladb.core.query.algebra;

import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Constant;
import org.vanilladb.core.sql.VarcharConstant;

public class ExplainScan implements Scan {
    private Scan s;
    private Schema schema;
    private String stat;

    public ExplainScan(Scan s, Schema schema, String explainResult) { // called by ExplainPlan.open()
        this.s = s;
        this.schema = schema;
        stat = explainResult;
        int numOfRecords = 0;
        s.beforeFirst();
        while (s.next())
            numOfRecords++;
        s.close();
        stat +=  "\n Actual #recs: " + numOfRecords;
    }

	/**
	 * Positions the scan before its first record. In other words, the LHS scan
	 * is positioned at its first record, and the RHS scan is positioned before
	 * its first record.
	 * 
	 * @see Scan#beforeFirst()
	 */
	@Override
	public void beforeFirst() {
		s.beforeFirst();
	}

    /**
	 * Moves the scan to the next record. The method moves to the next RHS
	 * record, if possible. Otherwise, it moves to the next LHS record and the
	 * first RHS record. If there are no more LHS records, the method returns
	 * false.
	 * 
	 * @see Scan#next()
	 */
	@Override
	public boolean next() {
		if (s.next())
            return true;
        return false;
	}

	/**
	 * Returns the value of the specified field. The value is obtained from
	 * whichever scan contains the field.
	 * 
	 * @see Scan#getVal(java.lang.String)
	 */
	@Override
	public Constant getVal(String fldName) {
		if (hasField(fldName))                  // "query-plan"
			return new VarcharConstant(stat);
		else
			throw new RuntimeException("field " + fldName + " not found.");
	}

	@Override
	public void close() {
		s.close();
	}

    /**
	 * Returns true if the specified field is in the projection list.
	 * 
	 * @see Scan#hasField(java.lang.String)
	 */
	@Override
	public boolean hasField(String fldName) {
		return this.schema.hasField(fldName);
	}

}