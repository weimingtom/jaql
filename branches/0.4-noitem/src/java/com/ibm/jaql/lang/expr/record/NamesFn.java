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

import com.ibm.jaql.json.type.JsonRecord;
import com.ibm.jaql.json.util.JsonIterator;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.IterExpr;
import com.ibm.jaql.lang.expr.core.JaqlFn;
import com.ibm.jaql.util.Bool3;

/**
 * names($rec) == for $k,$v in $rec return $k == fields($rec)[*][0];
 */
@JaqlFn(fnName = "names", minArgs = 1, maxArgs = 1)
public class NamesFn extends IterExpr
{

  /**
   * names(rec) names(exprs[0])
   * 
   * @param exprs
   */
  public NamesFn(Expr[] exprs)
  {
    super(exprs);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.Expr#isNull()
   */
  @Override
  public Bool3 isNull()
  {
    return Bool3.FALSE;
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
      return JsonIterator.EMPTY; // TODO: should this return null? If so, then not the same as fields($rec)[*][0]
    }
    return new JsonIterator() {
      int  i    = 0;

      public boolean moveNext() throws Exception
      {
        if (i < rec.arity())
        {
          currentValue = rec.getName(i);
          i++;
          return true;
        }
        return false;
      }
    };
  }
}
