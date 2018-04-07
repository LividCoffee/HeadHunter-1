package com.neo.headhunter.database.mysqlite;

import com.neo.headhunter.database.util.DBClause;
import com.neo.headhunter.database.util.SQLU;

public final class DBStatement {
    private DBClause operation;
    private final String table;
    private String[] columns = null, values = null, orderBy = null, assignments = null;
    private String where = null;
    private int limit = -1, offset = -1;

    public DBStatement(DBClause operation, String table) {
        this.operation = operation;
        this.table = table;
    }

    public String toString() {
        StringBuilder formula = new StringBuilder(operation.getFormat());
        switch(operation) {
        case UPDATE:
        case SELECT:
        case DELETE:
            if(where != null && !where.isEmpty())
                formula.append(DBClause.WHERE);
            if(orderBy != null && orderBy.length > 0)
                formula.append(DBClause.ORDER_BY);
            if(limit != -1) {
                if(offset != -1)
                    formula.append(DBClause.LIMIT_OFFSET);
                else
                    formula.append(DBClause.LIMIT);
            }
            break;
        }
        String result = formula.toString();
        result = result.replace(SQLU.TABLE,         table);
        result = result.replace(SQLU.COLUMNS,       SQLU.concat(columns));
        result = result.replace(SQLU.VALUES,        SQLU.concat(values));
        result = result.replace(SQLU.ASSIGNMENTS,   SQLU.concat(assignments));
        result = result.replace(SQLU.WHERE,         where);
        result = result.replace(SQLU.ORDER_BY,      SQLU.concat(orderBy));
        result = result.replace(SQLU.LIMIT,         String.valueOf(limit));
        result = result.replace(SQLU.OFFSET,        String.valueOf(offset));
        return result + ";";
    }
    
    public DBClause getOperation() {
        return operation;
    }
    
    public void setOperation(DBClause operation) {
        this.operation = operation;
    }
    
    public void setColumns(String... columns) {
        this.columns = columns;
    }

    public void setValues(String... values) {
        this.values = values;
    }

    public void setAssignments(String... assignments) {
        this.assignments = assignments;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public void setOrderBy(String... orderBy) {
        this.orderBy = orderBy;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
}
