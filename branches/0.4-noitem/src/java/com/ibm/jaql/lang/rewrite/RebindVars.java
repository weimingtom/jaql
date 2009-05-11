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
package com.ibm.jaql.lang.rewrite;

import java.util.HashMap;

import com.ibm.jaql.lang.core.Var;
import com.ibm.jaql.lang.expr.core.Expr;
import com.ibm.jaql.lang.expr.core.VarExpr;

/**
 * 
 */
public class RebindVars extends Rewrite
{
  HashMap<String, Var> scope = new HashMap<String, Var>();

  /**
   * @param phase
   */
  public RebindVars(RewritePhase phase)
  {
    super(phase, VarExpr.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.ibm.jaql.lang.rewrite.Rewrite#rewrite(com.ibm.jaql.lang.expr.core.Expr)
   */
  @Override
  public boolean rewrite(Expr expr)
  {
    // TODO: What happens when binding.var.name is the same as binding.var2.name? problem?
    VarExpr ve = (VarExpr) expr;
    Var var = ve.var();
    Expr realDef = findVarDef(ve);
    Expr nameDef = findVarDef(ve, var.name);
    if( realDef == null || nameDef == null )
    {
      findVarDef(ve);
      findVarDef(ve, var.name);
    }
    assert realDef != null && nameDef != null;
    if (realDef != nameDef)
    {
      engine.env.makeUnique(var);
      return true;
    }
    return false;
  }
}
