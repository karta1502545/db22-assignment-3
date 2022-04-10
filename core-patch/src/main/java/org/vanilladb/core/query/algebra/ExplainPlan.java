package org.vanilladb.core.query.algebra;

import org.vanilladb.core.storage.metadata.statistics.Histogram;
import org.vanilladb.core.sql.Schema;
import org.vanilladb.core.sql.Type;

public class ExplainPlan implements Plan {
    private Plan p;

	/**
	 * Creates a new select node in the query tree, having the specified
	 * subquery and predicate.
	 * 
	 * @param p
	 *            the subquery
	 * @param pred
	 *            the predicate
	 */
	public ExplainPlan(Plan p) {
		this.p = p;
	}

    /**
	 * Returns the schema of the selection, which is the same as in the
	 * underlying query.
	 * 
	 * @see Plan#schema()
	 */
	@Override
	public Schema schema() {
        Schema schema = new Schema();
        schema.addField("query-plan", Type.VARCHAR(500));
		return schema;
	}

    /**
	 * Creates a select scan for this query.
	 * 
	 * @see Plan#open()
	 */
	@Override
	public Scan open() {
		Scan s = p.open();
		return new ExplainScan(s);
	}

    /**
	 * Returns the histogram that approximates the join distribution of the
	 * field values of query results.
	 * 
	 * @see Plan#histogram()
	 */
	@Override
	public Histogram histogram() {
		return p.histogram();
	}

    /**
	 * Returns an estimate of the number of records in the query's output table.
	 * 
	 * @see Plan#recordsOutput()
	 */
	@Override
	public long recordsOutput() {
		return 1;
	}

	/**
	 * Estimates the number of block accesses in the product. The formula is:
	 * 
	 * <pre>
	 * B(product(p1, p2)) = B(p1) + R(p1) * B(p2)
	 * </pre>
	 * 
	 * @see Plan#blocksAccessed()
	 */
	@Override
	public long blocksAccessed() {
		return p.blocksAccessed();
	}
}
