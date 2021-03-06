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

import com.ibm.jaql.lang.core.Env;
import com.ibm.jaql.lang.expr.core.ConstExpr;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.MacroExpr;
import com.ibm.jaql.lang.expr.function.DefaultBuiltInFunctionDescriptor;

/**
 * 
 */
public class SpanEndFn extends MacroExpr
{
  public static final ConstExpr END_EXPR = new ConstExpr(SpanSelectFn.END);
  
  public static class Descriptor extends DefaultBuiltInFunctionDescriptor.Par11
  {
    public Descriptor()
    {
      super("span_end", SpanEndFn.class);
    }
  }
  
  /**
   * @param exprs
   */
  public SpanEndFn(Expr[] exprs)
  {
    super(exprs);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.MacroExpr#expand(com.ibm.jaql.lang.core.Env)
   */
  public Expr expand(Env env) throws Exception
  {
    return new SpanSelectFn(new Expr[] { exprs[0], END_EXPR});
  }
}