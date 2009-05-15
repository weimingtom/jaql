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
package com.ibm.jaql.lang.expr.span;

import com.ibm.jaql.json.type.JsonLong;
import com.ibm.jaql.json.type.JsonSpan;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.JaqlFn;

/**
 * 
 */
@JaqlFn(fnName = "span", minArgs = 2, maxArgs = 2)
public class SpanFn extends Expr
{
  /**
   * @param exprs
   */
  public SpanFn(Expr[] exprs)
  {
    super(exprs);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.Expr#eval(com.ibm.jaql.lang.core.Context)
   */
  public JsonSpan eval(final Context context) throws Exception
  {
    JsonLong begin = (JsonLong) exprs[0].eval(context);
    JsonLong end = (JsonLong) exprs[1].eval(context);
    if (begin == null || end == null)
    {
      return null;
    }
    return new JsonSpan(begin.value, end.value); // TODO: reuse
  }
}
