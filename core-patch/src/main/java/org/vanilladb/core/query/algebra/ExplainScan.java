package org.vanilladb.core.query.algebra;

import javax.xml.validation.Schema;

public class ExplainScan implements Scan {
    private Scan s;
    private Schema schema;

    public ExplainScan(Scan s, Schema schema, String explainResult) { // called by ExplainPlan.open()

    }
}