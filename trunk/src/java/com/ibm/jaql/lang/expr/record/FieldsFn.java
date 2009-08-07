/*
 * Copyright (C) IBM Corp. 2008.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ibm.jaql.lang.expr.record;

import com.ibm.jaql.json.schema.Schema;
import com.ibm.jaql.json.schema.SchemaFactory;
import com.ibm.jaql.json.type.JsonRecord;
import com.ibm.jaql.json.util.JsonIterator;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.IterExpr;
import com.ibm.jaql.lang.expr.core.JaqlFn;

/**
 * 
 */
@JaqlFn(fnName = "fields", minArgs = 1, maxArgs = 1)
public final class FieldsFn extends IterExpr
{
  /**
   * Expr record
   * 
   * @param exprs
   */
  public FieldsFn(Expr[] exprs)
  {
    super(exprs);
  }

  /**
   * @param recExpr
   */
  public FieldsFn(Expr recExpr)
  {
    this(new Expr[]{recExpr});
  }

  public Schema getSchema()
  {
    if (exprs[0].getSchema().isNull().maybe())
    {
      return SchemaFactory.arrayOrNullSchema();
    }
    else
    {
      return SchemaFactory.arraySchema();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.IterExpr#iter(com.ibm.jaql.lang.core.Context)
   */
  public JsonIterator iter(final Context context) throws Exception
  {
    final JsonRecord rec = (JsonRecord) exprs[0].eval(context);
    if (rec == null)
    {
      return JsonIterator.NULL; // TODO: should this be []?
    }
    
    return JsonRecord.keyValueIter(rec.iteratorSorted());
  }

}
