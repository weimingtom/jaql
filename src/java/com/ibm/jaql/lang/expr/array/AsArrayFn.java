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
package com.ibm.jaql.lang.expr.array;

import com.ibm.jaql.json.schema.Schema;
import com.ibm.jaql.json.schema.SchemaTransformation;
import com.ibm.jaql.json.util.JsonIterator;
import com.ibm.jaql.lang.core.Context;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.IterExpr;
import com.ibm.jaql.lang.expr.function.DefaultBuiltInFunctionDescriptor;

/**
 * This function ensures that input returns an array.
 */
public class AsArrayFn extends IterExpr
{
  public static class Descriptor extends DefaultBuiltInFunctionDescriptor.Par11
  {
    public Descriptor()
    {
      super("asArray", AsArrayFn.class);
    }
  }
  
  /**
   * asArray(array)
   * 
   * @param exprs
   */
  public AsArrayFn(Expr[] exprs)
  {
    super(exprs);
  }

  /**
   * @param expr
   */
  public AsArrayFn(Expr expr)
  {
    super(new Expr[]{expr});
  }

  @Override
  public Schema getSchema()
  {
    Schema s = exprs[0].getSchema();
    s = SchemaTransformation.restrictToArrayWithNullPromotion(s);
    return s;
  }


  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.expr.core.IterExpr#iter(com.ibm.jaql.lang.core.Context)
   */
  @Override
  public JsonIterator iter(final Context context) throws Exception
  {
    JsonIterator iter = exprs[0].iter(context);
    if (iter.isNull())
    {
      iter = JsonIterator.EMPTY;
    }
    return iter;
  }
}
